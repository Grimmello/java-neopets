import org.sql2o.*;
import java.util.*;
import java.sql.Timestamp;

public class WaterMonster extends Monster{
  private int waterLevel;

  public static final int MAX_WATER_LEVEL = 8;
  public static final String DATABASE_TYPE = "water";

  public WaterMonster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    timer = new Timer();
    type = DATABASE_TYPE;

    playLevel = MAX_PLAY_LEVEL / 2;
    sleepLevel = MAX_SLEEP_LEVEL / 2;
    foodLevel = MAX_FOOD_LEVEL / 2;
  }

  public int getWaterLevel(){
    return waterLevel;
  }

  public void water(){
    if(waterLevel >= MAX_WATER_LEVEL){
      throw new UnsupportedOperationException("You cannot water your pet any more!");
    } else{
      waterLevel++;
    }
  }

  @Override
  public boolean isAlive() {
    if (foodLevel <= MIN_ALL_LEVELS ||
    playLevel <= MIN_ALL_LEVELS ||
    waterLevel <= MIN_ALL_LEVELS ||
    sleepLevel <= MIN_ALL_LEVELS) {
      return false;
    }
    return true;
  }

  @Override
  public void depleteLevels(){
    if (isAlive()){
      playLevel--;
      foodLevel--;
      sleepLevel--;
      waterLevel--;
    }
  }

  public static List<WaterMonster> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM monsters WHERE type='water';";
      return con.createQuery(sql).executeAndFetch(WaterMonster.class);
    }
  }

  public static WaterMonster find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM monsters where id=:id";
      WaterMonster monster = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(WaterMonster.class);
      return monster;
    }
  }
}
