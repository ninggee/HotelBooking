package Utils;

import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    //genetate new password
    public static String calPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String passFromDatabase) {
        return BCrypt.checkpw(password, passFromDatabase);
    }

    public static String response(boolean is_success, String message, Object result) {
        Gson gson = new Gson();
        Map<String , Object> tmp = new HashMap<>();
        tmp.put("status", is_success);
        tmp.put("message", message);
        tmp.put("result", result);

        return gson.toJson(tmp);
    }
}
