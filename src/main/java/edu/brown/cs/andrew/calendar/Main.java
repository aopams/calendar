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
      myDBHandler.closeConnection();
      //System.out.println(myRanker.checkConflict(e2.getDate()));
      System.out.println(System.currentTimeMillis() / 1000);
    } catch (ClassNotFoundException | SQLException e) {
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