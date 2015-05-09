package edu.brown.cs.rmchandr.calendar;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.andrew.handlers.Event;

public class EventTests {

  @Test
  public void tests() {

    Date d1 = new Date(1431133493);
    List<String> attendees1 = new ArrayList<String>();
    attendees1.add("roll");
    Event e1 = new Event(d1, "Coding in the CIT", "Friday", attendees1,
        "group", 60, "description", "google");
    assertTrue(e1.getAttendees().get(0).equals("roll"));
    assertTrue(e1.getAttendees().size() == 1);
    attendees1.add("Rohan");
    assertTrue(e1.getAttendees().size() == 2);
    assertTrue(e1.getCreator().equals("google"));
    assertTrue(e1.getDuration() == 60);
    e1.setDuration(120);
    assertTrue(e1.getDuration() == 120);
    assertTrue(e1.getDescription().equals("description"));

  }
}