package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
/**
 * User thread handles registration.
 * @author wtruong02151
 *
 */
public class UserThread implements Callable<String> {
  private String user;
  private String pass;
  private String name;
  private DatabaseHandler myDBHandler;
  /**
   * User thread constructor, initializes global variables.
   * @param user username
   * @param pass password
   * @param name full name
   */
  public UserThread(String user, String pass, String name) {
    this.user = user;
    this.pass = pass;
    this.name = name;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public String call() throws Exception {
    if (!myDBHandler.findUser(user).isEmpty()) {
      System.out.println("user found");
      return "taken";
    }
    myDBHandler.insertUser(user, pass, name);
    myDBHandler.closeConnection();
    return "nottaken";
  }
}
