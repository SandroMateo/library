import org.sql2o.*;
import java.text.DateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Book{
  private int id;
  private String title;
  private String author;
  private int patronId;
  private Timestamp checkoutDate;
  private Timestamp dueDate;
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

  public int getPatronId(){
    return patronId;
  }

  public Timestamp getCheckoutDate(){
    return checkoutDate;
  }

  public Timestamp getDueDate(){
    return dueDate;
  }

  public int getRenewals(){
    return renewals;
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
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

  public static Book find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
    }
  }

  public void checkout(int patronId) {
    Patron.find(patronId).checkoutBook();
    try(Connection con = DB.sql2o.open()) {
      String sql1 = "UPDATE books SET patronId = :patronId, checkoutDate = now() WHERE id = :id";
      String sql2 = "UPDATE books SET dueDate = (checkoutDate + INTERVAL '60 days') WHERE id = :id";
      String sql3 = "INSERT INTO histories (patronid, bookid) VALUES (:patronid, :id)";
      con.createQuery(sql1)
        .addParameter("patronId", patronId)
        .addParameter("id", this.id)
        .executeUpdate();
      con.createQuery(sql2)
        .addParameter("id", this.id)
        .executeUpdate();
      con.createQuery(sql3)
        .addParameter("patronid", patronId)
        .addParameter("bookid", this.id)
        .executeUpdate();
    }
  }

  public void renew() {
    if(renewals >= Book.MAX_RENEWALS) {
      throw new UnsupportedOperationException("You have no renewals left. RETURN THIS BOOK");
    }
    this.renewals++;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET renewals = :renewals WHERE id = :id";
      con.createQuery(sql)
        .addParameter("renewals", this.renewals)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void returnThisBook() {
    Patron.find(this.patronId).returnBook();
    this.patronId = 0;
    this.renewals = 0;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET patronId = 0, renewals = 0 WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
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
