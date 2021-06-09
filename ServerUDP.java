package paddle;

public class ServerUDP extends Server {

	private DatagramSocket	socket;
	private int							rxId;
	

	public ServerUDP ( ServerState state, int port, String name ) {
		super( state, port, name );
	}
	
	public ServerUDP ( ServerState state, String name ) {
		super( state, -1, name );
	}
	
	// Exception is caught by abstract class Server
	public void init () throws Exception {
		sessionId = 0;
		if (port() != -1) {
			serverSocket = new DatagramSocket( this.port() );
		} else {
			serverSocket = new DatagramSocket();
		}
	}
	
	// Exception is caught by abstract class Server
	public void loop () {
		DatagramPacket packet = socket.receive();
		new ReceiveUDP(this.state(), packet, rxId++);		
	}
	
	public void send ( String address, int port, byte[] data ) {
		socket.send(
			new DatagramPacket(
				data,
				data.length,
				address,
				port
			)
		);
	}
	
	// test
	public static void main (String args) {
	
		ServerState state = new ServerState();

		ServerUDP server0 = new ServerUDP( state, 9000, "udpserver0" );
		ServerUDP server1 = new ServerUDP( state, 9001, "udpserver1" );
		
		server0.send( "localhost", 9001, new byte[]{'h','e','l','l','o'} );
		// Should ping-pong back and forth, each server replying...
		// ...That's why we need a Thread.sleep(__) delay in the state object!
		
	}
	
}