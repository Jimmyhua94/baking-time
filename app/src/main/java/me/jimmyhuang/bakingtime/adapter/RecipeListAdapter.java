package me.jimmyhuang.bakingtime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.jimmyhuang.bakingtime.DetailActivity;
import me.jimmyhuang.bakingtime.MainActivity;
import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.model.Ingredient;
import me.jimmyhuang.bakingtime.model.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    public static final String RECIPE_INTENT_EXTRA = "RecipeObject";

    private MainActivity.WidgetCallback mWidgetCallback;

    private RecipeClickListener mOnClickListener = new RecipeClickListener();

    private List<Recipe> mRecipes;

    private RecyclerView mRecyclerView;

    private Context mContext;

    public RecipeListAdapter(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    public RecipeListAdapter(List<Recipe> recipes, MainActivity.WidgetCallback callback) {
        mRecipes = recipes;
        mWidgetCallback = callback;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView rv) {
        super.onAttachedToRecyclerView(rv);

        mRecyclerView = rv;

        mContext = rv.getContext();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.adapter_recipe_card, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView mRecipeTextView;

        private RecipeViewHolder(View itemView) {
            super(itemView);

            mRecipeTextView = itemView.findViewById(R.id.recipe_list_name_tv);
            itemView.setOnClickListener(mOnClickListener);
        }

        private void bind(int position) {
            Recipe recipe = mRecipes.get(position);
            if (recipe != null) {
                String recipeName = recipe.getName();
                mRecipeTextView.setText(recipeName);
            }
        }
    }

    private class RecipeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = mRecyclerView.getChildLayoutPosition(v);
            Recipe recipe = mRecipes.get(position);

            if (mWidgetCallback == null) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(RECIPE_INTENT_EXTRA, recipe);
                mContext.startActivity(intent);
            } else {
                List<Ingredient> ingredients = recipe.getIngredients();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < ingredients.size(); i++) {
                    stringBuilder.append("• " + ingredients.get(i).getString() + "\n");
                }
                mWidgetCallback.setWidget(recipe, stringBuilder.toString());
            }
        }
    }
}
