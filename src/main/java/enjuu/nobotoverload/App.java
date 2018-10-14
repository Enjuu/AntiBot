package enjuu.nobotoverload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Tool for checking if the Server get's a ddos
 * and disable registrations. ~Please fork GitHub rep if you use this Tool
 * 
 */

public class App extends Thread
{
	// MySQL Java Shit.
	String url = "jdbc:mysql://" + Config.getString("mysqlip")+":" + Config.getString("mysqlport") + "/" + Config.getString("mysqldatabase");
	String username = Config.getString("mysqlusername");
	String password = Config.getString("mysqlpassword");
	
	// Important for checking
	public static int beforestate = 0;
	
	// Thread for checking
	public App() throws Exception{
	    while(true) {
	    	System.out.println();
	    	System.out.println("Start check if Server get's botted");
	    	 String server = "https://c."+Config.getString("api") + "/api/v1/onlineUsers";
		     URL objurl = new URL(server);
		     HttpsURLConnection connect = (HttpsURLConnection) objurl.openConnection();
		     connect.setRequestMethod("GET");
		     connect.setRequestProperty("User-Agent", "Mozilla/5.0");
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(connect.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     JSONParser parser = new JSONParser();
		     Object obj2 = parser.parse(response.toString());
		     JSONObject jsonObject = (JSONObject) obj2;
		     long result = (long) jsonObject.get("result");
		     int res = Integer.parseInt(new StringBuilder().append(result).toString());
		     int i = res - beforestate;
		     if(i > Config.getLong("howmuchusers")) {
		    	 System.out.println("Server get's botted, disabling registrations...");
		    	 BotsAreOnline();
		     }
		     System.out.println("Finished checking if Server get's botted only a diffrence of " +i);
		     beforestate = res;
		     App.sleep(Config.getLong("threadtimeoutbetweencheck"));
	    }
	  }
	
    public static void main( String[] args ) throws Exception
    {
    	// Splash Screen
    	System.out.println("█▀▀▄ █▀▀█ █▀▀▄ █▀▀█ ▀▀█▀▀ █▀▀ █▀▀█ █▀▀▄ █░░█ █▀▄▀█ █▀▀█ █▀▀█ █▀▀\r\n" + 
    			"█░░█ █░░█ █▀▀▄ █░░█ ░░█░░ ▀▀█ █▄▄█ █░░█ █▄▄█ █░▀░█ █░░█ █▄▄▀ █▀▀\r\n" + 
    			"▀░░▀ ▀▀▀▀ ▀▀▀░ ▀▀▀▀ ░░▀░░ ▀▀▀ ▀░░▀ ▀░░▀ ▄▄▄█ ▀░░░▀ ▀▀▀▀ ▀░▀▀ ▀▀▀");
    	System.out.println("~ written by Kazuki");
    	
    	// Config generator
    	Config.createConfig();
    	Config.loadConfig();
    	
    	beforestate = getOnlineUsers();
    	
    	// Loop for checking
    	Thread t = new App();
    	t.start();
    }
    
    public static int getOnlineUsers() throws IOException, ParseException {
    	String server = "https://c."+Config.getString("api") + "/api/v1/onlineUsers";
	     URL objurl = new URL(server);
	     HttpsURLConnection connect = (HttpsURLConnection) objurl.openConnection();
	     connect.setRequestMethod("GET");
	     connect.setRequestProperty("User-Agent", "Mozilla/5.0");
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(connect.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     JSONParser parser = new JSONParser();
	     Object obj2 = parser.parse(response.toString());
	     JSONObject jsonObject = (JSONObject) obj2;
	     long result = (long) jsonObject.get("result");
	     int res = Integer.parseInt(new StringBuilder().append(result).toString());
	     return res;
    }
    
    public void BotsAreOnline() {
    	System.out.println("Connecting database for disable registrations...");
    	try (Connection connection = DriverManager.getConnection(url, username, password)) {
    	    System.out.println("Database connected!");
    	    String query = "UPDATE system_settings\r\n" + 
    	    		"SET value_int = 0\r\n" + 
    	    		"WHERE id = 5;";
    	      PreparedStatement preparedStmt = connection.prepareStatement(query);
    	      preparedStmt.executeUpdate();
    	      
    	      connection.close();
    	} catch (SQLException e) {
    	    throw new IllegalStateException("Cannot connect the database!", e);
    	}
    	
    	

    }
}
