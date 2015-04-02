package edu.brown.cs.andrew.handlers;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Event {
  private int id;
  private Time time;
  private Date date;
  private String title;
  private String dayOfWeek;
  private List<String> attendees;
  
  public Event (int id, Time time, Date date, String title,
      String dayOfWeek, List<String> attendees) {
    this.id = id;
    this.time = time;
    this.date = date;
    this.title = title;
    this.dayOfWeek = dayOfWeek;
    this.attendees = attendees;
  }
  public int getId() {
    return id;
  }
  public String getTitle() {
    return title;
  }
  public String getDayOfWeek() {
    return dayOfWeek;
  }
  public Time getTime() {
    Time toReturn = new Time(time.getHours(), time.getMinutes(), time.getSeconds());
    return toReturn;
  }
  public Date getDate() {
    Date toReturn = new Date(date.getYear(), date.getMonth(), date.getDay());
    return toReturn;
  }
  public List<String> getAttendees() {
    List<String> toReturn = new ArrayList<String>();
    for (int i = 0; i < attendees.size(); i++) {
      toReturn.add(attendees.get(i));
    }
    return toReturn;
  }
}
