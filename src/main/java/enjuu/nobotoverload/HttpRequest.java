package enjuu.nobotoverload;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest {
	
	public static String getHttpString(String server) throws Exception {
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
		return response.toString();
	}

}
