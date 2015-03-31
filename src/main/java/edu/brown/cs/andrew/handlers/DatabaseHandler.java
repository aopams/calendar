package edu.brown.cs.andrew.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DatabaseHandler {
  
  private Connection conn;
  
  public DatabaseHandler(String dbFile) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection(dbFile);
  }
  
  public void createTablesForCalendar() {
    String userTable = "CREATE TABLE Users ("
      + "user_name nvarchar(16) PRIMARY KEY," 
      + "user_password nvarchar(30) NOT NULL);";
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
      + "FOREIGN KEY (event_id) references Events(id));";
    String userGroupTable = "CREATE TABLE User_Event ("
        + "user nvarchar(16) NOT NULL,"
        + "group_id integer NOT NULL,"
        + "PRIMARY KEY(user, group_id),"
        + "FOREIGN KEY (user) references Users(user_name),"
        + "FOREIGN KEY (event_id) references Groups(id));";
  }
}
