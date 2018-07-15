package me.jimmyhuang.bakingtime.utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.jimmyhuang.bakingtime.model.Ingredient;
import me.jimmyhuang.bakingtime.model.Recipe;
import me.jimmyhuang.bakingtime.model.Step;

public class Json {
    public static List<Recipe> parseRecipesJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            List<Recipe> recipesArray = new ArrayList<>();

            JSONArray recipesJsonArray = new JSONArray(json);

            for (int i = 0; i < recipesJsonArray.length(); i++) {
                JSONObject recipeObject = recipesJsonArray.optJSONObject(i);

                String name = recipeObject.optString("name", "");

                JSONArray ingredientsJsonArray = recipeObject.optJSONArray("ingredients");
                List<Ingredient> ingredientsArray = new ArrayList<>();
                for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                    JSONObject ingredientObject = ingredientsJsonArray.optJSONObject(j);

                    int quantity = ingredientObject.optInt("quantity", 0);
                    String measure = ingredientObject.optString("measure", "");
                    String ingredient = ingredientObject.optString("ingredient", "");

                    ingredientsArray.add(new Ingredient(quantity, measure, ingredient));
                }

                JSONArray stepsJsonArray = recipeObject.optJSONArray("steps");
                List<Step> stepsArray = new ArrayList<>();
                for (int j = 0; j < stepsJsonArray.length(); j++) {
                    JSONObject stepObject = stepsJsonArray.optJSONObject(j);

                    int id = stepObject.optInt("id", 0);
                    String shortDescription = stepObject.optString("shortDescription", "");
                    String description = stepObject.optString("description", "");
                    String videoUrl = stepObject.optString("videoURL", "");
                    String thumbnailUrl = stepObject.optString("thumbnailURL", "");

                    Step step = new Step(id);
                    step.setShortDescription(shortDescription);
                    step.setDescription(description);
                    step.setVideoUrl(videoUrl);
                    step.setThumbnailUrl(thumbnailUrl);

                    stepsArray.add(step);
                }

                int servings = recipeObject.optInt("servings", 0);

                String image = recipeObject.optString("image", "");

                Recipe recipe = new Recipe(name);
                recipe.setIngredients(ingredientsArray);
                recipe.setSteps(stepsArray);
                recipe.setServings(servings);
                recipe.setImageUrl(image);

                recipesArray.add(recipe);
            }

            return recipesArray;
        } catch (Exception e){
            return null;
        }
    }
}
