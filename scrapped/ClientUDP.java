package paddle;

import java.net.*;
import java.io.*;

public class ClientUDP implements Connector {
	
	private String					name;
	private String					address;
	private int							port;
	private DatagramSocket	socket;

	private int 						connectionId;
	private LocalState			state;

	public Client ( String name, String address, LocalState state ) throws Exception {
		this.name							= name;
		this.state						= state;
		String[] addrAndPort 	= address.split(":");
		if (addrAndPort.length > 1) {
			this.address 				= addrAndPort[0];
			this.port 					= String.valueOf( addrAndPort[1] );
		} else {
			this.port 					= -1;
		}
		connectionId = 0;
		socket = new DatagramSocket( this.port );		
	}
	
	public Connection connect () {
		Connection newConnection = new ConnectionUDP(this, socket, connectionId++);
		state.outgoing( newConnection );
	}
	
	public String name () {
		return name;
	}
	
	public String address () {
		return address;
	}
	
	public int port () {
		return port;
	}
	
	public LocalState state () {
		return state;
	}
	
}