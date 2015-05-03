package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.Commands;
import edu.brown.cs.andrew.handlers.DatabaseHandler;

public class ContactsThread implements Callable<String> {
  
  private ClientHandler client1;
  private DatabaseHandler myDBHandler;
  private String user2;
  private String groupName;
  private int removeGroupID;
  private List<String> groupMembers;
  private Commands command;

  //ADD GROUP ID FOR CONSTRUCTOR TO BE ABLE TO REMOVE YOURSELF FROM A GROUP
  public ContactsThread(ClientHandler client1, String client2, String groupName, Integer groupid, List<String> memberNames, Commands command) {
    this.client1 = client1;
    this.user2 = client2;
    this.groupName = groupName;
    groupMembers = memberNames;
    this.command = command;
    if (groupid != null) {
      removeGroupID = groupid;
    }
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  @Override
  public String call() throws SQLException {
    String user1 = client1.getClient();
      switch (command) {
        case GET_NAME:
          String name = myDBHandler.getName(user1);
          return name;
        case REMOVE_FRIEND:
          client1.removeFriend(user2);
          myDBHandler.removeFriend(user1, user2);
          break;
        case ADD_FRIEND :
          //friend exists
          if (myDBHandler.findUser(user2) != null) {
            //check if user already sent this friend a request,
            //or the user is already friends with this other user.
            String status = client1.requestFriend(user2);
            if (status.equals("exists")) {
              System.out.println("relation already exists");
              return "exists";
            } else {
              System.out.println("friend request sent");
              myDBHandler.addFriendRequest(user1, user2);
              return "success";
            }
          //friend does not exist
          } else {
            System.out.println("friend does not exist");
            return "toobad";
          }
        case ACCEPT_FRIEND : 
            client1.acceptFriend(user2);
            myDBHandler.acceptFriendRequest(user1, user2);
            break;
            //CHECK IF EACH USER IS VALID
        case ADD_GROUP :
          String groupStatus = "";
          StringBuilder invalidFriends = new StringBuilder();
          //check if groupname already exists, if it does, then
          //report an error back to user
          System.out.println("group name = " + groupName);
          if (myDBHandler.findGroup(groupName) != -1) {
            System.out.println("group found");
            groupStatus = "failure";
            return groupStatus;
          } else {
            System.out.println("group not found");
            Set<String> acceptedFriends = myDBHandler.getAcceptedFriends(user1);
            for (Iterator<String> it = groupMembers.iterator(); it.hasNext();) {
              //user does not exist, remove from list
              String user = it.next();
              if (myDBHandler.findUser(user) == null) {
                it.remove();
              } else if (!acceptedFriends.contains(user) && !user.equals(user1)) {
                System.out.println("not friends with " + user);
                invalidFriends.append(user);
                invalidFriends.append(",");
                it.remove();
              }
              //user list includes user logged in, so must make sure to not accidentally
              //remove the user him/herself
            }
            int groupID = myDBHandler.getNewGroupID();
            myDBHandler.addGroup(groupName, groupID);
            for (int i = 0; i < groupMembers.size(); i++) {
              System.out.println("member to add = " + groupMembers.get(i));
              myDBHandler.addUserToGroup(groupMembers.get(i), groupID);
            }
            client1.addGroup(groupName, groupID);
            groupStatus = invalidFriends.toString();
            return groupStatus;
          }
        case REMOVE_GROUP :
          client1.removeGroup(removeGroupID);
          myDBHandler.removeUserFromGroup(user1, removeGroupID);
          //if we just removed last member from the group, delete the group.
          if (myDBHandler.getUsersFromGroup(removeGroupID).isEmpty()) {
            System.out.println("Group is empty, remove it from db");
            myDBHandler.removeGroup(removeGroupID);
          }
          break;
        case FIND_MEMBERS :
          List<String> members = myDBHandler.getUsersFromGroup(removeGroupID);
          StringBuilder toReturn = new StringBuilder();
          for (Iterator<String> i = members.iterator(); i.hasNext();) {
            String person = i.next();
            toReturn.append(person);
            toReturn.append(",");
          }
          return toReturn.toString();
        case NEW_MEMBERS :
          Set<String> acceptedFriends2 = myDBHandler.getAcceptedFriends(user1);
          for (Iterator<String> it = groupMembers.iterator(); it.hasNext();) {
            //user does not exist, remove from list
            String user = it.next();
            if (myDBHandler.findUser(user) == null) {
              it.remove();
            //if the user is not friends with the person they want to add, don't add them
            } else if (!acceptedFriends2.contains(user)) {
              System.out.println("not friends with " + user);
              it.remove();
            }
          }
          for (int i = 0; i < groupMembers.size(); i++) {
            myDBHandler.addUserToGroup(groupMembers.get(i), removeGroupID);
          }
          break;
      default:
        break;
      }
      System.out.println("connection over");
      myDBHandler.closeConnection();
      return null;
  }
}
