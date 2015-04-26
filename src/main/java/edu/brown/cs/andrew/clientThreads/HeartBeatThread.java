package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
public class HeartBeatThread implements Callable<String>{
  private ConcurrentHashMap<Integer, ClientHandler> clients;
  String typeHeartBeat;
  
  public HeartBeatThread(String type, ConcurrentHashMap<Integer, ClientHandler> clients) {
    typeHeartBeat = type;
    this.clients = clients;
  }
  
  @Override
  public String call() throws Exception {
    DatabaseHandler myDBHandler = new DatabaseHandler("calendar.sqlite3");
    for(Entry<Integer, ClientHandler> e : clients.entrySet()) {
      ClientHandler client = e.getValue();
      try {
        System.out.println("one client made");
        String user = client.getClient();
        client.setFriends(myDBHandler.getFriendsFromUser(user));
        client.setGroups(myDBHandler.getGroupsNameFromUser(user));  
        client.setEvents(myDBHandler.getAllEventsFromUser(user));
        client.setMaxGroupId(myDBHandler.getMaxGroupID());
        client.setMaxEventId(myDBHandler.getMaxEventID());
      } catch (SQLException | ParseException e2) {
        e2.printStackTrace();
      }
    }
    myDBHandler.closeConnection();
    return null;
  }

}
