package routes;

import Utils.Utils;
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

        path("/user", () -> {
            //add a normal user
            post("/normal", UserController::addUser);

            //add an admin user
            post("/admin", UserController::addAdmin);

            post("/login", UserController::login);

            post("/logout", UserController::logout);

            //修改用户名和密码
            post("/password", UserController::changePassword);
        });


        path("/reservation", () -> {
            get("/", ReservationController::queryReservations);
            get("/:id", ReservationController::queryReservation);
            delete("/delete/:id", ReservationController::deleteReservation);
            post("/insert", ReservationController::insertReservation);
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