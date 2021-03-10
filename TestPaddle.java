import paddle.*;

public class TestPaddle {
  public static void main(String[] args) throws Exception {

    CountRequests c = new CountRequests();

    ServerHTTP p1 = new ServerHTTP( c, Integer.valueOf(args[0]) );
    ServerHTTP p2 = new ServerHTTP( c, Integer.valueOf(args[1]) );
    ServerHTTP p3 = new ServerHTTP( c, Integer.valueOf(args[2]) );


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

  public void respondHTTP ( RequestHTTP req, ResponseHTTP res ) {
    count++;
    res.setBody( "<h1>Count: "+count+"</h1>" );
  }

}
