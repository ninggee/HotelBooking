package controllers;

import Utils.Utils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import models.Model;
import models.VisitorModel;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

public class VisitorController {
    private static Dao<VisitorModel, String> visitorDao;

    static {
        try {
            visitorDao =  DaoManager.createDao(Model.connectionSource, VisitorModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String addVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, "Authentication failed: need to login.", null);
        }

        try {
            String gender = request.queryParams("gender");
            String identityCard = request.queryParams("identity_card");
            VisitorModel visitorModel = new VisitorModel(gender, identityCard);
            int result = visitorDao.create(visitorModel);
            return Utils.response(true, null, result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to add from database.", null);
        }
    }

    public static String deleteVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, "Authentication failed: need to login.", null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setId(id);
            int result = visitorDao.delete(visitorModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to delete from database.", null);
        }
    }

    public static String updateVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, "Authentication failed: need to login.", null);
        }

        try {
            int id = Integer.parseInt(request.queryParams("id"));
            String gender = request.queryParams("gender");
            String identityCard = request.queryParams("identity_card");
            VisitorModel visitorModel = new VisitorModel(id, gender, identityCard);
            int result = visitorDao.update(visitorModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to update from database.", null);
        }
    }

    public static String queryVisitors(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, "Authentication failed: need to login.", null);
        }

        try {
            List<VisitorModel> visitorModelList = visitorDao.queryForAll();
            return Utils.response(true, null, visitorModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to query from database.", null);
        }
    }

    public static String queryVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, "Authentication failed: need to login.", null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            VisitorModel visitorModel = visitorDao.queryForId(String.valueOf(id));
            return Utils.response(true, null, visitorModel);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to parse id to int.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, "Failed to query from database.", null);
        }
    }
}
