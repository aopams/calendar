package edu.brown.cs.andrew.calendar;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;

public class Main {
  
  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis()/1000);
    try {
      DatabaseHandler myHandler = 
          new DatabaseHandler("calendar.sqlite3");
      myHandler.createTablesForCalendar();
      myHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy", "hyeddana@cs.brown.edu");
      myHandler.deleteUser("Harsha");
      myHandler.deleteUser("Harsha2");
      myHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy", "hyeddana@cs.brown.edu");
      myHandler.insertUser("Harsha2", "meow", "Harsha Yeddanapudy", "hyeddana@cs.brown.edu");
      myHandler.addFriendRequest("Harsha", "Harsha2");
      myHandler.addFriendRequest("Harsha2", "Harsha");
      myHandler.acceptFriendRequest("Harsha2", "Harsha");
      myHandler.removeFriend("Harsha", "Harsha2");
      myHandler.addGroup("Harsha Squad");
      myHandler.addUserToGroup("Harsha", 1);
      myHandler.addUserToGroup("Harsha2", 1);
      Date myDate = new SimpleDateFormat("dd/M/yyyy").parse("03/4/2015");
      System.out.println(myDate.toString());
     List<String> hSquad = new ArrayList<String>();
     hSquad.add("Harsha");
     hSquad.add("Harsha2");
     List<String> hGroup = new ArrayList<String>();
     hGroup.add("Harsha");
      Event e = new Event(myDate, "Party Time!!!!", "Friday",
          hSquad, "",
          180,
          "Harsha Squad going Ham to Trap Queen for 3 hours");
      Date myDate2 = new SimpleDateFormat("dd/M/yyyy").parse("06/4/2015");
      Event e2 = new Event(myDate2, "Ninja Time!", "Monday",
          hGroup, "",
          30,
          "Harsha going stealth-mode");
      System.out.println(myHandler.findGroup("Harsha Squad"));
      myHandler.addEvent(e);
      myHandler.addEvent(e2);
      List<Event> events = myHandler.getEventsFromUser("Harsha");
      System.out.println(System.currentTimeMillis()/1000);
    } catch (ClassNotFoundException | SQLException | ParseException e) {
      e.printStackTrace();
    }
  }
}