package routes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import controllers.*;
import models.HeroModel;
import models.Model;
import models.UserModel;


import java.sql.SQLException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws SQLException{


        Dao<UserModel,String> userDao = DaoManager.createDao(Model.connectionSource, UserModel.class);

        //home page
        before("*", (req, res) -> res.header("Access-Control-Allow-Origin", "*"));

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
            get("", ReservationController::queryReservations);
            get("/:id", ReservationController::queryReservation);
            delete("/delete/:id", ReservationController::deleteReservation);
            post("/insert", ReservationController::addReservation);
            put("/update", ReservationController::updateReservation);
        });

        path("/room", () -> {
            get("", RoomController::queryAll);
            get("/:id", RoomController::queryById);
            post("/insert", RoomController::addRoom);
            delete("/delete/:id", RoomController::deleteById);
            put("/update", RoomController::updateById);
        });

        path("/visitor", () -> {
            get("", VisitorController::queryVisitors);
            get("/:id", VisitorController::queryVisitor);
            delete("/delete/:id", VisitorController::deleteVisitor);
            post("/insert", VisitorController::addVisitor);
            put("/update", VisitorController::updateVisitor);
        });

        //test path
        path("/heroes", () -> {
            get("", HeroController::getHeroes);
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