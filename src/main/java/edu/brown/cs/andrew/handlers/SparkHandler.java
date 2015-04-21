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

import edu.brown.cs.andrew.clientThreads.CalendarThread;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

public class SparkHandler {
  private static ConcurrentHashMap<Integer, Date> currentWeeks = new ConcurrentHashMap<Integer, Date>();
  private static String database;
  private static final int RESSTAT = 500;
  private static Gson GSON = new Gson();
  private static int randomHolder = (int)(Math.random() * 1000000);
  private static ConcurrentHashMap<Integer, ClientHandler> clients;
  private static ConcurrentHashMap<Integer, String> numbersToDay
    = new ConcurrentHashMap<Integer, String>();
  
  public SparkHandler(String db) {
      database = db;
      clients = new ConcurrentHashMap<Integer, ClientHandler>();
      numbersToDay.put(1, "Sunday");
      numbersToDay.put(2, "Monday");
      numbersToDay.put(3, "Tuesday");
      numbersToDay.put(4, "Wednesday");
      numbersToDay.put(5, "Thursday");
      numbersToDay.put(6, "Friday");
      numbersToDay.put(7, "Saturday");
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
    Spark.post("/getfriends", new FriendsHandler());
    Spark.post("/leftarrow", new BTFEventHandler());
    Spark.post("/rightarrow", new BTFEventHandler());
    Spark.post("/sendfriend", new SendFriendReqHandler());
    Spark.post("/acceptfriend", new AcceptFriendReqHandler());
    Spark.post("/newevent", new CreateEventHandler());
    Spark.post("/register", new RegisterHandler(), freeMarker);
  }
  private static class CreateEventHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int clientID = Integer.parseInt(qm.value("string").substring(10));
      ClientHandler cli = clients.get(clientID);
      String title = qm.value("title");
      Date date = null;
      try {
        date = new SimpleDateFormat("dd-MMM-yyy hh:00").parse(qm.value("date"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      String description = qm.value("description");
      String creator = cli.getClient();
      String group = qm.value("group");
      int duration = Integer.parseInt(qm.value("duration"));
      String users = qm.value("attendees");
      List<String> attendees = new ArrayList<String>(); 
      attendees.add(cli.getClient());
      while (users.contains(",")) {
        attendees.add(users.substring(0, users.indexOf(",")));
        users = users.substring(users.indexOf(",") + 1);
      }
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      int dayWeek = c.get(Calendar.DAY_OF_WEEK);
      String dayOfWeek = numbersToDay.get(dayWeek);
      Event e = new Event(date, title, dayOfWeek, attendees,
          group, duration, description, creator);
      CalendarThread ct = new CalendarThread(cli, "ae", e, null);
      ct.run();
      clients.put(clientID, cli);
      System.out.println("I'm done sluts");
      int status = 0;
      String message = "accepted";
      Map<String, Object> variables = new ImmutableMap.Builder()
      .put("status", status)
      .put("message", message).build();
      System.out.println(GSON.toJson(variables));
      return GSON.toJson(variables);
    }
    
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
        DatabaseHandler myDBHandler = new DatabaseHandler(database);
        found = myDBHandler.findUser(user, pass);
        myDBHandler.closeConnection();
      } catch (SQLException | ClassNotFoundException e) {
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
  
  private static List<DateHandler> getCurrentWeek(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    List<DateHandler> currentWeek = new ArrayList<DateHandler>();
    currentWeek.add(parseDate(c.getTime()));
    for(int i = 0; i < 6; i++) {
      c.add(Calendar.DATE, 1);
     //System.out.println(c.getTime());
      currentWeek.add(parseDate(c.getTime()));
    }
    return currentWeek;
  }
  
  private static Date setTimeToMidnight(Date date) {
    Calendar calendar = Calendar.getInstance();

    calendar.setTime( date );
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
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
      System.out.println("getting events");
      Date date = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      QueryParamsMap qm = req.queryMap();
      int week = c.get(Calendar.WEEK_OF_YEAR);
      int clientID = Integer.parseInt(qm.value("string").substring(10));
      c.set(Calendar.WEEK_OF_YEAR, week);
      c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
      System.out.println("checking hashmap");
      Date currentWeekStart = currentWeeks.get(clientID);
      System.out.println("got current date");
      if (currentWeekStart == null) {
        currentWeeks.put(clientID, c.getTime());
        currentWeekStart = c.getTime();
      } else {
        c.setTime(currentWeekStart);
      }
      // list of events that this user has
      try {
        String dateString = qm.value("date");
        System.out.println(currentWeekStart);
        if (dateString != null && !dateString.equals("")) {
          Date reference =
            new SimpleDateFormat("dd-MMM-yyyy hh:mm").parse(dateString + " 00:00");
          System.out.println(reference);
          if (setTimeToMidnight(reference).equals(setTimeToMidnight(currentWeekStart))) {
            c.add(Calendar.DATE, -7);
          } else {
            c.add(Calendar.DATE, 7);
          }
          currentWeekStart = c.getTime();
          System.out.println("new week " + currentWeekStart);
          currentWeeks.put(clientID, c.getTime());
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      System.out.println("got week");
      Gson gson = new Gson();
      List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
      ConcurrentHashMap<Integer, Event> testEvents;
      testEvents = clients.get(clientID).getEventsByWeek(currentWeekStart);
      System.out.println(testEvents.size());
      System.out.println("got events");
      List<String> toFrontEnd = new ArrayList<String>();
      for (Entry<Integer, Event> e : testEvents.entrySet()) {
        System.out.println("here");
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
  
  private static class FriendsHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url"));
      System.out.println(id);
      Map<String, String> tempMap = clients.get(id).getFriends();
      List<String[]> myFriends = new ArrayList<String[]>();
      for (String key : tempMap.keySet()) {
        String status = tempMap.get(key);
        String[] toAdd = {key, status};
        myFriends.add(toAdd);
      }
      Map<String, List<String[]>> variables = new ImmutableMap.Builder()
      .put("friends", myFriends).build();
      return GSON.toJson(variables);
    }
  }
  
  private static class SendFriendReqHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url"));
      String user1 = clients.get(id).user;
      String user2 = qm.value("friendToAdd").replaceAll("^\"|\"$", "");;
      String message = "";
      try {
        DatabaseHandler myDBHandler = new DatabaseHandler(database);
        myDBHandler.addFriendRequest(user1, user2);
        message = "Friend request sent!";
        Map<String, String> variables = new ImmutableMap.Builder()
        .put("message", message).build();
        return GSON.toJson(variables);
      } catch (SQLException | ClassNotFoundException e) {
        message = "ERROR: Invalid username entered, or you've already sent a request, or you're already friends.";
        Map<String, String> variables = new ImmutableMap.Builder()
        .put("message", message).build();
        return GSON.toJson(variables);
      }
    }
  }
  
  private static class AcceptFriendReqHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url"));
      String user1 = clients.get(id).user;
      String user2 = qm.value("toAdd").replaceAll("^\"|\"$", "");;
      String message = "";
      System.out.println(user1);
      System.out.println(user2);
      try {
        DatabaseHandler myDBHandler = new DatabaseHandler(database);
        message = "Friend request accepted!";
        Map<String, String> variables = new ImmutableMap.Builder()
        .put("message", message).build();
        return GSON.toJson(variables);
      } catch (SQLException | ClassNotFoundException e) {
        message = "ERROR: Bug in database, please try again.";
        Map<String, String> variables = new ImmutableMap.Builder()
        .put("message", message).build();
        return GSON.toJson(variables);
      }
    }
  }
  
  private static class CodeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String code = req.queryString().substring(
          req.queryString().indexOf('=') + 1);
      ServerCalls sc = new ServerCalls();
      HashMap<String, String> map = sc.authorize(code);
      String accessToken = map.get("access_token");
      System.out.println(accessToken);
      String user = "9999";
      ClientHandler client = new ClientHandler(database, user);
      HashMap<String, String> calendarList = sc.getCalendarList(accessToken);
      HashMap<String, String> eventsList = sc.getAllEventsMap(calendarList, accessToken);
      List<Event> events = sc.getAllEvents(eventsList);
      for (Event event : events) {
         client.addEvent(event);
      }
      try {
        System.out.println(events.get(0).getDate());
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      System.out.println("RECHED HERE");
      
      clients.put(120456778, client);
      Date currentWeekStart = new Date();
      List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
      ConcurrentHashMap<Integer, Event> testEvents;
      testEvents = client.getEventsByWeek(currentWeekStart);
      List<String> toFrontEnd = new ArrayList<String>();
      for (Entry<Integer, Event> e : testEvents.entrySet()) {
        System.out.println("here");
        Event curr = e.getValue();
        toFrontEnd.add(GSON.toJson(curr));
      }
      Map<String, Object> variables = new ImmutableMap.Builder()
      .put("events", toFrontEnd)
      .put("week", currentWeek).build();
      System.out.println(GSON.toJson(variables));
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
  
  private static class RegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "");
      ServerCalls sc = new ServerCalls();
      sc.openURLInBrowser("https://accounts.google.com/o/oauth2/auth?scope=https://www.googleapis.com/auth/calendar&response_type=code&redirect_uri=http://localhost:1234&client_id=223888438447-5vjvjsu85l893mjengfjvd0fjsd8fo1r.apps.googleusercontent.com");
      return new ModelAndView(variables, "main.ftl");
    }
  }
}
