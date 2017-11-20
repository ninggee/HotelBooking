package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.UserModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

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
    public static boolean addUser(Request request, Response response) {
        String username = request.queryParams("username");
        String email = request.queryParams("email");


        UserModel userModel;
        userModel = new UserModel();
        userModel.setName(username);
        userModel.setPassword(email);
        userModel.setIs_admin(false);

        try {
            userDao.create(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;


    }



}
