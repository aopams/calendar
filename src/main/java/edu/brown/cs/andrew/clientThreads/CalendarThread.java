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
/**
 * The calendar thread handles when a user interacts with
 * the calendar on our front end. A new thread is made
 * each time a user (possibly multiple users at a time) creates
 * an event, deletes an event, or edits an event.
 * @author wtruong02151
 *
 */
public class CalendarThread implements Callable<String> {
  private ClientHandler client1;
  private Commands command;
  private DatabaseHandler myDBHandler;
  private Event myEvent;
  private Event deleteEvent;
  private ConcurrentHashMap<Integer, ClientHandler> attendees;
  /**
   * Calender thread constructor. Initializes proper global variables.
   * @param client1 the client interacting with the calendar.
   * @param command the command specified from the front end (edit/add/delete).
   * @param e the event we've received from the front end.
   * @param d the even we have to delete (for edit/remove).
   * @param attendees the attendees (other users) of this event.
   */
  public CalendarThread(ClientHandler client1, Commands command, Event e,
      Event d, ConcurrentHashMap<Integer, ClientHandler> attendees) {
    this.client1 = client1;
    this.command = command;
    this.attendees = attendees;
    myEvent = e;
    deleteEvent = d;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException error) {
      error.printStackTrace();
    } catch (ClassNotFoundException error) {
      error.printStackTrace();
    }
  }
  @Override
  public String call() throws SQLException, ParseException {
    // TODO Auto-generated method stub
    switch (command) {
      case ADD_EVENT :
        for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
          ClientHandler cli1 = e.getValue();
          if (myEvent.getAttendees().contains(cli1.getClient())) {
            System.out.println(cli1.getEvents().size());
            Event nextDay = cli1.checkTwoDays(myEvent);
            System.out.println(myEvent.getId());
            if (client1.getClient().equals(cli1.getClient())) {
              myDBHandler.addEvent(myEvent);
            }
            while (nextDay != null) {
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
        System.out.println("event deleted");
        break;
      case REMOVE_USER_EVENT :
        client1.removeEvent(deleteEvent);
        List<String> newAttendees = deleteEvent.getAttendees();
        newAttendees.remove(client1.getClient());
        deleteEvent.setAttendees(newAttendees);
        myDBHandler.removeUserFromEvent(client1.getClient(),
            deleteEvent.getId());
        //loop through rest of clients and update their hashmaps to reflect that
        //this user just left the event
        for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
          if (deleteEvent.getAttendees().contains(e.getValue().getClient())
              && !client1.getClient().equals(e.getValue().getClient())) {
            e.getValue().getEvents().put(deleteEvent.getId(), deleteEvent);
          }
        }
        break;
      case EDIT_EVENT :
        List<String> attends = deleteEvent.getAttendees();
        String groupname = myEvent.getGroup();
        for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
          if (attends.contains(e.getValue().getClient())) {
            e.getValue().removeEvent(deleteEvent);
          }
        }
        myDBHandler.removeEvent(deleteEvent);
        for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
          System.out.println("Current client = " + e.getValue().getClient());
          ClientHandler cli1 = e.getValue();
          //check for all clients logged in, see if they are an attendee
          //to this event, if so, update their hashmap
          if (myEvent.getAttendees().contains(cli1.getClient())) {
            System.out.println("client is an attendee = " + cli1.getClient());
            Event nextDay = cli1.checkTwoDays(myEvent);
            //checks for user who made the event, add to database once
            if (client1.getClient().equals(cli1.getClient())) {
              System.out.println("add to database ONCEEE");
              myDBHandler.addEvent(myEvent);
            }
            while (nextDay != null) {
              System.out.println("next day is not null,"
                  + "make it show up on next day?");
              if (client1.getClient().equals(cli1.getClient())) {
                myDBHandler.addEvent(myEvent);
              }
              nextDay = cli1.checkTwoDays(nextDay);
            }
          }
        }
        break;
      default :
        myDBHandler.closeConnection();
        return null;
    }
    myDBHandler.closeConnection();
    return null;
  }
}
