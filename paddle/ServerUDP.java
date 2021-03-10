package paddle;

import java.util.*;
import java.io.*;
import java.net.*;


public class ServerUDP extends Thread {

	private DatagramSocket serverSocket;
	private ServerState state;
	private int port;
	private byte[] buffer;

	public ServerUDP ( ServerState state, int port, int bufferSize ) throws Exception {
		this.state = state;
		this.port = port;
		buffer = new byte[bufferSize];
		serverSocket = new DatagramSocket(port);
		start();
	}



	public void run () {

		System.out.println( "===\npaddle UDP server started on port "+port+"\n===");

		long packetId = 0;

		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket( buffer, buffer.length );
				serverSocket.receive(packet); // wait for a UDP packet
				packetId++;
				
				new ActionUDP(state, sessionId);  // Handle the action in a separate thread
			}
				catch (Exception e) {
				System.out.println("paddle UDP server ERROR: Exception while creating new Session thread.");
				System.out.println(e);
				e.printStackTrace();
			}
		}

	}

}
