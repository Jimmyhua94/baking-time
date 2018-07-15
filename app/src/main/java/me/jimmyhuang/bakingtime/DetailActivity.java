package me.jimmyhuang.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import me.jimmyhuang.bakingtime.adapter.RecipeListAdapter;
import me.jimmyhuang.bakingtime.fragment.RecipeDetailFragment;
import me.jimmyhuang.bakingtime.fragment.RecipeStepFragment;
import me.jimmyhuang.bakingtime.model.Recipe;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent.hasExtra(RecipeListAdapter.RECIPE_INTENT_EXTRA)) {
            Recipe recipe = intent.getParcelableExtra(RecipeListAdapter.RECIPE_INTENT_EXTRA);

            if (savedInstanceState == null) {
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

                recipeDetailFragment.setRecipe(recipe);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.detail_activity_fragment_container, recipeDetailFragment)
                        .commit();

                if (getResources().getBoolean(R.bool.isTablet)) {
                    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                    recipeStepFragment.setSteps(recipe.getSteps());
                    recipeStepFragment.setStep(0);
                    fragmentManager.beginTransaction()
                            .add(R.id.step_activity_fragment_container, recipeStepFragment)
                            .commit();
                }
            }
        }
    }
}
