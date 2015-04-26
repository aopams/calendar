package edu.brown.cs.andrew.handlers;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.brown.cs.andrew.clientThreads.HeartBeatThread;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;

public class ClientHandler {
  String user;
  private String accesToken;
  private ConcurrentHashMap<String, String> friends;
  private ConcurrentHashMap<Integer, String> groups;
  private ConcurrentHashMap<Integer, Event> events;
  String refreshToken;
  String accessToken;
  private List<String> updateList;
  private int maxGroupId;
  private int maxEventId;


  public ClientHandler(String db, String user, boolean initItself) {
      this.user = user;
      updateList = new CopyOnWriteArrayList<String>();
      ConcurrentHashMap<Integer, ClientHandler> dummyMap = new ConcurrentHashMap<Integer, ClientHandler>();
      dummyMap.put(0, this);
      if (initItself) {
        HeartBeatThread initThread = new HeartBeatThread("pull", dummyMap);
        SparkHandler.pool.submit(initThread);
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

  
  public ConcurrentHashMap<Integer, Event> getEventsByWeek(Date startTimed) {
    ConcurrentHashMap<Integer, Event> toReturn = new ConcurrentHashMap<Integer, Event>();
    Date start = SparkHandler.setTimeToMidnight(startTimed);
    for (Entry<Integer, Event> e : events.entrySet()) {
      try {
        Date eventDate = e.getValue().getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 6);
        Date endDate = c.getTime();
        if (eventDate.after(start) && eventDate.before(endDate)) {
          toReturn.put(e.getKey(), e.getValue());
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
    return toReturn;
  }
  public ConcurrentHashMap<Integer, Event> getEventsByDay(Date startTimed) {
    ConcurrentHashMap<Integer, Event> toReturn = new ConcurrentHashMap<Integer, Event>();
    Date start = SparkHandler.setTimeToMidnight(startTimed);
    System.out.println("events size: " + events.size());
    for (Entry<Integer, Event> e : events.entrySet()) {
      try {
        Date eventDate = e.getValue().getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        Date endDate = c.getTime();
        System.out.println(eventDate);
        if (eventDate.after(start) && eventDate.before(endDate)) {
          toReturn.put(e.getKey(), e.getValue());
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
    return toReturn;
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
  public synchronized void setMaxEventId(int maxID) {
    this.maxEventId = maxID;
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
  public String requestFriend(String user_name) {
    String toReturn = null;
    if (!friends.containsKey(user_name)) {
      friends.put(user_name, "pending");
      return toReturn;
    //friend already exists (pending or , no need to do anything
    } else {
      toReturn = "exists";
      return toReturn;
    }
  }
  public Event checkTwoDays(Event e) throws ParseException{
    java.util.Date start = e.getDate();
    int duration = e.getDuration();
    Calendar c = Calendar.getInstance();
    c.setTime(start);
    c.add(Calendar.MINUTE, duration);
    java.util.Date end = SparkHandler.setTimeToMidnight(c.getTime());
    
    if (SparkHandler.setTimeToMidnight(start).compareTo(end) != 0) {
      c.setTime(start);
      int hour = c.get(Calendar.HOUR_OF_DAY) * 60;
      int minutes = c.get(Calendar.MINUTE);
      hour += duration + minutes;
      int newDuration = 0;
      if (hour > 1440) {
        newDuration = hour - 1440;
        duration = duration - newDuration;
      }
      e.setDuration(duration);
      c.setTime(end);
      Event cont = new Event(end, e.getTitle() + " cont.", SparkHandler.numbersToDay.get(c.get(Calendar.DAY_OF_WEEK)),
          e.getAttendees(), e.getGroup(), e.getDuration(), e.getDescription(), e.getCreator());
      addEvent(cont);
      return cont;
    }
    addEvent(e);
    return null;
  }
  public void addEvent(Event e) {
    maxEventId++;
    e.setID(maxEventId);
    events.put(maxEventId, e);
  }
  public void removeEvent(Event e) {
    events.remove(e.getId());
  }
  public void addGroup(String group) {
    maxGroupId++;
    groups.put(maxGroupId, group);
  }
  public void removeGroup(String group) {
    groups.remove(group);
  }
  
 public void setAccessToken(String accessToken) {
   this.accessToken = accessToken;
 }
 
 public String getAccessToken() {
   return accessToken;
 }
}