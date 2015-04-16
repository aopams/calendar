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
      myDBHandler.createTablesForCalendar();
      myDBHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.deleteUser("Harsha");
      myDBHandler.deleteUser("Harsha2");
      myDBHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.insertUser("Harsha2", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myDBHandler.addFriendRequest("Harsha", "Harsha2");
      myDBHandler.addFriendRequest("Harsha2", "Harsha");
      myDBHandler.acceptFriendRequest("Harsha2", "Harsha");
      myDBHandler.removeFriend("Harsha", "Harsha2");
      myDBHandler.addGroup("Harsha Squad");
      myDBHandler.addUserToGroup("Harsha", 1);
      myDBHandler.addUserToGroup("Harsha2", 1);
      Date myDate = new SimpleDateFormat("dd/MM/yyyy").parse("03/4/2015");
      System.out.println(myDate.toString());
      List<String> hSquad = new ArrayList<String>();
      hSquad.add("Harsha");
      hSquad.add("Harsha2");
      List<String> hGroup = new ArrayList<String>();
      hGroup.add("Harsha");
      System.out.println(myDate.toString());
      Event e = new Event(myDate, "Party Time!!!!", "Friday", hSquad,
          "Harsha Squad", 180,
          "Harsha Squad going Ham to Trap Queen for 3 hours",
          "Harsha");
      Date myDate2 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/4/2015 13:00");
      System.out.println(myDate2);
      Event e2 = new Event(myDate2, "Ninja Time!", "Monday", hGroup, "", 30,
          "Harsha going stealth-mode",
          "Harsha2");
      System.out.println(myDBHandler.findGroup("Harsha Squad"));
      myDBHandler.addEvent(e);
      myDBHandler.addEvent(e2);
      myJSONParser = new JSONParser();
      myJSONParser.eventToJson(e);
      System.out.println(System.currentTimeMillis() / 1000);
    } catch (ClassNotFoundException | SQLException | ParseException e) {
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