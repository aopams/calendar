package edu.brown.cs.andrew.calendar;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.andrew.handlers.SparkHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Main method, handles initializing the DBHandler as well as
 * setting up test events. Starts spark server as well.
 * @author aosgood
 *
 */
//public vinal to avoid utility class warning
public final class Main {
  private static DatabaseHandler myDBHandler;
  private static SparkHandler server = new SparkHandler("calendar.sqlite3");
  private static final int THOUSANDNUM = 1000;
  private static final int ONEEIGHTY = 180;
  private static final int TEMPEVENTID = 11;
  //changed to private
  /**
   * main constructor, handles initializing spark server.
   * @param args command line arguments (port num)
   */
  public static void main(String[] args) {
    System.out.println("Hello World");
    System.out.println(System.currentTimeMillis() / THOUSANDNUM);
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
          "", ONEEIGHTY,
          "Harsha Squad going Ham to Trap Queen for 3 hours",
          "mickey");
      System.out.println(myDBHandler.findGroup("Harsha Squad"));
      myDBHandler.addEvent(e);
      List<String> hGroup1 = new ArrayList<String>();
      hGroup1.add("Harsha");
      Date myDate1 = new SimpleDateFormat("dd/MM/yyyy hh:mm")
        .parse("13/4/2015 13:00");
      Event e1 = new Event(myDate1, "Ninja Time!", "Monday", hGroup1, "",
          ONEEIGHTY,
          "Harsha going stealth-mode",
          "Harsha");
      e1.setID(TEMPEVENTID);
      myDBHandler.closeConnection();

      System.out.println(System.currentTimeMillis() / THOUSANDNUM);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (ParseException e4) {
      // TODO Auto-generated catch block
      e4.printStackTrace();
    } catch (SQLException e4) {
      // TODO Auto-generated catch block
      e4.printStackTrace();
    } finally {
      run(args);
    }
  }

  /**
   * run() method handles command line input to run program with gui if "--gui"
   * is indicated.
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
