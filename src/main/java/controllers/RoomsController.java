package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.RoomsModel;
import models.UserModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.ArrayList;
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
            RoomsModel rm = userDao.queryForId("1");
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
}
