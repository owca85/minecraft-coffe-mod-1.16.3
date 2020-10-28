package owca.coffeemod.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import owca.coffeemod.tileentity.Ingredient;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class NbtUtil {
    private static final Gson GSON = new Gson();

    public static String ingredientsToString(Map<Ingredient, Integer> ingredients){
        return GSON.toJson(ingredients);
    }

    public static Map<Ingredient, Integer> stringToIngredients(String json){
        Map<Ingredient, Integer>  map = GSON.fromJson(json, new TypeToken<HashMap<Ingredient, Integer>>() {
        }.getType());
        return new EnumMap<>(map);
    }

    private NbtUtil(){}
}
