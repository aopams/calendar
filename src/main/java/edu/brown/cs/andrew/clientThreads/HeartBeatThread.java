package edu.brown.cs.andrew.clientThreads;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.andrew.handlers.ClientHandler;
public class HeartBeatThread implements Runnable{
  private ConcurrentHashMap<Integer, ClientHandler> clients;
  String typeHeartBeat;
  
  public HeartBeatThread(String type, ConcurrentHashMap<Integer, ClientHandler> clients) {
    typeHeartBeat = type;
    this.clients = clients;
  }
  
  @Override
  public void run() {
    if(typeHeartBeat.equals("pul")) {
      
    }
    
  }

}
