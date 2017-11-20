package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.RoomsModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

public class RoomsController {
    private static Dao<RoomsModel, String> userDao;

    static {
        try {
            userDao =  DaoManager.createDao(Model.connectionSource, RoomsModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String queryById(Request request, Response response){
        String result = "";
        try {
            RoomsModel rm = userDao.queryForId(request.params(":id"));
            result = rm.getId() + "," + rm.getRoom_number() + "," + rm.getRoom_type() + "," + rm.getPrice() + "," + rm.getDescription() + "," + rm.isIs_ordered();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String queryAll(Request request, Response response) {
         String result = "";
        try {
            List<RoomsModel> rmList = userDao.queryForAll();
            for (RoomsModel rm : rmList){
                result += rm.getId() + "," + rm.getRoom_number() + "," + rm.getRoom_type() + "," + rm.getPrice() + "," + rm.getDescription() + "," + rm.isIs_ordered();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addRoom(Request request, Response response) {
        //插入字符有乱码
        try {

            int room_number = Integer.parseInt(request.queryParams("room_number"));
            String room_type = request.queryParams("room_type");
            System.out.print("room_type" + room_type);
            int price = Integer.parseInt(request.queryParams("price"));
            String description = request.queryParams("description");
            System.out.print("description" + description);
            boolean is_ordered = Boolean.parseBoolean(request.queryParams("is_ordered"));
            RoomsModel rm = new RoomsModel(room_number, room_type, price, description, is_ordered);
            System.out.print(rm.getPrice() + "-------------");

            userDao.create(rm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteById(Request request, Response response) {

        System.out.print(request.params(":id"));
        try {
            userDao.deleteById(request.params(":id"));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateById(Request request, Response response) {
        try {
            int room_number = Integer.parseInt(request.queryParams("room_number"));
            String room_type = request.queryParams("room_type");
            int price = Integer.parseInt(request.queryParams("price"));
            String description = request.queryParams("description");
            boolean is_ordered = Boolean.parseBoolean(request.queryParams("is_ordered"));
            RoomsModel rm = new RoomsModel(room_number, room_type, price, description, is_ordered);
            rm.setId(Integer.parseInt(request.params(":id")));
            userDao.update(rm);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
