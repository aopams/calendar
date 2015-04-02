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
  private final String redirectURI = "http://localhost:1234";

  public String loginClicked() {

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
      String toReturn = "";
      while ((inputLine = br.readLine()) != null) {
        toReturn += inputLine + "\n";
      }

      br.close();
      return toReturn;

    } catch (MalformedURLException e) {
      System.out.println("ERROR: Malformed URL");
    } catch (IOException e) {
      System.out.println("ERROR: IO Exception");
    }
    return null;

  }

  public HashMap<String, String> authorize(String code) {

    try {

      String website = "http://www.googleapis.com/oauth2/v3/token";
      URL url = new URL(website);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("POST");
      // conn.setRequestProperty("Host", "");
      conn.setRequestProperty("Content-Type",
          "application/x-www-form-urlencoded");

      HashMap<String, String> paramsMap = new HashMap<String, String>();
      paramsMap.put("code", code);
      paramsMap.put("client_id", clientID);
      paramsMap.put("client_secret", clientSecret);
      paramsMap.put("redirect_uri", redirectURI);
      paramsMap.put("grant_type", "authorization_code");
      String params = urlEncodedString(paramsMap);
      System.out.println(params);

      conn.setUseCaches(false);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.writeBytes(params);
      wr.flush();
      wr.close();

      int responseCode = conn.getResponseCode();
      System.out.println("\nSending 'POST' request to URL : " + url);
      System.out.println("Post parameters : " + params);
      System.out.println("Response Code : " + responseCode);

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
      e.printStackTrace();
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
