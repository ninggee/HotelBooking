package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.UserModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ReservationController {
    private static Dao<UserModel, String> userDao;

    static {
        try {
            userDao =  DaoManager.createDao(Model.connectionSource, UserModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String addReservation(Request request, Response response) {
        return null;
    }

    public static String queryReservations(Request request, Response response) {
        return "query all";
    }

    public static String queryReservation(Request request, Response response) {
        return "query " + request.params(":id");
    }
}
