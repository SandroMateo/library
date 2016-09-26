import org.sql2o.*;

public class Librarian{
  private int id;
  private String username;
  private String password;
  private String name;
  private static boolean active;

  public Librarian(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }

  public int getId(){
    return id;
  }

  public String getUsername(){
    return username;
  }

  public String getPassword(){
    return password;
  }

  public String getName(){
    return name;
  }

  public static boolean getActive(){
    return active;
  }

  public void save(){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO librarians (username, password, name) VALUES(:username, :password, :name)";
      this.id = (int) createQuery(sql, true)
        .addParameter("username", this.username)
        .addParameter("password", this.password)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }





}
