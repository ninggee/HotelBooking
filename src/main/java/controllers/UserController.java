package controllers;

import Utils.Utils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sun.org.apache.regexp.internal.RE;
import models.Model;
import models.UserModel;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private static Dao<UserModel, String> userDao;

    static {
        try {
            userDao =  DaoManager.createDao(Model.connectionSource, UserModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //create normal user
    public static String addUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if(UserController.isNameUsed(username)) {
            return Utils.response(false, "用户名已被使用", null);
        }

        UserModel userModel;
        userModel = new UserModel();
        userModel.setName(username);
        userModel.setPassword(Utils.calPassword(password));
        userModel.setIs_admin(false);

        try {
            userDao.create(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "创建失败，请重试", null);
        }
       return Utils.response(true, "创建成功", null);

    }

    public static String login(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");


        Map<String, Object > params = new HashMap<>();
        params.put("name", username);

        try {

            List<UserModel> userModels = userDao.queryForFieldValues(params);

            if(userModels.size()  == 1) {
                UserModel userModel = userModels.get(0);
                boolean result = Utils.checkPassword(password, userModel.getPassword());
                if(result) {
                    request.session().attribute("is_login", "1");
                    request.session().attribute("username", userModel.getName());
                    request.session().attribute("is_admin", userModel.isIs_admin() + "");

                    response.cookie("user_id", userModel.getId() + "", 3306);

                    return Utils.response(result, "登录成功", null);
                } else {
                    return Utils.response(result, "登录失败,用户名或密码错误", null);
                }
            } else {
                return Utils.response( false, "用户名重复，请联系管理员", null);
            }


        } catch (SQLException e) {
            return Utils.response( false, "登录失败，请重试", null);
        }

    }

    private static boolean isNameUsed(String name) {
        Map<String, Object > params = new HashMap<>();
        params.put("name", name);

        try {

            List<UserModel> userModels = userDao.queryForFieldValues(params);

            return userModels.size()  > 0;

        } catch (SQLException e) {
            return true;
        }
    }

//    //create admin user
//    public static boolean addAdmin(Request request, Response response) {
//        String username = request.queryParams("username");
//        String email = request.queryParams("email");
//
//
//        UserModel userModel;
//        userModel = new UserModel();
//        userModel.setName(username);
//        userModel.setPassword(Utils.calPassword(email));
//        userModel.setIs_admin(true);
//
//        try {
//            userDao.create(userModel);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//
//    }



}
