package edu.brown.cs.andrew.handlers;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
  String user;
  private DatabaseHandler myDBHandler;
  private List<String> friends;
  private ConcurrentHashMap<Integer, String> groups;
  private ConcurrentHashMap<Integer, Event> events;
  private List<String> updateList;
  private int maxGroupId;


  public ClientHandler(String db, String user) {
    try {
      myDBHandler = new DatabaseHandler(db);
      this.user = user;
      friends = myDBHandler.getFriendsFromUser(user);
      groups = myDBHandler.getGroupsNameFromUser(user);      // TODO Auto-generated catch block
      events = myDBHandler.getAllEventsFromUser(user);
      updateList = new CopyOnWriteArrayList<String>();
      maxGroupId = myDBHandler.getMaxrGroupID(user);
    } catch (ClassNotFoundException | SQLException | ParseException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized List<String> getFriends() {
    return friends;
  }
  public synchronized ConcurrentHashMap<Integer, String> getGroups() {
    return groups;
  }
  public synchronized ConcurrentHashMap<Integer, Event> getEvents() {
    return events;
  }
  public void editUpdateList(String edit) {
    updateList.add(edit);
  }
  
  public void removeFriend(String user_name) {
    friends.remove(user_name);
  }
  public void acceptFriend(String user_name) {
    friends.add(user_name);
  }
  public void addEvent(Event e) {
    events.put(e.getId(), e);
  }
  public void addGroup(String group) {
    maxGroupId++;
    groups.put(maxGroupId, group);
  }
}