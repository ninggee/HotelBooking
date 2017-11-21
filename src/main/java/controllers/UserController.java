package controllers;

import Utils.Utils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sun.org.apache.regexp.internal.RE;
import models.Model;
import models.UserModel;
import org.slf4j.Logger;
import org.slf4j.impl.SimpleLogger;
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
                    request.session().attribute("user", userModel);


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

    //create admin user
    public static String addAdmin(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if(UserController.isNameUsed(username)) {
            return Utils.response(false, "用户名已被使用", null);
        }

        if(Utils.checkAuth(request) != 2) {
            return Utils.response(false, "越权操作", null);
        }

        UserModel userModel;
        userModel = new UserModel();
        userModel.setName(username);
        userModel.setPassword(Utils.calPassword(password));
        userModel.setIs_admin(true);

        try {
            userDao.create(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "创建失败，请重试", null);
        }
        return Utils.response(true, "创建管理员成功", null);

    }

    public static String changePassword(Request request, Response response) {
        if(Utils.checkAuth(request) == 0) {//没有登录
            return Utils.response(false, "非法操作", null);
        }

        //获取登录用户
        UserModel user = Utils.getLoginUser(request);
        String old = request.queryParams("old");
        String newPassword = request.queryParams("newPassword");

        if(Utils.checkPassword(old, user.getPassword())) {
            user.setPassword(Utils.calPassword(newPassword));
            try {
                userDao.update(user);
            } catch (SQLException e) {

                //logger.error("密码更新出错",e);
                return Utils.response(false, "修改失败请重试", null);
            }
            return Utils.response(true, "密码修改成功", null);
        } else {
            return Utils.response(false, "密码错误", null);
        }

    }

    public static String logout(Request request, Response response) {
        if(Utils.checkAuth(request) == 0) {
            return Utils.response(false, "非法操作", null);
        } else {
            request.session().removeAttribute("is_login");
            request.session().removeAttribute("user");

            response.removeCookie("user_id");
            return Utils.response(true, "注销成功", null);
        }

    }

}
