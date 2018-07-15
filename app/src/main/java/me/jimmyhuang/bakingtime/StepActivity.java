package me.jimmyhuang.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import me.jimmyhuang.bakingtime.adapter.RecipeDetailAdapter;
import me.jimmyhuang.bakingtime.fragment.RecipeStepFragment;
import me.jimmyhuang.bakingtime.model.Step;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();

        if (intent.hasExtra(RecipeDetailAdapter.STEP_INTENT_EXTRA) && intent.hasExtra(RecipeDetailAdapter.STEPS_INTENT_EXTRA)) {
            int step = intent.getIntExtra(RecipeDetailAdapter.STEP_INTENT_EXTRA, 0);
            List<Step> steps = intent.getParcelableArrayListExtra(RecipeDetailAdapter.STEPS_INTENT_EXTRA);

            if (savedInstanceState == null) {
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

                recipeStepFragment.setStep(step);
                recipeStepFragment.setSteps(steps);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.step_activity_fragment_container, recipeStepFragment)
                        .commit();
            }
        }
    }
}
