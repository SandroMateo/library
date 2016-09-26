import org.sql2o.*;

public class Book{
  private int id;
  private String title;
  private String author;
  private int patronId;
  private String checkoutDate;
  private String dueDate;
  private int renewals;

  public static final int MAX_RENEWALS = 2;

  public Book(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }

}
