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
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticsController {

    public static String weekData(Request request, Response response) {
        try {
            List<String[]> result = ReservationController.reservationDao.queryRaw("select * from orders  where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(add_time)").getResults();

            int[] finalResult = new int[7];

            String [] weeks = StatisticsController.getWeekArray();
            for (String [] temp : result) {
                int week = StatisticsController.getWeek(weeks, temp[5]);
                finalResult[week]++;

            }
            Map map = new HashMap();
            map.put("week", weeks);
            map.put("data", finalResult);

            List<RoomModel> roomModels = RoomController.roomDao.queryForAll();

            map.put("total", roomModels.size());

            return Utils.response(true, "", map);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Utils.response(false, "查询失败", null);
    }

    private static String[] getWeekArray() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(now);
        cal.add(java.util.Calendar.DATE, - 7); // 向前一周；如果需要向后一周，用正数即可
        String[] result = new String[7];


        for(int i = 1; i <= 7; i++) {
            cal.add(Calendar.DATE, 1);
            result[i - 1] = format.format(cal.getTime());
        }

        return result;
    }

    private static int getWeek(String [] weeks, String date) {
        for(int i = 0; i < weeks.length; i++) {
            if(date.indexOf(weeks[i]) >= 0) {
                return i;
            }
        }

        return -1;
    }
}
