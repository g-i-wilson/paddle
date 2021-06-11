package paddle;

import java.util.*;

public interface Connection {

	// network info
	public String remoteAddress ();
	
	public int remotePort ();
	
	public String localAddress ();
	
	public int localPort ();
	
	// connection identity info
	public String protocol ();
	
	public boolean inbound ();
	
	public Server server ();
	
	public int connectionId ();
	
	public byte[] data ();
	
}
