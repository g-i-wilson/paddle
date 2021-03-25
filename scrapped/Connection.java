package paddle;

public interface Connection {

	public String remoteAddress ();
	public int remotePort ();

	public String localAddress ();
	public int localPort ();
	
	public String path ();
	
	public int connectionId ();
	public Server server ();
	
	public String data ();
	public Connection data ( String data );
	public Connection data ( byte[] data );
	
}