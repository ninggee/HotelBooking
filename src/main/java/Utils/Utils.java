package Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mindrot.jbcrypt.BCrypt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";

    private static DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

    //genetate new password
    public static String calPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String passFromDatabase) {
        return BCrypt.checkpw(password, passFromDatabase);
    }

    public static String response(boolean is_success, String message, Object result) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormatPattern).create();
        Map<String , Object> tmp = new HashMap<>();
        tmp.put("status", is_success);
        tmp.put("message", message);
        tmp.put("result", result);

        return gson.toJson(tmp);
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }
}
