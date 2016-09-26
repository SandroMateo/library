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
    try(Connection con = sql2o.open()){
      String sql = "INSERT INTO librarians ";
      this.id = (int) createQuery(sql, true)
        
        .executeUpdate();
        .getKey();
    }
  }





}
