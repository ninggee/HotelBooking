package controllers;
import Utils.Utils;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import enumerations.ResponseMessage;
import models.Model;
import models.ReservationModel;
import models.RoomModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsController {

    public static String weekData(Request request, Response response) {
        try {
            List<String[]> result = ReservationController.reservationDao.queryRaw("select * from orders  where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(add_time)").getResults();

            return Utils.response(true, "", result);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Utils.response(false, "查询失败", null);
    }
}
