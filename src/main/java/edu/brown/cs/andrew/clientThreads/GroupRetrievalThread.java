package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
/**
 * Group retrieval thread handles grabbing the users
 * from a group when adding them to an event.
 * @author wtruong02151
 *
 */
public class GroupRetrievalThread implements Callable<List<String>> {
  private String groupName;
  private DatabaseHandler myDBHandler;
  /**
   * GroupRetrievalThread constructor, initializes
   * global variables.
   * @param gn group name
   */
  public GroupRetrievalThread(String gn) {
    groupName = gn;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public List<String> call() throws SQLException {
    int groupID = myDBHandler.findGroup(groupName);
    List<String> toReturn = myDBHandler.getUsersFromGroup(groupID);
    myDBHandler.closeConnection();
    return toReturn;
  }

}
