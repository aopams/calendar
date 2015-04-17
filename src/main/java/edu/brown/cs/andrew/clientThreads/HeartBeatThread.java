package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.server.Server;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
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
          ServerCalls sc = new ServerCalls();
          HashMap<String, String> calendarList = sc.getCalendarList(accessToken);
          HashMap<String, String> eventsList = sc.getAllEventsMap(calendarList, accessToken);
          ArrayList<Event> events = sc.getAllEvents(eventsList);
          for (Event event : events) {
            client.addEvent(event);
          }
          myDBHandler.closeConnection();
        } catch (SQLException | ParseException | ClassNotFoundException e2) {
          e2.printStackTrace();
        }
      }
    } 
  }

}
