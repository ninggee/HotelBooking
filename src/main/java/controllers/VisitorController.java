package controllers;

import Utils.Utils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import enumerations.ResponseMessage;
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
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String gender = request.queryParams("gender");
            String identityCard = request.queryParams("identity_card");
            VisitorModel visitorModel = new VisitorModel(gender, identityCard);
            int result = visitorDao.create(visitorModel);
            return Utils.response(true, null, result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static String deleteVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setId(id);
            int result = visitorDao.delete(visitorModel);
            return Utils.response(true, null, result);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static String updateVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
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
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }

    public static String queryVisitors(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            String offset = request.queryParams("offset");
            String limit = request.queryParams("limit");
            List<VisitorModel> visitorModelList;

            if (offset != null && limit != null) {
                visitorModelList = visitorDao.queryBuilder()
                        .offset(Long.parseLong(offset)).limit(Long.parseLong(limit)).query();
            }
            else {
                visitorModelList = visitorDao.queryForAll();
            }
            return Utils.response(true, null, visitorModelList);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.LONG_PARSE_FAILED.getDetail("offset", "limit"), null);
        }
    }

    public static String queryVisitor(Request request, Response response) {
        int auth = Utils.checkAuth(request);
        if (auth == 0) {
            return Utils.response(false, ResponseMessage.AUTH_LESS_THAN_1.getDetail(), null);
        }

        try {
            int id = Integer.parseInt(request.params(":id"));
            VisitorModel visitorModel = visitorDao.queryForId(String.valueOf(id));
            return Utils.response(true, null, visitorModel);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.INT_PARSE_FAILED.getDetail("id"), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.response(false, ResponseMessage.DATABASE_ERROR.getDetail(), null);
        }
    }
}
