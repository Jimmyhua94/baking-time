package me.jimmyhuang.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import me.jimmyhuang.bakingtime.IdlingResource.SimpleIdlingResource;
import me.jimmyhuang.bakingtime.fragment.RecipeListFragment;
import me.jimmyhuang.bakingtime.model.Recipe;
import me.jimmyhuang.bakingtime.utility.Network;

import static me.jimmyhuang.bakingtime.utility.Json.parseRecipesJson;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "me.jimmyhuang.bakingtime.RecipeWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_OBJECT_KEY = "recipe_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean isWidget = false;

    private static String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final int RECIPES_LOADER = 20;

    private RecipeListFragment mRecipeListFragment;

    private Context mContext;
    private LoaderManager mLoaderManager;

    private LoaderManager.LoaderCallbacks<String> recipesLoaderListener = new LoaderManager.LoaderCallbacks<String>() {
        @NonNull
        @Override
        public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
            return new AsyncTaskLoader<String>(mContext) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (takeContentChanged()) {
                        forceLoad();
                    }
                }
                @Override
                public String loadInBackground() {
                    try {
                        return Network.getResponseFromHttpUrl(Network.buildUrl(RECIPE_URL));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                protected void onStopLoading() {
                    cancelLoad();
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String data) {
            if (data != null && !data.equals("")) {
                List<Recipe> recipesList = parseRecipesJson(data);
                if (recipesList != null) {
                    mRecipeListFragment.setRecipes(recipesList);
                    mIdlingResource.setIdleState(true);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) {

        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }
    // https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference/34625667
    static void saveObjectPref(Context context, int appWidgetId, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefs.putString(PREF_PREFIX_KEY + PREF_OBJECT_KEY + appWidgetId, json);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }
    static Recipe loadObjectPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String objectValue = prefs.getString(PREF_PREFIX_KEY + PREF_OBJECT_KEY + appWidgetId, null);
        Gson gson = new Gson();
        return gson.fromJson(objectValue, Recipe.class);
    }


    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
    static void deleteObjectPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + PREF_OBJECT_KEY + appWidgetId);
        prefs.apply();
    }

    public interface WidgetCallback{
        void setWidget(Recipe recipe, String data);
    }

    WidgetCallback mWidgetCallback = new WidgetCallback() {
        @Override
        public void setWidget(Recipe recipe, String widgetText) {
            final Context context = MainActivity.this;

            saveTitlePref(context, mAppWidgetId, widgetText);
            saveObjectPref(context, mAppWidgetId, recipe);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
                return;
            }
            isWidget = true;
        }

        mContext = this;

        if (savedInstanceState == null) {
            mRecipeListFragment = new RecipeListFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.main_activity_fragment_container, mRecipeListFragment)
                    .commit();
        } else {
            mRecipeListFragment = (RecipeListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.main_activity_fragment_container);
        }

        if (isWidget) {
            mRecipeListFragment.setWidgetCallback(mWidgetCallback);
        }

        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIdlingResource.setIdleState(false);
        mLoaderManager = getSupportLoaderManager();

        Loader<String> recipesLoader = mLoaderManager.getLoader(RECIPES_LOADER);

        if (recipesLoader == null) {
            mLoaderManager.initLoader(RECIPES_LOADER, null, recipesLoaderListener).onContentChanged();
        } else {
            mLoaderManager.restartLoader(RECIPES_LOADER, null, recipesLoaderListener).onContentChanged();
        }
    }
}
