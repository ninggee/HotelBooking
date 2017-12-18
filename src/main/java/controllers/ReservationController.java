package controllers;

import Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import enumerations.ResponseMessage;
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
    public static Dao<ReservationModel, String> reservationDao;
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
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        int roomId;
        int visitorId;
        Date startDate = null;
        Date endDate = null;

        try {
            JsonObject input = (JsonObject)Utils.parseRequest(request);

            roomId = input.get("room_id").getAsInt();
            visitorId = input.get("visitor_id").getAsInt();
            startDate = dateFormat.parse(input.get("start_date").getAsString());
            endDate = dateFormat.parse(input.get("end_date").getAsString());
//            System.out.print("asdasd" + startDate);


            ReservationModel reservationModel = new ReservationModel(roomId, visitorId, startDate, endDate);
            int result = reservationDao.create(reservationModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("room_id", "visitor_id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (ParseException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATE_PARSE_FAILED.getDetail("start_date", "end_date"), null);
        }
    }

    public static String deleteReservation(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setId(id);
            int result = reservationDao.delete(reservationModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static String updateReservation(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        int id;
        int roomId;
        int userId;
        Date startDate = null;
        Date endDate = null;

        try {
            id = Integer.parseInt(request.params("id"));
            JsonObject object = (JsonObject)Utils.parseRequest(request);
            roomId = object.get("room_id").getAsInt();
            userId = object.get("visitor_id").getAsInt();
            startDate = dateFormat.parse(object.get("start_date").getAsString());
            endDate = dateFormat.parse(object.get("end_date").getAsString());
            ReservationModel reservationModel = new ReservationModel(id, roomId, userId, startDate, endDate);
            int result = reservationDao.update(reservationModel);
            return Utils.response(true, null, reservationModel);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id", "room_id", "visitor_id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (ParseException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATE_PARSE_FAILED.getDetail("start_date", "end_date"), null);
        }
    }

    public static String queryReservations(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<ReservationModel> reservationModelList;

            if (offset != null && limit != null) {
                reservationModelList = reservationDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                reservationModelList = reservationDao.queryForAll();
            }
            return Utils.response(true, null, reservationModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }

    public static String queryReservation(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            ReservationModel reservationModel = reservationDao.queryForId(String.valueOf(id));
            return Utils.response(true, null, reservationModel);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static Object queryReservationsNumber(Request request, Response response) {

        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<ReservationModel> reservationModelList;

            if (offset != null && limit != null) {
                reservationModelList = reservationDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                reservationModelList = reservationDao.queryForAll();
            }
            int resInt = reservationModelList.size();
            return Utils.response(true, null, resInt);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }
}
