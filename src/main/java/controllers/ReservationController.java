package controllers;

import Utils.Utils;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.ReservationModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ReservationController {
    private static Dao<ReservationModel, String> reservationDao;
    private static DateFormat dateFormat;

    static {
        dateFormat = Utils.getDateFormat();
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
            userId = Integer.parseInt(request.queryParams("visitor_id"));
            startDate = dateFormat.parse(request.queryParams("start_date"));
            endDate = dateFormat.parse(request.queryParams("end_date"));

            ReservationModel reservationModel = new ReservationModel(roomId, userId, startDate, endDate);
            int result = reservationDao.create(reservationModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse room_id and visitor_id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to add from database.", null);
        } catch (ParseException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse start_date and end_date to Date.", null);
        }
    }

    public static String deleteReservation(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setId(id);
            int result = reservationDao.delete(reservationModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to delete from database.", null);
        }
    }

    public static String updateReservation(Request request, Response response) {
        int id;
        int roomId;
        int userId;
        Date startDate = null;
        Date endDate = null;

        try {
            id = Integer.parseInt(request.queryParams("id"));
            roomId = Integer.parseInt(request.queryParams("room_id"));
            userId = Integer.parseInt(request.queryParams("visitor_id"));
            startDate = dateFormat.parse(request.queryParams("start_date"));
            endDate = dateFormat.parse(request.queryParams("end_date"));
            ReservationModel reservationModel = new ReservationModel(id, roomId, userId, startDate, endDate);
            int result = reservationDao.update(reservationModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id, room_id and visitor_id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to update from database.", null);
        } catch (ParseException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse start_date and end_date to Date.", null);
        }
    }

    public static String queryReservations(Request request, Response response) {
        try {
            List<ReservationModel> reservationModelList = reservationDao.queryForAll();
            return Utils.response(true, null, reservationModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to query from database.", null);
        }
    }

    public static String queryReservation(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            ReservationModel reservationModel = reservationDao.queryForId(String.valueOf(id));
            if (reservationModel == null) {
                return Utils.response(false, "no reservation found.", null);
            }
            else {
                return Utils.response(true, null, reservationModel);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to query from database.", null);
        }
    }
}
