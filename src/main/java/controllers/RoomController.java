package controllers;

import Utils.Utils;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import enumerations.ResponseMessage;
import models.Model;
import models.RoomModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

public class RoomController {
    private static Dao<RoomModel, String> roomDao;

    static {
        try {
            roomDao =  DaoManager.createDao(Model.connectionSource, RoomModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String queryById(Request request, Response response){

        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            RoomModel roomModel = roomDao.queryForId(String.valueOf(id));
            return Utils.response(true, null, roomModel);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }


    public static String queryAll(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<RoomModel> roomModelList;

            if (offset != null && limit != null) {
                roomModelList = roomDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                roomModelList = roomDao.queryForAll();
            }
            return Utils.response(true, null, roomModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }

    public static String queryNotOrder(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<RoomModel> roomModelList;

            if (offset != null && limit != null) {
                roomModelList = roomDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                roomModelList = roomDao.queryForAll();
            }
            for(int i = 0;i < roomModelList.size(); i++){
                if(roomModelList.get(i).isIs_ordered())
                    roomModelList.remove(i);
            }
            System.out.print(roomModelList);
            return Utils.response(true, null, roomModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }

    public static String addRoom(Request request, Response response) {
        //插入字符有乱码

        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int roomNumber, price;
            String roomType, description;
            boolean isOrdered;

            JsonObject object = (JsonObject)Utils.parseRequest(request);
            roomNumber = object.get("room_number").getAsInt();
            price = object.get("price").getAsInt();
            description = object.get("description").getAsString();
            isOrdered = false;
            roomType = object.get("room_type").getAsString();
            RoomModel roomModel = new RoomModel(roomNumber, roomType, price, description, isOrdered);
            int result = roomDao.create(roomModel);
            return Utils.response(true, null, roomModel);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("room_number", "price"), null);
        }
    }

    public static String deleteById(Request request, Response response) {

        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            int result = roomDao.deleteById(String.valueOf(id));
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static String updateById(Request request, Response response) {

        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params("id"));
//            request.params();
            int roomNumber, price;
            String roomType, description;
            boolean isOrdered;

            JsonObject object = (JsonObject)Utils.parseRequest(request);
            id = object.get("id").getAsInt();
            roomNumber = object.get("room_number").getAsInt();
            price = object.get("price").getAsInt();
            description = object.get("description").getAsString();
            isOrdered = object.get("is_ordered").getAsBoolean();
            roomType = object.get("room_type").getAsString();


            RoomModel roomModel = new RoomModel(id, roomNumber, roomType, price, description, isOrdered);
            int result = roomDao.update(roomModel);
            return Utils.response(true, null, result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id", "room_number", "price"), null);
        }
    }
}
