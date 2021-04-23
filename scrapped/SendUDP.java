package paddle;

public class SendUDP extends Thread implements Connection {

	private String address;
	private int port;
	private byte[] data;
	
	private DatagramSocket socket;
	private DatagramPacket packet;

	public SendUDP ( String address, int port, String data ) {
		this( data.getBytes() );
	}

	public SendUDP ( String address, int port, byte[] data ) {
		this.packet = packet;
		this.server = server;
		start();
	}
	
	public void run () {
		try {
		
		} catch (Exception e) {
			System.out.print(this+": "+e);
			e.printStackTrace();
		}
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