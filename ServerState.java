package paddle;

/*
- A ServerState instance is a vehicle for maintaining the state of the server in
  one object.
- This one object is passed to each Session of each Server.
*/

public class ServerState {

  public void respondHTTP (RequestHTTP req, ResponseHTTP res) {
    System.out.println( req );
    res.setBody( "<h1>HTTP works!<h1><br>Requested path: "+req.path() );
  }
  
  public void respondUDP (ReceiveUDP udp) {
  	System.out.println( "Received '"+udp.data()+"' on port "+udp.localPort() );
  	udp.reply( "UDP works! Received data: "+udp.data() );
  }

}
