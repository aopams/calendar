package edu.brown.cs.andrew.calendar;

import java.sql.SQLException;

import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class Main {
  
  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis()/1000);
    try {
      DatabaseHandler myHandler = 
          new DatabaseHandler("calendar.sqlite3");
      myHandler.createTablesForCalendar();
      myHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy", "hyeddana@cs.brown.edu");
      myHandler.insertUser("Harsha2", "meow", "Harsha Yeddanapudy", "hyeddana@cs.brown.edu");
      myHandler.addFriendRequest("Harsha", "Harsha2");
      myHandler.addFriendRequest("Harsha2", "Harsha");
      myHandler.acceptFriendRequest("Harsha2", "Harsha");
      myHandler.removeFriend("Harsha", "Harsha2");

      System.out.println(System.currentTimeMillis()/1000);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
}