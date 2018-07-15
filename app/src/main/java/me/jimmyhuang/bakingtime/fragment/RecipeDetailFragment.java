package me.jimmyhuang.bakingtime.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.adapter.RecipeDetailAdapter;
import me.jimmyhuang.bakingtime.model.Ingredient;
import me.jimmyhuang.bakingtime.model.Recipe;

public class RecipeDetailFragment extends Fragment {

    public static final String RECIPE = "recipe";

    private Recipe mRecipe;

    public RecipeDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflator.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView textView = rootView.findViewById(R.id.fragment_recipe_detail_ingredient_tv);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_recipe_detail_rv);
        recyclerView.setFocusable(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
        }

        RecipeDetailAdapter adapter = new RecipeDetailAdapter(mRecipe.getSteps());
        recyclerView.setAdapter(adapter);

        List<Ingredient> ingredients = mRecipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            textView.append("• " + ingredients.get(i).getString() + "\n");
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE, mRecipe);
    }
}
