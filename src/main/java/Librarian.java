import org.sql2o.*;

public class Librarian extends Patron{

  public Librarian(String username, String password, String name) {
    super(username, password, name);
    this.checkedBooks = 0;
    this.librarian = true;
  }

  public boolean getActive(){
    return librarian;
  }

}
