package edu.brown.cs.andrew.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

public class SparkHandler {
  private static String database;
  private static final int RESSTAT = 500;
  private static DatabaseHandler myDBHandler;
  private static JSONParser myJSONParser;
  private static Gson GSON = new Gson();
  private static String testUser = "";
  private static int randomHolder = (int)(Math.random() * 1000000);
  private static ConcurrentHashMap<Integer, ClientHandler> clients;
  public SparkHandler(String db) {
    try {
      myDBHandler = new DatabaseHandler(db);
      database = db;
      clients = new ConcurrentHashMap<Integer, ClientHandler>();
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Error connecting to the Database: " + db);
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

  public void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.setPort(1234);
    Spark.exception(Exception.class, new ExceptionPrinter());
    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Routes

    Spark.get("/", new CodeHandler(), freeMarker);
    //Spark.get("/calendar", new FrontHandler(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.post("/calendar/:id", new LoginEventHandler(), freeMarker);
    Spark.post("/getevents", new BTFEventHandler());
  }

  private static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      while (clients.containsKey(randomHolder)) {
        randomHolder = (int)(Math.random() * 1000000);
      }
      String form =   "<form method = \"POST\" action=\"/calendar/" + randomHolder +"\">";
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "", "form", form);
      return new ModelAndView(variables, "login.ftl");
    }
  }
  private static class LoginEventHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String user = qm.value("user");
      String pass = qm.value("pass");
      int id =Integer.parseInt(req.params(":id"));
      boolean found = false;
      try {
        found = myDBHandler.findUser(user, pass);
      } catch (SQLException e) {
        String newMessage = "An Error Occurred while logging in, please try again.";
        Map<String, Object> variables = ImmutableMap.of("title",
            "Login", "message", newMessage);
        return new ModelAndView(variables, "login.ftl");
      }
      if (found) {
        Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
            "message", "");
        //ServerCalls sc = new ServerCalls();
        //String html = sc.loginClicked();
        ClientHandler newClient = new ClientHandler(database, user);
        clients.put(id, newClient);
        return new ModelAndView(variables, "main.ftl");
      } else {
        String newMessage = "The username or password entered was not found";
        Map<String, Object> variables = ImmutableMap.of("title",
            "Login", "message", newMessage);
        return new ModelAndView(variables, "login.ftl");
      }
    }
  }
  /**
   * Back end to front end; for a given user, grabs all of that user's events so
   * that they can be displayed on the calendar page when the user logs in.
   *
   * @author wtruong02151
   *
   */
  private static class BTFEventHandler implements Route {

    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      // list of events that this user has
      ConcurrentHashMap<Integer, Event> testEvents;
      try {
        testEvents = myDBHandler.getAllEventsFromUser(testUser);
        List<String> toFrontEnd = new ArrayList<String>();
        for (Entry<Integer, Event> e : testEvents.entrySet()) {
          Event curr = e.getValue();
          toFrontEnd.add(myJSONParser.eventToJson(curr));
        }
        Map<String, Object> variables = new ImmutableMap.Builder()
        .put("events", toFrontEnd).build();
        System.out.println(GSON.toJson(variables));
        return GSON.toJson(variables);
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