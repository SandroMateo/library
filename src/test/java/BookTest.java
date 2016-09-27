import java.text.DateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BookTest {
  private Book firstBook;
  private Book secondBook;
  private Patron firstPatron;

  @Before
  public void initialize() {
    firstBook = new Book("Moby Dick", "Melville");
    secondBook = new Book("Dreams from my Father", "Barack Obama");
    firstPatron = new Patron("seashells33", "elvis55", "Myrtle", false);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Book_instantiatesCorrectly_true() {
    assertEquals(true, firstBook instanceof Book);
  }

  @Test
  public void getTitle_returnsTitle_String() {
    assertEquals("Moby Dick", firstBook.getTitle());
  }

  @Test
  public void getAuthor_returnsAuthor_String() {
    assertEquals("Melville", firstBook.getAuthor());
  }

  @Test
  public void getCheckoutDate_returnsCheckoutDate_String() {
    firstPatron.save();
    firstBook.save();
    firstBook.checkout(firstPatron.getId());
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), Book.find(firstBook.getId()).getCheckoutDate().getDay());
  }

  @Test
  public void getDueDate_returnsDueDate_String() {
    firstPatron.save();
    firstBook.save();
    firstBook.checkout(firstPatron.getId());
    Timestamp rightNow = new Timestamp(new Date().getTime());
    Calendar cal = Calendar.getInstance();
    cal.setTime(rightNow);
    cal.add(Calendar.DAY_OF_WEEK, 60);
    rightNow.setTime(cal.getTime().getTime());
    assertEquals(rightNow.getDay(), Book.find(firstBook.getId()).getDueDate().getDay());
  }

  @Test
  public void getRenewals_returnsrenewals_int() {
    firstBook.save();
    assertEquals(0, firstBook.getRenewals());
  }

  @Test
  public void getId_returnsId_true() {
    firstBook.save();
    assertTrue(firstBook.getId() > 0);
  }

  @Test
  public void all_returnsAllInstancesOfBook_true() {
    firstBook.save();
    secondBook.save();
    assertTrue(Book.all().get(0).equals(firstBook));
    assertTrue(Book.all().get(1).equals(secondBook));
  }

  @Test
  public void find_returnsBookWithSameId_secondBook() {
    firstBook.save();
    secondBook.save();
    assertEquals(Book.find(secondBook.getId()), secondBook);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Book myBook = new Book("Moby Dick", "Melville");
    assertTrue(firstBook.equals(myBook));
  }

  @Test
  public void save_returnsTrueIfNamesAreTheSame() {
    firstBook.save();
    assertTrue(Book.all().get(0).equals(firstBook));
  }

  @Test
  public void save_assignsIdToObject() {
    firstBook.save();
    Book savedBook = Book.all().get(0);
    assertEquals(firstBook.getId(), savedBook.getId());
  }

  @Test
  public void updateTitle_updatesBookTitle_true() {
    firstBook.save();
    firstBook.updateTitle("Moby Dick");
    assertEquals("Moby Dick", Book.find(firstBook.getId()).getTitle());
  }

  @Test
  public void updateAuthor_updatesBookAuthor_true() {
    firstBook.save();
    firstBook.updateAuthor("Melville");
    assertEquals("Melville", Book.find(firstBook.getId()).getAuthor());
  }

  @Test
  public void delete_deletesBook_true() {
    firstBook.save();
    int firstBookId = firstBook.getId();
    firstBook.delete();
    assertEquals(null, Book.find(firstBookId));
  }


  @Test
  public void checkout_updatesPatronId_int(){
    firstBook.save();
    firstPatron.save();
    firstBook.checkout(firstPatron.getId());
    assertEquals(firstPatron.getId(), Book.find(firstBook.getId()).getPatronId());
  }

  @Test
  public void returnThisBook_updatesPatronIdAndRenewals_int(){
    firstBook.save();
    firstPatron.save();
    firstBook.checkout(firstPatron.getId());
    Book.find(firstBook.getId()).returnThisBook();
    assertEquals(0, Book.find(firstBook.getId()).getPatronId());
    assertEquals(0, Book.find(firstBook.getId()).getRenewals());
  }

}
