package routes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import models.Model;
import models.UserModel;
import spark.Request;
import spark.Response;
import spark.Route;


import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws SQLException{


        Dao<UserModel,String> userDao = DaoManager.createDao(Model.connectionSource, UserModel.class);


//       get("/users", (request, response) -> {
//            String username = request.queryParams("username");
//            String email = request.queryParams("email");
//
//            UserModel user = new UserModel();
//            user.setName(username);
//            user.setPassword(email);
//
//            userDao.create(user);
//
//            response.status(201); // 201 Created
//        });

        get("/", (request, response) -> {
            // Show something

            String username = request.queryParams("username");
            String email = request.queryParams("email");

            UserModel user = new UserModel();
            user.setName(username);
            user.setPassword(email);
            user.setIs_admin(true);

            userDao.create(user);

            response.status(201); // 201 Created

            return true;
        });

        System.out.println("hello world");
    }
}