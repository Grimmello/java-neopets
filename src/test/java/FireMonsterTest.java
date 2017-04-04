import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class FireMonsterTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void monster_initializesCorrectly(){
    FireMonster testFireMonster = new FireMonster("Bob", 1);
    assertTrue(testFireMonster instanceof FireMonster);
  }

  @Test
  public void getName_returnsFireMonsterName() {
    FireMonster testFireMonster = new FireMonster("Bob", 1);
    assertEquals("Bob", testFireMonster.getName());
  }

  @Test
  public void getPersonId_returnsPersonId_1() {
    FireMonster testFireMonster = new FireMonster("Bob", 1);
    assertEquals(1, testFireMonster.getPersonId());
  }

  @Test
  public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    FireMonster otherFireMonster = new FireMonster("Bubbles", 1);
    assertTrue(testFireMonster.equals(otherFireMonster));
  }

  @Test
  public void save_assignsIdToFireMonster() {
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    FireMonster savedFireMonster = FireMonster.all().get(0);
    assertEquals(savedFireMonster.getId(), testFireMonster.getId());
  }

  @Test
  public void all_returnsAllInstances(){
    FireMonster firstFireMonster = new FireMonster("Bubbles", 1);
    firstFireMonster.save();
    FireMonster secondFireMonster = new FireMonster("Spud", 1);
    secondFireMonster.save();
    assertTrue(FireMonster.all().get(0).equals(firstFireMonster));
    assertTrue(FireMonster.all().get(1).equals(secondFireMonster));
  }

  @Test
  public void find_returnsCorrectObject(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    assertEquals(testFireMonster, FireMonster.find(testFireMonster.getId()));
  }

  @Test
  public void save_savesPersonIntoDB(){
    Person testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
    FireMonster testFireMonster = new FireMonster("Bubbles", testPerson.getId());
    testFireMonster.save();
    FireMonster savedFireMonster = FireMonster.find(testFireMonster.getId());
    assertEquals(testPerson.getId(), savedFireMonster.getPersonId());
  }

  @Test
  public void monster_initializesWithHalfFullPlayLeveL(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2));
  }

  @Test
  public void monster_initializesWithHalfFullFoodLevel(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2));
  }

  @Test
  public void isAlive_confirmsFireMonsterIsAliveIfAllLevelsAboveMin(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    assertTrue(testFireMonster.isAlive());
  }

  @Test
  public void depleteLevels_depletesAllLevels(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.depleteLevels();
    assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2) - 1);
    assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2) - 1);
    assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2) - 1);
  }

  @Test
  public void isAlive_recognizesFireMonsterIsDeadWhenLevelsReachMin(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    for(int i = FireMonster.MIN_ALL_LEVELS; i <= FireMonster.MAX_FOOD_LEVEL; i++){
      testFireMonster.depleteLevels();
    }
    assertEquals(false, testFireMonster.isAlive());
  }

  @Test
  public void play_increasesPlayLevel(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.play();
    assertTrue(testFireMonster.getPlayLevel() > (FireMonster.MAX_PLAY_LEVEL / 2));
  }

  @Test
  public void sleep_increasesSleepLevel(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.sleep();
    assertTrue(testFireMonster.getSleepLevel() > (FireMonster.MAX_SLEEP_LEVEL / 2));
  }

  @Test
  public void feed_increasesFoodLevel(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.feed();
    assertTrue(testFireMonster.getFoodLevel() > (FireMonster.MAX_FOOD_LEVEL / 2));
  }

  @Test
  public void monster_foodLevelCannotGoBeyondMaxValue(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_FOOD_LEVEL); i++){
      try {
        testFireMonster.feed();
      } catch (UnsupportedOperationException exception) {}
    }
    assertTrue(testFireMonster.getFoodLevel() <= FireMonster.MAX_FOOD_LEVEL);
  }

  @Test
  public void monster_sleepLevelCannotGoBeyondMaxValue(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_SLEEP_LEVEL); i++){
      try {
        testFireMonster.sleep();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testFireMonster.getSleepLevel() <= FireMonster.MAX_SLEEP_LEVEL);
  }

  @Test
  public void save_recordsTimeOfCreationInDatabase() {
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    Timestamp savedFireMonsterBirthday = FireMonster.find(testFireMonster.getId()).getBirthday();
    assertEquals(rightNow.getDay(), savedFireMonsterBirthday.getDay());
  }

  @Test
  public void sleep_recordsTimeLastSleptInDatabase(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    testFireMonster.sleep();
    Timestamp saveFireMonsterLastSlept = FireMonster.find(testFireMonster.getId()).getLastSlept();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveFireMonsterLastSlept));
  }

  @Test
  public void feed_recordsTimeLastAteInDatabase(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    testFireMonster.feed();
    Timestamp saveFireMonsterLastAte = FireMonster.find(testFireMonster.getId()).getLastAte();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveFireMonsterLastAte));
  }

  @Test
  public void play_recordsTimeLastPlayed(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.save();
    testFireMonster.play();
    Timestamp saveFireMonsterLastPlayed = FireMonster.find(testFireMonster.getId()).getLastPlayed();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(saveFireMonsterLastPlayed));
  }

  @Test
  public void timer_executesDepleteLevelsMethod(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    int firstPlayLevel = testFireMonster.getPlayLevel();
    testFireMonster.startTimer();
    try{
      Thread.sleep(6000);
    } catch(InterruptedException exception){}
    int secondPlayLevel = testFireMonster.getPlayLevel();
    assertTrue(firstPlayLevel > secondPlayLevel);
  }

  @Test
  public void timer_haltsAfterFireMonsterDies(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.startTimer();
    try{
      Thread.sleep(6000);
    } catch(InterruptedException exception){}
    assertFalse(testFireMonster.isAlive());
    assertTrue(testFireMonster.getFoodLevel() >= 0);
  }

  @Test
  public void kindling_increasesFireMonsterFireLevel(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.kindling();
    assertTrue(testFireMonster.getFireLevel() > (FireMonster.MAX_FIRE_LEVEL / 2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void kindling_throwsExceptionIfFireLevelIsAtMax(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_FIRE_LEVEL); i++){
      testFireMonster.kindling();
    }
  }

  @Test
  public void depleteLevels_reducesAllLevels(){
    FireMonster testFireMonster = new FireMonster("Bubbles", 1);
    testFireMonster.depleteLevels();
    assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2) - 1);
    assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2) - 1);
    assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2) - 1);
    assertEquals(testFireMonster.getFireLevel(), (FireMonster.MAX_FIRE_LEVEL / 2) - 1);
  }
}
