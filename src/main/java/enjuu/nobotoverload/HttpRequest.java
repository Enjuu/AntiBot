package enjuu.nobotoverload;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

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
	
	public static void sendOfflineWebhook() throws Exception {
		URL url = new URL(Config.getString("webhook"));
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("User-Agent", "Mozilla/5.0");
		byte[] out = "{\"username\":\"AntiBot - Status\",\"avatar_url\":\"https://www.iconfinder.com/icons/2400508/download/png/256\",\"content\":\"Registrations are disabled for 10 minutes because botting\"}" .getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		http.connect();
		try(OutputStream os = http.getOutputStream()) {
		    os.write(out);
		}
		http.getInputStream();
	}
	
	public static void sendOnlineWebhook() throws Exception {
		URL url = new URL(Config.getString("webhook"));
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("User-Agent", "Mozilla/5.0");
		byte[] out = "{\"username\":\"AntiBot - Status\",\"avatar_url\":\"https://www.iconfinder.com/icons/2400508/download/png/256\",\"content\":\"Registrations are enabled back\"}" .getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		http.connect();
		try(OutputStream os = http.getOutputStream()) {
		    os.write(out);
		}
		http.getInputStream();
	}
}
