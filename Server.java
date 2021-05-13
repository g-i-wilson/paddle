package paddle;

public abstract class Server extends Thread {

	// General
	private ServerState 		state;
	private int 						port;
	private String 					name;
	private boolean					starting;
	private boolean					running;
	private boolean					willPause;
	private boolean					paused;
	
	
	public Server ( ServerState state, int port, String name ) {
		running				= true;
		starting			= true;
		paused				= false;
		this.state 		= state;
		this.port 		= port;
		this.name 		= name;
		start();
	}
	
	public ServerState state () {
		return state;
	}
	
	public int port () {
		return port;
	}
	
	public String name () {
		return name;
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
			"Server:    "+name+"\n" +
			"port:      "+port+"\n" +
			"running:   "+running+"\n" +
			"starting:  "+starting+"\n" +
			"paused:    "+paused+"\n"
		;
	}

	public void run () {

		try {
			init();
		} catch (Exception e) {
			System.out.println("Exception caught during init() in paddle.Server '"+name+"':");
			System.out.println(e);
			e.printStackTrace();
		}
		starting = false;
		System.out.println( "paddle.Server '"+name+"' is running on port "+port+"..." );

		while (running) {
			try {
				loop();
			}
			catch (Exception e) {
				System.out.println("Exception caught during loop() in paddle.Server '"+name+"':");
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
					System.out.println("Exception caught during sleep after pausing paddle.Server '"+name+"':");
					System.out.println(e);
					e.printStackTrace();
				}
				if (!running) {
					paused = false;
					break;
				}
			}

		}

		System.out.println( "paddle.Server '"+name+"' on port "+port+" has ended." );

	}

}
