package paddle;

public class ConnectionUDP extends Thread implements Connection {

	private Server server;
	private DatagramPacket packet;
	private int packetId;
	private String data;
	

	public ServerUDP ( DatagramSocket packet, Server server ) {
		this.packet = packet;
		this.server = server;
		start();
	}
	
	public void run () {
		data = new String( packet.getData() );
		//////// pass this incoming connection to LocalState ////////
		server.state().incoming( this );
	}
	
	// network info
	public String remoteAddress () {
		socket.getInetAddress().toString();
	}
	public int remotePort () {
		socket.getPort();
	}
	public String localAddress () {
		socket.getLocalAddress().toString();
	}
	public int localPort () {
		socket.getLocalPort();
	}
	
	// connection identity info
	public int connectionId () {
		return connectionId;
	}
	public Server server () {
		return server;
	}
	
	// receiving data
	public Map<String,String> metadata () {
		return null;
	}
	public String data () {
		return data;
	}
	
	// sending data
	public void send ( String data ) throws Exception {
		send( data.getBytes() );
	}

	public void send ( byte[] data ) throws Exception {
		socket.send(
			new DatagramPacket(
				data, data.length, remoteAddress(), remotePort()
			)
		);
	}
	
}