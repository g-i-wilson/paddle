package paddle;

public class ClientSessionUDP extends Thread implements ClientSession {

	private DatagramPacket packet;
	private DatagramSocket socket;
	private String name;
	private int sessionId;

	public ClientSessionUDP ( String name, String data, DatagramSocket socket, int sessionId ) {
		this( name, data.getBytes(), socket, sessionId );
	}
	
	public ClientSessionUDP ( String name, byte[] data, DatagramSocket socket, int sessionId ) {
		packet = new DatagramPacket( data, data.length, address, port );
		this.name = name;
		this.packet = packet;
		this.socket = socket;
		this.sessionId = sessionId;
		start();
	}
	
	public void run () {
		try {
			state.process( this );
		} catch (Exception e) {
			System.out.println(this+": ERROR: Exception while sending DatagramPacket.");
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public String name () {
		return name;
	}
	
	public String endpoint () {
		return address+":"+port;
	}
	
	public void transmit () throws Exception {
		socket.send( packet );
	}
	
}