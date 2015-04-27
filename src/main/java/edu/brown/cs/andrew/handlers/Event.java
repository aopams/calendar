package edu.brown.cs.andrew.handlers;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Event {
  private int id;
  private Date date;
  private String title;
  private String dayOfWeek;
  private int duration;
  private String group;
  private List<String> attendees;
  private String description;
  private String creator;
  
  public Event (Date date, String title,
      String dayOfWeek, List<String> attendees,  
      String group, int duration, String description, String creator) {
    this.date = date;
    this.title = title;
    this.dayOfWeek = dayOfWeek;
    this.attendees = attendees;
    this.group = group;
    this.duration = duration;
    this.description = description;
    this.creator = creator;
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
  public Date getDate() throws ParseException {
    Calendar myCal = Calendar.getInstance();
    myCal.setTime(date);
    Date toReturn = myCal.getTime();
    return toReturn;
  }
  public String getGroup() {
    return group;
  }
  public String getDescription() {
    return description;
  }
  public int getDuration() {
    return duration;
  }
  public String getCreator() {
    return creator;
  }
  public void setID(int id) {
    this.id = id;
  }
  public List<String> getAttendees() {
    List<String> toReturn = new ArrayList<String>();
    for (int i = 0; i < attendees.size(); i++) {
      toReturn.add(attendees.get(i));
    }
    return toReturn;
  }
  public void setDuration(int d) {
    duration = d;
  }
}
