package edu.brown.cs.rmchandr.calendar;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Test;

import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class DBHandlerTests {

  @Test
  public void tests() {

    DatabaseHandler db;
    try {
      db = new DatabaseHandler("calendar.sqlite3");
      try {
        assertTrue(db.getAllEventsFromUser("Harsha").size() == 4);
        assertTrue(db.getAllEventsFromUser("roll").size() == 0);
        assertTrue(db.getAllEventsFromUser("rock").size() == 4);
        assertTrue(db.getUsersFromEvent(11).get(0).equals("rock"));
        assertTrue(db.getUsersFromEvent(11).get(1).equals("hyeddana"));
        assertTrue(db.getUsersFromGroup(1).size() == 4);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}