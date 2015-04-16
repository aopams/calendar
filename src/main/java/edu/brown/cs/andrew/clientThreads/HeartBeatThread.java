package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
public class HeartBeatThread implements Runnable{
  private ConcurrentHashMap<Integer, ClientHandler> clients;
  String typeHeartBeat;
  
  public HeartBeatThread(String type, ConcurrentHashMap<Integer, ClientHandler> clients) {
    typeHeartBeat = type;
    this.clients = clients;
  }
  
  @Override
  public void run() {
    if(typeHeartBeat.equals("pull")) {
      for(Entry<Integer, ClientHandler> e : clients.entrySet()) {
        ClientHandler client = e.getValue();
        try {
          DatabaseHandler myDBHandler = new DatabaseHandler("calendar.sqlite3");
          String user = client.getClient();
          client.setFriends(myDBHandler.getFriendsFromUser(user));
          client.setGroups(myDBHandler.getGroupsNameFromUser(user));  
          client.setEvents(myDBHandler.getAllEventsFromUser(user));
          client.setMaxGroupId(myDBHandler.getMaxrGroupID(user));
          myDBHandler.closeConnection();
        } catch (SQLException | ParseException | ClassNotFoundException e2) {
          e2.printStackTrace();
        }
      }
    } 
  }

}
