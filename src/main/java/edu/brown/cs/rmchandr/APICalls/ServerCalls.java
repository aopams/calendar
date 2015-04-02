package edu.brown.cs.rmchandr.APICalls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ServerCalls {

  private final String clientID = "223888438447-5vjvjsu85l893mjengfjvd0fjsd8fo1r.apps.googleusercontent.com";
  private final String clientSecret = "6rmO_xu590Oe89yGFL-kX-l8";
  private final String redirectURI = "http://localhost:4567/authorize";

  public void loginClicked() {

    String website = "https://accounts.google.com/o/oauth2/auth";

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("response_type", "code");
    params.put("client_id", clientID);
    params.put("scope", "email%20profile");
    params.put("redirect_uri", redirectURI);

    website += "?" + urlEncodedString(params);
    System.out.println(website);
    try {
      URL url = new URL(website);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      int statusCode = conn.getResponseCode();
      System.out.println(statusCode);

      // open the stream and put it into BufferedReader
      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));

      String inputLine;
      while ((inputLine = br.readLine()) != null) {
        System.out.println(inputLine);
      }
      br.close();

    } catch (MalformedURLException e) {
      System.out.println("ERROR: Malformed URL");
    } catch (IOException e) {
      System.out.println("ERROR: IO Exception");
    }

  }

  public HashMap<String, String> authorize(String code) {

    try {

      String website = "www.googleapis.com/oauth2/v3/token";
      URL url = new URL(website);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("POST");
      conn.setRequestProperty("code", code);
      conn.setRequestProperty("client_id", clientID);
      conn.setRequestProperty("client_secret", clientSecret);
      conn.setRequestProperty("redirect_uri", redirectURI);
      conn.setRequestProperty("grant_type", "authorization_code");

      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.flush();
      wr.close();

      BufferedReader in = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      // print result
      System.out.println(response.toString());

      return parseQueryString(response.toString());
    } catch (Exception e) {
      System.out.println("ERROR:");
      return null;
    }

  }

  public String urlEncodedString(HashMap<String, String> params) {
    String toReturn = "";
    for (String key : params.keySet()) {
      String value = params.get(key);
      toReturn += key + "=" + value + "&";
    }
    if (toReturn.length() > 0) {
      toReturn = toReturn.substring(0, toReturn.length() - 1);
    }
    return toReturn;
  }

  public HashMap<String, String> parseQueryString(String query) {
    HashMap<String, String> toReturn = new HashMap<String, String>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      String[] elements = pair.split("=");
      toReturn.put(elements[0], elements[1]);
    }
    return toReturn;
  }

}
