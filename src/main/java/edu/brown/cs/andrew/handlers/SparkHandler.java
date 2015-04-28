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
import edu.brown.cs.andrew.clientThreads.ContactsThread;
import edu.brown.cs.andrew.handlers.SparkHandler.BTFEventHandler.CodeHandler;
import edu.brown.cs.andrew.handlers.SparkHandler.BTFEventHandler.FriendsHandler;
import edu.brown.cs.andrew.handlers.SparkHandler.BTFEventHandler.GetNameHandler;
import edu.brown.cs.andrew.handlers.SparkHandler.BTFEventHandler.ModifyFriendsHandler;
import edu.brown.cs.andrew.handlers.SparkHandler.BTFEventHandler.RegisterHandler;
import edu.brown.cs.rmchandr.APICalls.ServerCalls;
import freemarker.template.Configuration;

public class SparkHandler {
  private static ConcurrentHashMap<Integer, Date> currentWeeks = new ConcurrentHashMap<Integer, Date>();
  private static String database;
  private static final int RESSTAT = 500;
  protected static final ExecutorService pool = Executors.newFixedThreadPool(8);
  private static Gson GSON = new Gson();
  private static int randomHolder = (int) (Math.random() * 1000000);
  private static ConcurrentHashMap<Integer, ClientHandler> clients;
  protected static ConcurrentHashMap<Integer, String> numbersToDay = new ConcurrentHashMap<Integer, String>();

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
    // Spark.exception(Exception.class, new ExceptionHandler());
    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Routes

    Spark.get("/", new CodeHandler(), freeMarker);
    // Spark.get("/calendar", new FrontHandler(), freeMarker);
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.post("/calendar/:id", new LoginEventHandler(), freeMarker);
    Spark.post("/getevents", new BTFEventHandler());
    Spark.post("/getfriends", new FriendsHandler());
    Spark.post("/leftarrow", new BTFEventHandler());
    Spark.post("/rightarrow", new BTFEventHandler());
    Spark.post("/newevent", new CreateEventHandler());
    Spark.post("/register", new RegisterHandler(), freeMarker);
    Spark.post("/logout", new LogoutHandler());
    Spark.post("/editfriends", new ModifyFriendsHandler());
    Spark.post("/getusername", new GetNameHandler());
    Spark.post("/removeevent", new RemoveEventHandler());
  }

  private static class RemoveEventHandler implements Route {

    @Override
    public Object handle(Request arg0, Response arg1) {
      System.out.println("hahaha");
      QueryParamsMap qm = arg0.queryMap();
      int eventID = Integer.parseInt(qm.value("id"));
      CalendarThread ct = new CalendarThread(null, Commands.DELETE_EVENT, null,
          eventID);
      pool.submit(ct);
      return null;
    }

  }

  private static class CreateEventHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int clientID = Integer.parseInt(qm.value("string").substring(10));
      ClientHandler cli = clients.get(clientID);
      String title = qm.value("title");
      boolean noon = qm.value("date").contains(" 12:");
      System.out.println(noon);
      Date date = null;
      try {
        System.out.println(qm.value("date"));
        date = new SimpleDateFormat("dd-MMM-yyy hh:mm").parse(qm.value("date"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      String description = qm.value("description");
      String creator = cli.user;
      String group = qm.value("group");
      int duration = Integer.parseInt(qm.value("duration"));
      String users = qm.value("attendees");
      List<String> attendees = new ArrayList<String>();
      attendees.add(cli.getClient());
      while (users.contains(",")) {
        String friend = users.substring(0, users.indexOf(","));
        if (cli.getFriends().containsKey(friend)) {
          attendees.add(friend);
        }
        users = users.substring(users.indexOf(",") + 1);
      }
      Calendar c = Calendar.getInstance();

      c.setTime(date);
      if (noon) {
        c.set(Calendar.HOUR_OF_DAY, 12);
        date = c.getTime();
      }
      int dayWeek = c.get(Calendar.DAY_OF_WEEK);
      String dayOfWeek = numbersToDay.get(dayWeek);
      Event e = new Event(date, title, dayOfWeek, attendees, group, duration,
          description, creator);
      CalendarThread ct = new CalendarThread(cli, Commands.ADD_EVENT, e, 0);
      Future<String> t = pool.submit(ct);
      try {
        t.get();
      } catch (InterruptedException | ExecutionException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      clients.put(clientID, cli);
      int status = 0;
      String message = "accepted";
      Map<String, Object> variables = new ImmutableMap.Builder()
          .put("status", status).put("message", message).build();
      System.out.println(GSON.toJson(variables));
      return GSON.toJson(variables);
    }

  }

  private static class LogoutHandler implements Route {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String idUnparsed = qm.value("url");
      int id = Integer.parseInt(qm.value("url").substring(0,
          idUnparsed.length() - 1));
      clients.remove(id);
      while (clients.containsKey(randomHolder)) {
        randomHolder = (int) (Math.random() * 1000000);
      }
      String form = "<form method = \"POST\" action=\"/calendar/"
          + randomHolder + "\">";
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "", "form", form);
      return new ModelAndView(variables, "login.ftl");
    }
  }

  private static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      Boolean l = Boolean.parseBoolean(qm.value("logout"));
      if (l) {
        int id = Integer.parseInt(qm.value("url"));
        clients.remove(id);
      }
      while (clients.containsKey(randomHolder)) {
        randomHolder = (int) (Math.random() * 1000000);
      }

      String form = "<form method = \"POST\" action=\"/calendar/"
          + randomHolder + "\">";
      Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
          "message", "", "form", form);
      return new ModelAndView(variables, "login.ftl");
    }
  }

  private static String getRandomForm() {
    while (clients.containsKey(randomHolder)) {
      randomHolder = (int) (Math.random() * 1000000);
    }
    String form = "<form method = \"POST\" action=\"/calendar/" + randomHolder
        + "\">";
    return form;
  }

  private static class LoginEventHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String code = qm.value("code");
      System.out.println("CODE: " + code);
      if (code != null) {
        ServerCalls sc = new ServerCalls();
        HashMap<String, String> map = sc.authorize(code);
        String accessToken = map.get("access_token");
        String user = "9999";
        ClientHandler client = new ClientHandler(database, user, true);
        HashMap<String, String> calendarList = sc.getCalendarList(accessToken);
        HashMap<String, String> eventsList = sc.getAllEventsMap(calendarList,
            accessToken);
        client.setEvents(new ConcurrentHashMap<Integer, Event>());
        List<Event> events = sc.getAllEvents(eventsList);
        System.out.println(client);
        for (Event event : events) {
          // System.out.println(event);

          client.addEvent(event);
        }

        clients.put(120456778, client);
        Date currentWeekStart = new Date();
        List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
        ConcurrentHashMap<Integer, Event> testEvents;
        testEvents = client.getEventsByWeek(currentWeekStart);
        try {
          System.out.println(events.get(0).getDate());
        } catch (ParseException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        List<String> toFrontEnd = new ArrayList<String>();
        for (Entry<Integer, Event> e : testEvents.entrySet()) {
          Event curr = e.getValue();
          
          toFrontEnd.add(GSON.toJson(curr));
          System.out.println("GSON: " + GSON.toJson(curr));
        }
        Map<String, Object> variables = new ImmutableMap.Builder()
            .put("events", toFrontEnd).put("week", currentWeek)
            .put("form", getRandomForm()).build();
        return new ModelAndView(variables, "main.ftl");
      } else {
        String user = qm.value("user");
        String pass = qm.value("pass");
        int id = Integer.parseInt(req.params(":id"));
        boolean found = false;
        try {
          DatabaseHandler myDBHandler = new DatabaseHandler(database);
          found = myDBHandler.findUser(user, pass);
          myDBHandler.closeConnection();
        } catch (SQLException | ClassNotFoundException e) {
          String form = getRandomForm();
          String newMessage = "An Error Occurred while logging in, please try again.";
          Map<String, Object> variables = ImmutableMap.of("title", "Login",
              "message", newMessage, "form", form);
          return new ModelAndView(variables, "login.ftl");
        }
        if (found) {

          Map<String, Object> variables = ImmutableMap.of("title", "Calendar",
              "message", "");
          // ServerCalls sc = new ServerCalls();
          // String html = sc.loginClicked();
          ClientHandler newClient = new ClientHandler(database, user, true);
          clients.put(id, newClient);
          return new ModelAndView(variables, "main.ftl");
        } else {
          clients.remove(randomHolder);
          String form = getRandomForm();
          String newMessage = "The username or password entered was not found";
          Map<String, Object> variables = ImmutableMap.of("title", "Login",
              "message", newMessage, "form", form);
          return new ModelAndView(variables, "login.ftl");
        }
      }
    }
  }

  private static DateHandler parseDate(Date d) {
    String date = d.toString();
    String month = date.substring(4, 7);
    ;
    int day = Integer.parseInt(date.substring(8, 10));
    int year = Integer
        .parseInt(date.substring(date.length() - 4, date.length()));
    DateHandler dH = new DateHandler(month, day, year);
    return dH;
  }

  private static List<DateHandler> getCurrentWeek(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    List<DateHandler> currentWeek = new ArrayList<DateHandler>();
    currentWeek.add(parseDate(c.getTime()));
    for (int i = 0; i < 6; i++) {
      c.add(Calendar.DATE, 1);
      // System.out.println(c.getTime());
      currentWeek.add(parseDate(c.getTime()));
    }
    return currentWeek;
  }

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
        int clientID = Integer.parseInt(qm.value("string").substring(10));
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
              c.add(Calendar.DATE, -7);
            } else {
              c.add(Calendar.DATE, 7);
            }
            currentWeekStart = c.getTime();
            currentWeeks.put(clientID, c.getTime());
          }

          Gson gson = new Gson();
          System.out.println(currentWeekStart);
          List<DateHandler> currentWeek = getCurrentWeek(currentWeekStart);
          ConcurrentHashMap<Integer, Event> testEvents;
          testEvents = clients.get(clientID).getEventsByWeek(currentWeekStart);
          System.out.println(testEvents.size());
          List<String> toFrontEnd = new ArrayList<String>();
          for (Entry<Integer, Event> e : testEvents.entrySet()) {
            Event curr = e.getValue();
            toFrontEnd.add(gson.toJson(curr));
          }
        } catch (ParseException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        Gson gson = new Gson();
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
            eventList = eventList
                .substring(0, eventList.indexOf("\"creator\""))
                + "\"creator\": " + cre + "}";
          }
          toFrontEnd.add(eventList);
        }

        Map<String, Object> variables = new ImmutableMap.Builder()
            .put("events", toFrontEnd).put("week", currentWeek).build();
        System.out.println(GSON.toJson(variables));
        return GSON.toJson(variables);
      
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
        System.out.println(id);
        ContactsThread ct = new ContactsThread(clients.get(id), null, null,
            null, Commands.GET_NAME);
        Future<String> t = pool.submit(ct);
        String name;
        try {
          name = t.get();
          Map<String, String> variables = new ImmutableMap.Builder().put(
              "name", name).build();
          return GSON.toJson(variables);
        } catch (InterruptedException | ExecutionException e) {
          name = "SQL ERROR";
          e.printStackTrace();
          Map<String, String> variables = new ImmutableMap.Builder().put(
              "name", name).build();
          return GSON.toJson(variables);
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
        System.out.println(id);
        Map<String, String> tempMap = clients.get(id).getFriends();
        List<String[]> myFriends = new ArrayList<String[]>();
        for (String key : tempMap.keySet()) {
          String status = tempMap.get(key);
          System.out.println(key + " " + status);
          String[] toAdd = { key, status };
          myFriends.add(toAdd);
        }
        Map<String, List<String[]>> variables = new ImmutableMap.Builder().put(
            "friends", myFriends).build();
        return GSON.toJson(variables);
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
        ;
        String command = qm.value("command").replace("\"", "");
        String message = "";
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(command);
        switch (command) {
        case "accept":
          try {
            System.out.println("in accept");
            ct = new ContactsThread(clients.get(id), user2, null, null,
                Commands.ACCEPT_FRIEND);
            Future<String> t = pool.submit(ct);
            t.get();
            message = "Friends request accepted!";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return GSON.toJson(variables);
          } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            message = "ERROR: Bug in SQL.";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return GSON.toJson(variables);
          }
        case "remove":
          try {
            System.out.println("in remove");
            ct = new ContactsThread(clients.get(id), user2, null, null,
                Commands.REMOVE_FRIEND);
            Future<String> t = pool.submit(ct);
            t.get();
            message = "Friend removed!";
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return GSON.toJson(variables);
          } catch (ExecutionException | InterruptedException e1) {
            message = "ERROR: Bug in SQL.";
            e1.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return GSON.toJson(variables);
          }
        case "add":
          try {
            System.out.println("in add");
            ct = new ContactsThread(clients.get(id), user2, null, null,
                Commands.ADD_FRIEND);
            Future<String> t = pool.submit(ct);
            String exists = t.get();
            if (exists.equals("toobad")) {
              message = "User does not exist.";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return GSON.toJson(variables);
            } else if (exists.equals("exists")) {
              message = "Already friends or request was sent before.";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return GSON.toJson(variables);
            } else {
              message = "Friend request sent!";
              variables = new ImmutableMap.Builder().put("message", message)
                  .build();
              return GSON.toJson(variables);
            }
          } catch (InterruptedException | ExecutionException e2) {
            System.out.println("caught");
            message = "ERROR: Bug in SQL.";
            e2.printStackTrace();
            variables = new ImmutableMap.Builder().put("message", message)
                .build();
            return GSON.toJson(variables);
          }
        }
        message = "ERROR: Bug has occured, try again.";
        variables = new ImmutableMap.Builder().put("message", message).build();
        return GSON.toJson(variables);
      }
    }

    /**
     * GroupsHandler grabs all the groups for a given user. Used to load up
     * contacts page and refresh groups list.
     *
     * @author wtruong02151
     *
     */
    private static class GroupsHandler implements Route {
      @Override
      public Object handle(Request arg0, Response arg1) {
        QueryParamsMap qm = arg0.queryMap();
        int id = Integer.parseInt(qm.value("url").replace("#", ""));
        System.out.println(id);
        Map<Integer, String> tempMap = clients.get(id).getGroups();
        List<String> myGroups = new ArrayList<String>();
        for (Integer key : tempMap.keySet()) {
          String groupName = tempMap.get(key);
          System.out.println(groupName);
          ;
          myGroups.add(groupName);
        }
        Map<String, List<String[]>> variables = new ImmutableMap.Builder().put(
            "groups", myGroups).build();
        return GSON.toJson(variables);
      }
    }

    public static class CodeHandler implements TemplateViewRoute {
      @Override
      public ModelAndView handle(Request req, Response res) {
        String code = req.queryString().substring(
            req.queryString().indexOf('=') + 1);
        String form = getRandomForm();
        Map<String, String> variables = new ImmutableMap.Builder()
            .put("code", code).put("form", form).build();

        return new ModelAndView(variables, "redirect.ftl");

      }
    }

    public static class ExceptionPrinter implements ExceptionHandler {
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

    public static class RegisterHandler implements TemplateViewRoute {
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
}
