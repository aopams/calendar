package edu.brown.cs.rmchandr.calendar;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.andrew.handlers.Event;
import edu.brown.cs.andrew.handlers.Ranker;

public class RankerTests {

  @Test
  public void tests() {

    Date d1 = new Date(1431133493);
    List<String> attendees1 = new ArrayList<String>();
    attendees1.add("roll");
    Event e1 = new Event(d1, "Coding in the CIT", "Friday", attendees1,
        "group", 60, "description", "google");
    Ranker r1 = new Ranker(e1);

    Integer[] times1 = r1.getBestTimes(3, d1);
    assertTrue(times1.length == 3);
    assertTrue(times1[0] == 7);
    assertTrue(times1[1] == 9);
    assertTrue(times1[2] == 10);

    Date d2 = new Date(1431173793);
    List<String> attendees2 = new ArrayList<String>();
    attendees2.add("Rohan");
    attendees2.add("rock");
    attendees2.add("roll");
    attendees2.add("hyeddana");
    attendees2.add("aosgood");
    attendees2.add("wtruong");
    Event e2 = new Event(d2, "Coding in the CIT", "Friday", attendees2,
        "group", 120, "description", "google");
    Ranker r2 = new Ranker(e2);

    Integer[] times2 = r2.getBestTimes(3, d2);
    assertTrue(times2.length == 3);
    assertTrue(times2[0] == 16);
    assertTrue(times2[1] == 17);
    assertTrue(times2[2] == 20);

  }
}