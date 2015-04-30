package edu.brown.cs.andrew.handlers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.brown.cs.andrew.clientThreads.HeartBeatThread;

public class Ranker {

  private Event base;
  private ConcurrentHashMap<Integer, ClientHandler> attendees = new ConcurrentHashMap<Integer, ClientHandler>();
  private HashMap<Integer, Integer> bestHoursTable;

  public Ranker(Event e) {
    this.setBestHours();
    List<String> attendeeList = e.getAttendees();
    for (int i = 0; i < attendeeList.size(); i++) {
      ClientHandler currHandler = new ClientHandler("calendar.sqlite3",
          attendeeList.get(i), false);
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
      System.out.println("attendees : " + attendees.entrySet().size());
      ClientHandler curr = e.getValue();
      ConcurrentHashMap<Integer, Event> daysEvents = curr.getEventsByDay(d);
      for (Entry<Integer, Event> event : daysEvents.entrySet()) {
        if (!toReturn) {
          return false;
        }
        Date eventTime;
        try {
          eventTime = event.getValue().getDate();
          System.out.println(d + " START VERSUS " + eventTime);
          Calendar innerCal = Calendar.getInstance();
          innerCal.setTime(eventTime);
          System.out.println(event.getValue().getDuration());
          innerCal.add(Calendar.MINUTE, event.getValue().getDuration());
          Date endTime = innerCal.getTime();
          System.out.println(end + " End VERSUS " + endTime);
          toReturn = !((d.after(eventTime) && d.before(endTime)) || 
              (end.after(eventTime) && end.before(endTime))
              || (eventTime.after(d) && eventTime.before(end))
              || (endTime.after(d) && endTime.before(end))
              || (d.compareTo(eventTime) == 0 || end.compareTo(endTime) == 0));
          System.out.println(toReturn);
        } catch (ParseException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }
    return toReturn;
  }

  public void checkAllConflicts(Date d) {

    for (Entry<Integer, ClientHandler> e : attendees.entrySet()) {
      ClientHandler curr = e.getValue();
      ConcurrentHashMap<Integer, Event> daysEvents = curr.getEventsByDay(d);
      System.out.println(d);
      System.out.println("Events number " + daysEvents.size());
      for (Entry<Integer, Event> event : daysEvents.entrySet()) {
        try {
          Calendar c = Calendar.getInstance();

          c.setTime(event.getValue().getDate());

          int startHour = c.get(Calendar.HOUR_OF_DAY);
          c.add(Calendar.MINUTE, base.getDuration());
          int endHour = c.get(Calendar.HOUR_OF_DAY);
          if (startHour <= endHour) {
            for (int j = startHour; j <= endHour; j++) {
              int val = bestHoursTable.get(j);
              int newVal = val - (200 / attendees.size());
              bestHoursTable.put(j, newVal);
            }
          }
          if (startHour != 0) {
            int val = bestHoursTable.get(startHour - 1);
            int newVal = val + (200 / attendees.size());
            bestHoursTable.put(startHour - 1, newVal);
          }
          if (endHour != 23) {
            int val = bestHoursTable.get(endHour + 1);
            int newVal = val + (200 / attendees.size());
            bestHoursTable.put(endHour + 1, newVal);
          }
        } catch (ParseException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }

      }
    }
  }

  private void setBestHours() {
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    map.put(0, 200);
    map.put(1, 100);
    map.put(2, 100);
    map.put(3, 50);
    map.put(4, 20);
    map.put(5, 20);
    map.put(6, 20);
    map.put(7, 20);
    map.put(8, 20);
    map.put(9, 20);
    map.put(10, 50);
    map.put(11, 100);
    map.put(12, 50);
    map.put(13, 100);
    map.put(14, 100);
    map.put(15, 200);
    map.put(16, 400);
    map.put(17, 400);
    map.put(18, 200);
    map.put(19, 200);
    map.put(20, 300);
    map.put(21, 300);
    map.put(22, 300);
    map.put(23, 300);
    this.bestHoursTable = map;
  }

  private Comparator<Integer> hourComp = new Comparator<Integer>() {
    @Override
    public int compare(Integer i1, Integer i2) {
      if (bestHoursTable.get(i1) < bestHoursTable.get(i2)) {
        return 1;
      } else if (bestHoursTable.get(i1) > bestHoursTable.get(i2)) {
        return -1;
      } else {
        return 0;
      }
    }
  };

  public Integer[] getBestTimes(int k, Date d) {
    this.checkAllConflicts(d);
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>(hourComp);
    for (int i = 0; i < 24; i++) {
      pq.add(i);
    }
    Integer[] toReturn = new Integer[k];
    int p = 0;

    while (p < k) {
      Integer integer = pq.poll();
      toReturn[p] = integer;
      p++;
    }
    return toReturn;

  }
}
