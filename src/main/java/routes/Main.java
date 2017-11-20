package routes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import controllers.ReservationController;
import controllers.UserController;
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


        //home page
        get("/", (req, res) -> {
            res.status(200);
            res.body("welcome to hotel booking backend");

           return res.body();
        });

        //add a normal user
        post("/user", UserController::addUser);

        path("/reservation", () -> {
            get("/", ReservationController::queryReservations);
            get("/:id", ReservationController::queryReservation);
        });

    }
}