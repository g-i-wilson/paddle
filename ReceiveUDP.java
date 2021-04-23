package paddle;

public class ReceiveUDP extends Thread {

	private Server server;
	private DatagramPacket packet;
	private int packetId;
	private String data;
	

	public ReceiveUDP ( Server server, DatagramSocket packet, int packetId ) {
		this.packet = packet;
		this.server = server;
		this.packetId = packetId;
		start();
	}
	
	public void run () {
		data = new String( packet.getData() );
		server.state().process( this );
	}
	
	// network info
	public String remoteAddress () {
		packet.getAddress().toString();
	}
	public int remotePort () {
		packet.getPort();
	}
	public String localAddress () {
		socket.getLocalAddress().toString();
	}
	public int localPort () {
		socket.getLocalPort();
	}
	
	// connection identity info
	public int connectionId () {
		return packetId;
	}
	public Server server () {
		return server;
	}
	
	public void reply ( String data ) {
		reply( data.getBytes() );
	}
	public void reply ( byte[] data ) {
		server.send( remoteAddress(), remotePort(), data );
	}
	
	public String data () {
		return data;
	}
		
}