package edu.brown.cs.andrew.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
  private static ConcurrentHashMap<Integer, Date> currentWeeks;
  private static String database;
  private static final int RESSTAT = 500;
  private static DatabaseHandler myDBHandler;
  private static Gson GSON = new Gson();
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
    Spark.post("/leftarrow", new BTFEventHandler());
    Spark.post("/rightarrow", new BTFEventHandler());
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
  private static String getRandomForm() {
    while (clients.containsKey(randomHolder)) {
      randomHolder = (int)(Math.random() * 1000000);
    }
    String form = "<form method = \"POST\" action=\"/calendar/" + randomHolder +"\">";
    return form;
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
        String form = getRandomForm();
        String newMessage = "An Error Occurred while logging in, please try again.";
        Map<String, Object> variables = ImmutableMap.of("title",
            "Login", "message", newMessage, "form", form);
        return new ModelAndView(variables, "login.ftl");
      }
      if (found) {
        
        Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
            "message", "");
        ServerCalls sc = new ServerCalls();
        String html = sc.loginClicked();
        ClientHandler newClient = new ClientHandler(database, user);
        clients.put(id, newClient);
        return new ModelAndView(variables, "main.ftl");
      } else {
        clients.remove(randomHolder);
        String form = getRandomForm();
        String newMessage = "The username or password entered was not found";
        Map<String, Object> variables = ImmutableMap.of("title",
            "Login", "message", newMessage, "form", form);
        return new ModelAndView(variables, "login.ftl");
      }
    }
  }
  private static DateHandler parseDate(Date d) {
    System.out.println("Parsing Date");
    String date = d.toString();
    String month = date.substring(4, 7);
    System.out.println(date.substring(8,10));
    int day = Integer.parseInt(date.substring(8,10));
    System.out.println(date.substring(date.length()-4, date.length()));
    int year = Integer.parseInt(date.substring(date.length()-4, date.length()));
    DateHandler dH = new DateHandler(month, day, year);
    return dH;
  }
  
  private static List<DateHandler> getCurrentWeek() {
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int week = c.get(Calendar.WEEK_OF_YEAR);
    c.set(Calendar.WEEK_OF_YEAR, week);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    List<DateHandler> currentWeek = new ArrayList<DateHandler>();
    currentWeek.add(parseDate(c.getTime()));
    for(int i = 0; i < 6; i++) {
      c.add(Calendar.DATE, 1);
      System.out.println(c.getTime());
      currentWeek.add(parseDate(c.getTime()));
    }
    return currentWeek;
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
      Date date = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      QueryParamsMap qm = req.queryMap();
      int week = c.get(Calendar.WEEK_OF_YEAR);
      int clientID = Integer.parseInt(qm.value("string").substring(10));
      c.set(Calendar.WEEK_OF_YEAR, week);
      c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
      Date currentWeekStart = currentWeeks.get(clientID);
      if (currentWeekStart == null) {
        currentWeeks.put(clientID, c.getTime());
        currentWeekStart = c.getTime();
      }
      // list of events that this user has
      try {
        String dateString = qm.value("date");
        System.out.println(dateString);
        if (dateString != null && !dateString.equals("")) {
          Date reference =
            new SimpleDateFormat("MM dd YYYY").parse(dateString);
          if (reference.equals(currentWeekStart)) {
            c.set(Calendar.WEEK_OF_YEAR, -1);
          } else {
            c.set(Calendar.DATE, 1);
          }
          currentWeekStart = c.getTime();
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      Gson gson = new Gson();
      List<DateHandler> currentWeek = getCurrentWeek();

      System.out.println(clientID);
      ConcurrentHashMap<Integer, Event> testEvents;
      testEvents = clients.get(clientID).getEventsByWeek(currentWeekStart);
      List<String> toFrontEnd = new ArrayList<String>();
      for (Entry<Integer, Event> e : testEvents.entrySet()) {
        Event curr = e.getValue();
        toFrontEnd.add(gson.toJson(curr));
      }

      Map<String, Object> variables = new ImmutableMap.Builder()
      .put("events", toFrontEnd)
      .put("week", currentWeek).build();
      System.out.println(GSON.toJson(variables));
      return GSON.toJson(variables);
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
      HashMap<String, String> map = sc.authorize(code);
      map.get("access_token");
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
