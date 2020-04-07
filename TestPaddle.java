package paddle;

public class TestPaddle {
  public static void main(String[] args) throws Exception {

    CountRequests c = new CountRequests();

    Server p1 = new Server( c, Integer.valueOf(args[0]) );
    Server p2 = new Server( c, Integer.valueOf(args[1]) );
    Server p3 = new Server( c, Integer.valueOf(args[2]) );


    while(true) {
      Thread.sleep(1000);
    }


  }

}


class CountRequests extends ServerState {

  private int count = 0;

  public CountRequests () {
    System.out.println( "CountRequests initialized!");
  }

  public void createResponse ( Request req, Response res ) {
    count++;
    res.setBody( "<h1>Count: "+count+"</h1>" );
  }

}
