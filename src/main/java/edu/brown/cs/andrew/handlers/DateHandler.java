package edu.brown.cs.andrew.handlers;
/**
 * datehandler object that stores a date in three fields.
 * @author wtruong02151
 *
 */
public class DateHandler {
  private String month;
  private int day;
  private int year;
  /**
   * constructor that instantiates global variables.
   * @param month month
   * @param day day
   * @param year year
   */
  public DateHandler(String month, int day, int year) {
    this.month = month;
    this.year = year;
    this.day = day;
  }
  /**
   * return month.
   * @return month month
   */
  public String getMonth() {
    return month;
  }
  /**
   * return day.
   * @return day day
   */
  public int getDay() {
    return day;
  }
  /**
   * return year.
   * @return year year
   */
  public int getYear() {
    return year;
  }
}
