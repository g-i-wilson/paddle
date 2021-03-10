package paddle;

import java.util.*;
import java.io.*;
import java.net.*;


public class Server extends Thread {

	private ServerSocket serverSocket;
	private ServerState state;
	private int port;

	public Server ( ServerState s, int p ) throws Exception {
		state = s;
		port = p;
		serverSocket = new ServerSocket(port);
		start();
	}



	public void run () {

		System.out.println( "===\npaddle Server started on port "+port+"\n===");

		int sessionId = 0;

		while (true) {
			try {
				Socket socket = serverSocket.accept();  // Wait for a client to connect
				sessionId++;
				new Session(socket, state, sessionId);  // Handle the client in a separate thread
			}
				catch (Exception e) {
				System.out.println("paddle Server ERROR: Exception while creating new Session thread.");
				System.out.println(e);
				e.printStackTrace();
			}
		}

	}

}
