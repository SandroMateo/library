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

  public int getId(){
    return id;
  }

  public String getTitle(){
    return title;
  }

  public String getAuthor(){
    return author;
  }

  public String getPatronId(){
    return patronId;
  }

  public String getCheckoutDate(){
    return checkoutDate;
  }

  public String getDueDate(){
    return dueDate;
  }

  public String getRenewals(){
    return renewals;
  }


}
