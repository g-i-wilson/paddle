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

	public void initSuccess () {
		System.out.println( this.getClass().getName()+" '"+getName()+"' is listening on port "+port+"..." );
	}

	public void initException ( Exception e ) {
		System.out.println( this.getClass().getName()+" '"+getName()+"': Exception during init():" );
		e.printStackTrace();
		end();
	}

	public boolean running () {
		return running;
	}
	
	/////////////// ABSTRACT METHOD ///////////////
	public abstract void loop () throws Exception;
	
	public void loopException ( Exception e ) {
		System.out.println( this.getClass().getName()+" '"+getName()+"': Exception during loop():" );
		e.printStackTrace();
		try {
			sleep(500);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void loopEnded () {
		System.out.println( this.getClass().getName()+" '"+getName()+"' on port "+port+" has ended." );
	}

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
	
	public void run () {

		try {
			init();
			starting = false;
			initSuccess();
		} catch (Exception e) {
			initException( e );
		}

		while (running) {
			try {
				loop();
			}
			catch (Exception e) {
				loopException( e );
			}
		
			if (willPause) {
				paused = true;
				willPause = false;
			}
			while (paused) {
				try {
					sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!running) {
					paused = false;
					break;
				}
			}

		}
		
		loopEnded();

	}
	
}
