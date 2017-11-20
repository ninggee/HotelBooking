package controllers;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.ReservationModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationController {
    private static Dao<ReservationModel, String> reservationDao;
    private static SimpleDateFormat dateFormat;
    private static Gson gson;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        gson = new Gson();
        try {
            reservationDao =  DaoManager.createDao(Model.connectionSource, ReservationModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String addReservation(Request request, Response response) {
        int roomId;
        int userId;
        Date startDate = null;
        Date endDate = null;
        try {
            roomId = Integer.parseInt(request.queryParams("room_id"));
            userId = Integer.parseInt(request.queryParams("user_id"));
            startDate = dateFormat.parse(request.queryParams("start_date"));
            endDate = dateFormat.parse(request.queryParams("end_date"));

            ReservationModel reservationModel = new ReservationModel(roomId, userId, startDate, endDate);
            reservationDao.create(reservationModel);
            return gson.toJson(true);
        } catch (ParseException e) {
            e.printStackTrace();
            return gson.toJson(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return gson.toJson(false);
        }
    }

    public static String deleteReservation(Request request, Response response) {
        return null;
    }

    public static String insertReservation(Request request, Response response) {
        return null;
    }

    public static String updateReservation(Request request, Response response) {
        return null;
    }

    public static String queryReservations(Request request, Response response) {
        return "query all";
    }

    public static String queryReservation(Request request, Response response) {
        return "query " + request.params(":id");
    }
}
