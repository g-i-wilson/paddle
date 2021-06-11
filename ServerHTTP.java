package paddle;

import java.util.*;
import java.io.*;
import java.net.*;


public class ServerHTTP extends Server {

	private ServerSocket serverSocket;
	private int sessionId;
	
	public ServerHTTP ( ServerState state, int port, String name ) {
		super(state, port, name);
	}
	
	public int sessionId () {
		return sessionId;
	}

	// Exception is caught by abstract class Server
	public void init () throws Exception {
		sessionId = 0;
		serverSocket = new ServerSocket( this.port() );
	}
	
	// Exception is caught by abstract class Server
	public void loop () throws Exception {
		Socket socket = serverSocket.accept();  // Wait for a client to connect
		sessionId++;
		new InboundHTTP(this, socket, sessionId);  // Handle the client in a separate thread
	}
	
	// test
	public static void main (String[] args) throws Exception {
	
		ServerState state = new ServerState();

		ServerHTTP server0 = new ServerHTTP( state, 9000, "httpserver0" );
		ServerHTTP server1 = new ServerHTTP( state, 9001, "httpserver1" );
				
	}
	
}
