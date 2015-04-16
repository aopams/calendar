package edu.brown.cs.andrew.handlers;

public class DateHandler {
  private String month;
  private int day;
  private int year;
  
  public DateHandler(String month, int day, int year) {
    this.month = month;
    this.year = year;
    this.day = day;
  }
  public String getMonth() {
    return month;
  }
  public int getDay() {
    return day;
  }
  public int getYear() {
    return year;
  }
}
