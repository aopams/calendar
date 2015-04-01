package edu.brown.cs.andrew.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

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
    
    stmt.executeUpdate("Drop Table if Exists Users");
    stmt.executeUpdate("Drop Table if Exists Groups");
    stmt.executeUpdate("Drop Table if Exists Events");
    
    
    
    stmt.close();
    String userTable = "CREATE TABLE Users ("
      + "user_name nvarchar(16) PRIMARY KEY," 
      + "user_password nvarchar(30) NOT NULL,"
      + "name nvarchar(30) NOT NULL,"
      + "email nvarchar(30) NOT NULL);";
    String groupTable = "CREATE Table Groups ("
      + "group_id integer identity PRIMARY KEY,"
      + "group_name nvarchar(16));";
    String eventTable = "CREATE TABLE Events ("
      + "event_id integer identity Primary Key,"
      + "date DATE not NULL,"
      + "time TIME not NULL,"
      + "title nvarchar(50) not NULL,"
      + "day_of_week nvarchar(10) not NULL,"
      + "description nvarchar(150));";
    String friendsTable = "CREATE Table Friends ("
      + "user_name1 nvarchar(16) NOT NULL,"
      + "user_name2 nvarchar(16) NOT NULL,"
      + "status nvarchar(10) NOT NULL,"
      + "PRIMARY KEY(user_name1, user_name2),"
      + "FOREIGN KEY (user_name1) References Users(user_name)"
      + "FOREIGN KEY (user_name2) References Users(user_name));";
    String userEventTable = "CREATE TABLE User_Event ("
      + "user nvarchar(16) NOT NULL,"
      + "event_id integer NOT NULL,"
      + "PRIMARY KEY(user, event_id),"
      + "FOREIGN KEY (user) references Users(user_name),"
      + "FOREIGN KEY (event_id) references Events(event_id));";
    String userGroupTable = "CREATE TABLE User_Group ("
      + "user nvarchar(16) NOT NULL,"
      + "group_id integer NOT NULL,"
      + "PRIMARY KEY(user, group_id),"
      + "FOREIGN KEY (user) references Users(user_name),"
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
    if (rs.isClosed()) {
      toReturn = rs.getString(1);
    }
    return toReturn;
  }
  public void insertUser(String user_name, String password,
      String name, String email) throws SQLException {
    if (findUser(user_name) == null) {
      String query = "INSERT into Users (user_name, user_password, name, email)"
          + "VALUES (?, ?, ?, ?);";
      PreparedStatement theStat = conn.prepareStatement(query);
      theStat.setString(1, user_name);
      theStat.setString(2, password);
      theStat.setString(3, name);
      theStat.setString(4, email);
      theStat.executeUpdate();
    }
  }
  public void deleteUser(String user_name) throws SQLException {
    if (findUser(user_name) == null) {
      String query = "Delete from Users where user_name = \"" + user_name + "\"";
      Statement theStat = conn.createStatement();
      theStat.executeUpdate(query);
      theStat.close();
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
  private void buildTable(String schema) throws SQLException {
    PreparedStatement myStat = conn.prepareStatement(schema);
    myStat.execute();
    myStat.close();
    
  }
}
