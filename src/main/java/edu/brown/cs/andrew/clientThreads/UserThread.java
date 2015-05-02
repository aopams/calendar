package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class UserThread implements Callable<String>{
  private String user;
  private String pass;
  private String name;
  private DatabaseHandler myDBHandler;
  
  public UserThread(String user, String pass, String name) {
    this.user = user;
    this.pass = pass;
    this.name = name;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (ClassNotFoundException | SQLException e) {
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
