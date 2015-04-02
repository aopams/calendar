package edu.brown.cs.andrew.handlers;

import com.google.gson.*;

public class JSONParser {
  private final Gson gson = new Gson();
  
  public String eventToJson(Event e) {
    String toReturn = gson.toJson(e);
    System.out.println(toReturn);
    return toReturn;
  }
}
