package edu.brown.cs.rmchandr.calendar;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import edu.brown.cs.rmchandr.APICalls.ServerCalls;

public class ParserTests {

  @Test
  public void tests() {

    ServerCalls sc = new ServerCalls();
    HashMap<String, String> map1 = sc
        .parseQueryString("{ accessToken: qwerty }");
    assertTrue(map1.get("accessToken").equals("qwerty "));

    HashMap<String, String> map2 = sc
        .parseQueryString("{ accessToken: qwerty, hello: goodbye }");
    assertTrue(map2.get("accessToken").equals("qwerty"));
    assertTrue(map2.get("hello").equals("goodbye "));

  }
}