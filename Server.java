package paddle;

public abstract class Server extends Thread {

	// General
	private ServerState 		state;
	private int 						port;
	private boolean					starting;
	private boolean					running;
	private boolean					willPause;
	private boolean					paused;
	
	
	public Server ( ServerState state, int port, String name ) {
		super( name );
		running				= true;
		starting			= true;
		paused				= false;
		this.state 		= state;
		this.port 		= port;
		start();
	}
	
	public ServerState state () {
		return state;
	}
	
	public int port () {
		return port;
	}
	
	public String name () {
		return getName();
	}

	public boolean starting () {
		return starting;
	}
	
	/////////////// ABSTRACT METHOD ///////////////
	public abstract void init() throws Exception;

	public boolean running () {
		return running;
	}
	
	/////////////// ABSTRACT METHOD ///////////////
	public abstract void loop () throws Exception;
	
	public void pause () {
		willPause = true;
	}
	
	public void unpause () {
		paused = false;
	}
	
	public boolean paused () {
		return paused;
	}
	
	public void end () {
		running = false;
	}
	
	public String toString () {
		return
			"Server:    "+getName()+"\n" +
			"port:      "+port+"\n" +
			"running:   "+running+"\n" +
			"starting:  "+starting+"\n" +
			"paused:    "+paused+"\n"
		;
	}
	
	public void postInit () {
		System.out.println("Server '"+getName()+"' is listening on port "+port+"...");
	}

	public void run () {

		try {
			init();
			starting = false;
			postInit();
		} catch (Exception e) {
			System.out.println("Server '"+getName()+"': Exception caught during init():");
			System.out.println(e);
			e.printStackTrace();
			end();
		}

		while (running) {
			try {
				loop();
			}
			catch (Exception e) {
				System.out.println("Server '"+getName()+"': Exception caught during loop():");
				System.out.println(e);
				e.printStackTrace();
			}
		
			if (willPause) {
				paused = true;
				willPause = false;
			}
			while (paused) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					System.out.println("Server '"+getName()+"': Exception caught during pause:");
					System.out.println(e);
					e.printStackTrace();
				}
				if (!running) {
					paused = false;
					break;
				}
			}

		}

		System.out.println( "Server '"+getName()+"' on port "+port+" has ended." );

	}

}
