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

	public void init () throws Exception {
		sessionId = 0;
		serverSocket = new ServerSocket( this.port() );
	}
	
	public void loop () throws Exception {
		Socket socket = serverSocket.accept();  // Wait for a client to connect
		sessionId++;
		new SessionHTTP(socket, this.state(), sessionId);  // Handle the client in a separate thread
	}
	
}
