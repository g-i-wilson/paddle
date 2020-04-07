package paddle;

import java.util.*;
import java.io.*;
import java.net.*;

public class Response {

	int sessionId;
	Socket socket;
	String body = "";
	String mime = "text/html";
	LinkedHashMap<String,String> header = new LinkedHashMap<>();

	// init
	public Response ( Socket s, int id ) {
		sessionId = id;
		socket = s;
	}

	// output to the socket
	public void transmit () {
		try {
			System.out.println("["+sessionId+"] Responce: writing to socket...");
			// Open an output stream to the socket
			PrintWriter out	= new PrintWriter(socket.getOutputStream(), true); // autoFlush true
			// Build the responce string
			String res =	"HTTP/1.0 200 OK\r\n"+
										"Content-type: "+mime+"; charset=utf-8\r\n";
			// Loop through header key,value pairs
			for (String key : header.keySet()) {
				res += key + ": " + header.get(key) + "\r\n";
			}
			// Additional new-line + body
			res += "\r\n" + body;
			// Transmit
			out.print( res );
			// Close the output stream
			out.close();
			System.out.println("["+sessionId+"] Responce: finished writing.");
		} catch (Exception e) {
			System.out.println("["+sessionId+"] Responce: ERROR writing to socket");
			e.printStackTrace();
		}
	}

	public void addHeader (String key, String value) {
		header.put( key, value );
	}

	public void setBody (String b) {
		body = b;
	}

	public void setMIME (String m) {
		mime = m;
	}

}
