import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PatronTest {
  private Patron firstPatron;
  private Patron secondPatron;

  @Before
  public void initialize() {
    firstPatron = new Patron("seashells33", "elvis55", "Myrtle", false);
    secondPatron = new Patron("jimmybobjoe", "imhere", "Joe-Bob", true);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Patron_instantiatesCorrectly_true() {
    assertEquals(true, firstPatron instanceof Patron);
  }

  @Test
  public void getName_returnsName_String() {
    assertEquals("Myrtle", firstPatron.getName());
  }

  @Test
  public void getUsername_returnsUsername_String() {
    assertEquals("seashells33", firstPatron.getUsername());
  }

  @Test
  public void getPassword_returnsPassword_String() {
    assertEquals("elvis55", firstPatron.getPassword());
  }

  @Test
  public void isLibrarian_returnsLibrarian_boolean() {
    assertEquals(false, firstPatron.isLibrarian());
  }

  @Test
  public void getId_returnsId_true() {
    firstPatron.save();
    assertTrue(firstPatron.getId() > 0);
  }

  @Test
  public void getCheckedBooks_returnsCheckedBooks_int() {
    assertEquals(0, firstPatron.getCheckedBooks());
  }

  @Test
  public void getBooks_returnListOfBooks_True() {
    firstPatron.save();
    Book firstBook = new Book("Moby Dick", "Melville");
    firstBook.save();
    firstBook.checkout(firstPatron.getId());
    Book secondBook = new Book("Dreams from my Father", "Barack Obama");
    secondBook.save();
    secondBook.checkout(firstPatron.getId());
    assertEquals(firstPatron.getBooks().get(0).getId(), Book.find(firstBook.getId()).getId());
    assertEquals(firstPatron.getBooks().get(1).getId(), Book.find(secondBook.getId()).getId());
  }

  @Test
  public void getBookHistory_returnListOfBooks_True() {
    firstPatron.save();
    Book firstBook = new Book("Moby Dick", "Melville");
    firstBook.save();
    firstBook.checkout(firstPatron.getId());
    Book secondBook = new Book("Dreams from my Father", "Barack Obama");
    secondBook.save();
    secondBook.checkout(firstPatron.getId());
    assertTrue(firstPatron.getBookHistory().contains(firstBook.getId()));
    assertTrue(firstPatron.getBookHistory().contains(secondBook.getId()));
  }

  @Test
  public void all_returnsAllInstancesOfPatron_true() {
    firstPatron.save();
    secondPatron.save();
    assertTrue(Patron.all().get(0).equals(firstPatron));
    assertTrue(Patron.all().get(1).equals(secondPatron));
  }

  @Test
  public void find_returnsPatronWithSameId_secondPatron() {
    firstPatron.save();
    secondPatron.save();
    assertEquals(Patron.find(secondPatron.getId()), secondPatron);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Patron myPatron = new Patron("seashells33", "elvis55", "Myrtle", false);
    assertTrue(firstPatron.equals(myPatron));
  }

  @Test
  public void save_returnsTrueIfNamesAreTheSame() {
    firstPatron.save();
    assertTrue(Patron.all().get(0).equals(firstPatron));
  }

  @Test
  public void save_assignsIdToObject() {
    firstPatron.save();
    Patron savedPatron = Patron.all().get(0);
    assertEquals(firstPatron.getId(), savedPatron.getId());
  }

  @Test
  public void updateName_updatesPatronName_true() {
    firstPatron.save();
    firstPatron.updateName("Myrtle Fitzgerald");
    assertEquals("Myrtle Fitzgerald", Patron.find(firstPatron.getId()).getName());
  }

  @Test
  public void updateUsername_updatesPatronUsername_true() {
    firstPatron.save();
    firstPatron.updateUsername("seashells88");
    assertEquals("seashells88", Patron.find(firstPatron.getId()).getUsername());
  }

  @Test
  public void updatePassword_updatesPatronPassword_true() {
    firstPatron.save();
    firstPatron.updatePassword("belieber16");
    assertEquals("belieber16", Patron.find(firstPatron.getId()).getPassword());
  }

  @Test
  public void delete_deletesPatron_true() {
    firstPatron.save();
    int firstPatronId = firstPatron.getId();
    firstPatron.delete();
    assertEquals(null, Patron.find(firstPatronId));
  }


  @Test
  public void checkoutBook_increasesCount_int(){
    firstPatron.save();
    firstPatron.checkoutBook();
    assertEquals(1, Patron.find(firstPatron.getId()).getCheckedBooks());
  }

  @Test (expected = UnsupportedOperationException.class)
  public void checkoutBook_provokesException_exception(){
    for (int i = 0; i <=(Patron.MAX_CHECKED_BOOKS + 1); i++) {
      firstPatron.checkoutBook();
    }
  }

  @Test
  public void checkoutBook_catchException_exception(){
    for (int i = 0; i <=(Patron.MAX_CHECKED_BOOKS + 1); i++) {
      try {
        firstPatron.checkoutBook();
      } catch(UnsupportedOperationException exception){}
    }
    assertTrue(firstPatron.getCheckedBooks()<=Patron.MAX_CHECKED_BOOKS);

  }

  @Test
  public void returnBook_lowersCount_int(){
    firstPatron.save();
    firstPatron.checkoutBook();
    firstPatron.returnBook();
    assertEquals(0, Patron.find(firstPatron.getId()).getCheckedBooks());
  }

}
