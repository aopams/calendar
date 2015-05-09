package edu.brown.cs.andrew.handlers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Event class that stores all proper fields needed
 * for an event, including the event's date and time,
 * the duration, day of the week, description, creator,
 * attendees, etc.
 * @author wtruong02151
 *
 */
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
  private String conflictColor;
  /**
   * constructor that initializes global variables.
   * @param date date
   * @param title title
   * @param dayOfWeek dayOfWeek
   * @param attendees attendees
   * @param group group
   * @param duration duration
   * @param description description
   * @param creator creator
   */
  public Event(Date date, String title,
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
  /**
   * grab event's id.
   * @return id
   */
  public int getId() {
    return id;
  }
  /**
   * grab event's title.
   * @return title the title
   */
  public String getTitle() {
    return title;
  }
  /**
   * grab event's day of the week.
   * @return week the week
   */
  public String getDayOfWeek() {
    return dayOfWeek;
  }
  /**
   * grab event's date.
   * @return date date
   * @throws ParseException error
   */
  public Date getDate() throws ParseException {
    Calendar myCal = Calendar.getInstance();
    myCal.setTime(date);
    Date toReturn = myCal.getTime();
    return toReturn;
  }
  /**
   * grab event's group.
   * @return group group
   */
  public String getGroup() {
    return group;
  }
  /**
   * grab event's description.
   * @return description description
   */
  public String getDescription() {
    return description;
  }
  /**
   * grab event's duration.
   * @return duration duration
   */
  public int getDuration() {
    return duration;
  }
  /**
   * grab event's creator.
   * @return creator creator
   */
  public String getCreator() {
    return creator;
  }
  /**
   * set event's id.
   * @param eid eid
   */
  public void setID(int eid) {
    this.id = eid;
  }
  /**
   * set event's attendees.
   * @param attends attendees
   */
  public void setAttendees(List<String> attends) {
    attendees = attends;
  }
  /**
   * grab list of attendees for this event.
   * @return list of attendees
   */
  public List<String> getAttendees() {
    List<String> toReturn = new ArrayList<String>();
    for (int i = 0; i < attendees.size(); i++) {
      toReturn.add(attendees.get(i));
    }
    return toReturn;
  }
  /**
   * set the event's duration.
   * @param d duration
   */
  public void setDuration(int d) {
    duration = d;
  }
  /**
   * set event's conflict color.
   * @param color color to set
   */
  public void setConflictColor(String color) {
    this.conflictColor = color;
  }
}
