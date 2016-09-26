import org.sql2o.*;

public class Patron{
  private int id;
  private String username;
  private String password;
  private String name;
  private int checkedBooks;

  public static final int MAX_CHECKED_BOOKS = 5;

  public Patron(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }

}