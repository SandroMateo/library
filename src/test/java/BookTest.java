import java.text.DateFormat;
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
    firstPatron = new Patron("seashells33", "elvis55", "Myrtle");
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
    firstBook.checkout(firstPatron.getId());
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), firstBook.getCheckoutDate().getDay());
  }

  @Test
  public void getId_returnsId_true() {
    firstBook.save();
    assertTrue(firstBook.getId() > 0);
  }

  @Test
  public void getCheckedBooks_returnsCheckedBooks_int() {
    assertEquals(0, firstBook.getCheckedBooks());
  }

  @Test
  public void getBooks_returnListOfBooks_True() {
    firstBook.save();
    Book firstBook = new Book("Moby Dick", "Melville");
    firstBook.save();
    firstBook.checkout(firstBook.getId());
    Book secondBook = new Book("Dreams from my Father", "Barrack Obama");
    secondBook.save();
    secondBook.checkout(firstBook.getId());
    assertTrue(firstBook.getBooks().contains(firstBook));
    assertTrue(firstBook.getBooks().contains(secondBook));
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
    Book myBook = new Book("seashells33", "elvis55", "Myrtle");
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
  public void updateName_updatesBookName_true() {
    firstBook.save();
    firstBook.updateName("Myrtle Fitzgerald");
    assertEquals("Myrtle Fitzgerald", Book.find(firstBook.getId()).getName());
  }

  @Test
  public void updateUsername_updatesBookUsername_true() {
    firstBook.save();
    firstBook.updateUsername("seashells88");
    assertEquals("seashells88", Book.find(firstBook.getId()).getUsername());
  }

  @Test
  public void updatePassword_updatesBookPassword_true() {
    firstBook.save();
    firstBook.updatePassword("belieber16");
    assertEquals("belieber16", Book.find(firstBook.getId()).getPassword());
  }

  @Test
  public void delete_deletesBook_true() {
    firstBook.save();
    int firstBookId = firstBook.getId();
    firstBook.delete();
    assertEquals(null, Book.find(firstBookId));
  }


  @Test
  public void checkoutBook_increasesCount_int(){
    firstBook.save();
    firstBook.checkoutBook();
    assertEquals(1, Book.find(firstBook.getId()).getCheckedBooks());
  }

  @Test
  public void returnBook_lowersCount_int(){
    firstBook.save();
    firstBook.checkoutBook();
    firstBook.returnBook();
    assertEquals(0, Book.find(firstBook.getId()).getCheckedBooks());
  }

}
