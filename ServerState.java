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
  		"Protocol:      "+c.protocol()+"\n"+
  		"Direction:     "+(c.inbound() ? "IN" : "OUT")+"\n"+
  		"Server Name:   "+c.server().name()+"\n"+
  		"Connection ID: "+c.connectionId()+"\n"+
  		"Remote Addr:   "+c.remoteAddress()+":"+c.remotePort()+"\n"+
  		"Local Addr:    "+c.localAddress()+":"+c.localPort()+"\n"+
  		"Data:          "+(c.data() != null ? new String(c.data()) : "")+"\n"
  	);
	}

  public void respond ( InboundHTTP session ) {
  	count++;
    session.response().setBody(
    	"<h1>HTTP works!</h1><br>"+
    	"path: "+session.request().path()+"<br>"+
    	"body: "+session.request().body()
    );
    printConnection( session );
  }
  
  public void respond ( InboundUDP rxPacket ) {
  	count++;
    printConnection( rxPacket );
    try {
  		Thread.sleep(500);
  		OutboundUDP txPacket = rxPacket.reply(
  			"UDP works! Received data ("+rxPacket.connectionId()+"): "+(new String(rxPacket.data()))
  		);
  		printConnection( txPacket );
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  }
  
}
