package Utils;

import com.google.gson.Gson;
import models.UserModel;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;

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

    public static int checkAuth(Request request) {
        boolean is_login = request.session().attribute("is_login");

        if(is_login) {
            UserModel user = (UserModel) request.session().attribute("user");

            if(user.isIs_admin()) {
                return 2; //admin user
            } else {
                return 1; //normal user
            }

        } else {
            return  0; //user is not login
        }
    }

    public static UserModel getLoginUser(Request request) {
        boolean is_login = request.session().attribute("is_login");

        if(is_login) {
            UserModel user = (UserModel) request.session().attribute("user");

           return user;

        } else {
            return  null; //user is not login
        }
    }
}
