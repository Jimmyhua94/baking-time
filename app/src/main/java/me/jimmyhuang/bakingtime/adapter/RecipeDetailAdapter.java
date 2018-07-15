package me.jimmyhuang.bakingtime.adapter;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.StepActivity;
import me.jimmyhuang.bakingtime.fragment.RecipeStepFragment;
import me.jimmyhuang.bakingtime.model.Step;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.StepViewHolder> {

    public static final String STEPS_INTENT_EXTRA = "Steps";
    public static final String STEP_INTENT_EXTRA = "StepObject";

    private final View.OnClickListener mOnClickListener = new StepClickListener();

    private List<Step> mSteps;

    private RecyclerView mRecyclerView;

    private Context mContext;

    public RecipeDetailAdapter(List<Step> steps) {
        mSteps = steps;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView rv) {
        super.onAttachedToRecyclerView(rv);

        mRecyclerView = rv;

        mContext = rv.getContext();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.adapter_recipe_card, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        private final TextView mRecipeTextView;

        private StepViewHolder(View itemView) {
            super(itemView);

            mRecipeTextView = itemView.findViewById(R.id.recipe_list_name_tv);
            itemView.setOnClickListener(mOnClickListener);
        }

        private void bind(int position) {
            Step step = mSteps.get(position);
            if (step != null) {
                mRecipeTextView.setText(step.getShortDescription());
            }
        }
    }

    private class StepClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = mRecyclerView.getChildLayoutPosition(v);

            if (mContext.getResources().getBoolean(R.bool.isTablet)) {
                // https://stackoverflow.com/questions/30866925/how-to-call-getfragmentmanager-on-recycler-adapter
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setSteps(mSteps);
                recipeStepFragment.setStep(position);
                fragmentManager.beginTransaction()
                        .replace(R.id.step_activity_fragment_container, recipeStepFragment)
                        .commit();
            } else {
                Intent intent = new Intent(mContext, StepActivity.class);
                intent.putParcelableArrayListExtra(STEPS_INTENT_EXTRA,(ArrayList<Step>) mSteps);
                intent.putExtra(STEP_INTENT_EXTRA, position);
                mContext.startActivity(intent);
            }
        }
    }
}
