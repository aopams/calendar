package edu.brown.cs.andrew.handlers;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.brown.cs.andrew.clientThreads.HeartBeatThread;

public class ClientHandler {
  String user;
  private DatabaseHandler myDBHandler;
  private ConcurrentHashMap<String, String> friends;
  private ConcurrentHashMap<Integer, String> groups;
  private ConcurrentHashMap<Integer, Event> events;
  private List<String> updateList;
  private int maxGroupId;


  public ClientHandler(String db, String user) {
    try {
      myDBHandler = new DatabaseHandler(db);
      this.user = user;
      updateList = new CopyOnWriteArrayList<String>();
      ConcurrentHashMap<Integer, ClientHandler> dummyMap = new ConcurrentHashMap<Integer, ClientHandler>();
      dummyMap.put(0, this);
      HeartBeatThread initThread = new HeartBeatThread("pull", dummyMap);
      initThread.run();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized ConcurrentHashMap<String, String> getFriends() {
    return friends;
  }
  public synchronized ConcurrentHashMap<Integer, String> getGroups() {
    return groups;
  }
  public synchronized ConcurrentHashMap<Integer, Event> getEvents() {
    return events;
  }
  public synchronized DatabaseHandler getDB() {
    return myDBHandler;
  }
  
  public synchronized void setFriends(ConcurrentHashMap<String, String> friends) {
    this.friends = friends;
  }
  public synchronized void setGroups(ConcurrentHashMap<Integer, String> groups) {
    this.groups = groups;
  }
  public synchronized void setEvents(ConcurrentHashMap<Integer, Event> events) {
    this.events = events;
  } 
  public synchronized void setMaxGroupId(int maxID) {
    this.maxGroupId = maxID;
  }
  
  public synchronized String getClient() {
    return user;
  }
  public void editUpdateList(String edit) {
    updateList.add(edit);
  }
  
  public void removeFriend(String user_name) {
    friends.remove(user_name);
  }
  public void acceptFriend(String user_name) {
    friends.put(user_name, "accepted");
  }
  public void requestFriend(String user_name) {
    friends.put(user_name, "pending");
  }
  public void addEvent(Event e) {
    events.put(e.getId(), e);
  }
  public void addGroup(String group) {
    maxGroupId++;
    groups.put(maxGroupId, group);
  }
}