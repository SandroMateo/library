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

  public Book(String title, String author) {
    this.title = title;
    this.author = author;
    this.renewals = 0;
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

  public void save(){
    try(Connection con = DB.sql2o()) {
      String sql = "INSERT INTO books (title, author) VALUES (:title, :author)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("author", this.author)
        .executeUpdate()
        .getKey();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM books WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id",this.id)
        .executeUpdate();
    }
  }

  public static List<Book> all(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books";
      return con.createQuery(sql)
        .executeAndFetch(Book.class);
    }
  }

  public static Book find(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetchFirst(Book.class);
    }
  }

  @Override
  public boolean equals(Object otherBook){
    if(!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
        this.getAuthor().equals(newBook.getAuthor()) &&
        this.getCheckoutDate().equals(newBook.getCheckoutDate()) &&
        this.getDueDate().equals(newBook.getDueDate()) &&
        this.getPatronId() == newBook.getPatronId() &&
        this.getRenewals() == newBook.getRenewals();
    }
  }


}
