package edu.brown.cs.andrew.handlers;
/**
 * enums used as commands for events and contacts
 * (helps clarify when dealing with communication from
 * front end to back end.
 * @author wtruong02151
 *
 */
public enum Commands {
  ADD_FRIEND, ACCEPT_FRIEND, REMOVE_FRIEND,
  ADD_GROUP, REMOVE_GROUP, ADD_EVENT, DELETE_EVENT, REMOVE_USER_EVENT,
  EDIT_EVENT, GET_NAME, FIND_MEMBERS, NEW_MEMBERS;
}
