package edu.brown.cs.andrew.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * Database Handler class, handles all ointeractions with
 * database.
 * @author wtruong02151
 *
 */
public class DatabaseHandler {
  private Connection conn;
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int FIVE = 5;
  private static final int SIX = 6;
/**
 * DB constructor, set pragma on.
 * @param dbFile file to databasee
 * @throws ClassNotFoundException exception
 * @throws SQLException exception
 */
  public DatabaseHandler(
      String dbFile) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
    stat.close();
  }
  /**
   * creates all tables for our database.
   * @throws SQLException sql exception
   */
  public void createTablesForCalendar() throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.executeUpdate("Drop Table if Exists Friends");
    stmt.executeUpdate("Drop Table if Exists User_Group");
    stmt.executeUpdate("Drop Table if Exists User_Event");
    stmt.executeUpdate("Drop Table if Exists Group_Event");
    stmt.executeUpdate("Drop Table if Exists Events");
    stmt.executeUpdate("Drop Table if Exists Users");
    stmt.executeUpdate("Drop Table if Exists Groups");
    stmt.close();
    String userTable = "CREATE TABLE Users ("
        + "user_name nvarchar(16) PRIMARY KEY,"
        + "user_password nvarchar(30) NOT NULL,"
        + "name nvarchar(30) NOT NULL,"
        + "google_token nvarchar(200));";
    String groupTable = "CREATE Table Groups ("
        + "group_id integer PRIMARY KEY AUTOINCREMENT,"
        + "group_name nvarchar(16));";
    String eventTable = "CREATE TABLE Events ("
        + "event_id integer Primary Key AUTOINCREMENT,"
        + "title nvarchar(50) not NULL,"
        + "day_of_week nvarchar(10) not NULL,"
        + "date Date not NUll,"
        + "description nvarchar(150) not NULL,"
        + "duration integer not Null,"
        + "creator nvarchar(30), "
        + "FOREIGN KEY(creator)  references Users(user_name))";
    String friendsTable = "CREATE Table Friends ("
        + "user_name1 nvarchar(16) NOT NULL,"
        + "user_name2 nvarchar(16) NOT NULL,"
        + "status nvarchar(10) NOT NULL,"
        + "PRIMARY KEY(user_name1, user_name2),"
        + "FOREIGN KEY (user_name1) References Users(user_name)"
        + "FOREIGN KEY (user_name2) References Users(user_name));";
    String userEventTable = "CREATE TABLE User_Event ("
        + "user_name nvarchar(16) NOT NULL,"
        + "event_id integer NOT NULL,"
        + "PRIMARY KEY(user_name, event_id),"
        + "FOREIGN KEY (user_name) references Users(user_name),"
        + "FOREIGN KEY (event_id) references Events(event_id));";
    String userGroupTable = "CREATE TABLE User_Group ("
        + "user_name nvarchar(16) NOT NULL,"
        + "group_id integer NOT NULL,"
        + "PRIMARY KEY(user_name, group_id),"
        + "FOREIGN KEY (user_name) references Users(user_name),"
        + "FOREIGN KEY (group_id) references Groups(group_id));";
    String groupEventTable = "CREATE TABLE Group_Event ("
        + "group_id integer,"
        + "event_id integer,"
        + "PRIMARY KEY (group_id, event_id),"
        + "FOREIGN KEY (group_id) references Groups(group_id),"
        + "FOREIGN KEY (event_id) references Events(event_id));";
    buildTable(userTable);
    buildTable(groupTable);
    buildTable(eventTable);
    buildTable(friendsTable);
    buildTable(userEventTable);
    buildTable(userGroupTable);
    buildTable(groupEventTable);
  }
  /**
   * gets user name of a given user.
   * @param username username
   * @return full name of user
   * @throws SQLException error
   */
  public String getName(String username) throws SQLException {
    String query = "select name from Users where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    String toReturn = null;
    if (rs.next()) {
      toReturn = rs.getString(1);
    }
    rs.close();
    return toReturn;
  }
  /**
   * checks if a username exists.
   * @param username user to check
   * @return user name or empty
   * @throws SQLException error
   */
  public String findUser(String username) throws SQLException {
    String query = "select user_name from Users where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    String toReturn = "";
    if (rs.next()) {
      toReturn = rs.getString(1);
    }
    rs.close();
    return toReturn;
  }
  /**
   * checks if the user exists (for login).
   * @param username username
   * @param userpassword password
   * @return true or false depending on if user exists
   * @throws SQLException error
   */
  public boolean findUser(
      String username, String userpassword)throws SQLException {
    String query = "select user_name from Users"
        + "where user_name = ? and user_password = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    theStat.setString(2, userpassword);
    ResultSet rs = theStat.executeQuery();
    boolean toReturn = rs.next();
    rs.close();
    return toReturn;
  }
  /**
   * inserts user into database (registration).
   * @param username username
   * @param password password
   * @param name full name
   * @throws SQLException error
   */
  public void insertUser(String username, String password,
      String name) throws SQLException {
    String query = "INSERT into Users (user_name, user_password, name)"
        + "Select ?, ?, ? where not exists ("
        + "select * from Users where user_name = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    theStat.setString(2, password);
    theStat.setString(THREE, name);
    theStat.setString(FOUR, username);
    theStat.executeUpdate();
  }
  /**
   * deletes a user from the database.
   * @param username username
   * @throws SQLException error
   */
  public void deleteUser(String username) throws SQLException {
    String query = "Delete from Users where user_name = \"" + username + "\"";
    Statement theStat = conn.createStatement();
    theStat.executeUpdate(query);
    theStat.close();
  }
  /**
   * sends a friend request from user1 to user2.
   * @param username1 user1
   * @param username2 user2
   * @throws SQLException error
   */
  public void addFriendRequest(
      String username1, String username2) throws SQLException {
    if (username1.equals(username2)) {
      return;
    }
    String query = "INSERT into Friends (user_name1, user_name2, status)"
        + "Select ?, ?, \'pending\' Where NOT EXISTS("
        + "Select * from Friends where (user_name1 = ? and user_name2 = ?)"
        + "or (user_name1 = ? and user_name2 = ? ));";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username1);
    theStat.setString(2, username2);
    theStat.setString(THREE, username1);
    theStat.setString(FOUR, username2);
    theStat.setString(FIVE, username2);
    theStat.setString(SIX, username1);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * accepts a friend request between two users (changes status in db).
   * @param username1 user1
   * @param username2 user2
   * @throws SQLException error
   */
  public void acceptFriendRequest(
      String username1, String username2) throws SQLException {
    String query = "UPDATE Friends SET status = \"accepted\""
        + "where (user_name1 = ? and user_name2 = ?) "
        + "or (user_name1 = ? and user_name2 = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username1);
    theStat.setString(2, username2);
    theStat.setString(THREE, username2);
    theStat.setString(FOUR, username1);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * remove connection between two users in db.
   * @param username1 user1
   * @param username2 user2
   * @throws SQLException error
   */
  public void removeFriend(
      String username1, String username2) throws SQLException {
    String query = "Delete From Friends where"
        + "(user_name1 = ? and user_name2 = ?) "
        + "or (user_name1 = ? and user_name2 = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username1);
    theStat.setString(2, username2);
    theStat.setString(THREE, username2);
    theStat.setString(FOUR, username1);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * grab the friends of a user and puts them
   * in a hashmap.
   * @param username user we're dealing with
   * @return hashmap of friends
   * @throws SQLException error
   */
  public ConcurrentHashMap<String, String> getFriendsFromUser(
      String username) throws SQLException {
    ConcurrentHashMap<String, String> toReturn =
        new ConcurrentHashMap<String, String>();
    String query = "Select user_name1, status from"
        + "Friends where user_name2 = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      String status = rs.getString(2);
      toReturn.put(rs.getString(1), status);
    }
    rs.close();
    String query2 = "Select user_name2, status from"
        + "Friends where user_name1 = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    theStat2.setString(1, username);
    ResultSet rs2 = theStat2.executeQuery();
    while (rs2.next()) {
      if (rs2.getString(2).equals("pending")) {
        toReturn.put(rs2.getString(1), "sent");
      } else {
        toReturn.put(rs2.getString(1), rs2.getString(2));
      }
    }
    rs2.close();
    return toReturn;
  }
  /**
   * returns set of only accepted friends.
   * @param username user we're dealing with
   * @return set of strings of accepted friends
   * @throws SQLException error
   */
  public Set<String> getAcceptedFriends(String username) throws SQLException {
    Set<String> toReturn =
        Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    String query = "SELECT user_name1 FROM Friends WHERE"
        + "user_name2 = ? AND status = 'accepted'";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    rs.close();
    String query2 = "SELECT user_name2 FROM Friends WHERE"
        + "user_name1 = ? AND status = 'accepted'";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    theStat2.setString(1, username);
    ResultSet rs2 = theStat2.executeQuery();
    while (rs2.next()) {
      toReturn.add(rs2.getString(1));
    }
    rs2.close();
    return toReturn;
  }
  /**
   * finds group id of a given group name.
   * @param groupname groupname.
   * @return group id
   * @throws SQLException error
   */
  public int findGroup(String groupname) throws SQLException {
    String query = "select group_id from Groups where group_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, groupname);
    ResultSet rs = theStat.executeQuery();
    int toReturn = -1;
    if (rs.next()) {
      toReturn = rs.getInt(1);
    }
    return toReturn;
  }
  /**
   * for a given group id, return the name.
   * @param groupid group id
   * @return group name
   * @throws SQLException error
   */
  public String findGroup(int groupid) throws SQLException {
    String query = "select group_name from Groups where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, groupid);
    ResultSet rs = theStat.executeQuery();
    String toReturn = "";
    if (rs.next()) {
      toReturn = rs.getString(1);
    }
    return toReturn;
  }
  /**
   * returns a new unique id for a group.
   * @return new unique group id
   * @throws SQLException error
   */
  public int getNewGroupID() throws SQLException {
    String query = "SELECT COUNT(*) FROM Groups";
    PreparedStatement theStat = conn.prepareStatement(query);
    ResultSet rs = theStat.executeQuery();
    int count = 0;
    if (rs.next()) {
      count = rs.getInt(1);
    }
    count++;
    return count;
  }
  /**
   * add a group to the database (groupname and id required).
   * @param groupname group's name
   * @param groupid group's id
   * @throws SQLException error
   */
  public void addGroup(String groupname, int groupid) throws SQLException {
    String query = "INSERT into Groups (group_name, group_id)"
        + "VALUES (?, ?)";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, groupname);
    theStat.setInt(2, groupid);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * remove a group from the db with the given id.
   * @param groupid groupid
   * @throws SQLException error
   */
  public void removeGroup(int groupid) throws SQLException {
    String query = "DELETE FROM Groups WHERE group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, groupid);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * adds a user to a group.
   * @param username user to add
   * @param groupid group's id
   * @throws SQLException error
   */
  public void addUserToGroup(String username, int groupid) throws SQLException {
    String query = "INSERT into User_Group(user_name, group_id)"
        + "Select ?, ? where not exists ("
        + "select * from User_Group where user_name = ? and group_id = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    theStat.setInt(2, groupid);
    theStat.setString(THREE, username);
    theStat.setInt(FOUR, groupid);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * remove a given user from the group id.
   * @param username username
   * @param groupid groupid
   * @throws SQLException error
   */
  public void removeUserFromGroup(
      String username, int groupid) throws SQLException {
    String query = "Delete from User_Group"
        + " where user_name = ? and group_id = ?;";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    theStat.setInt(2, groupid);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * remove a given user from a particular event.
   * @param username user's username
   * @param eventid id of event
   * @throws SQLException error
   */
  public void removeUserFromEvent(
      String username, int eventid) throws SQLException {
    String query = "Delete from User_Event"
        + " where user_name = ? and event_id = ?;";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    theStat.setInt(2, eventid);
    theStat.executeUpdate();
    theStat.close();
  }
  /**
   * add an event to the database.
   * @param e event to add
   * @throws SQLException error
   * @throws ParseException error
   */
  public void addEvent(Event e) throws SQLException, ParseException {
    String groupname = e.getGroup();
    List<String> users = e.getAttendees();
    String eventQuery = "";
    eventQuery = "Insert into Events(date, title,"
        + "day_of_week, description, duration,creator)"
        + "Values( ?, ?, ?, ?, ?, ?)";
    PreparedStatement theStat = conn.prepareStatement(eventQuery);
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    theStat.setString(1, df.format(e.getDate()));
    theStat.setString(2, e.getTitle());
    theStat.setString(THREE, e.getDayOfWeek());
    theStat.setString(FOUR, e.getDescription());
    theStat.setInt(FIVE, e.getDuration());
    theStat.setString(SIX, e.getCreator());
    theStat.executeUpdate();
    ResultSet row =  theStat.getGeneratedKeys();
    int theRow = row.getInt(1);
    theStat.close();
    if (e.getId() >= 0 && !e.getCreator().equals("google")) {
      e.setID(theRow);
    }
    String eventToGroup = "";
    eventToGroup = "INSERT into User_Event (user_name, event_id)"
        + "Values(?, ?);";
    PreparedStatement theStat2 = conn.prepareStatement(eventToGroup);
    for (int i = 0; i < users.size(); i++) {
      theStat2.setString(1, users.get(i));
      theStat2.setInt(2, theRow);
      theStat2.addBatch();
    }
    conn.setAutoCommit(false);
    theStat2.executeBatch();
    conn.setAutoCommit(true);
    if (groupname != null && !groupname.equals("")) {
      int groupid = findGroup(groupname);
      eventToGroup = "INSERT into Group_Event (group_id, event_id)"
          + "Values(?, ?);";
      PreparedStatement theStat3 = conn.prepareStatement(eventToGroup);
      theStat3.setInt(1, groupid);
      theStat3.setInt(2, theRow);
      theStat3.executeUpdate();
      theStat3.close();
    }
  }
  /**
   * remove an event from the database.
   * @param e event
   * @throws SQLException error
   * @throws ParseException error
   */
  public void removeEvent(Event e) throws SQLException, ParseException {
    int eventID = e.getId();
    if (eventID < 0) {
      eventID = eventID * -1;
    }
    String query3 = "Delete From Group_Event where event_id = ?";
    PreparedStatement stat3 = conn.prepareStatement(query3);
    stat3.setInt(1, eventID);
    stat3.executeUpdate();
    stat3.close();
    String query2 = "Delete From User_Event where event_id = ?";
    PreparedStatement stat2 = conn.prepareStatement(query2);
    stat2.setInt(1, eventID);
    stat2.executeUpdate();
    stat2.close();

    String query = "Delete From Events where event_id = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setInt(1, eventID);
    stat.executeUpdate();
    stat.close();
  }
  /**
   * for a given group, grab all users.
   * @param groupid group's id
   * @return list of users
   * @throws SQLException error
   */
  public List<String> getUsersFromGroup(int groupid) throws SQLException {
    List<String> toReturn = new CopyOnWriteArrayList<String>();
    String query = "Select user_name from User_Group where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, groupid);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    return toReturn;
  }
  /**
   * for a given user, grab all the groups they're in (group ids).
   * @param username user's name
   * @return list of group ids
   * @throws SQLException error
   */
  public List<Integer> getGroupsIDFromUser(
      String username) throws SQLException {
    List<Integer> toReturn = new CopyOnWriteArrayList<Integer>();
    String query = "Select group_id from User_Group where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getInt(1));
    }
    return toReturn;
  }
  /**
   * grab all group names for a given user.
   * @param username user's username
   * @return hashmap of id to username
   * @throws SQLException error
   */
  public ConcurrentHashMap<Integer, String> getGroupsNameFromUser(
      String username) throws SQLException {
    ConcurrentHashMap<Integer, String> toReturn =
        new ConcurrentHashMap<Integer, String>();
    List<Integer> ids = getGroupsIDFromUser(username);
    String query = "Select group_name from Groups where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    for (int i = 0; i < ids.size(); i++) {
      theStat.setInt(1, ids.get(i));
      ResultSet rs = theStat.executeQuery();
      if (rs.next()) {
        toReturn.put(ids.get(i), rs.getString(1));
      }
    }
    return toReturn;
  }
  /**
   * grab users for a given event.
   * @param eventid event's id
   * @return list of users for this event
   * @throws SQLException error
   */
  public List<String> getUsersFromEvent(int eventid) throws SQLException {
    List<String> toReturn = new CopyOnWriteArrayList<String>();
    String query = "Select user_name from User_Event where event_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, eventid);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    rs.close();
    return toReturn;
  }
  /**
   * grab events from this group.
   * @param groupname grou's name
   * @return list of events for this group
   * @throws SQLException error
   */
  public List<Event> getEventsFromGroup(String groupname) throws SQLException {
    int groupid = findGroup(groupname);
    List<String> users = getUsersFromGroup(groupid);
    String query = "Select event_id from Group_Event where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, groupid);
    ResultSet rs = theStat.executeQuery();
    List<Event> groupEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      Event toAdd = new Event(rs2.getDate("date"),
          rs2.getString("title"), rs2.getString("day_of_week"),
          users, groupname, rs2.getInt("duration"),
          rs2.getString("description"), rs2.getString("creator"));
      toAdd.setID(rs2.getInt("event_id"));
      groupEvents.add(toAdd);
    }
    return groupEvents;
  }
  /**
   * grab events for this particular group id.
   * @param groupid group's id
   * @return list of events
   * @throws SQLException error
   * @throws ParseException error
   */
  public List<Event> getEventsFromGroup(
      int groupid) throws SQLException, ParseException {
    String groupname = findGroup(groupid);
    List<String> users = getUsersFromGroup(groupid);
    String query = "Select event_id from Group_Event where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, groupid);
    ResultSet rs = theStat.executeQuery();
    List<Event> groupEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      if (rs2.next()) {
        Event toAdd = new Event(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
          .parse(rs2.getString("date")),
          rs2.getString("title"), rs2.getString("day_of_week"),
            users, groupname, rs2.getInt("duration"),
            rs2.getString("description"),  rs2.getString("creator"));
        toAdd.setID(rs2.getInt("event_id"));
        groupEvents.add(toAdd);
      }
    }
    return groupEvents;
  }
  /**
   * gets events for this user.
   * @param username username
   * @return list of events
   * @throws SQLException error
   * @throws ParseException error
   */
  public List<Event> getPersonnalEventsFromUser(
      String username) throws SQLException, ParseException {
    String query = "Select event_id from User_Event where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, username);
    ResultSet rs = theStat.executeQuery();
    List<Event> userEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      List<String> users = getUsersFromEvent(rs2.getInt(1));
      Event toAdd = new Event(new
          SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(rs2.getString("date")),
          rs2.getString("title"), rs2.getString("day_of_week"),
          users, "", rs2.getInt("duration"), rs2.getString("description"),
          rs2.getString("creator"));
      toAdd.setID(rs2.getInt("event_id"));
      if (toAdd.getCreator().equals("google") && toAdd.getId() > 0) {
        toAdd.setID(toAdd.getId() * -1);
      }
      userEvents.add(toAdd);
    }
    return userEvents;
  }
  /**
   * get all events from this user.
   * @param username username
   * @return hashmap of events and their ids
   * @throws SQLException error
   * @throws ParseException error
   */
  public ConcurrentHashMap<Integer, Event> getAllEventsFromUser(
      String username) throws SQLException, ParseException {
    List<Event> eventList = new CopyOnWriteArrayList<Event>();
    eventList.addAll(getPersonnalEventsFromUser(username));
    List<Integer> groups = getGroupsIDFromUser(username);
    for (int i = 0; i < groups.size(); i++) {
      eventList.addAll(getEventsFromGroup(groups.get(i)));
    }
    ConcurrentHashMap<Integer, Event> actualReturn =
        new ConcurrentHashMap<Integer, Event>();
    for (int i = 0; i < eventList.size(); i++) {
      Event curr = eventList.get(i);
      actualReturn.put(curr.getId(), curr);
    }
    return actualReturn;
  }
  /**
   * get max id of a group.
   * @return group id
   * @throws SQLException error
   */
  public int getMaxGroupID() throws SQLException {
    int toReturn = -1;
    String query = "Select MAX(group_id) from User_Group";
    PreparedStatement theStat = conn.prepareStatement(query);
    ResultSet rs = theStat.executeQuery();
    if (rs.next()) {
      toReturn = rs.getInt(1);
    }
    return toReturn;
  }
  /**
   * get max id of an event.
   * @return event id
   * @throws SQLException error
   */
  public int getMaxEventID() throws SQLException {
    int toReturn = -1;
    String query = "Select MAX(event_id) from Events";
    PreparedStatement theStat = conn.prepareStatement(query);
    ResultSet rs = theStat.executeQuery();
    if (rs.next()) {
      toReturn = rs.getInt(1);
    }
    return toReturn;
  }
  /**
   * function that builds table.
   * @param schema schema
   * @throws SQLException sqlexception
   */
  private void buildTable(String schema) throws SQLException {
    PreparedStatement myStat = conn.prepareStatement(schema);
    myStat.execute();
    myStat.close();
  }
  /**
   * close connection.
   * @throws SQLException error
   */
  public void closeConnection() throws SQLException {
    conn.close();
  }
}
