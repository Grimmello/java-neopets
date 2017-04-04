import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class MonsterTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void monster_initializesCorrectly(){
    Monster testMonster = new Monster("Bob", 1);
    assertTrue(testMonster instanceof Monster);
  }

  @Test
  public void getName_returnsMonsterName() {
    Monster testMonster = new Monster("Bob", 1);
    assertEquals("Bob", testMonster.getName());
  }

  @Test
  public void getPersonId_returnsPersonId_1() {
    Monster testMonster = new Monster("Bob", 1);
    assertEquals(1, testMonster.getPersonId());
  }

  @Test
  public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
    Monster testMonster = new Monster("Bubbles", 1);
    Monster otherMonster = new Monster("Bubbles", 1);
    assertTrue(testMonster.equals(otherMonster));
  }

  @Test
  public void save_assignsIdToMonster() {
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    Monster savedMonster = Monster.all().get(0);
    assertEquals(savedMonster.getId(), testMonster.getId());
  }

  @Test
  public void all_returnsAllInstances(){
    Monster firstMonster = new Monster("Bubbles", 1);
    firstMonster.save();
    Monster secondMonster = new Monster("Spud", 1);
    secondMonster.save();
    assertTrue(Monster.all().get(0).equals(firstMonster));
    assertTrue(Monster.all().get(1).equals(secondMonster));
  }

  @Test
  public void find_returnsCorrectObject(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    assertEquals(testMonster, Monster.find(testMonster.getId()));
  }

  @Test
  public void save_savesPersonIntoDB(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    Monster testMonster = new Monster("Bubbles", testPerson.getId());
    testMonster.save();
    Monster savedMonster = Monster.find(testMonster.getId());
    assertEquals(testPerson.getId(), savedMonster.getPersonId());
  }

  @Test
  public void monster_initializesWithHalfFullPlayLeveL(){
    Monster testMonster = new Monster("Bubbles", 1);
    assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2));
  }

  @Test
  public void monster_initializesWithHalfFullFoodLevel(){
    Monster testMonster = new Monster("Bubbles", 1);
    assertEquals(testMonster.getfoodLevel(), (Monster.MAX_FOOD_LEVEL / 2));
  }

  @Test
  public void isAlive_confirmsMonsterIsAliveIfAllLevelsAboveMin(){
    Monster testMonster = new Monster("Bubbles", 1);
    assertTrue(testMonster.isAlive());
  }

  @Test
  public void depleteLevels_depletesAllLevels(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.depleteLevels();
    assertEquals(testMonster.getfoodLevel(), (Monster.MAX_FOOD_LEVEL / 2) - 1);
    assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2) - 1);
    assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL / 2) - 1);
  }

  @Test
  public void isAlive_recognizesMonsterIsDeadWhenLevelsReachMin(){
    Monster testMonster = new Monster("Bubbles", 1);
    for(int i = Monster.MIN_ALL_LEVELS; i <= Monster.MAX_FOOD_LEVEL; i++){
      testMonster.depleteLevels();
    }
    assertEquals(false, testMonster.isAlive());
  }

  @Test
  public void play_increasesPlayLevel(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.play();
    assertTrue(testMonster.getPlayLevel() > (Monster.MAX_PLAY_LEVEL / 2));
  }

  @Test
  public void sleep_increasesSleepLevel(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.sleep();
    assertTrue(testMonster.getSleepLevel() > (Monster.MAX_SLEEP_LEVEL / 2));
  }

  @Test
  public void feed_increasesFoodLevel(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.feed();
    assertTrue(testMonster.getfoodLevel() > (Monster.MAX_FOOD_LEVEL / 2));
  }

  @Test
  public void monster_foodLevelCannotGoBeyondMaxValue(){
    Monster testMonster = new Monster("Bubbles", 1);
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_FOOD_LEVEL); i++){
      try {
        testMonster.feed();
      } catch (UnsupportedOperationException exception) {}
    }
    assertTrue(testMonster.getfoodLevel() <= Monster.MAX_FOOD_LEVEL);
  }

  @Test
  public void monster_sleepLevelCannotGoBeyondMaxValue(){
    Monster testMonster = new Monster("Bubbles", 1);
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_SLEEP_LEVEL); i++){
      try {
        testMonster.sleep();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testMonster.getSleepLevel() <= Monster.MAX_SLEEP_LEVEL);
  }

  @Test
  public void save_recordsTimeOfCreationInDatabase() {
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    Timestamp savedMonsterBirthday = Monster.find(testMonster.getId()).getBirthday();
    assertEquals(rightNow.getDay(), savedMonsterBirthday.getDay());
  }

  @Test
  public void sleep_recordsTimeLastSleptInDatabase(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    testMonster.sleep();
    Timestamp saveMonsterLastSlept = Monster.find(testMonster.getId()).getLastSlept();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveMonsterLastSlept));
  }

  @Test
  public void feed_recordsTimeLastAteInDatabase(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    testMonster.feed();
    Timestamp saveMonsterLastAte = Monster.find(testMonster.getId()).getLastAte();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveMonsterLastAte));
  }

  @Test
  public void play_recordsTimeLastPlayed(){
    Monster testMonster = new Monster("Bubbles", 1);
    testMonster.save();
    testMonster.play();
    Timestamp saveMonsterLastPlayed = Monster.find(testMonster.getId()).getLastPlayed();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveMonsterLastPlayed));
  }
}
