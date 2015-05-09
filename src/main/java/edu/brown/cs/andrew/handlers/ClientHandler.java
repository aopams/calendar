package edu.brown.cs.andrew.handlers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.clientThreads.HeartBeatThread;
/**
 * Client handler class. When a user logs in,
 * a new client is made. The SparkHandler
 * maintains a hashmap of clients to keep track of all users
 * logged in.
 * @author wtruong02151
 *
 */
public class ClientHandler {
  private String user;
  private ConcurrentHashMap<String, String> friends;
  private ConcurrentHashMap<Integer, String> groups;
  private ConcurrentHashMap<Integer, Event> events;
  private String accessToken;
  private int maxEventId;
  private static final int WEEK = 7;
  private static final int MINUTES = 60;
  private static final int MININDAY = 1440;
  /**
   * Client handler constructor, initializes all gloval variables.
   * @param db path to database
   * @param user user logged in associated with this client.
   * @param initItself boolean that checks if client should init
   * itself or if its part of a group init.
   */
  public ClientHandler(String db, String user, boolean initItself) {
    this.user = user;
    ConcurrentHashMap<Integer, ClientHandler> dummyMap =
        new ConcurrentHashMap<Integer, ClientHandler>();
    dummyMap.put(0, this);
    if (initItself) {
      HeartBeatThread initThread = new HeartBeatThread(dummyMap);
      SparkHandler.pool.submit(initThread);
    }
  }
  /**
   * returns friends hashmap.
   * @return friends
   */
  public synchronized ConcurrentHashMap<String, String> getFriends() {
    return friends;
  }
  /**
   * returns groups hashmap.
   * @return groups
   */
  public synchronized ConcurrentHashMap<Integer, String> getGroups() {
    return groups;
  }
  /**
   * returns user's events hashmap.
   * @return events;
   */
  public synchronized ConcurrentHashMap<Integer, Event> getEvents() {
    return events;
  }
  /**
   * grabs the events that this user has by week.
   * @param startTimed starting time parameter
   * @return hashmap of events for this week
   */
  public ConcurrentHashMap<Integer, Event> getEventsByWeek(Date startTimed) {
    ConcurrentHashMap<Integer, Event> toReturn =
        new ConcurrentHashMap<Integer, Event>();
    Date start = SparkHandler.setTimeToMidnight(startTimed);
    for (Entry<Integer, Event> e : events.entrySet()) {
      try {
        Date eventDate = e.getValue().getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, WEEK);
        Date endDate = c.getTime();
        if (eventDate.after(start) && eventDate.before(endDate)
            || eventDate.equals(start)) {
          toReturn.put(e.getKey(), e.getValue());
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
    return toReturn;
  }
  /**
   * grabs the events that this user has by day.
   * @param startTimed starting time parameter
   * @return hashmap of events for this day
   */
  public ConcurrentHashMap<Integer, Event> getEventsByDay(Date startTimed) {
    ConcurrentHashMap<Integer, Event> toReturn =
        new ConcurrentHashMap<Integer, Event>();
    Date start = SparkHandler.setTimeToMidnight(startTimed);
    for (Entry<Integer, Event> e : events.entrySet()) {
      try {
        Date eventDate = e.getValue().getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
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
  /**
   * method to set hashmap of friends.
   * @param friends hashmap to set
   */
  public synchronized void setFriends(
      ConcurrentHashMap<String, String> friends) {
    this.friends = friends;
  }
  /**
   * method to set hashmap of groups.
   * @param groups hashmap to set
   */
  public synchronized void setGroups(
      ConcurrentHashMap<Integer, String> groups) {
    this.groups = groups;
  }
  /**
   * method to set hashmap of events.
   * @param events hashmap to set
   */
  public synchronized void setEvents(
      ConcurrentHashMap<Integer, Event> events) {
    this.events = events;
  }
  /**
   * set max event id.
   * @param maxID int max id to set
   */
  public synchronized void setMaxEventId(int maxID) {
    this.maxEventId = maxID;
  }
  /**
   * returns client's username.
   * @return user username
   */
  public synchronized String getClient() {
    return user;
  }
  /**
   * remove friend from hashmap of friends.
   * @param userName user to remove
   */
  public void removeFriend(String userName) {
    friends.remove(userName);
  }
  /**
   * friend request accepted, change status in
   * hashmap.
   * @param userName user to accept
   */
  public void acceptFriend(String userName) {
    friends.put(userName, "accepted");
  }
  /**
   * friend to add into hashmap of friends.
   * @param userName user to add
   */
  public void addFriend(String userName) {
    friends.put(userName, "pending");
  }
  /**
   * sending a friend request, check if a friend request
   * is already sent.
   * @param userName user to add
   * @return returns string that spark handler checks
   * to print proper message to front end.
   */
  public String requestFriend(String userName) {
    String toReturn = "";
    if (!friends.containsKey(userName)) {
      friends.put(userName, "sent");
      return toReturn;
      // friend already exists (pending or , no need to do anything
    } else {
      toReturn = "exists";
      return toReturn;
    }
  }
  /**
   * method that checks if an event lasts longer than 1 day
   * (falls in between two), and also adds events to the
   * hashmap that this client maintains.
   * @param e event to add
   * @return returns the split event.
   * @throws ParseException ParseException
   */
  public Event checkTwoDays(Event e) throws ParseException {
    java.util.Date start = e.getDate();
    int duration = e.getDuration();
    Calendar c = Calendar.getInstance();
    c.setTime(start);
    c.add(Calendar.MINUTE, duration);
    java.util.Date end = SparkHandler.setTimeToMidnight(c.getTime());
    Event cont = null;
    if (SparkHandler.setTimeToMidnight(start).compareTo(end) != 0) {
      c.setTime(start);
      int hour = c.get(Calendar.HOUR_OF_DAY) * MINUTES;
      int minutes = c.get(Calendar.MINUTE);
      hour += duration + minutes;
      int newDuration = 0;
      if (hour > MININDAY) {
        newDuration = hour - MININDAY;
        duration = duration - newDuration;
      }
      e.setDuration(duration);
      c.setTime(end);
      cont = new Event(end, e.getTitle() + " cont.",
          SparkHandler.numbersToDay.get(c.get(Calendar.DAY_OF_WEEK)),
          e.getAttendees(), e.getGroup(), newDuration, e.getDescription(),
          e.getCreator());
      addEvent(cont);
    }
    System.out.println(user);
    addEvent(e);
    return cont;
  }
  /**
   * adds an event to the hashmap of events.
   * @param e event to add
   */
  public void addEvent(Event e) {
    maxEventId += 1;
    e.setID(maxEventId);
    if (e.getCreator().equals("google")) {
      e.setID(maxEventId * -1);
      events.put(maxEventId * -1, e);
    } else {
      events.put(maxEventId, e);
    }
  }
  /**
   * removes an event from the hashmap of events.
   * @param e event to remove
   */
  public void removeEvent(Event e) {
    //remove own self from event's attendees
    events.remove(e.getId());
  }
  /**
   * add group to hash map of groups.
   * @param group group name
   * @param groupID groip id
   */
  public void addGroup(String group, int groupID) {
    groups.put(groupID, group);
  }
  /**
   * removes group form hashmap of groups.
   * @param groupID group id
   */
  public void removeGroup(int groupID) {
    groups.remove(groupID);
  }
  /**
   * sets this client's access token.
   * @param accessToken takes in a stirng access token
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  /**
   * grabs this client's access token.
   * @return string access token
   */
  public String getAccessToken() {
    return accessToken;
  }
}
