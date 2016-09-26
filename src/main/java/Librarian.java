import org.sql2o.*;

public class Librarian{
  private int id;
  private String username;
  private String password;
  private String name;


  public Librarian(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }

}
