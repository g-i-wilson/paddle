package paddle;

import java.util.*;
import java.io.*;
import java.net.*;

public class InboundHTTP extends Thread implements Connection {
	private Socket socket;
	private Server server;
	private int sessionId;
	private RequestHTTP request;
	private ResponseHTTP response;

	// Start the thread in the constructor
	public InboundHTTP(Server server, Socket socket, int sessionId) {
		this.server = server;
		this.socket = socket;
		this.sessionId = sessionId;
		start();
	}

	// Read the HTTP request, respond, and close the connection
	public void run() {
		try {

			// Log session start
			System.out.println("\n\n====\n"+this+" ["+sessionId+"]: new thread started...");

			try {
				// Use the ServerState object to process the Request and Responce objects
				request = new RequestHTTP( socket, sessionId );
				response = new ResponseHTTP( socket, sessionId );
				server.state().respond( this );
				response.transmit();
			} catch (Exception e) {
				System.out.println(this+" ["+sessionId+"] ERROR: Exception while processing client.");
				System.out.println(e);
				e.printStackTrace();
			}

			// Close the socket
			socket.close();

			// Log session end
			System.out.println(this+" ["+sessionId+"]: socket closed.\n====\n\n");

		} catch (Exception e) {
			System.out.println(this+" ["+sessionId+"] ERROR: Exception in thread.");
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	// specific to InboundHTTP
	public boolean path ( String comparePath ) {
		// NOTE: comparePath should be lower-case,
		//       since request().path() converts to lower-case.
		//       Also, HTTP paths must start with '/'.
		return request().path().startsWith( comparePath );
	}
	
	public RequestHTTP request () {
		return request;
	}
	
	public ResponseHTTP response () {
		return response;
	}
	
	// network info
	public String remoteAddress () {
		return socket.getRemoteSocketAddress().toString();
	}
	
	public int remotePort () {
		return socket.getPort();
	}
	
	public String localAddress () {
		return socket.getLocalSocketAddress().toString();
	}
	
	public int localPort () {
		return socket.getLocalPort();
	}
	
	// connection identity info
	public String protocol () {
		return "HTTP";
	}
	
	public boolean inbound () {
		return true;
	}
	
	public Server server () {
		return server;
	}
	
	public int connectionId () {
		return sessionId;
	}
	
	public byte[] data () {
		return request.data();
	}
	
}
