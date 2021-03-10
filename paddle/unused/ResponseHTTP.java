package paddle;

import java.io.*;
import java.net.*;

public class ResponseHTTP extends Response {

	// output to the socket
	public void send () {
		try {
			System.out.println("["+sessionId+"] Responce: writing to socket...");
			// Open an output stream to the socket
			PrintWriter out	= new PrintWriter(socket.getOutputStream(), true); // autoFlush true
			// Build the responce string
			String res =	"HTTP/1.0 200 OK\r\n"+
										"Content-type: "+metadata().get("Content-type")+"; charset=utf-8\r\n";
			// Loop through header key,value pairs
			for (String key : metadata().keySet()) {
				if (! key.equals("Content-type")
					res += key + ": " + metadata().get(key) + "\r\n";
			}
			// Additional new-line + body
			res += "\r\n" + data;
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

}
