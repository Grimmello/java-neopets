import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PersonTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void person_initializesCorrectly(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    assertEquals(true, testPerson instanceof Person);
  }

  @Test
  public void getName_returnsName(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    assertEquals("Henry", testPerson.getName());
  }

  @Test
  public void getEmail_returnsEmail(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    assertEquals("henry@henry.com", testPerson.getEmail());
  }

  @Test
  public void all_returnsAllPersons(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    assertEquals(true, Person.all().contains(testPerson));
  }

  @Test
  public void save_assignsIdToObject(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    Person savedPerson = Person.all().get(0);
    assertEquals(testPerson.getId(), savedPerson.getId());
  }

  @Test
  public void find_returnsCorrectObject(){
    Person firstPerson = new Person("Henry", "henry@henry.com");
    firstPerson.save();
    Person secondPerson = new Person("Martha", "martha@martha.com");
    secondPerson.save();
    assertEquals(secondPerson, Person.find(secondPerson.getId()));
  }

  @Test
  public void getMonsters_returnsAllMonstersFromDB(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    Monster firstMonster = new Monster("Bubbles", testPerson.getId());
    firstMonster.save();
    Monster secondMonster = new Monster("Spud", testPerson.getId());
    secondMonster.save();
    Monster[] monsters = new Monster[] {firstMonster, secondMonster};
    assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
  }
}
