package paddle;

import java.util.*;
import java.io.*;
import java.net.*;

public class Session extends Thread {
	private Socket socketObject;
	private ServerState stateObject;
	private int sessionId;

	// Start the thread in the constructor
	public Session(Socket a, ServerState b, int c) {
		socketObject = a;
		stateObject = b;
		sessionId = c;
		start();
	}

	// Read the HTTP request, respond, and close the connection
	public void run() {
		try {

			// Log session start
			System.out.println("\n\n====\n["+sessionId+"] paddle Session: new thread started...");

			Request req;
			Response res;

			try {
				// Use the ServerState object to process the Request and Responce objects
				req = new Request( socketObject, sessionId );
				res = new Response( socketObject, sessionId );
				stateObject.createResponse( req, res );
				res.transmit();
			} catch (Exception e) {
				System.out.println("["+sessionId+"] paddle Session ERROR: Exception while processing client.");
				System.out.println(e);
				e.printStackTrace();
			}

			// Close the socket
			socketObject.close();

			// Log session end
			System.out.println("["+sessionId+"] paddle Client: socket closed.\n====\n\n");

		} catch (Exception e) {
			System.out.println("["+sessionId+"] paddle Client ERROR: Exception in thread.");
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
