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
            get("", UserController::getAllUser);
            get("/number", UserController::getAllUserNumber);//user个数

            get("/:id", UserController::getUserById);
            post("/update/:id", UserController::updateUserById);
            //add a normal user
            post("/add/normal", UserController::addUser);
            //add an admin user
            post("/add/admin", UserController::addAdmin);
            post("/login", UserController::login);
            post("/logout", UserController::logout);

            delete("/:id", UserController::deleteById);

            //修改用户名和密码
            post("/password", UserController::changePassword);
        });


        path("/reservation", () -> {
            get("", ReservationController::queryReservations);
            get("/number", ReservationController::queryReservationsNumber);//订单个数
            get("/:id", ReservationController::queryReservation);
            delete("/:id", ReservationController::deleteReservation);
            post("/insert", ReservationController::addReservation);
            put("/:id", ReservationController::updateReservation);
        });

        path("/room", () -> {
            get("", RoomController::queryAll);
            get("/number", RoomController::queryAllNumber);//所有房间个数
            get("/order",RoomController::queryNotOrder);//没有订的房间
            get("/order/number",RoomController::queryNotOrderNumber);//没有订房间的数量
            get("/:id", RoomController::queryById);
            post("/insert", RoomController::addRoom);
            delete("/:id", RoomController::deleteById);
            put("/:id", RoomController::updateById);
        });

        path("/visitor", () -> {
            get("", VisitorController::queryVisitors);
            get("/statistics/number", VisitorController::queryVisitorsNumber);//访客个数
            get("/:id", VisitorController::queryVisitor);
            get("/identity_card/:identity_card", VisitorController::queryVisitorByIdentity_card);
            delete("/:id", VisitorController::deleteVisitor);
            post("/insert", VisitorController::addVisitor);
            put("/update", VisitorController::updateVisitor);
        });

        //test path
        path("/heroes", () -> {
            get("", HeroController::getHeroes);
        });

        path("/statistics", () -> {
           get("/week_data", StatisticsController::weekData);
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