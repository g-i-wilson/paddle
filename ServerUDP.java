package paddle;

public class ServerUDP extends Server {

	private DatagramSocket	socket;
	private int							rxId;
	private int							txId;
	

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
	
}