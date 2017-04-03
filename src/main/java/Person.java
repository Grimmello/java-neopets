import org.sql2o.*;
import java.util.*;

public class Person {
  private String name;
  private String email;
  private int id;

  public Person(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName(){
    return name;
  }

  public String getEmail(){
    return email;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO persons (name, email) VALUES (:name, :email)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("email", this.email)
        .executeUpdate()
        .getKey();
    }
  }

  public List<Monster> getMonsters(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM monsters WHERE personid=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Monster.class);
    }
  }

  public static List<Person> all() {
    String sql = "SELECT * FROM persons";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Person.class);
    }
  }

  public static Person find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM persons WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Person.class);
    }
  }

  @Override
  public boolean equals(Object otherPerson) {
    if (!(otherPerson instanceof Person)) {
      return false;
    } else {
      Person newPerson = (Person) otherPerson;
      return this.getEmail().equals(newPerson.getEmail()) &&
             this.getName().equals(newPerson.getName()) &&
             this.getId() == newPerson.getId();
    }
  }

}
