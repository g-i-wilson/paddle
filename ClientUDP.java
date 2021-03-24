package paddle;

import java.net.*;
import java.io.*;

public class ClientUDP extends Client {
	
	private DatagramSocket	socket;

	public Client ( String name, String address, ServerState state, String name ) throws Exception {
		super( name, address, state );
		socket = new DatagramSocket( this.port() );		
	}
	
	public void send ( String data ) {
		new ClientSessionUDP(name, data, socket, state(), sessionId());
	}
	
}