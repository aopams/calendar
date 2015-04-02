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
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

public class Main {

  private static final int RESSTAT = 500;

  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis() / 1000);
    try {
      DatabaseHandler myHandler = new DatabaseHandler("calendar.sqlite3");
      myHandler.createTablesForCalendar();
      myHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myHandler.deleteUser("Harsha");
      myHandler.deleteUser("Harsha2");
      myHandler.insertUser("Harsha", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myHandler.insertUser("Harsha2", "meow", "Harsha Yeddanapudy",
          "hyeddana@cs.brown.edu");
      myHandler.addFriendRequest("Harsha", "Harsha2");
      myHandler.addFriendRequest("Harsha2", "Harsha");
      myHandler.acceptFriendRequest("Harsha2", "Harsha");
      myHandler.removeFriend("Harsha", "Harsha2");
      myHandler.addGroup("Harsha Squad");
      myHandler.addUserToGroup("Harsha", 1);
      myHandler.addUserToGroup("Harsha2", 1);
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
      System.out.println(myHandler.findGroup("Harsha Squad"));
      myHandler.addEvent(e);
      myHandler.addEvent(e2);
      List<Event> events = myHandler.getAllEventsFromUser("Harsha");
      for (int i = 0; i < events.size(); i++) {
        System.out.println(events.get(i).getAttendees().size());
      }
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

    Spark.get("/calendar", new FrontHandler(), freeMarker);
    Spark.get("/", new CodeHandler(), freeMarker);

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