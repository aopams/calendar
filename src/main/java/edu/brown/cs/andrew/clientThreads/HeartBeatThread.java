package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
/**
 * Heart beat thread handles pulling proper information
 * from DB to front end on a particular time interval.
 * (grabs updated events, and contact relations).
 */
public class HeartBeatThread implements Callable<String> {
  private ConcurrentHashMap<Integer, ClientHandler> clients;
  /**
   * Heartbeat thread constructor, initializes global variables.
   * @param clients clients to update
   */
  public HeartBeatThread(ConcurrentHashMap<Integer, ClientHandler> clients) {
    this.clients = clients;
  }
  @Override
  public String call() throws Exception {
    DatabaseHandler myDBHandler = new DatabaseHandler("calendar.sqlite3");
    for (Entry<Integer, ClientHandler> e : clients.entrySet()) {
      ClientHandler client = e.getValue();
      try {
        String user = client.getClient();
        client.setFriends(myDBHandler.getFriendsFromUser(user));
        client.setGroups(myDBHandler.getGroupsNameFromUser(user));
        client.setEvents(myDBHandler.getAllEventsFromUser(user));
        int maxID = myDBHandler.getMaxEventID();
        client.setMaxEventId(maxID);
      } catch (SQLException e2) {
        e2.printStackTrace();
      } catch (ParseException e2) {
        e2.printStackTrace();
      }
    }
    myDBHandler.closeConnection();
    return null;
  }

}
