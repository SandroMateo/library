import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Patron {
  public int id;
  public String username;
  public String password;
  public String name;
  public int checkedBooks;
  public boolean librarian;

  public static final int MAX_CHECKED_BOOKS = 5;

  public Patron(String username, String password, String name, boolean librarian) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.librarian = librarian;
    this.checkedBooks = 0;
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

  public int getCheckedBooks(){
    return checkedBooks;
  }

  public boolean isLibrarian() {
    return librarian;
  }

  public void updateName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateUsername(String username) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET username = :username WHERE id = :id";
      con.createQuery(sql)
        .addParameter("username", username)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updatePassword(String password) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET password = :password WHERE id = :id";
      con.createQuery(sql)
        .addParameter("password", password)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public static List<Patron> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patrons";
      return con.createQuery(sql).executeAndFetch(Patron.class);
    }
  }

  public static Patron find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patrons WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patron.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO patrons (username, password, name, librarian) VALUES(:username, :password, :name, :librarian)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("username", this.username)
        .addParameter("password", this.password)
        .addParameter("name", this.name)
        .addParameter("librarian", this.librarian)
        .executeUpdate()
        .getKey();
    }
  }

  public void checkoutBook() {
    if(checkedBooks >= Patron.MAX_CHECKED_BOOKS) {
      throw new UnsupportedOperationException("You have already checked out the max amount of books!");
    }
    this.checkedBooks++;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET checkedBooks = :checkedBooks WHERE id=:id";
      con.createQuery(sql)
        .addParameter("checkedBooks", this.checkedBooks)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void returnBook() {
    this.checkedBooks--;
    try(Connection con = DB.sql2o.open()){
      String sql = "UPDATE patrons SET checkedBooks=:checkedBooks WHERE id=:id";
      con.createQuery(sql)
        .addParameter("checkedBooks", this.checkedBooks)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patrons WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public List<Book> getBooks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE patronid = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Book.class);
    }
  }

  public List<Integer> getBookHistory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT bookid FROM histories WHERE patronid = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Integer.class);
    }
  }

  @Override
  public boolean equals(Object otherPatron) {
    if(!(otherPatron instanceof Patron)) {
      return false;
    } else {
      Patron newPatron = (Patron) otherPatron;
      return this.username.equals(newPatron.getUsername()) &&
             this.password.equals(newPatron.getPassword()) &&
             this.name.equals(newPatron.getName()) &&
             this.id == newPatron.getId() &&
             this.checkedBooks == newPatron.getCheckedBooks();
    }
  }
}
