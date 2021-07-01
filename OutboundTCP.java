package paddle;

import java.net.*;
import java.io.*;

public class OutboundTCP extends Server implements Connection {

	private Socket socket;
	private int connectionId;
	private String address;
	private BufferedInputStream input;
	private OutputStream output;
	private byte[] outboundMemory;
	private int outboundMemoryPlace;
	private int outboundMemoryValid;
	private byte[] inboundMemory;
	private int inboundMemoryPlace;
	private int chunks;
	private int timeout;

	// Annonymously send String immediately (simple)
	public OutboundTCP ( String address, int port, String outboundText ) {
		this( new ServerState(), address, port, "OutboundTCP", outboundText.getBytes(), new byte[1024], -1, true);
	}

	// Send String immediately (simple)
	public OutboundTCP ( ServerState state, String address, int port, String outboundText ) {
		this( state, address, port, "OutboundTCP", outboundText.getBytes(), new byte[1024], -1, true );
	}

	// Send String immediately
	public OutboundTCP ( ServerState state, String address, int port, String name, String outboundText, int inboundLength, int connectionId ) {
		this( state, address, port, name, outboundText.getBytes(), new byte[inboundLength], connectionId, true );
	}

	// Send bytes incrementally
	public OutboundTCP ( ServerState state, String address, int port, String name, byte[] outboundMemory, byte[] inboundMemory, int connectionId ) {
		this( state, address, port, name, outboundMemory, inboundMemory, connectionId, false );
	}

	// All-argument constructor
	public OutboundTCP ( ServerState state, String address, int port, String name, byte[] outboundMemory, byte[] inboundMemory, int connectionId, boolean allValid ) {
		super( state, port, name );
		this.connectionId = connectionId;
		this.address = address;
		this.inboundMemory = inboundMemory;
		inboundMemoryPlace = 0;
		this.outboundMemory = outboundMemory;
		outboundMemoryPlace = 0;
		outboundMemoryValid = ( allValid ? outboundMemory.length : 0 );
		chunks = 0;
		timeout = 10000; // 10 seconds
	}
	
	public void init () throws Exception {
		socket = new Socket ( address, this.port() );
		
//		input =
//			new BufferedReader (
//				new InputStreamReader (
//					socket.getInputStream()
//				)
//			);

		input = new BufferedInputStream( socket.getInputStream() );			
		output = socket.getOutputStream();
	}
	
	public void initSuccess () {
		System.out.println( this.getClass().getName()+" '"+getName()+"' is connected to "+address+":"+port()+"..." );
	}
	
	public void initException ( Exception e ) {
		System.out.println( this.getClass().getName()+" '"+getName()+"': Exception while connecting to "+address+":"+port()+"\n"+e );
		end();
	}
	
	public void loop () throws Exception {
		// catch up on writing if outboundMemory has increased...
		while (
			outboundMemory.length > outboundMemoryPlace &&
			outboundMemoryValid > outboundMemoryPlace
		) {
			output.write( (int)outboundMemory[outboundMemoryPlace] );
			outboundMemoryPlace++;
		}
		output.flush();
		
		sleep(1);
		
		// catch up on reading if there's data in the stream...
		int nextByte = input.read();
		if ( nextByte != -1 ) {
			while (
				inboundMemory.length > inboundMemoryPlace &&
				input.available() > 0 &&
				nextByte != -1
			) {
				inboundMemory[inboundMemoryPlace] = (byte)(nextByte & 0xff);
				inboundMemoryPlace++;
				nextByte = input.read();
				sleep(1);
			}
			chunks++;
			this.state().respond( this );
		}
	}
	
	public void loopEnded () {
		try {
			socket.close();
		} catch (Exception e) {}
		System.out.println( this.getClass().getName()+" '"+getName()+"' has disconnected from "+address+":"+port()+"." );
	}
	
	// specific to OutboundTCP
	public byte[] outboundMemory () {
		return outboundMemory;
	}
	
	public int outboundMemoryValid ( int outboundMemoryValid ) {
		this.outboundMemoryValid = outboundMemoryValid;
		return outboundMemoryPlace;
	} 
	
	public byte[] inboundMemory () {
		return inboundMemory;
	}
	
	public int inboundMemoryPlace () {
		return inboundMemoryPlace;
	}
	
	public OutboundTCP receive () {
		return receive(1);
	}
	
	public OutboundTCP timeout ( int timeout ) {
		this.timeout = timeout;
		return this;
	}
	
	public OutboundTCP receive ( int chunksToReceive ) {
		long timeoutStart = System.currentTimeMillis();
		while (
			chunks < chunksToReceive &&
			(int)(System.currentTimeMillis() - timeoutStart) < timeout
		) {
			try {
				Thread.sleep (1);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		end();
		return this;
	}
	
	public int chunks () {
		return chunks;
	}
	
	public String text () {
		return new String( data() );
	}
	
	public String hex () {
		return ( new Bytes(data()) ).toString();
	}
	
	
	// network info
	public String remoteAddress () {
		return address;
	}
	public int remotePort () {
		return port();
	}
	public String localAddress () {
		return socket.getLocalAddress().toString();
	}
	public int localPort () {
		return socket.getLocalPort();
	}
	
	// connection identity info
	public String protocol () {
		return "TCP";
	}
	
	public boolean inbound () {
		return false;
	}
	
	public Server server () {
		return this;
	}
	public int connectionId () {
		return connectionId;
	}
	
	public byte[] data () {
		byte[] currentData = new byte[inboundMemoryPlace];
		for (int i=0; i<inboundMemoryPlace; i++) {
			currentData[i] = inboundMemory[i];
		}
		return currentData;
	}
	
	// test
	public static void main (String[] args) throws Exception {
		ServerState state = new ServerState();
		
		ServerHTTP http0 = new ServerHTTP( state, 9000, "http0" );
		
		OutboundTCP tcp0 = new OutboundTCP(
			state,
			"localhost",
			9000,
			"tcp0",
			"GET /?some_data HTTP/1.1\r\n\r\n".getBytes(),
			new byte[300],
			100
		);

		while(http0.starting()) Thread.sleep(1);
		
		tcp0.outboundMemoryValid(300);
		
		Thread.sleep(1000);
		
		System.out.println(
			"Text received: "+
			(new OutboundTCP( "localhost", 9000, "GET /easy HTTP/1.1\r\n\r\n" ))
			.receive()
			.text()
		);
		
		Thread.sleep(1000);
		
		tcp0.end();
		
		System.out.println(
			"Text received: "+
			(new OutboundTCP( "10.0.1.20", 18000, "test data" ))
			.receive()
			.text()
		);
		
	}
	
}
