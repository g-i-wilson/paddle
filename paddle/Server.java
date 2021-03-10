package paddle;

import java.util.*;
import java.io.*;
import java.net.*;


public abstract class Server extends Thread {

	// General
	private ServerState 		state;
	private int 						port;
	private String 					name;
	private String 					socType;
	
	// TCP
	private ServerSocket 		tcpSocket;
	private int 						sessionId;
	
	// UDP
	private DatagramSocket 	udpSocket;
	private long 						packetId;
	private byte[] 					udpBuffer;
	
	
	public Server ( ServerState state, int port, String name ) throws Exception {
		this( state, port, name, "tcp" );
	}

	public Server ( ServerState state, int port, String name, String socType ) throws Exception {
		this( state, port, name, socType, 1500 );
	}

	public Server ( ServerState state, int port, String name, String socType, int udpBufSize ) throws Exception {
		this.state 		= state;
		this.port 		= port;
		this.name 		= name;
		this.socType 	= socType.toLowerCase();
		if (socType.equals("tcp")) {
			tcpSocket 	= new ServerSocket(port);
			sessionId 	= 0;
		} else if (socType.equals("udp")) {
			udpSocket 	= new DatagramSocket(port);
			udpBuffer 	= new byte[udpBufSize];
			packetId		= 0;
		} else {
			throw Exception("Server "+name+" error: unrecognized socket type: "+socType);
		}
		start();
	}


	public void tcpAction( Socket socket, ServerState state, int sessionId, String name );
	
	public void udpAction( DatagramPacket packet, ServerState state, long packetId, String name );


	public void run () {

		System.out.println( "**********************" );
		System.out.println( "Server started! );
		System.out.println( "port: "+port );
		System.out.println( "name: "+name );
		System.out.println( "type: "+socType );
		System.out.println( "**********************" );

		while (true) {
			try {
				if (socType.equals("tcp")) {
					Socket socket = tcpSocket.accept();  // Wait for a client to connect
					sessionId++;
					tcpAction(socket, state, sessionId);
				} else if (socType.equals("udp")) {
					DatagramPacket packet = new DatagramPacket(udpBuffer, udpBuffer.length);
					udpSocket.receive(packet);
					packetId++;
					udpAction(packet, state, sessionId);
				}
				new SessionHTTP(socket, state, sessionId);  // Handle the client in a separate thread
			}
				catch (Exception e) {
				System.out.println("paddle HTTP server ERROR: Exception while creating new SessionHTTP thread.");
				System.out.println(e);
				e.printStackTrace();
			}
		}

	}

}
