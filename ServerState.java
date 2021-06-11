package paddle;

/*
- A ServerState instance is a vehicle for maintaining the state of the server in
  one object.
- This one object is passed to each Session of each Server.
*/

public class ServerState {

	private int count = 0;
	
	public void printConnection ( Connection c ) {
  	System.out.println(
  		this+" ["+count+"]:\n"+
  		"Type: "+c+"\n"+
  		"Name: "+c.server().name()+" ("+c.connectionId()+")\n"+
  		"From: "+c.remoteAddress()+":"+c.remotePort()+"\n"+
  		"To:   "+c.localAddress()+":"+c.localPort()+"\n"+
  		"Data: '"+c.data()+"'\n"
  	);
	}

  public void respond ( SessionHTTP session ) {
  	count++;
    session.response().setBody(
    	"<h1>HTTP works!<h1><br>"+
    	"path: "+session.request().path()+"<br>"+
    	"body: "+session.request().body()
    );
    printConnection( session );
  }
  
  public void respond ( ReceiveUDP packet ) {
  	count++;
    printConnection( packet );
    try {
  		Thread.sleep(500);
  	} catch (Exception e) {}
  	packet.reply( "UDP works! Received data("+packet.connectionId()+"): "+packet.data() );
  }
  
}
