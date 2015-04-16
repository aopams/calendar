package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;

public class CalendarThread implements Runnable{
  
  private ClientHandler client1; 
  private String command;
  private DatabaseHandler myDBHandler;
  private Event myEvent;
  private Event deleteEvent;
  
  public CalendarThread(ClientHandler client1, String command, Event e, Event d) {
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
  public void run() {
    try {
    switch (command) {
      case "ae" :
        client1.addEvent(myEvent);
        myDBHandler.addEvent(myEvent);
        break;
      case "de" :
        client1.removeEvent(deleteEvent);
        myDBHandler.removeEvent(deleteEvent);
        break;
      case "ee" :
        client1.removeEvent(deleteEvent);
        myDBHandler.removeEvent(deleteEvent);
        client1.addEvent(myEvent);
        myDBHandler.addEvent(myEvent);
      }
    } catch (SQLException | ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
