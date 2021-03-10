package paddle;

/*
- A ServerState instance is a vehicle for maintaining the state of the server in
  one object.
- This one object is passed to each Session of each Server.
*/

public class ServerState {

  public void createResponse (Request req, Response res) {
    System.out.println( req );
    res.setBody( "<h1>It works!<h1>" );
  }

}
