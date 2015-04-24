package edu.brown.cs.andrew.handlers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.brown.cs.andrew.calendar.Main;
import edu.brown.cs.andrew.clientThreads.HeartBeatThread;

public class Ranker {
  
  private Event base;
  private ConcurrentHashMap<Integer, ClientHandler> attendees = new ConcurrentHashMap<Integer, ClientHandler>();
  
  public Ranker(Event e) {
    List<String> attendeeList = e.getAttendees();
    System.out.println(attendeeList.get(0));
    for (int i = 0; i < attendeeList.size(); i++) {
      ClientHandler currHandler =
          new ClientHandler("calendar.sqlite3", attendeeList.get(i));
      attendees.put(i, currHandler);
    }
    base = e;
    HeartBeatThread hbt = new HeartBeatThread("pull", attendees);
    Future<String> wait = SparkHandler.pool.submit(hbt);
    try {
      wait.get();
    } catch (InterruptedException | ExecutionException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    System.out.println(attendees.get(0).getEvents().size());
  }

  public boolean checkConflict(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(Calendar.MINUTE, base.getDuration());
    Date end = c.getTime();   
    boolean toReturn = true;
    for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
      if (!toReturn) {
        return false;
      }
      ClientHandler curr = e.getValue();
      ConcurrentHashMap<Integer, Event> daysEvents = curr.getEventsByDay(d);
      System.out.println(d);
      for(Entry<Integer, Event> event : daysEvents.entrySet()) {
        Date eventTime;
        try {
          eventTime = event.getValue().getDate();
          Calendar innerCal = Calendar.getInstance();
          innerCal.setTime(eventTime);
          c.add(Calendar.MINUTE, event.getValue().getDuration());
          Date endTime = c.getTime();
          toReturn = !((d.after(eventTime) && d.before(endTime))
              || (end.after(eventTime) && end.before(endTime)));
        } catch (ParseException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }
    return toReturn;
  }
 
}
