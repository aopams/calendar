package edu.brown.cs.andrew.handlers;

import com.google.gson.Gson;
/**
 * JSONParser used to pass back an eventoJSON.
 * @author wtruong02151
 *
 */
public class JSONParser {
  private final Gson gson = new Gson();
  /**
   * takes in an event and parses it into a JSON object.
   * @param e event
   * @return json object
   */
  public String eventToJson(Event e) {
    String toReturn = gson.toJson(e);
    return toReturn;
  }
}
