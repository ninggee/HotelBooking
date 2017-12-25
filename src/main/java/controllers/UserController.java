package controllers;

import Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sun.org.apache.regexp.internal.RE;
import enumerations.ResponseMessage;
import models.Model;
import models.ReservationModel;
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
        JsonObject input = (JsonObject)Utils.parseRequest(request);

        String username = input.get("name").getAsString();
        String password = input.get("password").getAsString();

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

        if (username == null && password == null) {
            Object querys = UserController.parseRequest(request);

            JsonObject jsonObject = (JsonObject)querys;


            username = String.valueOf(jsonObject.get("username").getAsString());

            password = String.valueOf(jsonObject.get("password").getAsString());
        }
        System.out.println(username);

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
                    params.put("uid", userModel.getId());
                    params.put("isAdmin", userModel.isIs_admin());

                    return Utils.response(result, "登录成功", params);
                } else {
                    return Utils.response(result, "登录失败,用户名或密码错误", null);
                }
            } else if(userModels.size() == 0){
                return Utils.response( false, "登录失败,用户名或密码错误", null);
            } else {
                return Utils.response(false, "登录失败，请联系管理员", null);
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
        JsonObject input = (JsonObject)Utils.parseRequest(request);

        String username = input.get("name").getAsString();
        String password = input.get("password").getAsString();

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

    public static String getAllUser(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<UserModel> reservationModelList;

            if (offset != null && limit != null) {
                reservationModelList = userDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                reservationModelList = userDao.queryForAll();
            }
            return Utils.response(true, null, reservationModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }

    private static  Object parseRequest(Request request) {
        JsonParser parser = new JsonParser();

        String body = request.body();

        if (body.length() > 0) {
            return parser.parse(body);
        } else {
            return "";
        }

    }

    public static String getUserById(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {

            int id = Integer.parseInt(request.params("id"));
            UserModel user = userDao.queryForId(id + "");
            return Utils.response(true, "查詢成功", user);
        } catch (Exception e) {
            return Utils.response(false, "查詢失敗", null);
        }


    }

    public static String updateUserById(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params("id"));

            JsonObject input = (JsonObject)Utils.parseRequest(request);

            String username = input.get("name").getAsString();
            JsonElement passwordElement = input.get("password");
            String password = "";

            if(passwordElement != null) {
                password = passwordElement.getAsString();
            }


            boolean is_admin = input.get("is_admin").getAsBoolean();

            UserModel user = userDao.queryForId(id + "");


            if(!user.getName().equals(username) && UserController.isNameUsed(username)) {
                return Utils.response(false, "新用户名已经被占用", null);
            }

            user.setName(username);
            user.setIs_admin(is_admin);

            if(!password.equals("")) {
                user.setPassword(Utils.calPassword(password));
            }

            userDao.update(user);

            return Utils.response(true,"", user);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Utils.response(false, "", null);
        }
    }


    public static String deleteById(Request request, Response respone) {
        try {
            int id = Integer.parseInt(request.params("id"));

            int result = userDao.deleteById(id + "");

            if(result == 1) {
                return Utils.response(true, "删除成功", null);
            } else {
                return Utils.response(false, "删除失败请重试", null);
            }
        } catch (Exception e) {
            return Utils.response(false, "删除失败请重试", null);
        }
    }

    public static Object getAllUserNumber(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<UserModel> reservationModelList;

            if (offset != null && limit != null) {
                reservationModelList = userDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                reservationModelList = userDao.queryForAll();
            }
            int resInt = reservationModelList.size();
            return Utils.response(true, null, resInt);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }
}
