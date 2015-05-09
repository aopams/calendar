package edu.brown.cs.andrew.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.ImmutableMap;
import com.google.gson.gson;

import edu.brown.cs.andrew.clientThreads.CalendarThread;
import edu.brown.cs.andrew.clientThreads.ContactsThread;
import edu.brown.cs.andrew.clientThreads.GroupRetrievalThread;
import edu.brown.cs.andrew.clientThreads.UserThread;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
/**
 * Sparkhandler class that handles all communication between
 * frontend and backend of our project.
 * @author wtruong02151
 *
 */
public class SparkHandler {
  private static ConcurrentHashMap<Integer, Date> currentWeeks =
      new ConcurrentHashMap<Integer, Date>();
  private static String database;
  protected static final ExecutorService POOL =
      Executors.newFixedThreadPOOL(30);
  private static gson gson = new gson();
  private static int randomHolder = (int) (Math.random() * RANDOMBOUND);
  private static ConcurrentHashMap<Integer, ClientHandler> clients;
  private static ConcurrentHashMap<Integer, String> numbersToDay =
      new ConcurrentHashMap<Integer, String>();
  private static final int RESSTAT = 500;
  private static final int RANDOMBOUND = 1000000;
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int FIVE = 5;
  private static final int SIX = 6;
  private static final int SEVEN = 7;
  private static final int EIGHT = 8;
  private static final int TEN = 10;
  private static final int ELEVEN = 11;
  private static final int TWELVE = 12;
  private static final int FOURFIVE = 45;
  private static final int NEGTWO = -2;
  private static final int NEGSEVEN = -7;
  private static final double POINTFIVE = .5;
  private static final int PORT = 1234;
  /**
   * constructor that initializes global variables, creates a hashmap
   * of clients, as well as puts the proper numbers to days
   * in our hashmap.
   * @param db path to db
   */
  public SparkHandler(String db) {
    database = db;
    clients = new ConcurrentHashMap<Integer, ClientHandler>();
    numbersToDay.put(1, "Sunday");
    numbersToDay.put(2, "Monday");
    numbersToDay.put(THREE, "Tuesday");
    numbersToDay.put(FOUR, "Wednesday");
    numbersToDay.put(FIVE, "Thursday");
    numbersToDay.put(SIX, "Friday");
    numbersToDay.put(SEVEN, "Saturday");
  }
  /**
   * creates engine.
   * @return nothing no
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }
  /**
   * runs the spark server.
   */
  public void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.setPort(PORT);
    // Spark.exception(Exception.class, new ExceptionHandler());
    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Routes
    Spark.get("/", new CodeHandler(), freeMarker);
    // Spark.get("/calendar", new FrontHandler(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.post("/validate", new ValidateLoginHandler());
    Spark.post("/calendar/:id", new LoginEventHandler(), freeMarker);
    Spark.post("/getevents", new BTFEventHandler());
    Spark.post("/getGoogleEvents", new GoogleEventsHandler());
    Spark.get("/hasAccessToken", new HasAccessTokenHandler());
    Spark.post("/getfriends", new FriendsHandler());
    Spark.post("/leftarrow", new BTFEventHandler());
    Spark.post("/rightarrow", new BTFEventHandler());
    Spark.post("/newevent", new CreateEventHandler());
    Spark.post("/register", new RegisterHandler());
    Spark.post("/logout", new LogoutHandler(), freeMarker);
    Spark.post("/editfriends", new ModifyFriendsHandler());
    Spark.post("/getusername", new GetNameHandler());
    Spark.post("/getgroups", new GroupsHandler());
    Spark.post("/editgroups", new ModifyGroupsHandler());
    Spark.post("/removeevent", new RemoveEventHandler());
    Spark.post("/removeuserevent", new RemoveUserEventHandler());
  }
  /**
   * remove user from an event from the front end.
   * @author wtruong02151
   *
   */
  private static class RemoveUserEventHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int clientID = Integer.parseInt(qm.value("string").substring(TEN));
      int eventID = Integer.parseInt(qm.value("id"));
      Event e = clients.get(clientID).getEvents().get(eventID);
      ClientHandler cli = clients.get(clientID);
      CalendarThread ct = new CalendarThread(cli, Commands.REMOVE_USER_EVENT,
          null, e, clients);
      Future<String> t = POOL.submit(ct);
      try {
        t.get();
      } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (ExecutionException e1) {
        e1.printStackTrace();
      }
      int status = 0;
      String message = "accepted";
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("status", status).put("message", message).build();
      return gson.toJson(variables);
    }

  }
  /**
   * handler that manages removing an event from the frontend.
   * @author wtruong02151
   *
   */
  private static class RemoveEventHandler implements Route {

    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int clientID = Integer.parseInt(qm.value("string").substring(TEN));
      int eventID = Integer.parseInt(qm.value("id"));
      Event e = clients.get(clientID).getEvents().get(eventID);
      System.out.println(eventID);
      System.out.println(clients.get(clientID).getEvents().get(eventID));
      System.out.println(clients.get(clientID).user);
      CalendarThread ct = new CalendarThread(null, Commands.DELETE_EVENT, null,
          e, clients);
      Future<String> t = POOL.submit(ct);
      try {
        //sometimes throws DB locked
        t.get();
      } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (ExecutionException e1) {
        e1.printStackTrace();
      }
      int status = 0;
      String message = "accepted";
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("status", status).put("message", message).build();
      return gson.toJson(variables);
    }
  }
  /**
   * handler that creates an event on the front end
   * and reciprocates that production on the back end.
   * @author wtruong02151
   *
   */
  private static class CreateEventHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String id = qm.value("id");
      int eventID = -1;
      if (id != null) {
        eventID = Integer.parseInt(id);
      }
      int clientID = Integer.parseInt(qm.value("string").substring(TEN));
      ClientHandler cli = clients.get(clientID);
      String toOverride = qm.value("override");
      int override = -1;
      if (toOverride != null) {
        override = Integer.parseInt(qm.value("override"));
      }
      String title = qm.value("title");
      boolean noon = qm.value("date").contains(" 12:");
      Date date = null;
      try {
        date = new SimpleDateFormat("dd-MMM-yyy hh:mm").parse(qm.value("date"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      String description = qm.value("description");
      String creator = cli.user;
      String group = qm.value("group");
      String invalidGroup = "";
      //client is not a part of this group -> invalid group?
      //so set string of invalid group to this, and pass back to front end.
      if (group != null && !cli.getGroups().contains(group)) {
        invalidGroup = group;
        group = null;
      }
      int duration = Integer.parseInt(qm.value("duration"));
      String users = qm.value("attendees");
      String[] usersBuffer = users.split(",");
      List<String> groupAttendees = new ArrayList<String>();
      if (group != null && users.equals(",")) {
        GroupRetrievalThread grt = new GroupRetrievalThread(group);
        try {
          Future<List<String>> t = POOL.submit(grt);
          groupAttendees = t.get();
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        } catch (ExecutionException e1) {
          e1.printStackTrace();
        }
      }
      List<String> attendees = groupAttendees;
      System.out.println(attendees.size());
      attendees.add(cli.getClient());
      if (group == null) {
        group = "";
      }
      StringBuilder invalidUsers = new StringBuilder();
      for (String friend : usersBuffer) {
        friend = friend.trim();
        if (cli.getFriends().containsKey(friend)) {
          if (cli.getFriends().get(friend).equals("accepted")) {
            attendees.add(friend);
            //we have a valid user, but we're not friends yet (pending),
            //so can't add to event -> add to invalid users list
          } else {
            invalidUsers.append(friend);
            invalidUsers.append(" ");
          }
          //no relation with this user, either invalid username,
          //or simply no conncetion, and also do not append
          //own self to invalid users list
        } else if (!friend.equals(cli.getClient())) {
          invalidUsers.append(friend);
          invalidUsers.append(" ");
        }
      }
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      if (noon) {
        c.set(Calendar.HOUR_OF_DAY, TWELVE);
        date = c.getTime();
      }
      int dayWeek = c.get(Calendar.DAY_OF_WEEK);
      String dayOfWeek = numbersToDay.get(dayWeek);
      Event e = new Event(date, title, dayOfWeek, attendees, group, duration,
          description, creator);
      c.setTime(date);
      String daylightSavings = checkDaylightSavings(e, c);
      if (daylightSavings != null) {
        return daylightSavings;
      }
      Ranker rank = new Ranker(e);
      boolean conflict = false;
      try {
        conflict = rank.checkConflict(e.getDate());
      } catch (ParseException e2) {
        e2.printStackTrace();
      }
      if (conflict || override == 1) {

        CalendarThread ct = new CalendarThread(cli, Commands.ADD_EVENT, e,
            null, clients);
        if (eventID > -1) {
          Event d = cli.getEvents().get(eventID);
          ct = new CalendarThread(cli, Commands.EDIT_EVENT, e, d, clients);
        }
        POOL.submit(ct);
        clients.put(clientID, cli);
        c.setTime(date);
        if (c.get(Calendar.WEEK_OF_YEAR) == FOURFIVE) {
          if (c.get(Calendar.DAY_OF_WEEK) == 1
              && c.get(Calendar.HOUR_OF_DAY) == 1) {
            int status = NEGTWO;
            String message = "The event is being placed on the first 1:00 AM"
                + "for Daylights Savings time";
            Map<String, Object> variables = new ImmutableMap.Builder()
              .put("status", status)
              .put("message", message)
              .put("invalidUsers", invalidUsers.toString())
              .put("invalidGroup", invalidGroup).build();
            return gson.toJson(variables);
          }
        }
        int status = 1;
        String message = "accepted";
        Map<String, Object> variables = new ImmutableMap.Builder()
            .put("status", status)
            .put("message", message)
            .put("invalidUsers", invalidUsers.toString())
            .put("invalidGroup", invalidGroup).build();
        return gson.toJson(variables);
      } else {
        List<Event> toFrontEnd = new ArrayList<Event>();
        rank.checkAllConflicts(date);
        HashMap<Integer, Integer> conflicts =  rank.getConflicts();
        Integer[] bestTimes = rank.getBestTimes(THREE, date);
        for (int i = 0; i < THREE; i++) {
          c.set(Calendar.HOUR_OF_DAY, bestTimes[i]);
          Event newE = new Event(c.getTime(), title, dayOfWeek, attendees,
              group, duration, description, creator);
          if (eventID != -1) {
            newE.setID(eventID);
          }
          String color = "red";
          if (conflicts.get(c.get(Calendar.HOUR_OF_DAY)) == 0) {
            color = "success";
          } else if ((double) conflicts.get(c.get(Calendar.HOUR_OF_DAY))
              / attendees.size() < POINTFIVE) {
            color = "warning";
          }
          newE.setConflictColor(color);
          toFrontEnd.add(newE);
        }
        e.setID(eventID);
        toFrontEnd.add(e);
        int status = 0;
        String message = "conflict";
        Map<String, Object> variables = new ImmutableMap.Builder()
            .put("status", status)
            .put("message", message)
            .put("events", toFrontEnd)
            .put("invalidUsers", invalidUsers.toString())
            .put("invalidGroup", invalidGroup).build();
        return gson.toJson(variables);
      }
    }

    private String checkDaylightSavings(Event e, Calendar c) {
      try {
        c.setTime(e.getDate());
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
      if (c.get(Calendar.WEEK_OF_YEAR) == ELEVEN) {
        if (c.get(Calendar.DAY_OF_WEEK) == 1
            && c.get(Calendar.HOUR_OF_DAY) == 2) {
          int status = NEGTWO;
          String message = "This hour does not exist"
              + "due to Daylight Savings Time";
          Map<String, Object> variables = new ImmutableMap.Builder()
            .put("status", status).put("message", message).build();
          return gson.toJson(variables);
        }
      }
      return null;
    }
  }
  /**
   * handles logging out from the front end.
   * @author wtruong02151
   *
   */
  private static class LogoutHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String idUnparsed = qm.value("string");
      idUnparsed.replaceAll("#", "");
      int id = Integer.parseInt(idUnparsed.substring(TEN));
      clients.remove(id);
      String form = getRandomForm();
      Map<String, Object> variables = ImmutableMap.of("title", "Login",
          "message", "", "form", form);
      return new ModelAndView(variables, "login.ftl");
    }
  }
  /**
   * handles logging in from the front end.
   * @author wtruong02151
   *
   */
  private static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      Boolean l = Boolean.parseBoolean(qm.value("logout"));
      if (l) {
        int id = Integer.parseInt(qm.value("url"));
        clients.remove(id);
      }

      String form = getRandomForm();
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "", "form", form);
      return new ModelAndView(variables, "login.ftl");
    }
  }
  /**
   * function that generates a random and unique
   * client ID that gets attached to the URL of a
   * user logging in.
   * @return random clientID
   */
  private static String getRandomForm() {
    int rando = (int) (Math.random() * RANDOMBOUND);
    while (clients.containsKey(rando)) {
      rando = (int) (Math.random() * RANDOMBOUND);
    }
    String form = "<form id =\"loginForm\""
        + " method = \"POST\" action=\"/calendar/" + rando
        + "\">";
    return form;
  }
  /**
   * handler that validates the login parameters.
   * @author wtruong02151
   *
   */
  private static class ValidateLoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String user = qm.value("username");
      String pass = qm.value("password");
      boolean found = false;
      try {
        DatabaseHandler myDBHandler = new DatabaseHandler(database);
        found = myDBHandler.findUser(user, pass);
        myDBHandler.closeConnection();
      } catch (SQLException e) {
        String message = "An Error Occurred while"
            + "logging in, please try again.";
        String status = "error";
        Map<String, String> variables = new ImmutableMap.Builder()
          .put("status", status)
          .put("message", message).build();
        return gson.toJson(variables);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      if (found) {
        String message = "";
        String status = "success";
        Map<String, String> variables = new ImmutableMap.Builder()
          .put("status", status)
          .put("message", message).build();
        return gson.toJson(variables);
      } else {
        String message = "The username or password entered was not found.";
        String status = "failure";
        Map<String, String> variables = new ImmutableMap.Builder()
          .put("status", status)
          .put("message", message).build();
        return gson.toJson(variables);
      }
    }
  }
  /**
   * Login event handles generating the random and unique
   * clientID for logging in.
   * @author wtruong02151
   *
   */
  private static class LoginEventHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String user = qm.value("user");
      String pass = qm.value("pass");
      int id = Integer.parseInt(req.params(":id"));
      boolean found = false;
      try {
        DatabaseHandler myDBHandler = new DatabaseHandler(database);
        found = myDBHandler.findUser(user, pass);
        myDBHandler.closeConnection();
      } catch (SQLException e) {
        String form = getRandomForm();
        String newMessage = "An Error Occurred while"
            + " logging in, please try again.";
        Map<String, Object> variables = ImmutableMap.of("title", "Login",
            "message", newMessage, "form", form);
        return new ModelAndView(variables, "login.ftl");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      if (found) {
        Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
            "message", "");
        ClientHandler newClient = new ClientHandler(database, user, true);
        if (!clients.containsKey(id)) {
          clients.put(id, newClient);
        }
        return new ModelAndView(variables, "main.ftl");
      } else {
        clients.remove(randomHolder);
        String form = getRandomForm();
        String newMessage = "The username or password entered was not found";
        Map<String, Object> variables = ImmutableMap.of("title", "Login",
            "message", newMessage, "form", form);
        //changed from 6.tl**
        return new ModelAndView(variables, "login.ftl");

      }
    }
  }
  /**
   * method that parses the date into a datehandler
   * object.
   * @param d date to parse
   * @return date parsed
   */
  private static DateHandler parseDate(Date d) {
    String date = d.toString();
    String month = date.substring(FOUR, SEVEN);
    int day = Integer.parseInt(date.substring(EIGHT, TEN));
    int year = Integer
        .parseInt(date.substring(date.length() - FOUR, date.length()));
    DateHandler dH = new DateHandler(month, day, year);
    return dH;
  }
  /**
   * gets the current week to update the front end.
   * @param d date to grab week of
   * @return list of dates of the week
   */
  private static List<DateHandler> getCurrentWeek(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    List<DateHandler> currentWeek = new ArrayList<DateHandler>();
    currentWeek.add(parseDate(c.getTime()));
    for (int i = 0; i < SIX; i++) {
      c.add(Calendar.DATE, 1);
      // System.out.println(c.getTime());
      currentWeek.add(parseDate(c.getTime()));
    }
    return currentWeek;
  }
  /**
   * sets time to midnight handler form 00.
   * @param date date to parse into proper midnight time
   * @return date to return
   */
  public static Date setTimeToMidnight(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
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
  public static class BTFEventHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      Date date = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      QueryParamsMap qm = req.queryMap();
      int week = c.get(Calendar.WEEK_OF_YEAR);
      String unparsedID = qm.value("string").replace("#", "");
      int clientID = Integer.parseInt(unparsedID.substring(TEN));
     /** if (clients.get(clientID).getAccessToken() != null) {
        ServerCalls sc = new ServerCalls();
        String accessToken = clients.get(clientID).getAccessToken();
        HashMap<String, String> calendarList = sc.getCalendarList(accessToken);
        HashMap<String, String> eventsList = sc.getAllEventsMap(calendarList,
            accessToken);
        List<Event> events = sc.getAllEvents(eventsList);
        ClientHandler ch = clients.get(clientID);
        for (Event event : events) {
          System.out.println(event.getTitle());
          ch.addEvent(event);
          System.out.println(event.getTitle());
        }
      } **/
      /**
       * if (clients.get(clientID).getAccessToken() != null) {
       *
       * ServerCalls sc = new ServerCalls(); String accessToken =
       * clients.get(clientID).getAccessToken(); HashMap<String, String>
       * calendarList = sc.getCalendarList(accessToken); HashMap<String, String>
       * eventsList = sc.getAllEventsMap(calendarList, accessToken); List<Event>
       * events = sc.getAllEvents(eventsList); ClientHandler ch =
       * clients.get(clientID); for (Event event : events) { //
       * System.out.println(event);
       *
       * ch.addEvent(event); System.out.println(event.getTitle()); } }
       **/
      c.set(Calendar.WEEK_OF_YEAR, week);
      c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
      Date currentWeekStart = currentWeeks.get(clientID);
      if (currentWeekStart == null) {
        currentWeeks.put(clientID, c.getTime());
        currentWeekStart = c.getTime();
      } else {
        c.setTime(currentWeekStart);
      }
      // list of events that this user has
      try {
        String dateString = qm.value("date");
        if (dateString != null && !dateString.equals("")) {
          Date reference = new SimpleDateFormat("dd-MMM-yyyy hh:mm")
              .parse(dateString + " 00:00");
          if (setTimeToMidnight(reference).equals(
              setTimeToMidnight(currentWeekStart))) {
            c.add(Calendar.DATE, NEGSEVEN);
          } else {
            c.add(Calendar.DATE, SEVEN);
          }
          currentWeekStart = c.getTime();
          currentWeeks.put(clientID, c.getTime());
        }
        ConcurrentHashMap<Integer, Event> testEvents;
        testEvents = clients.get(clientID).getEventsByWeek(currentWeekStart);
        List<String> toFrontEnd = new ArrayList<String>();
        for (Entry<Integer, Event> e : testEvents.entrySet()) {
          Event curr = e.getValue();
          toFrontEnd.add(gson.toJson(curr));
        }
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
      ConcurrentHashMap<Integer, Event> testEvents;
      testEvents = clients.get(clientID).getEventsByWeek(currentWeekStart);
      List<String> toFrontEnd = new ArrayList<String>();
      for (Entry<Integer, Event> e : testEvents.entrySet()) {
        Event curr = e.getValue();
        String creator = e.getValue().getCreator();
        int cre = 0;
        if (creator != null && creator.equals(clients.get(clientID).user)) {
          cre = 1;
        }
        String eventList = gson.toJson(curr);
        if (creator != null) {
          eventList = eventList.substring(0, eventList.indexOf("\"creator\""))
              + "\"creator\": " + cre + "}";
        }
        toFrontEnd.add(eventList);
      }
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("events", toFrontEnd).put("week", currentWeek).build();
      return gson.toJson(variables);
    }
  }

  /**
   * FriendsHandler grabs all the friends for a given user. Used to load up
   * contacts page and refresh friends list.
   *
   * @author wtruong02151
   *
   */
  public static class GetNameHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url").replace("#", ""));
      ContactsThread ct = new ContactsThread(clients.get(id), null, null, null,
          null, Commands.GET_NAME, clients);
      Future<String> t = POOL.submit(ct);
      String name;
      try {
        name = t.get();
        Map<String, String> variables = new ImmutableMap.Builder().put("name",
            name).build();
        return gson.toJson(variables);
      } catch (InterruptedException e) {
        name = "SQL ERROR";
        e.printStackTrace();
        Map<String, String> variables = new ImmutableMap.Builder().put("name",
            name).build();
        return gson.toJson(variables);
      } catch (ExecutionException e) {
        name = "SQL ERROR";
        e.printStackTrace();
        Map<String, String> variables = new ImmutableMap.Builder().put("name",
            name).build();
        return gson.toJson(variables);
      }
    }
  }

  /**
   * FriendsHandler grabs all the friends for a given user. Used to load up
   * contacts page and refresh friends list.
   *
   * @author wtruong02151
   *
   */
  public static class FriendsHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url").replace("#", ""));
      Map<String, String> tempMap = clients.get(id).getFriends();
      List<String[]> myFriends = new ArrayList<String[]>();
      for (String key : tempMap.keySet()) {
        String status = tempMap.get(key);
        String[] toAdd = {key, status};
        myFriends.add(toAdd);
      }
      Map<String, List<String[]>> variables = new ImmutableMap.Builder().put(
          "friends", myFriends).build();
      return gson.toJson(variables);
    }
  }

  /**
   * ModifyFriendsHandler takes in a particular command and modifies a user's
   * friend's list acoording to the command.
   *
   * @author wtruong02151
   *
   */
  public static class ModifyFriendsHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      ContactsThread ct;
      Map<String, String> variables;
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url").replace("#", ""));
      String user1 = clients.get(id).user;
      String user2 = qm.value("user").replaceAll("^\"|\"$", "");
      String command = qm.value("command").replace("\"", "");
      String message = "";
      switch (command) {
        case "accept":
          try {
            ct = new ContactsThread(clients.get(id), user2, null, null, null,
                Commands.ACCEPT_FRIEND, clients);
            Future<String> t = POOL.submit(ct);
            t.get();
            message = "Friends request accepted!";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException e) {
            e.printStackTrace();
            message = "ERROR: Bug in SQL.";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (InterruptedException e) {
            e.printStackTrace();
            message = "ERROR: Bug in SQL.";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        case "remove":
          try {
            ct = new ContactsThread(clients.get(id), user2, null, null, null,
                Commands.REMOVE_FRIEND, clients);
            Future<String> t = POOL.submit(ct);
            t.get();
            message = "Friend removed!";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException  e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (InterruptedException e) {
            message = "ERROR: Bug in SQL.";
            e.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        case "add":
          try {
            ct = new ContactsThread(clients.get(id), user2, null, null, null,
                Commands.ADD_FRIEND, clients);
            Future<String> t = POOL.submit(ct);
            String exists = t.get();
            if (exists.equals("toobad")) {
              message = "User does not exist.";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return gson.toJson(variables);
            } else if (exists.equals("exists")) {
              message = "Already friends or request was sent before.";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return gson.toJson(variables);
            } else {
              message = "Friend request sent!";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return gson.toJson(variables);
            }
          } catch (InterruptedException e2) {
            message = "ERROR: Bug in SQL.";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException e) {
            message = "ERROR: Bug in SQL.";
            e.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        default :
          break;
      }
      message = "ERROR: Bug has occured, try again.";
      variables = new ImmutableMap.Builder().put("message", message).build();
      return gson.toJson(variables);
    }
  }

  /**
   * GroupsHandler grabs all the groups for a given user. Used to load up
   * contacts page and refresh groups list. Passes back to front end the unique
   * integer key of a group and the group's name.
   *
   * @author wtruong02151
   *
   */
  private static class GroupsHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url").replace("#", ""));
      Map<Integer, String> tempMap = clients.get(id).getGroups();
      List<String[]> myGroups = new ArrayList<String[]>();
      for (Integer key : tempMap.keySet()) {
        String keyString = Integer.toString(key);
        String groupName = tempMap.get(key);
        String[] toAdd = {keyString, groupName};
        myGroups.add(toAdd);
      }
      Map<String, List<String>> variables = new ImmutableMap.Builder().put(
          "groups", myGroups).build();
      return gson.toJson(variables);
    }
  }

  /**
   * ModifyFriendsHandler takes in a particular command and modifies a user's
   * friend's list acoording to the command.
   *
   * @author wtruong02151
   *
   */
  private static class ModifyGroupsHandler implements Route {
    @Override
    public Object handle(Request arg0, Response arg1) {
      ContactsThread ct;
      Map<String, String> variables;
      QueryParamsMap qm = arg0.queryMap();
      int id = Integer.parseInt(qm.value("url").replace("#", ""));
      String command = qm.value("command").replace("\"", "");
      String user1 = clients.get(id).user;
      String groupName = qm.value("groupname").replace("\"", "");
      String message = "";
      String gid;
      int groupID;
      switch (command) {
        case "members":
          gid = qm.value("groupid");
          groupID = Integer.parseInt(gid);
          try {
            ct = new ContactsThread(clients.get(id), null, null, groupID, null,
                Commands.FIND_MEMBERS, clients);
            Future<String> t = POOL.submit(ct);
            String[] members = t.get().split(",");
            message = "Members found!";
            Map<String, String[]> variables2 = new ImmutableMap.Builder().put(
                "members", members).build();
            return gson.toJson(variables2);
          } catch (ExecutionException  e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (InterruptedException e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        case "remove":
          gid = qm.value("groupid");
          groupID = Integer.parseInt(gid);
          try {
            ct = new ContactsThread(clients.get(id), null, groupName, groupID,
                null, Commands.REMOVE_GROUP, clients);
            Future<String> t = POOL.submit(ct);
            t.get();
            message = "Friend removed!";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (InterruptedException e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        case "add":
          String users = qm.value("users").replace("\"", "");
          String[] tempUsersList = users.split(",");
          List<String> usersList = new ArrayList<String>();
          // add user himself
          usersList.add(user1);
          for (String s : tempUsersList) {
            usersList.add(s.trim());
          }
          try {
            ct = new ContactsThread(clients.get(id), null, groupName, null,
                usersList, Commands.ADD_GROUP, clients);
            Future<String> t = POOL.submit(ct);
            String exists = t.get();
            if (exists.equals("failure")) {
              message = "failure";
            } else {
              message = exists;
            }
            variables = new ImmutableMap.Builder()
              .put("message", message).build();
            return gson.toJson(variables);
          } catch (InterruptedException e2) {
            message = "error";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException e2) {
            message = "error";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        case "newmembers":
          String users2 = qm.value("users").replace("\"", "");
          String[] tempUsersList2 = users2.split(",");
          List<String> usersList2 = new ArrayList<String>();
          for (String s : tempUsersList2) {
            usersList2.add(s.trim());
          }
          gid = qm.value("groupid");
          groupID = Integer.parseInt(gid);
          try {
            ct = new ContactsThread(clients.get(id), null, groupName, groupID,
                usersList2, Commands.NEW_MEMBERS, clients);
            Future<String> t = POOL.submit(ct);
            message = t.get();
            variables = new ImmutableMap.Builder().
                put("message", message).build();
            return gson.toJson(variables);
          } catch (InterruptedException e2) {
            message = "ERROR: Bug in SQL.";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          } catch (ExecutionException e2) {
            message = "ERROR: Bug in SQL.";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return gson.toJson(variables);
          }
        default :
          break;
      }
      message = "ERROR: Bug has occured, try again.";
      variables = new ImmutableMap.Builder().put("message", message).build();
      return gson.toJson(variables);
    }
  }
  /**
   * code handler for the google code replacement.
   * @author wtruong02151
   *
   */
  public static class CodeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      Map<String, String> variables = new ImmutableMap.Builder().build();
      return new ModelAndView(variables, "redirect.ftl");
    }
  }
  /**
   * checks for if a user has an access token.
   * @author wtruong02151
   *
   */
  public static class HasAccessTokenHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String unparsed = qm.value("string");
      int clientID = Integer.parseInt(unparsed.substring(TEN));
      boolean toReturn;
      toReturn = (clients.get(clientID).getAccessToken() != null);
      Map<String, Object> variables = new ImmutableMap.Builder().put(
          "hasAccessToken", toReturn).build();
      return gson.toJson(variables);
    }
  }
  /**
   * handles grabbing google events from google.
   * @author wtruong02151
   *
   */
  public static class GoogleEventsHandler implements Route {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      boolean hasAccessToken = Boolean.parseBoolean(qm.value("hasAccessToken"));
      String unparsed = qm.value("string");
      unparsed = unparsed.replace("#", "");
      int clientID = Integer.parseInt(unparsed.substring(TEN));
      ClientHandler ch = clients.get(clientID);
      ServerCalls sc = new ServerCalls();
      String accessToken;
      if (!hasAccessToken) {
        String code = qm.value("code");
        HashMap<String, String> map = sc.authorize(code);
        accessToken = map.get("access_token");
        String refreshToken = map.get("refresh_token");
        ch.setAccessToken(accessToken);
        ch.setRefreshToken(refreshToken);
        for (Entry<Integer, Event> e : ch.getEvents().entrySet()) {
          if (e.getKey() < 0) {
            CalendarThread ct = new CalendarThread(
                null, Commands.DELETE_EVENT, null,
                e.getValue(), clients);
            Future<String> t = POOL.submit(ct);
            try {
              t.get();
            } catch (InterruptedException e1) {
              e1.printStackTrace();
            } catch (ExecutionException e1) {
              e1.printStackTrace();
            }
          }
        }
      } else {
        accessToken = ch.getAccessToken();
        for (Entry<Integer, Event> e : ch.getEvents().entrySet()) {
          if (e.getKey() < 0) {
            CalendarThread ct = new CalendarThread(
                null, Commands.DELETE_EVENT, null,
                e.getValue(), clients);
            Future<String> t = POOL.submit(ct);
            try {
              t.get();
            } catch (InterruptedException e1) {
              e1.printStackTrace();
            } catch (ExecutionException e1) {
              e1.printStackTrace();
            }
          }
        }
      }
      HashMap<String, String> calendarList = sc.getCalendarList(accessToken);
      HashMap<String, String> eventsList = sc.getAllEventsMap(calendarList,
          accessToken);
      List<Event> events = sc.getAllEvents(eventsList);
      for (Event event : events) {
        // System.out.println(event);
        List<String> attends = new ArrayList<String>();
        attends.add(ch.user);
        event.setAttendees(attends);
        CalendarThread ct = new CalendarThread(ch, Commands.ADD_EVENT, event,
            null, clients);
        Future<String> t = POOL.submit(ct);
        try {
          t.get();
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        } catch (ExecutionException e1) {
          e1.printStackTrace();
        }
      }
      // try {
      // System.out.println(events.get(0).getDate());
      // } catch (ParseException e1) {
      // // TODO Auto-generated catch block
      // e1.printStackTrace();
      // }
      //
      // clients.put(120456778, client);
      // Date currentWeekStart = new Date();
      // List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
      // ConcurrentHashMap<Integer, Event> testEvents;
      // testEvents = client.getEventsByWeek(currentWeekStart);
      // try {
      // System.out.println(events.get(0).getDate());
      // } catch (ParseException e1) {
      // // TODO Auto-generated catch block
      // e1.printStackTrace();
      // }
      // List<String> toFrontEnd = new ArrayList<String>();
      // for (Entry<Integer, Event> e : testEvents.entrySet()) {
      // Event curr = e.getValue();
      // toFrontEnd.add(gson.toJson(curr));
      // }
      Map<String, Object> variables = new ImmutableMap.Builder().build();
      return new ModelAndView(variables, "main.ftl");
    }
  }
  /**
   * handles exception printing.
   * @author wtruong02151
   *
   */
  public static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(RESSTAT);
      StringWriter stacktrace = new StringWriter();
      try {
        PrintWriter pw = new PrintWriter(stacktrace);
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      } finally {
        System.out.println("");
      }
    }
  }
  /**
   * registration handler.
   * @author wtruong02151
   *
   */
  public static class RegisterHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String user = qm.value("username");
      String pass = qm.value("password");
      String regName = qm.value("fullname");
      UserThread ut = new UserThread(user, pass, regName);
      Future<String> t = POOL.submit(ut);
      int success = 1;
      try {
        if (t.get().equals("taken")) {
          success = 0;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
      int randomNumber = 0;
      while (clients.containsKey(randomHolder)) {
        randomNumber = (int) (Math.random() * RANDOMBOUND);
      }
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("success", success).put("user", user).put("id", randomNumber)
          .put("pass", pass).build();
      return gson.toJson(variables);
    }
  }
}
