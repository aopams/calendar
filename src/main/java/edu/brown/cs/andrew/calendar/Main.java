package edu.brown.cs.andrew.calendar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.andrew.handlers.JSONParser;
import edu.brown.cs.andrew.handlers.SparkHandler;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

public class Main {

  private static DatabaseHandler myDBHandler;
  private static JSONParser myJSONParser;
  private static SparkHandler server = new SparkHandler("calendar.sqlite3");
  
  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis() / 1000);
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
     /** myDBHandler.createTablesForCalendar();
      myDBHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.deleteUser("Harsha");
      myDBHandler.deleteUser("Harsha2");
      myDBHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.insertUser("Harsha2", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.insertUser("Rohan", "6969", "Rohan Chandra", "rohan_chandra@brown.edu");
      myDBHandler.insertUser("Dylan", "dg", "Dylan Gattey", "dylan_gattey@brown.edu");
      myDBHandler.insertUser("Will", "Nagasaki", "William Truong", "william_turong@brown.edu");
      myDBHandler.insertUser("Andrew", "dino", "Andrew Osgood", "andrew_osgood@brown.edu");
      myDBHandler.insertUser("Will2", "Nagasaki", "William Truong", "william_turong@brown.edu");
      myDBHandler.insertUser("Will3", "Nagasaki", "William Truong", "william_turong@brown.edu");
      myDBHandler.insertUser("Will4", "Nagasaki", "William Truong", "william_turong@brown.edu");
      myDBHandler.addFriendRequest("Rohan", "Harsha2");
      myDBHandler.addFriendRequest("Rohan", "Harsha");
      myDBHandler.addFriendRequest("Rohan", "Will");
      myDBHandler.addFriendRequest("Rohan", "Dylan");
      myDBHandler.addFriendRequest("Rohan", "Andrew");
      myDBHandler.addFriendRequest("Rohan", "Will2");
      myDBHandler.addFriendRequest("Rohan", "Will3");
      myDBHandler.addFriendRequest("Rohan", "Will4");
      myDBHandler.acceptFriendRequest("Rohan", "Harsha2");
      myDBHandler.addFriendRequest("Harsha", "Harsha2");
      myDBHandler.acceptFriendRequest("Harsha2", "Harsha");
      myDBHandler.addGroup("Harsha Squad");
      myDBHandler.addUserToGroup("Harsha", 1);
      myDBHandler.addUserToGroup("Harsha2", 1);
      Date myDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/4/2015");
      System.out.println(myDate.toString());
      List<String> hSquad = new ArrayList<String>();
      hSquad.add("Harsha");
      hSquad.add("Harsha2");
      List<String> hGroup = new ArrayList<String>();
      hGroup.add("Harsha");
      hGroup.add("Rohan");
      System.out.println(myDate.toString());
      Event e = new Event(myDate, "Party Time!!!!", "Friday", hSquad,
          "Harsha Squad", 180,
          "Harsha Squad going Ham to Trap Queen for 3 hours",
          "Harsha");
      Date myDate2 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("16/4/2015 13:00");
      Date myDate3 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("21/4/2015 18:00");
      System.out.println(myDate2);
      Event e2 = new Event(myDate2, "Ninja Time!", "Monday", hGroup, "", 30,
          "Harsha going stealth-mode",
          "Harsha2");
      Event e3 = new Event(myDate3, "Ninja Time! Pt. 2", "Tuesday", hGroup, "", 60,
          "Harsha and Rohan going stealth-mode",
          "Rohan");
      System.out.println(myDBHandler.findGroup("Harsha Squad"));
      myDBHandler.addEvent(e);
      myDBHandler.addEvent(e2);
      myDBHandler.addEvent(e3); **/
      myDBHandler.closeConnection();
      System.out.println(System.currentTimeMillis() / 1000);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    } finally {
      run(args);
      //try {
        //myDBHandler.closeConnection();
      //} catch (SQLException e) {
        // TODO Auto-generated catch block
      //  e.printStackTrace();
      //}
    }
  }

  /**
   * run() method handles command line input to run program with gui if "--gui"
   * is indicated
   */
  private static void run(String[] args) {
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      server.runSparkServer();
    }
  }
}