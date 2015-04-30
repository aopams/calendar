package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class GroupRetrievalThread implements Callable<List<String>> {

  private String group_name;
  private DatabaseHandler myDBHandler;
  
  public GroupRetrievalThread(String gn) {
    group_name = gn;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public List<String> call() throws SQLException {
    int group_id = myDBHandler.findGroup(group_name);
    List<String> toReturn = myDBHandler.getUsersFromGroup(group_id);
    myDBHandler.closeConnection();
    return toReturn;
  }

}
