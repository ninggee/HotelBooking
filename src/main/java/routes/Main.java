package routes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import controllers.ReservationController;
import controllers.RoomsController;
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
        post("/login", UserController::login);

        path("/reservation", () -> {
            get("", ReservationController::queryReservations);
            get("/:id", ReservationController::queryReservation);
            delete("/delete/:id", ReservationController::deleteReservation);
            post("/insert", ReservationController::addReservation);
            put("/update", ReservationController::updateReservation);
        });

        path("/room", () -> {
            get("/", RoomsController::queryAll);
//            get("/:id", ReservationController::queryReservation);
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
        // Using Route
        internalServerError((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 500 handling\"}";
        });

    }
}