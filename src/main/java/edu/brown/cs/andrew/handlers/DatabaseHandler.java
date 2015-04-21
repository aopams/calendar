package edu.brown.cs.andrew.handlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseHandler {
  
  private Connection conn;
  
  public DatabaseHandler(String dbFile) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection("jdbc:sqlite:" +dbFile);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
    stat.close();
  }
  
  public void createTablesForCalendar() throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.executeUpdate("Drop Table if Exists Friends");
    stmt.executeUpdate("Drop Table if Exists User_Group");
    stmt.executeUpdate("Drop Table if Exists User_Event");
    stmt.executeUpdate("Drop Table if Exists Group_Event");
    stmt.executeUpdate("Drop Table if Exists basic");
    stmt.executeUpdate("Drop Table if Exists Users");
    stmt.executeUpdate("Drop Table if Exists Groups");
    stmt.executeUpdate("Drop Table if Exists Events");
    
    stmt.close();
    String userTable = "CREATE TABLE Users ("
      + "user_name nvarchar(16) PRIMARY KEY," 
      + "user_password nvarchar(30) NOT NULL,"
      + "name nvarchar(30) NOT NULL,"
      + "google_token nvarchar(200),"
      + "email nvarchar(30) NOT NULL);";
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
  public String findUser(String user_name) throws SQLException {
    String query = "select user_name from Users where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    ResultSet rs = theStat.executeQuery();
    String toReturn = null;
    if (rs.next()) {
      toReturn = rs.getString(1);
    }
    rs.close();
    return toReturn;
  }
  public boolean findUser(String user_name, String user_password) throws SQLException {
    String query = "select user_name from Users where user_name = ? and user_password = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    theStat.setString(2, user_password);
    ResultSet rs = theStat.executeQuery();
    boolean toReturn = rs.next();
    rs.close();
    return toReturn;
  }
  public void insertUser(String user_name, String password,
      String name, String email) throws SQLException {
    String query = "INSERT into Users (user_name, user_password, name, email)"
        + "Select ?, ?, ?, ? where not exists ("
        + "select * from Users where user_name = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    theStat.setString(2, password);
    theStat.setString(3, name);
    theStat.setString(4, email);
    theStat.setString(5, user_name);
    theStat.executeUpdate();
    
  }
  public void deleteUser(String user_name) throws SQLException {
    String query = "Delete from Users where user_name = \"" + user_name + "\"";
    Statement theStat = conn.createStatement();
    theStat.executeUpdate(query);
    theStat.close();
  }
  
  public boolean friendshipExists(String user_name1, String user_name2) throws SQLException {
    String query = "SELECT * FROM Friends WHERE NOT EXISTS(SELECT * FROM Friends WHERE (user_name1 = ? AND user_name2 = ?) "
        + "OR (user_name1 = ? AND user_name2 = ?));";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, user_name1);
    stat.setString(2, user_name2);
    stat.setString(3, user_name2);
    stat.setString(4, user_name1);
    ResultSet results = stat.executeQuery();
    if (results.next()) {
      return false;
    } else {
      return true;
    }
  }
  
  public void addFriendRequest(String user_name1, String user_name2) throws SQLException {
    String query = "INSERT into Friends (user_name1, user_name2, status)"
        + "Select ?, ?, \'pending\' Where NOT EXISTS("
        + "Select * from Friends where (user_name1 = ? and user_name2 = ?)"
        + "or (user_name1 = ? and user_name2 = ?));";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name1);
    theStat.setString(2, user_name2);
    theStat.setString(3, user_name1);
    theStat.setString(4, user_name2);
    theStat.setString(5, user_name2);
    theStat.setString(6, user_name1);
    theStat.executeUpdate();
    theStat.close();
  }
  public void acceptFriendRequest(String user_name1, String user_name2) throws SQLException {
    String query = "UPDATE Friends SET status = \"accepted\" where (user_name1 = ? and user_name2 = ?) "
        + "or (user_name1 = ? and user_name2 = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name1);
    theStat.setString(2, user_name2);
    theStat.setString(3, user_name2);
    theStat.setString(4, user_name1);
    theStat.executeUpdate();
    theStat.close();
  }
  public void removeFriend(String user_name1, String user_name2) throws SQLException {
    String query = "Delete From Friends where (user_name1 = ? and user_name2 = ?) "
        + "or (user_name1 = ? and user_name2 = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name1);
    theStat.setString(2, user_name2);
    theStat.setString(3, user_name2);
    theStat.setString(4, user_name1);
    theStat.executeUpdate();
    theStat.close();
  }
  public ConcurrentHashMap<String, String> getFriendsFromUser(String user_name) throws SQLException {
    ConcurrentHashMap<String, String> toReturn = new ConcurrentHashMap<String, String>();
    String query = "Select user_name1, status from Friends where user_name2 = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.put(rs.getString(1), rs.getString(2));
    }
    rs.close();
    String query2 = "Select user_name2, status from Friends where user_name1 = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    theStat2.setString(1, user_name);
    ResultSet rs2 = theStat2.executeQuery();
    while (rs2.next()) {
      toReturn.put(rs2.getString(1), rs2.getString(2));
    }
    rs2.close();
    return toReturn;
  }
  public int findGroup(String group_name) throws SQLException {
    String query = "select group_id from Groups where group_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, group_name);
    ResultSet rs = theStat.executeQuery();
    int toReturn = -1;
    if (rs.next()) {
      toReturn = rs.getInt(1);
    }
    return toReturn;
  }
  public String findGroup(int group_id) throws SQLException {
    String query = "select group_name from Groups where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, group_id);
    ResultSet rs = theStat.executeQuery();
    String toReturn = "";
    if (rs.next()) {
      toReturn = rs.getString(1);
    }
    return toReturn;
  }
  public void addGroup(String group_name) throws SQLException {
    String query = "INSERT into Groups(group_name)"
        + "Select ? where not exists ("
        + "select * from Groups where group_name = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, group_name);
    theStat.setString(2, group_name);
    theStat.executeUpdate();
    theStat.close();
  }
  
  public void addUserToGroup(String user_name, int group_id) throws SQLException {
    String query = "INSERT into User_Group(user_name, group_id)"
        + "Select ?, ? where not exists ("
        + "select * from User_Group where user_name = ? and group_id = ?);";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    theStat.setInt(2, group_id);
    theStat.setString(3, user_name);
    theStat.setInt(4, group_id);
    theStat.executeUpdate();
    theStat.close();
  }
  public void removeUserFromGroup(String user_name, int group_id) throws SQLException {
    String query = "Delete from User_Group"
        + " where user_name = ? and group_id = ?;";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    theStat.setInt(2, group_id);
    theStat.executeUpdate();
    theStat.close();
  }
  public void addEvent(Event e) throws SQLException, ParseException {
    String group_name = e.getGroup();
    List<String> users = e.getAttendees();
    String eventQuery = "";
    eventQuery = "Insert into Events(date, title, day_of_week, description, duration)"
      + "Values( ?, ?, ?, ?, ?)";
    PreparedStatement theStat = conn.prepareStatement(eventQuery);
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    theStat.setString(1, df.format(e.getDate()));
    theStat.setString(2, e.getTitle());
    theStat.setString(3, e.getDayOfWeek());
    theStat.setString(4, e.getDescription());
    theStat.setInt(5, e.getDuration());
    theStat.executeUpdate();
    ResultSet row =  theStat.getGeneratedKeys();
    int theRow = row.getInt(1);
    theStat.close();
    System.out.println("Row Number " + theRow);
    e.setID(theRow);
    String eventToGroup = "";
    if (group_name.equals("")) {
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
    } else {
      int group_id = findGroup(group_name);
      eventToGroup = "INSERT into Group_Event (group_id, event_id)"
          + "Values(?, ?);";
      PreparedStatement theStat2 = conn.prepareStatement(eventToGroup);
      theStat2.setInt(1, group_id);
      theStat2.setInt(2, theRow);
      theStat2.executeUpdate();
      theStat2.close();
    }
  }
  public void removeEvent(Event e) throws SQLException, ParseException {
    int eventID = e.getId();
    List<String> attendees = e.getAttendees();
    String group = e.getGroup();
    if (group != null && !group.equals("")) {
      int group_id = findGroup(group);
      String query3 = "Delete From Group_Event where group_id = ? and event_id = ?";
      PreparedStatement stat3 = conn.prepareStatement(query3);
      stat3.setInt(1, group_id);
      stat3.setInt(2, eventID);
      stat3.executeUpdate();
    } else {
      String query2 = "Delete From User_Event where event_id = ? and user_id = ?";
      PreparedStatement stat2 = conn.prepareStatement(query2);
      for (int i = 0; i < attendees.size(); i++) {
        stat2.setInt(1, eventID);
        stat2.setString(1, attendees.get(i));
        stat2.addBatch();
      }
      stat2.executeQuery();
      stat2.close();
    }
    String query = "Delete From Events where event_id = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setInt(1, eventID);
    stat.executeUpdate(); 
    stat.close();
   
  }
  public List<String> getUsersFromGroup(int group_id) throws SQLException {
    List<String> toReturn = new CopyOnWriteArrayList<String>();
    String query = "Select user_name from User_Group where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, group_id);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    return toReturn;
  }
  public List<Integer> getGroupsIDFromUser(String user_name) throws SQLException {
    List<Integer> toReturn = new CopyOnWriteArrayList<Integer>();
    String query = "Select group_id from User_Group where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getInt(1));
    }
    return toReturn;
  }
  public ConcurrentHashMap<Integer, String> getGroupsNameFromUser(String user_name) throws SQLException {
    ConcurrentHashMap<Integer, String> toReturn = new ConcurrentHashMap<Integer, String>();
    List<Integer> ids = getGroupsIDFromUser(user_name);
    String query = "Select group_name from Groups where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    for (int i = 0; i < ids.size(); i++) {
      theStat.setInt(1, ids.get(i));
      ResultSet rs = theStat.executeQuery();
      if (rs.next()){
        toReturn.put(ids.get(i),rs.getString(1));
      }
    }
    return toReturn;
  }
  public List<String> getUsersFromEvent(int event_id) throws SQLException {
    List<String> toReturn = new CopyOnWriteArrayList<String>();
    String query = "Select user_name from User_Event where event_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, event_id);
    ResultSet rs = theStat.executeQuery();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    rs.close();
    return toReturn;
  }
  public List<Event> getEventsFromGroup(String group_name) throws SQLException {
    int group_id = findGroup(group_name);
    List<String> users = getUsersFromGroup(group_id);
    String query = "Select event_id from Group_Event where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, group_id);
    ResultSet rs = theStat.executeQuery();
    List<Event> groupEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      Event toAdd = new Event(rs2.getDate("date"), rs2.getString("title"), rs2.getString("day_of_week"),
          users, group_name, rs2.getInt("duration"), rs2.getString("description"), rs2.getString("creator"));
      toAdd.setID(rs2.getInt("event_id"));
      groupEvents.add(toAdd);
    }
    return groupEvents;
  }
  
  public List<Event> getEventsFromGroup(int group_id) throws SQLException, ParseException {
    String group_name = findGroup(group_id);
    List<String> users = getUsersFromGroup(group_id);
    String query = "Select event_id from Group_Event where group_id = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setInt(1, group_id);
    ResultSet rs = theStat.executeQuery();
    List<Event> groupEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      Event toAdd = new Event(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(rs2.getString("date")), rs2.getString("title"), rs2.getString("day_of_week"),
          users, group_name, rs2.getInt("duration"), rs2.getString("description"),  rs2.getString("creator"));
      toAdd.setID(rs2.getInt("event_id"));
      groupEvents.add(toAdd);
    }
    return groupEvents;
  }
  public List<Event> getPersonnalEventsFromUser(String user_name) throws SQLException, ParseException {
    String query = "Select event_id from User_Event where user_name = ?";
    PreparedStatement theStat = conn.prepareStatement(query);
    theStat.setString(1, user_name);
    ResultSet rs = theStat.executeQuery();
    List<Event> userEvents = new CopyOnWriteArrayList<Event>();
    String query2 = "select * from Events where event_id = ?";
    PreparedStatement theStat2 = conn.prepareStatement(query2);
    while (rs.next()) {
      theStat2.setInt(1, rs.getInt(1));
      ResultSet rs2 = theStat2.executeQuery();
      List<String> users = getUsersFromEvent(rs2.getInt(1));
      Event toAdd = new Event(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(rs2.getString("date")),
        rs2.getString("title"), rs2.getString("day_of_week"),
        users, "", rs2.getInt("duration"), rs2.getString("description"),
        rs2.getString("creator"));
      toAdd.setID(rs2.getInt("event_id"));
      userEvents.add(toAdd);
    }
    return userEvents;
  }

  public ConcurrentHashMap<Integer, Event> getAllEventsFromUser(String user_name) throws SQLException, ParseException {
    List<Event> eventList = new CopyOnWriteArrayList<Event>();
    eventList.addAll(getPersonnalEventsFromUser(user_name));
    System.out.println(eventList.size());
    List<Integer> groups = getGroupsIDFromUser(user_name);
    for (int i = 0; i < groups.size(); i++) {
      eventList.addAll(getEventsFromGroup(groups.get(i)));
    }
    ConcurrentHashMap<Integer, Event> actualReturn = new ConcurrentHashMap<Integer, Event>();
    for (int i = 0; i < eventList.size(); i++) {
      Event curr = eventList.get(i);
      System.out.println(curr.getId());
      actualReturn.put(curr.getId(), curr);
    }
    System.out.println(actualReturn.size());
    return actualReturn;
  }
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
  public int getMaxEventID() throws SQLException {
    int toReturn = -1;
    String query = "Select MAX(event_id) from User_Event";
    PreparedStatement theStat = conn.prepareStatement(query);
    ResultSet rs = theStat.executeQuery();
    if (rs.next()) {
      toReturn = rs.getInt(1);
    }
    return toReturn;
  }
  private void buildTable(String schema) throws SQLException {
    PreparedStatement myStat = conn.prepareStatement(schema);
    myStat.execute();
    myStat.close();
  }
  public void closeConnection() throws SQLException {
    conn.close();
  }
}
