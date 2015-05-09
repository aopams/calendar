package edu.brown.cs.rmchandr.APICalls;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.andrew.handlers.Event;
/**
 * This class handles the server calls to google's calendar
 * API to grab all events for a given authorized user.
 * @author wtruong02151
 *
 */
public class ServerCalls {

  private final String clientID =
      "223888438447-"
      + "5vjvjsu85l893mjengfjvd0fjsd8fo1r.apps.googleusercontent.com";
  private final String clientSecret = "6rmO_xu590Oe89yGFL-kX-l8";
  private final String redirectURI = "http://localhost:1234";
  private static final int TWOHUND = 200;
  private static final int FIVE = 5;
  private static final int NINE = 9;
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int SIX = 6;
  private static final int SEVEN = 7;
  private static final int FOURTEEN = 14;
  private static final int TWENTY = 20;
  private static final int EIGHTEEN = 18;
  private static final int SIXTEEN = 16;
  private static final int SIXTYTHOUS = 60000;
  /**
   * function that opens the google URL in our browser.
   * @param url url to open
   */
  public void openURLInBrowser(String url) {
    Desktop d = Desktop.getDesktop();
    try {
      d.browse(new URI(url));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // String os = System.getProperty("os.name").toLowerCase();
    // Runtime rt = Runtime.getRuntime();
    //
    // try {
    //
    // if (os.indexOf("win") >= 0) {
    //
    // // this doesn't support showing urls in the form of "page.html#nameLink"
    // rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
    //
    // } else if (os.indexOf("mac") >= 0) {
    //
    // rt.exec("open " + url);
    //
    // } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
    //
    // // Do a best guess on unix until we get a platform independent way
    // // Build a list of browsers to try, in this order.
    // String[] browsers = { "chromium", "epiphany", "firefox", "mozilla",
    // "konqueror",
    // "netscape", "opera", "links", "lynx" };
    //
    // // Build a command string which looks like
    // // "browser1 "url" || browser2 "url" ||..."
    // StringBuffer cmd = new StringBuffer();
    // for (int i = 0; i < browsers.length; i++)
    // cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
    //
    // rt.exec(new String[] { "sh", "-c", cmd.toString() });
    //
    // } else {
    // return;
    // }
    // } catch (Exception e) {
    // return;
    // }
    // return;
  }
  /**
   * funciton that handles clicking the login button
   * on the google authorization page.
   * @return return stream string
   */
  public String loginClicked() {
    String website = "https://accounts.google.com/o/oauth2/auth";
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("response_type", "code");
    params.put("client_id", clientID);
    params.put("scope", "email%20profile");
    params.put("redirect_uri", redirectURI);
    website += "?" + urlEncodedString(params);
    System.out.println(website);
    try {
      URL url = new URL(website);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      int statusCode = conn.getResponseCode();
      System.out.println(statusCode);
      // open the stream and put it into BufferedReader
      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      String inputLine;
      String toReturn = "";
      while ((inputLine = br.readLine()) != null) {
        toReturn += inputLine + "\n";
      }
      br.close();
      return toReturn;
    } catch (MalformedURLException e) {
      System.out.println("ERROR: Malformed URL");
    } catch (IOException e) {
      System.out.println("ERROR: IO Exception");
    }
    return null;
  }
  /**
   * function that handles authorizing a user
   * to a google account.
   * @param code the generated code for the user
   * @return hashmap?
   */
  public HashMap<String, String> authorize(String code) {
    try {
      String website = "https://www.googleapis.com/oauth2/v3/token";
      URL url = new URL(website);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      // conn.setRequestProperty("Host", "");
      conn.setRequestProperty("Content-Type",
          "application/x-www-form-urlencoded");
      HashMap<String, String> paramsMap = new HashMap<String, String>();
      paramsMap.put("code", code);
      paramsMap.put("client_id", clientID);
      paramsMap.put("client_secret", clientSecret);
      paramsMap.put("redirect_uri", redirectURI);
      paramsMap.put("grant_type", "authorization_code");
      String params = urlEncodedString(paramsMap);
      System.out.println(params);
      System.out.println("code " + code);
      System.out.println(clientID);
      System.out.println(clientSecret);
      System.out.println(redirectURI);
      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.writeBytes(params);
      wr.flush();
      wr.close();
      int responseCode = conn.getResponseCode();
      System.out.println("\nSending 'POST' request to URL : " + url);
      System.out.println("Post parameters : " + params);
      System.out.println("Response Code : " + responseCode);
      BufferedReader in;
      if (responseCode == TWOHUND) {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
        in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      // print result
      System.out.println(response.toString());
      return parseQueryString(response.toString());
    } catch (Exception e) {
      System.out.println("ERROR:");
      e.printStackTrace();
      return null;
    }
  }
  /**
   * function that grabs the events from this user's
   * google account.
   * @param accessToken takes token from user
   * @return hashmap of calendar list
   */
  public HashMap<String, String> getCalendarList(String accessToken) {
    try {
      String website = "https://www.googleapis."
          + "com/calendar/v3/users/me/calendarList/";
      // String website =
      // "https://www.googleapis.com/
      //calendar/v3/calendars/rohan_chandra@brown.edu";
      URL url = new URL(website);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Authorization", "Bearer " + accessToken);
      int responseCode = conn.getResponseCode();
      System.out.println("\nSending 'GET' request to URL : " + url);
      System.out.println("Response Code : " + responseCode);
      BufferedReader in;
      if (responseCode == TWOHUND) {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
        in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      // InputStream in = conn.getInputStream();
      // String encoding = conn.getContentEncoding();
      // encoding = encoding == null ? "UTF-8" : encoding;
      // String response = IOUtils.toString(in);
      // System.out.println(response);
      // HashMap<String, String> map = new Gson().fromJson(response.toString(),
      // new TypeToken<HashMap<String, String>>() {
      // }.getType());
      HashMap<String, String> map = new HashMap<String, String>();
      JsonParser parser = new JsonParser();
      JsonObject jObject = parser.parse(response.toString()).getAsJsonObject();
      Iterator<Entry<String, JsonElement>> it = jObject.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, JsonElement> entry = it.next();
        String key = entry.getKey();
        String value = entry.getValue().toString();
        map.put(key, value);
      }
      return map;
    } catch (Exception e) {
      System.out.println("ERROR:");
      e.printStackTrace();
      return null;
    }
  }
  /**
   * funciton that takes in a calendar list and returns all
   * events in a hashmap.
   * @param calendarList calendar list to parse.
   * @param accessToken accesstoken for this user
   * @return map of events.
   */
  public HashMap<String, String> getAllEventsMap(
      HashMap<String, String> calendarList, String accessToken) {
    try {
      System.out.println(calendarList);
      String itemString = calendarList.get("items");
      itemString = itemString.substring(itemString.indexOf("id") + FIVE);
      System.out.println(itemString);
      String calendarID = itemString.substring(0, itemString.indexOf('\"'));
      System.out.println(calendarID);
      String website = "https://www.googleapis.com/calendar/v3/calendars/"
          + calendarID + "/events";
      URL url = new URL(website);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Authorization", "Bearer " + accessToken);
      int responseCode = conn.getResponseCode();
      System.out.println("\nSending 'GET' request to URL : " + url);
      System.out.println("Response Code : " + responseCode);
      BufferedReader in;
      if (responseCode == TWOHUND) {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
        in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      // System.out.println(response.toString());
      HashMap<String, String> map = new HashMap<String, String>();
      JsonParser parser = new JsonParser();
      JsonObject jObject = parser.parse(response.toString()).getAsJsonObject();
      Iterator<Entry<String, JsonElement>> it = jObject.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, JsonElement> entry = it.next();
        String key = entry.getKey();
        String value = entry.getValue().toString();
        map.put(key, value);
      }
      return map;
    } catch (Exception e) {
      System.out.println("ERROR:");
      e.printStackTrace();
      return null;
    }
  }
  /**
   * function that grabs all the events of a user?
   * @param eventsList events list
   * @return list of events
   */
  @SuppressWarnings("deprecation")
  public ArrayList<Event> getAllEvents(HashMap<String, String> eventsList) {
    ArrayList<Event> toReturn = new ArrayList<Event>();
    String itemString = eventsList.get("items");
    System.out.println(itemString);
    String[] events = itemString.split("kind");
    for (int i = 1; i < events.length; i++) {
      int id = -1;
      int titleLoc = events[i].indexOf("summary");
      String title = events[i].substring(titleLoc + NINE,
          events[i].substring(titleLoc).indexOf(',') + titleLoc).replaceAll(
          "\"", "");
      String group = null;
      List<String> attendees = null;
      int descriptionLoc = events[i].indexOf("description");
      String description;
      if (descriptionLoc != -1) {
        description = events[i].substring(descriptionLoc + FOURTEEN,
            events[i].substring(descriptionLoc).indexOf(',') + descriptionLoc)
            .replaceAll("\"", "");
      } else {
        description = "";
      }
      Date date = null;
      int duration = 0;
      int startLoc = events[i].indexOf("\"start\":");
      int endLoc = events[i].indexOf("\"end\":");
      String start;
      if (events[i].contains("dateTime")) {
        start = events[i]
            .substring(startLoc + TWENTY,
                events[i].substring(startLoc).indexOf(',') + startLoc)
            .replaceAll("\"", "").replaceAll("}", "").replaceAll("T", " ");
        String end = events[i]
            .substring(endLoc + EIGHTEEN,
                events[i].substring(endLoc).indexOf(',') + endLoc)
            .replaceAll("\"", "").replaceAll("}", "").replaceAll("T", " ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
        try {
          date = simpleDateFormat.parse(start);
          Date endDate = simpleDateFormat.parse(end);
          Calendar calendar = GregorianCalendar.getInstance(); // creates a new
                                                               // calendar
                                                               // instance
          calendar.setTime(date); // assigns calendar to given date
          long dateMinutes = calendar.getTimeInMillis();
          calendar.setTime(endDate); // assigns calendar to given date
          long endDateMinutes = calendar.getTimeInMillis();
          duration = (int) ((endDateMinutes - dateMinutes) / SIXTYTHOUS);
          // System.out.println("date : " + simpleDateFormat.format(endDate));
        } catch (ParseException ex) {
          System.out.println("Exception " + ex);
        }
      } else {
        start = events[i]
            .substring(startLoc + SIXTEEN,
                events[i].substring(startLoc).indexOf(',') + startLoc)
            .replaceAll("\"", "").replaceAll("}", "");
        String end = events[i]
            .substring(endLoc + FOURTEEN,
                events[i].substring(endLoc).indexOf(',') + endLoc)
            .replaceAll("\"", "").replaceAll("}", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
          date = simpleDateFormat.parse(start);
          Date endDate = simpleDateFormat.parse(end);
          duration = endDate.getMinutes() - date.getMinutes();
          System.out.println("date : " + simpleDateFormat.format(endDate));
        } catch (ParseException ex) {
          System.out.println("Exception " + ex);
        }
      }
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      int dayWeek = c.get(Calendar.DAY_OF_WEEK);
      ConcurrentHashMap<Integer, String> numbersToDay =
          new ConcurrentHashMap<Integer, String>();
      numbersToDay.put(1, "Sunday");
      numbersToDay.put(2, "Monday");
      numbersToDay.put(THREE, "Tuesday");
      numbersToDay.put(FOUR, "Wednesday");
      numbersToDay.put(FIVE, "Thursday");
      numbersToDay.put(SIX, "Friday");
      numbersToDay.put(SEVEN, "Saturday");
      String dayOfWeek = numbersToDay.get(dayWeek);
      String creator = null;
      Event toPut = new Event(date, title, dayOfWeek, attendees, group,
          duration, description, "google");
      toPut.setID(id);
      toReturn.add(toPut);
    }

    return toReturn;
  }
  /**
   * encodes URL from google.
   * @param params parameters?
   * @return encoded url
   */
  public String urlEncodedString(HashMap<String, String> params) {
    String toReturn = "";
    for (String key : params.keySet()) {
      String value = params.get(key);
      toReturn += key + "=" + value + "&";
    }
    if (toReturn.length() > 0) {
      toReturn = toReturn.substring(0, toReturn.length() - 1);
    }
    return toReturn;
  }
  /**
   * parses query.
   * @param query parses query
   * @return not sure?
   */
  public HashMap<String, String> parseQueryString(String query) {
    HashMap<String, String> toReturn = new HashMap<String, String>();
    query = query.substring(2);
    // query = query.replaceAll("\"", "");
    System.out.println(query);
    String[] pairs = query.split(", ");
    for (String pair : pairs) {
      String[] elements = pair.split(": ");
      String key = elements[0];
      String value = elements[1];
      key = key.replace("\"", "");
      key = key.replace("{", "");
      key = key.replace("}", "");
      value = value.replace("\"", "");
      value = value.replace("{", "");
      value = value.replace("}", "");
      toReturn.put(key, value);
      System.out.println(key);
      System.out.println(value);
    }
    return toReturn;
  }
}
