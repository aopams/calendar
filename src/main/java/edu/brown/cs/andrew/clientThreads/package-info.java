/**
 * This package is our thread package, which handles all cases in
 * threads are made for our project during its usage. The contacts
 * thread handles communicating with the database whenever a
 * a user interacts with their contacts page (adding/removing a
 * friend or a group). The calendar thread handles communcating with
 * the database when a user interacts with the calendar. The
 * group retrieval thread handles when we want to grab the groups
 * for a given user. Our heartbeat thread periodically communicates
 * with the DB to update our front end (new events, friend requests,
 * etc.). The UserThread handles registration.
 */
/**
 * @author aosgood
 */
package edu.brown.cs.andrew.clientThreads;
