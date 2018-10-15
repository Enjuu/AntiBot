package enjuu.nobotoverload;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Tool for checking if the Server get's a ddos
 * and disable registrations. ~Please fork GitHub rep if you use this Tool
 */

public class App extends Thread {
    // MySQL Java Shit.
    private String url = "jdbc:mysql://" + Config.getString("mysqlip") + ":" + Config.getString("mysqlport") + "/" + Config.getString("mysqldatabase");
    private String username = Config.getString("mysqlusername");
    private String password = Config.getString("mysqlpassword");
    private Connection connection = null;

    // Important for checking
    private static int beforestate = 0;

    // Thread for checking
    public App() throws Exception {
        while (true) {
            System.out.println();
            System.out.println("Start check if Server get's botted");
            JSONParser parser = new JSONParser();

            // GET Online Users
            Object obj2 = parser.parse(HttpRequest.getHttpString("https://c." + Config.getString("api") + "/api/v1/onlineUsers"));

            // Parse to JSON
            JSONObject jsonObject = (JSONObject) obj2;
            long result = (long) jsonObject.get("result");

            int res = Integer.parseInt(new StringBuilder().append(result).toString());
            int i = res - beforestate;

            // Check how many Users are Online
            if (i > (Config.getLong("howmuchusers"))) {
                System.out.println("Server get's botted, disabling registrations...");
                BotsAreOnline();
            }

            System.out.println("Finished checking if Server get's botted only a diffrence of " + i);
            beforestate = res;
            App.sleep(Config.getLong("threadtimeoutbetweencheck"));
        }
    }

    public static void main(String[] args) throws Exception {

        // Splash Screen
        System.out.println(
                "█▀▀▄ █▀▀█ █▀▀▄ █▀▀█ ▀▀█▀▀ █▀▀ █▀▀█ █▀▀▄ █░░█ █▀▄▀█ █▀▀█ █▀▀█ █▀▀\r\n" +
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

    public static int getOnlineUsers() throws Exception {

        JSONParser parser = new JSONParser();
        // GET Online Users
        Object obj2 = parser.parse(HttpRequest.getHttpString("https://c." + Config.getString("api") + "/api/v1/onlineUsers"));

        // Parse to JSON
        JSONObject jsonObject = (JSONObject) obj2;
        long result = (long) jsonObject.get("result");
        int res = Integer.parseInt(new StringBuilder().append(result).toString());
        return res;
    }

    public void BotsAreOnline() throws Exception {
        System.out.println("Connecting database for disable registrations...");
        ChangeRegistrations(true);
        HttpRequest.sendOfflineWebhook();

        App.sleep(600000);

        System.out.println("Connecting database for enable registrations...");
        ChangeRegistrations(false);
        HttpRequest.sendOnlineWebhook();
    }

    private void ChangeRegistrations(boolean disable) throws IllegalStateException {
        if (this.connection == null) {
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("Database Connected!");
                this.connection = connection;
            } catch (SQLException e) {
                throw new IllegalStateException("Cannot Connect the Database!", e);
            }
        }
        try {
            PreparedStatement preparedStmt = this.connection.prepareStatement("UPDATE system_settings SET value_int =  WHERE id = 5;");
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            this.connection = null;
            ChangeRegistrations(disable);
            System.out.println("Database Connection died.");
        }
    }
}
