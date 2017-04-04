import org.sql2o.*;
import java.util.*;
import java.sql.Timestamp;

public abstract class Monster {
  public String name;
  public int id;
  public int personId;
  public int foodLevel;
  public int sleepLevel;
  public int playLevel;
  public Timestamp birthday;
  public Timestamp lastSlept;
  public Timestamp lastAte;
  public Timestamp lastPlayed;

  public Timer timer;

  public static final int MAX_FOOD_LEVEL = 3;
  public static final int MAX_SLEEP_LEVEL = 8;
  public static final int MAX_PLAY_LEVEL = 12;
  public static final int MIN_ALL_LEVELS = 0;

  public String getName() {
    return name;
  }

  public int getPersonId() {
    return personId;
  }

  public int getId() {
    return id;
  }

  public int getSleepLevel(){
    return sleepLevel;
  }

  public int getFoodLevel(){
    return foodLevel;
  }

  public int getPlayLevel(){
    return playLevel;
  }

  public Timestamp getBirthday(){
    return birthday;
  }

  public Timestamp getLastSlept(){
    return lastSlept;
  }

  public Timestamp getLastAte(){
    return lastAte;
  }

  public Timestamp getLastPlayed(){
    return lastPlayed;
  }

  public boolean isAlive(){
    if(foodLevel <= MIN_ALL_LEVELS || playLevel <= MIN_ALL_LEVELS || sleepLevel <= MIN_ALL_LEVELS){
      return false;
    } else{
      return true;
    }
  }

  public void depleteLevels(){
    if(isAlive()){
      playLevel--;
      foodLevel--;
      sleepLevel--;
    }
  }

  public void startTimer(){
    Monster currentMonster = this;
    TimerTask timerTask = new TimerTask(){
      @Override
      public void run(){
        if(currentMonster.isAlive() == false){
          cancel();
        } else{
          depleteLevels();
        }
      }
    };
    this.timer.schedule(timerTask, 0, 600);
  }

  public void play(){
    if(playLevel >= MAX_PLAY_LEVEL){
      throw new UnsupportedOperationException("You cannot play with monster anymore!");
    } else{
      try(Connection con = DB.sql2o.open()){
        String sql = "UPDATE monsters SET lastplayed = now() WHERE id = :id";
        con.createQuery(sql)
          .addParameter("id", this.id)
          .executeUpdate();
      }
      playLevel++;
    }
  }

  public void sleep(){
    if (sleepLevel >= MAX_SLEEP_LEVEL){
      throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
    } else{
      try(Connection con = DB.sql2o.open()){
        String sql = "UPDATE monsters SET lastslept = now() WHERE id=:id";
        con.createQuery(sql)
          .addParameter("id", this.id)
          .executeUpdate();
      }
      sleepLevel++;
    }
  }

  public void feed(){
    if(foodLevel >= MAX_FOOD_LEVEL){
      throw new UnsupportedOperationException("You cannot feed your monster anymore.");
    } else{
      try(Connection con = DB.sql2o.open()){
        String sql = "UPDATE monsters SET lastAte = now() WHERE id=:id";
        con.createQuery(sql)
          .addParameter("id", this.id)
          .executeUpdate();
      }
      foodLevel++;
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO monsters (name, personid, birthday) VALUES (:name, :personId, now())";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("personId", this.personId)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
 public boolean equals(Object otherMonster){
   if (!(otherMonster instanceof Monster)) {
     return false;
   } else {
     Monster newMonster = (Monster) otherMonster;
     return this.getName().equals(newMonster.getName()) &&
            this.getPersonId() == newMonster.getPersonId();
   }
 }
}
