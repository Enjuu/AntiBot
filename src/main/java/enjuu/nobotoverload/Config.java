package enjuu.nobotoverload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Spring;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

    public static Object config = null;
    public static JSONObject objconfig = null;

    public static void loadConfig() throws FileNotFoundException, IOException, ParseException {
        File dir = new File("tmp");
        dir.mkdir();
        JSONParser parser = new JSONParser();
        Object obj = null;
        obj = parser.parse(new FileReader("config.json"));
        config = obj;
        JSONObject jsonObject = (JSONObject) obj;
        objconfig = jsonObject;
        System.out.println("[INFO]: Successfully loaded Config.json");
    }

    @SuppressWarnings({"unchecked"})
    public static void createConfig() {
        File f = new File("config.json");
        if (f.exists()) {
            System.out.println("[INFO]: Found Config! No one must be created");
        } else {
            System.out.println("[WARNING]: No Config Found! Create one...");
            JSONObject obj = new JSONObject();
            obj.put("api", "enjuu.click");
            obj.put("mysqlip", "localhost");
            obj.put("mysqlport", "3306");
            obj.put("mysqldatabase", "ripple");
            obj.put("mysqlusername", "root");
            obj.put("mysqlpassword", "");
            obj.put("threadtimeoutbetweencheck", 8000);
            obj.put("howmuchusers", 10);
            obj.put("webhook", "NaN");
            try (FileWriter file = new FileWriter("config.json")) {
                file.write(obj.toJSONString());
                System.out.println("[INFO]: Successfully created Config...");
            } catch (IOException e) {
                System.err.println("[ERROR]: Can't create Config...");
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    public static String getString(String string) {
        try {
            String result = (String) objconfig.get(string);
            return result;
        } catch (Exception e) {
            return "Error Load " + string + " in config.json! Is it possible that your String ins't a String?";
        }
    }

    public static int getInt(Spring path) {
        try {
            int result = (int) objconfig.get(path);
            return result;
        } catch (Exception e) {
            System.out.println("I can't load the int! Maybe the in the Config the Number ins't a int. Try use a Long");
            return 404;
        }
    }

    public static Boolean getBool(String string) {
        try {
            Boolean result = (Boolean) objconfig.get(string);
            return result;
        } catch (Exception e) {
            System.out.println("Error Loading the Config");
            return false;
        }
    }

    public static Long getLong(String string) {
        try {
            Long result = (Long) objconfig.get(string);
            return result;
        } catch (Exception e) {
            System.out.println("Error Loading the Config");
            return null;
        }
    }
}