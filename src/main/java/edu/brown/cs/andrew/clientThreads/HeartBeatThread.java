package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
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
        String user = client.getClient();
        System.out.println("user = " + user);
        client.setFriends(myDBHandler.getFriendsFromUser(user));
        client.setGroups(myDBHandler.getGroupsNameFromUser(user));
        client.setEvents(myDBHandler.getAllEventsFromUser(user));
//        client.setMaxGroupId(myDBHandler.getMaxGroupID());
        int maxID = myDBHandler.getMaxEventID();
        client.setMaxEventId(maxID);
      } catch (SQLException | ParseException e2) {
        e2.printStackTrace();
      }
    }
    myDBHandler.closeConnection();
    return null;
  }

}
