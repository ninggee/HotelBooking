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
import java.util.Date;
import java.util.List;

public class ReservationController {
    private static Dao<ReservationModel, String> reservationDao;
    private static DateFormat dateFormat;
    private static Gson gson;

    static {
        dateFormat = Utils.getDateFormat();
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
            userId = Integer.parseInt(request.queryParams("visitor_id"));
            startDate = dateFormat.parse(request.queryParams("start_date"));
            endDate = dateFormat.parse(request.queryParams("end_date"));

            ReservationModel reservationModel = new ReservationModel(roomId, userId, startDate, endDate);
            reservationDao.create(reservationModel);
            return Utils.response(true, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.response(false, e.getMessage(), null);
        }
    }

    public static String deleteReservation(Request request, Response response) {
        try {
            int id = Integer.parseInt(request.params(":id"));
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setId(id);
            int result = reservationDao.delete(reservationModel);
            return Utils.response(true, null, result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Utils.response(false, e.getMessage(), null);
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
        }
        catch (Exception e) {
            e.printStackTrace();
            return Utils.response(false, e.getMessage(), null);
        }
    }

    public static String queryReservations(Request request, Response response) {
        try {
            List<ReservationModel> reservationModelList = reservationDao.queryForAll();
            return Utils.response(true, null, reservationModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, e.getMessage(), null);
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
        }
        catch (Exception e) {
            e.printStackTrace();
            return Utils.response(false, e.getMessage(), null);
        }
    }
}
