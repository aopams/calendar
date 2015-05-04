package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.Commands;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;

public class CalendarThread implements Callable<String>{
  
  private ClientHandler client1; 
  private Commands command;
  private DatabaseHandler myDBHandler;
  private Event myEvent;
  private Event deleteEvent;
  private ConcurrentHashMap<Integer, ClientHandler> attendees;
  
  public CalendarThread(ClientHandler client1, Commands command, Event e, Event d, ConcurrentHashMap<Integer, ClientHandler> attendees) {
    this.client1 = client1;
    this.command = command;
    this.attendees = attendees;
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
      System.out.println("in add event");
      
      for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
        ClientHandler cli1 = e.getValue();
        System.out.println(cli1.getClient());
        System.out.println("in for loop");
        if (myEvent.getAttendees().contains(cli1.getClient())) {
          System.out.println("event should contain me");
          Event nextDay = cli1.checkTwoDays(myEvent);
          if (client1.getClient().equals(cli1.getClient())) {
            System.out.println("this is me");
            myDBHandler.addEvent(myEvent);
          }
          while(nextDay != null) {
            if (client1.getClient().equals(cli1.getClient())) {
              myDBHandler.addEvent(myEvent);
            }
            nextDay = client1.checkTwoDays(nextDay);
          }
        }
      }

      break;
    case DELETE_EVENT :
      List<String> attens = deleteEvent.getAttendees();
      for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
        if (attens.contains(e.getValue().getClient())) {
          e.getValue().removeEvent(deleteEvent);
        }
      }
      myDBHandler.removeEvent(deleteEvent);
      break;
    case REMOVE_USER_EVENT :
      client1.removeEvent(deleteEvent);
      myDBHandler.removeUserFromEvent(client1.getClient(), deleteEvent.getId());
      //loop through rest of clients and update their hashmaps to reflect that
      //this user just left the event
      for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
        if (deleteEvent.getAttendees().contains(e.getValue().getClient())) {
          System.out.println("iNHEREEEE");
          System.out.println(e.getValue().getClient());
          e.getValue().getEvents().get(deleteEvent.getId()).getAttendees().remove(client1.getClient());
        }
      }
      break;
    case EDIT_EVENT :
      System.out.println(myEvent.getDescription());
      List<String> attends = deleteEvent.getAttendees();
      for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
        if (attends.contains(e.getValue().getClient())) {
          e.getValue().removeEvent(deleteEvent);
        }
      }
      myDBHandler.removeEvent(deleteEvent);
      Event nextDay2 = client1.checkTwoDays(myEvent);
      myDBHandler.addEvent(myEvent);
      while (nextDay2 != null) {
        myDBHandler.addEvent(nextDay2);
        nextDay2 = client1.checkTwoDays(nextDay2);
      }
      break;
    default :
      return null;
    }
    return null;
  }
}
