package paddle;

import java.net.*;

public class ServerUDP extends Server {

	private DatagramSocket	socket;
	private int							rxId;
	private int							mtuSize;
	
	public ServerUDP ( ServerState state, int port, String name ) {
		super( state, port, name );
		mtuSize = 1500;
	}
	
	public ServerUDP ( ServerState state, int port, String name, int mtuSize ) {
		super( state, port, name );
		this.mtuSize = mtuSize;
	}
	
	public ServerUDP ( ServerState state, String name ) {
		super( state, -1, name );
	}
	
	// Exception is caught by abstract class Server
	public void init () throws Exception {
		rxId = 0;
		if (port() != -1) {
			socket = new DatagramSocket( this.port() );
		} else {
			socket = new DatagramSocket();
		}
	}
	
	// Exception is caught by abstract class Server
	public void loop () throws Exception {
		DatagramPacket packet = new DatagramPacket(new byte[mtuSize], mtuSize);
		socket.receive( packet );
		new ReceiveUDP(this, socket, packet, rxId++);
	}
	
	public void send ( String address, int port, byte[] data ) throws Exception {
		socket.send(
			new DatagramPacket(
				data,
				data.length,
				InetAddress.getByName( address ),
				port
			)
		);
	}
	
	// Maximum Tranmission Unit size
	public void mtu ( int mtuSize ) {
		this.mtuSize = mtuSize;
	}
	
	public int mtu () {
		return mtuSize;
	}
	
	// test
	public static void main (String[] args) {
	
		ServerState state = new ServerState();

		ServerUDP server0 = new ServerUDP( state, 9000, "udpserver0" );
		ServerUDP server1 = new ServerUDP( state, 9001, "udpserver1" );
		
		while( server0.starting() );
		
		try {
			server0.send( "localhost", 9001, new byte[]{'h','e','l','l','o'} );
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Should ping-pong back and forth, each server replying...
		// ...That's why we need a Thread.sleep(__) delay in the state object!
		
	}
	
}