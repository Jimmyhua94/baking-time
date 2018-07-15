package me.jimmyhuang.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.List;

import static me.jimmyhuang.bakingtime.utility.Network.buildUrl;

public class Recipe implements Parcelable{
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private URL imageUrl;

    public Recipe(String name) {
        this.name = name;
    }

    private Recipe(Parcel in) {
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        imageUrl = buildUrl(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        if (imageUrl != null) {
            dest.writeString(imageUrl.toString());
        } else {
            dest.writeString("");
        }
    }

    public final static Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }

    public URL getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = buildUrl(imageUrl);
    }
}