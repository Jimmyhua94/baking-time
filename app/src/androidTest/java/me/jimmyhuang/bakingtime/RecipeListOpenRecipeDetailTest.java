package me.jimmyhuang.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeListOpenRecipeDetailTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickItemRecipeList_OpensDetailActivity() {
        // https://guides.codepath.com/android/UI-Testing-with-Espresso#interacting-with-a-recyclerview
        onView(withId(R.id.fragment_recipe_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_detail_ingredient_tv)).check(matches(not(withText(""))));
    }

    @Test
    public void recipeDetail_OpensRecipeStep() {
        // https://guides.codepath.com/android/UI-Testing-with-Espresso#interacting-with-a-recyclerview
        onView(withId(R.id.fragment_recipe_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_detail_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_step_tv)).check(matches(withText("Recipe Introduction")));
    }

    @Test
    public void recipeStep_hasNextStep() {
        // https://guides.codepath.com/android/UI-Testing-with-Espresso#interacting-with-a-recyclerview
        onView(withId(R.id.fragment_recipe_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_detail_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_step_next)).perform(click());
    }

    @Test
    public void recipeDetail_hasPrevStep() {
        // https://guides.codepath.com/android/UI-Testing-with-Espresso#interacting-with-a-recyclerview
        onView(withId(R.id.fragment_recipe_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_detail_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_step_next)).perform(click());
        onView(withId(R.id.recipe_step_prev)).perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
