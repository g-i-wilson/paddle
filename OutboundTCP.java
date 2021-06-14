package paddle;

import java.net.*;
import java.io.*;

public class OutboundTCP extends Server implements Connection {

	private Socket socket;
	private int connectionId;
	private String address;
	private BufferedReader input;
	private OutputStream output;
	private byte[] outboundMemory;
	private int outboundMemoryPlace;
	private int outboundMemoryValid;
	private byte[] inboundMemory;
	private int inboundMemoryPlace;


	public OutboundTCP ( ServerState state, String address, int port, String name, byte[] outboundMemory, byte[] inboundMemory, int connectionId ) {
		super( state, port, name );
		this.connectionId = connectionId;
		this.address = address;
		this.inboundMemory = inboundMemory;
		inboundMemoryPlace = 0;
		this.outboundMemory = outboundMemory;
		outboundMemoryPlace = 0;
	}
	
	public void init () throws Exception {
		socket = new Socket ( address, this.port() );
		
		input =
			new BufferedReader (
				new InputStreamReader (
					socket.getInputStream()
				)
			);
			
		output = socket.getOutputStream();
	}
	
	public void postInit () {
		System.out.println("OutboundTCP '"+getName()+"' is connected to "+address+":"+port()+"...");
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
		
		// catch up on reading if there's data in the stream...
		int nextByte = input.read();
		if ( nextByte != -1 ) {
			while (
				inboundMemory.length > inboundMemoryPlace &&
				nextByte != -1
			) {
				inboundMemory[inboundMemoryPlace] = (byte)(nextByte & 0xff);
				inboundMemoryPlace++;
				nextByte = input.read();
			}
			this.state().respond( this );
		}
		
		sleep(100);
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
	
	public void end () {
		try {
			socket.close();
			super.end();
		} catch (Exception e) {
			e.printStackTrace();	
		}
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
			"tcp-outbound",
			"GET /?some_data HTTP/1.1\r\n\r\n".getBytes(),
			new byte[1000],
			100
		);

		while(http0.starting()) Thread.sleep(1);
		
		tcp0.outboundMemoryValid(999);
	}

}