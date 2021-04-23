package paddle;

public interface Connector {

	public String name ();
	
	public String address ();
	
	public int port ();
	
	public LocalState state ();
	
	public Connection connect ();

}