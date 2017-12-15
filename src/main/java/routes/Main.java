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

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    public static void main(String[] args) throws SQLException{


        Dao<UserModel,String> userDao = DaoManager.createDao(Model.connectionSource, UserModel.class);


        Main.enableCORS("*", "GET,PUT,POST,DELETE,OPTIONS", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");

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
            delete("/:id", RoomController::deleteById);
            put("/:id", RoomController::updateById);
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