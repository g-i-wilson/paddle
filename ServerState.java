package paddle;

/*
- A ServerState instance is a vehicle for maintaining the state of the server in
  one object.
- This one object is passed to each Session of each Server.
*/

public class ServerState {

	private int responses = 0;

  public void respondHTTP (RequestHTTP req, ResponseHTTP res) {
    System.out.println( req );
    res.setBody(
    	"<h1>HTTP works!<h1><br>"+
    	"response: "+(responses++)+"<br>"+
    	"server: "req.server().name()+"<br>"+
    	"port: "req.server().port()+"<br>"+
    	"sessionId: "+req.sessionId()+"<br>"+
    	"path: "+req.path()+"<br>"+
    	"data: "+req.data()
    );
  }
  
  public void respondUDP (ReceiveUDP udp) {
  	System.out.println(
  		"UDP Packet\n"+
  		"response: "+(responses++)+"\n"+
  		"port: "+udp.localPort()+"\n"+
  		"packetId: "+udp.packetId()+"\n"+
  		"data: '"+udp.data()+"'\n"
  	);
  	Thread.sleep(500);
  	udp.reply( "UDP works! Received data("+udp.packetId()+"): "+udp.data() );
  }

}
