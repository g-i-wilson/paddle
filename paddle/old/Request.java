package paddle;

import java.io.*;
import java.net.*;

public abstract class SessionData {

	// connection
	private Socket socket;
	private int sessionId;
	
	// data
	public LinkedHashMap<String,String> metadata = new LinkedHashMap<>();
	public string data;

	// constructor
	public SessionData ( Socket s, int id ) {
		socket = s;
		sessionId = id;
		transfer();
	}
	
	public Socket socket () {
		return socket;
	}
	
	public int port () {
		return socket.getLocalPort();
	}

	public int sessionId () {
		return sessionId;
	}

	// add metadata
	public void metadata ( String key, String value ) {
		metadata.put( key, value );
	}

	// get metadata
	public Map<String,String> metadata () {
		return metadata;
	}

	// set data
	public String data ( String dataStr ) {
		data = dataStr;
	}
	
	// get data
	public String data () {
		return data;
	}
	
	// execute a data transfer
	public abstract void transfer();
	
	public String toString () {
		return metadata() + "\n" + data();
	}

}
