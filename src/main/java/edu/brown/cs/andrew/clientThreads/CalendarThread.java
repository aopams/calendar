package edu.brown.cs.andrew.clientThreads;

import java.sql.SQLException;

import edu.brown.cs.andrew.handlers.ClientHandler;
import edu.brown.cs.andrew.handlers.DatabaseHandler;
import edu.brown.cs.andrew.handlers.Event;

public class CalendarThread implements Runnable{
  
  private ClientHandler client1; 
  private String command;
  private DatabaseHandler myDBHandler;
  
  public CalendarThread(ClientHandler client1, String command, Event e) {
    this.client1 = client1;
    this.command = command;
    try {
      myDBHandler = new DatabaseHandler("calendar.sqlite3");
    } catch (SQLException | ClassNotFoundException error) {
      error.printStackTrace();
    }
  }
  
  @Override
  public void run() {
    
  }

}
