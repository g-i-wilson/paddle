package paddle;

public abstract class Automaton extends Thread {

	// General
	private String 					name;
	private boolean					starting;
	private boolean					running;
	private boolean					willPause;
	private boolean					paused;
	private int							cycleId;
	
	
	public Automaton ( String name ) {
		super( name );
		running				= true;
		starting			= true;
		paused				= false;
		cycleId				= 0;
		start();
	}
	
	public String name () {
		return getName;
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
			this+": "+name+"\n" +
			"port: "+port+"\n" +
			"running: "+running+"\n" +
			"starting: "+starting+"\n" +
			"paused: "+paused+"\n"
		;
	}

	public void run () {

		while (starting) {
			try {
				init();
				starting = false;
			} catch (Exception e) {
				System.out.println(this+": Exception caught during init() in '"+name+"':");
				System.out.println(e);
				e.printStackTrace();
				int delay = (int)(Math.random()*10+1);
				System.out.println(this+": Will try again in "+delay+"ms...");
				Thread.sleep(delay); // 1-10 milliseconds
			}
		}

		while (running) {
			try {
				loop( cycleId++ );
			}
			catch (Exception e) {
				System.out.println(this+": Exception caught during loop() in '"+name+"':");
				System.out.println(e);
				e.printStackTrace();
			}
		
			if (willPause) {
				paused = true;
				willPause = false;
			}
			while (paused) {
				try {
					Thread.sleep(1); // 1 millisecond
				} catch (Exception e) {
					System.out.println(this+": Exception caught during sleep after pausing '"+name+"':");
					System.out.println(e);
					e.printStackTrace();
				}
				if (!running) {
					paused = false;
					break;
				}
			}

		}

		System.out.println( this+": Thread ended." );

	}

}
