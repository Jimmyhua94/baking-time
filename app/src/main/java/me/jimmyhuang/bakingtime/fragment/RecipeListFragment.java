package me.jimmyhuang.bakingtime.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.jimmyhuang.bakingtime.MainActivity;
import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.adapter.RecipeListAdapter;
import me.jimmyhuang.bakingtime.model.Recipe;

public class RecipeListFragment extends Fragment {

    public static final String RECIPES = "recipe_list";

    private List<Recipe> mRecipes = new ArrayList<>();

    private RecipeListAdapter mAdapter;

    private MainActivity.WidgetCallback mWidgetCallback;

    public RecipeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflator.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_recipe_list_rv);

        if (container.getResources().getBoolean(R.bool.isTablet)) {
            final GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(), 3);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
            recyclerView.setLayoutManager(layoutManager);
        }

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES);
        }

        if (mWidgetCallback == null) {
            mAdapter = new RecipeListAdapter(mRecipes);
        } else {
            mAdapter = new RecipeListAdapter(mRecipes, mWidgetCallback);
        }
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
    }

    public void setWidgetCallback(MainActivity.WidgetCallback callback) {
        mWidgetCallback = callback;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(RECIPES,(ArrayList<Recipe>) mRecipes);
    }
}
