package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.Commands;
import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class ContactsThread implements Callable<String> {
  
  private ClientHandler client1;
  private DatabaseHandler myDBHandler;
  private String user2;
  private String groupName;
  private List<String> groupMembers;
  private Commands command;

  public ContactsThread(ClientHandler client1, String client2, String groupName, List<String> memberNames, Commands command) {
    this.client1 = client1;
    this.user2 = client2;
    this.groupName = groupName;
    groupMembers = memberNames;
    this.command = command;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
 


  @Override
  public String call() throws SQLException {
    String user1 = client1.getClient();
      int groupId = myDBHandler.findGroup(groupName);
      switch (command) {
        case REMOVE_FRIEND:
          client1.removeFriend(user2);
          myDBHandler.removeFriend(user1, user2);
          break;
        case ADD_FRIEND :
          client1.requestFriend(user2);
          myDBHandler.addFriendRequest(user1, user2);
          break;
        case ACCEPT_FRIEND : 
          client1.acceptFriend(user2);
          myDBHandler.acceptFriendRequest(user1, user2);
          break;
        case ADD_GROUP :
          myDBHandler.addGroup(groupName);
          for (int i = 0; i < groupMembers.size(); i++) {
            myDBHandler.addUserToGroup(groupMembers.get(i), groupId);
          }
          break;
        case REMOVE_GROUP :
          client1.removeGroup(groupName);
          myDBHandler.removeUserFromGroup(user1, groupId);
          break;
      default:
        break;
      }
      return null;
  }
}
