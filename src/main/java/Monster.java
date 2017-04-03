import org.sql2o.*;
import java.util.*;

public class Monster {
  private String name;
  private int id;
  private int personId;

  public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
  }

  public String getName() {
    return name;
  }

  public int getPersonId() {
    return personId;
  }

  public int getId() {
    return id;
  }

  public static List<Monster> all() {
    String sql = "SELECT * FROM monsters";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Monster.class);
    }
  }

  public static Monster find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM monsters WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Monster.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO monsters (name, personid) VALUES (:name, :personId)";
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
