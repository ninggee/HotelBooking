package controllers;

import com.google.gson.Gson;
import models.HeroModel;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class HeroController {
    public static String getHeroes(Request request, Response response) {
        List<HeroModel> heroModels = new ArrayList<>();
        heroModels.add(new HeroModel(0, "Zero"));
        heroModels.add(new HeroModel(11, "Mr. Nice"));
        heroModels.add(new HeroModel(12, "Narco"));
        heroModels.add(new HeroModel(13, "Bombasto"));
        heroModels.add(new HeroModel(14, "Celeritas"));
        heroModels.add(new HeroModel(15, "Magneta"));
        heroModels.add(new HeroModel(16, "RubberMan"));
        heroModels.add(new HeroModel(17, "Dynama"));
        heroModels.add(new HeroModel(18, "Dr IQ"));
        heroModels.add(new HeroModel(19, "Magma"));
        heroModels.add(new HeroModel(20, "Tornado"));
        Gson gson = new Gson();
        return gson.toJson(heroModels);
    }
}
