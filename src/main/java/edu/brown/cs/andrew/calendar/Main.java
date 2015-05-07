package edu.brown.cs.andrew.calendar;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.andrew.handlers.Ranker;
import edu.brown.cs.andrew.handlers.SparkHandler;
;

public class Main {

  private static DatabaseHandler myDBHandler;
  private static SparkHandler server = new SparkHandler("calendar.sqlite3");
  
  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis() / 1000);
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
      Date myDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/4/2015");
      System.out.println(myDate.toString());
      List<String> hSquad = new ArrayList<String>();
      List<String> hGroup = new ArrayList<String>();
      hGroup.add("Harsha");
      hGroup.add("Rohan");
      System.out.println(myDate.toString());
      Event e = new Event(myDate, "Party Time!!!!", "Friday", hSquad,
          "", 180,
          "Harsha Squad going Ham to Trap Queen for 3 hours",
          "mickey");
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
     // myDBHandler.addEvent(e2);
     // myDBHandler.addEvent(e3); 
      List<String> hGroup1 = new ArrayList<String>();
      hGroup1.add("Harsha");
      Date myDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("13/4/2015 13:00");
      Event e1 = new Event(myDate1, "Ninja Time!", "Monday", hGroup1, "", 180,
          "Harsha going stealth-mode",
          "Harsha");
      e1.setID(11);
      myDBHandler.closeConnection();
      //System.out.println(myRanker.checkConflict(e2.getDate()));
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