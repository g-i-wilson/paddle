package paddle;

import java.util.*;
import java.io.*;
import java.net.*;

public class ResponseHTTP {

	int sessionId;
	Socket socket;
	byte[] body;
	String mime = "text/html";
	String charset = "UTF-8";

	LinkedHashMap<String,String> header = new LinkedHashMap<>();

	// init
	public ResponseHTTP ( Socket s, int id ) {
		sessionId = id;
		socket = s;
	}

	// output to the socket
	public void transmit () {
		try {
			System.out.println("["+sessionId+"] Responce: writing to socket...");
			// Build the responce string
			String res =	"HTTP/1.0 200 OK\r\n"+
										"Content-type: "+mime+"; charset="+charset+"\r\n";
			// Loop through header key,value pairs
			for (String key : header.keySet()) {
				res += key + ": " + header.get(key) + "\r\n";
			}
			// Additional new-line + body
			// res += "\r\n" + body;
			res += "\r\n";

			// // Open an output stream to the socket
			// PrintWriter out	= new PrintWriter(socket.getOutputStream(), true); // autoFlush true
			// // Transmit
			// out.print( res );
			// // Close the output stream
			// out.close();

			OutputStream out = socket.getOutputStream();
			out.write( res.getBytes() );
			if (body != null) out.write( body );
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

	public void setBody (byte[] b) {
		body = b;
	}

	public void setBody (String b) {
		body = b.getBytes();
	}

	public void setMIME (String m) {
		mime = m;
	}

	public void setCharset (String c) {
		charset = c;
	}

}
