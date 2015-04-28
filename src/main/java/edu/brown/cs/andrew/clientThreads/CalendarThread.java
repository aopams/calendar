package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.Commands;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;

public class CalendarThread implements Callable<String>{
  
  private ClientHandler client1; 
  private Commands command;
  private DatabaseHandler myDBHandler;
  private Event myEvent;
  private int deleteEvent;
  
  public CalendarThread(ClientHandler client1, Commands command, Event e, int d) {
    this.client1 = client1;
    this.command = command;
    myEvent = e;
    deleteEvent = d;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException | ClassNotFoundException error) {
      error.printStackTrace();
    }
  }
  
 

  @Override
  public String call() throws SQLException, ParseException {
    // TODO Auto-generated method stub
    switch (command) {
    case ADD_EVENT :
      Event nextDay = client1.checkTwoDays(myEvent);
      myDBHandler.addEvent(myEvent);
      while(nextDay != null) {
        myDBHandler.addEvent(nextDay);
        nextDay = client1.checkTwoDays(myEvent);
      }
      break;
    case DELETE_EVENT :
      client1.removeEvent(deleteEvent);
      myDBHandler.removeEvent(deleteEvent);
      break;
    case EDIT_EVENT :
      client1.removeEvent(deleteEvent);
      myDBHandler.removeEvent(deleteEvent);
      Event nextDay2 = client1.checkTwoDays(myEvent);
      myDBHandler.addEvent(myEvent);
      if (nextDay2 != null) {
        myDBHandler.addEvent(nextDay2);
      }
      break;
    default :
      return null;
    }
    return null;
  }

}
