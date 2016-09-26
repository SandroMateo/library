import org.sql2o.*;

public class Patron {
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
      String sql = "INSERT INTO patrons (username, password, name) VALUES(:username, :password, :name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("username", this.username)
        .addParameter("password", this.password)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public void updateCheckedBooks() {
    if(checkedBooks >= Patron.MAX_CHECKED_BOOKS) {
      throw new UnsupportedOperationException("You have already checked out the max amount of books!");
    }
    this.checkedBooks++;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET checkedBooks = :checkedBooks";
      con.createQuery(sql)
        .addParameter("checkedBooks", this.checkedBooks)
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
