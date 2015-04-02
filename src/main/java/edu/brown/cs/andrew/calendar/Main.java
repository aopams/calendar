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
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.andrew.handlers.JSONParser;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

import com.google.gson.*;

public class Main {

  private static final int RESSTAT = 500;
  private static DatabaseHandler myDBHandler;
  private static JSONParser myJSONParser;

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
      Date myDate = new SimpleDateFormat("dd/M/yyyy").parse("03/4/2015");
      System.out.println(myDate.toString());
      List<String> hSquad = new ArrayList<String>();
      hSquad.add("Harsha");
      hSquad.add("Harsha2");
      List<String> hGroup = new ArrayList<String>();
      hGroup.add("Harsha");
      System.out.println(myDate.toString());
      Event e = new Event(myDate, "Party Time!!!!", "Friday", hSquad,
          "Harsha Squad", 180,
          "Harsha Squad going Ham to Trap Queen for 3 hours");
      Date myDate2 = new SimpleDateFormat("dd/M/yyyy").parse("06/4/2015");
      Event e2 = new Event(myDate2, "Ninja Time!", "Monday", hGroup, "", 30,
          "Harsha going stealth-mode");
      System.out.println(myDBHandler.findGroup("Harsha Squad"));
      myDBHandler.addEvent(e);
      myDBHandler.addEvent(e2);
      List<Event> events = myDBHandler.getAllEventsFromUser("Harsha");
      for (int i = 0; i < events.size(); i++) {
        System.out.println(events.get(i).getAttendees().size());
      }
      myJSONParser = new JSONParser();
      myJSONParser.eventToJson(e);
      System.out.println(System.currentTimeMillis() / 1000);
    } catch (ClassNotFoundException | SQLException | ParseException e) {
      e.printStackTrace();
    } finally {
      run(args);
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
      runSparkServer();
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable " + "use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.setPort(1234);
    Spark.exception(Exception.class, new ExceptionPrinter());
    FreeMarkerEngine freeMarker = createEngine();
    System.out.println("spark?");
    // Setup Spark Routes

    Spark.get("/calendar", new CodeHandler(), freeMarker);
    Spark.post("/calendar", new BTFEventHandler(), freeMarker);
  }

  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "");
      ServerCalls sc = new ServerCalls();
      String html = sc.loginClicked();
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Back end to front end; for a given user, grabs all of that user's events so
   * that they can be displayed on the calendar page when the user logs in.
   *
   * @author wtruong02151
   *
   */
  private static class BTFEventHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String testUser = "Harsha";
      // list of events that this user has
      List<Event> testEvents;
      try {
        testEvents = myDBHandler.getAllEventsFromUser(testUser);
        List<String> toFrontEnd = new ArrayList<String>();
        for (Event e : testEvents) {
          toFrontEnd.add(myJSONParser.eventToJson(e));
        }
        Map<String, List<String>> variables = new ImmutableMap.Builder()
        .put("events", testEvents).build();
        return new ModelAndView(variables, "main.ftl");
      } catch (SQLException e1) {
        System.out.println("ERROR: SQLException");
      } catch (ParseException e1) {
        System.out.println("ERROR: SQLException");
      }

      return null;
    }
  }

  private static class CodeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "");
      String code = req.queryString().substring(
          req.queryString().indexOf('=') + 1);
      System.out.println(code);
      ServerCalls sc = new ServerCalls();
      sc.authorize(code);

      return new ModelAndView(variables, "main.ftl");
    }
  }

  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(RESSTAT);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}