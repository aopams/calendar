package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.Commands;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
/**
 * The contacts thread handles when users interact with the
 * contacts page. This includes when users create groups, add/
 * accept/remove friends, view members of a group, and add
 * members to a group as well. This thread also handles the
 * heartbeat pull for displaying and updating friends and groups
 * on the front end.
 * @author wtruong02151
 *
 */
public class ContactsThread implements Callable<String> {
  private ClientHandler client1;
  private DatabaseHandler myDBHandler;
  private String user2;
  private String groupName;
  private int removeGroupID;
  private List<String> groupMembers;
  private Commands command;
  private ConcurrentHashMap<Integer, ClientHandler> clients;
  /**
   * Contacts thread constructor handles initializing all global
   * variables.
   * @param client1 the client we're working with.
   * @param client2 the client receiving the request (for friend interactions)
   * @param groupName the group's name.
   * @param groupid the group's id.
   * @param memberNames list of members to add to a group.
   * @param command command we're dealing with.
   * @param clients list of clients (must update all logged in clients' hashmaps
   * of information so that we don't have to call back to the database).
   */
  public ContactsThread(ClientHandler client1, String client2,
      String groupName, Integer groupid,
      List<String> memberNames, Commands command,
      ConcurrentHashMap<Integer, ClientHandler> clients) {
    this.client1 = client1;
    this.user2 = client2;
    this.groupName = groupName;
    groupMembers = memberNames;
    this.command = command;
    if (groupid != null) {
      removeGroupID = groupid;
    }
    this.clients = clients;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  @Override
  public String call() throws SQLException, ParseException {
    String user1 = client1.getClient();
    switch (command) {
      case GET_NAME:
        String name = myDBHandler.getName(user1);
        myDBHandler.closeConnection();
        return name;
      case REMOVE_FRIEND:
        //loop through clients, find users who were friends with current user,
        //and update their friend's hashmap
        for (Entry<Integer, ClientHandler> cli: clients.entrySet()) {
          if (cli.getValue().getClient().equals(user2)) {
            cli.getValue().removeFriend(user1);
          }
        }
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
            myDBHandler.closeConnection();
            return "exists";
          } else {
            System.out.println("friend request sent");
            for (Entry<Integer, ClientHandler> cli: clients.entrySet()) {
              if (cli.getValue().getClient().equals(user2)) {
                cli.getValue().addFriend(user1);
              }
            }
            myDBHandler.addFriendRequest(user1, user2);
            myDBHandler.closeConnection();
            return "success";
          }
        //friend does not exist
        } else {
          System.out.println("friend does not exist");
          myDBHandler.closeConnection();
          return "toobad";
        }
      case ACCEPT_FRIEND :
        client1.acceptFriend(user2);
        for (Entry<Integer, ClientHandler> cli: clients.entrySet()) {
          if (cli.getValue().getClient().equals(user2)) {
            cli.getValue().acceptFriend(user1);
          }
        }
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
          myDBHandler.closeConnection();
          return groupStatus;
        } else {
          System.out.println("group not found");
          Set<String> acceptedFriends = myDBHandler.getAcceptedFriends(user1);
          for (Iterator<String> it = groupMembers.iterator(); it.hasNext();) {
            //user does not exist, remove from list
            String user = it.next();
            if (myDBHandler.findUser(user).isEmpty()) {
              invalidFriends.append(user);
              invalidFriends.append(",");
              it.remove();
            //not friends with this user, so it is also considered invalid
            } else if (!acceptedFriends.contains(user) && !user.equals(user1)) {
              System.out.println("not friends with " + user);
              invalidFriends.append(user);
              invalidFriends.append(",");
              it.remove();
            }
            //user list includes user logged in, so
            //must make sure to not accidentally
            //remove the user him/herself
          }
          int groupID = myDBHandler.getNewGroupID();
          System.out.println("");
          myDBHandler.addGroup(groupName, groupID);
          for (int i = 0; i < groupMembers.size(); i++) {
            System.out.println("member to add = " + groupMembers.get(i));
            myDBHandler.addUserToGroup(groupMembers.get(i), groupID);
            for (Entry<Integer, ClientHandler> cli: clients.entrySet()) {
              if (cli.getValue().getClient().equals(groupMembers.get(i))) {
                cli.getValue().addGroup(groupName, groupID);
              }
            }
          }
          client1.addGroup(groupName, groupID);
          groupStatus = invalidFriends.toString();
          myDBHandler.closeConnection();
          return groupStatus;
        }
      case REMOVE_GROUP :
        client1.removeGroup(removeGroupID);
        myDBHandler.removeUserFromGroup(user1, removeGroupID);
        //remove user from all events in
        //database of this given group
        //also update hashmap of events
        List<Event> eventsToEdit = myDBHandler
            .getEventsFromGroup(removeGroupID);
        for (Event e : eventsToEdit) {
          myDBHandler.removeUserFromEvent(user1, e.getId());
          client1.removeEvent(e);
        }
        //if we just removed last member from the group, delete the group.
        if (myDBHandler.getUsersFromGroup(removeGroupID).isEmpty()) {
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
        myDBHandler.closeConnection();
        return toReturn.toString();
      case NEW_MEMBERS :
        StringBuilder invalidFriends2 = new StringBuilder();
        Set<String> acceptedFriends2 = myDBHandler.getAcceptedFriends(user1);
        for (Iterator<String> it = groupMembers.iterator(); it.hasNext();) {
          //user does not exist, remove from list
          String user = it.next();
          if (myDBHandler.findUser(user).isEmpty()) {
            System.out.println("removing user = " + user);
            invalidFriends2.append(user);
            invalidFriends2.append(" ");
            it.remove();
          //if the user is not friends with the
          //person they want to add, don't add them
          //also don't add yourself
          //not friends with this user, so it is also considered invalid
          } else if (!acceptedFriends2.contains(user) || user.equals(user1)) {
            System.out.println("removing user = " + user);
            invalidFriends2.append(user);
            invalidFriends2.append(",");
            it.remove();
          }
        }
        for (int i = 0; i < groupMembers.size(); i++) {
          System.out.println("member to add = " + groupMembers.get(i));
          myDBHandler.addUserToGroup(groupMembers.get(i), removeGroupID);
          for (Entry<Integer, ClientHandler> cli: clients.entrySet()) {
            if (cli.getValue().getClient().equals(groupMembers.get(i))) {
              cli.getValue().addGroup(groupName, removeGroupID);
            }
          }
        }
        myDBHandler.closeConnection();
        return invalidFriends2.toString();
      default:
        break;
    }
    System.out.println("connection over");
    myDBHandler.closeConnection();
    return null;
  }
}
