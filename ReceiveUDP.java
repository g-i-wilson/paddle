package paddle;

import java.net.*;

public class ReceiveUDP extends Thread implements Connection {

	private ServerUDP server;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private int packetId;
	private byte[] data;
	private String remoteAddress;
	private int remotePort;
	

	public ReceiveUDP ( ServerUDP server, DatagramSocket socket, DatagramPacket packet, int packetId ) {
		this.server = server;
		this.socket = socket;
		this.packet = packet;
		this.packetId = packetId;
		
		start();
	}
	
	public void run () {
		data = packet.getData();
		server.state().respond( this );
	}
	
	// network info
	public String remoteAddress () {
		return packet.getAddress().toString();
	}
	public int remotePort () {
		return packet.getPort();
	}
	public String localAddress () {
		return socket.getLocalAddress().toString();
	}
	public int localPort () {
		return socket.getPort();
	}
	
	// connection identity info
	public Server server () {
		return server;
	}
	public int connectionId () {
		return packetId;
	}
	
	public void reply ( String replyData ) {
		reply( replyData.getBytes() );
	}
	public void reply ( byte[] replyData ) {
		server.send( remoteAddress(), remotePort(), replyData );
	}
	
	public byte[] data () {
		return data;
	}
		
}