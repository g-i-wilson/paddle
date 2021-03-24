package paddle;

public abstract class Client {

	private String					name;
	private String					address;
	private int							port;
	private int							sessionId;
	private ServerState			state;
	
	public Client ( String name, String address, ServerState state ) {
		this.state						= state;
		this.name							= name;
		this.sessionId				= 0;
		String[] addrAndPort 	= address.split(":");
		if (addrAndPort.length > 1) {
			this.address 				= addrAndPort[0];
			this.port 					= String.valueOf( addrAndPort[1] );
		} else {
			this.port 					= -1;
		}
	}
	
	public String name () {
		return name;
	}
	
	public String address () {
		return address;
	}
	
	public int port () {
		return port;
	}
	
	public int state () {
		return state;
	}
	
	public int sessionId () {
		return sessionId++;
	}
	
}