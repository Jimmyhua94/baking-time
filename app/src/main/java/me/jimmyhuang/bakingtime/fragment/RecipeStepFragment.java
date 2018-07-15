package me.jimmyhuang.bakingtime.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.model.Step;

public class RecipeStepFragment extends Fragment {

    public static final String STEP = "step";
    public static final String STEPS = "steps";

    private List<Step> mSteps;
    private int mStep;

    private Context mContext;
    private ExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private TextView mTextView;
    private Button mPrevButton;
    private Button mNextButton;

    public RecipeStepFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        mPlayerView = rootView.findViewById(R.id.recipe_step_pv);
        mTextView = rootView.findViewById(R.id.recipe_step_tv);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getInt(STEP);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS);
        }

        if (!getResources().getBoolean(R.bool.isTablet)) {
            mPrevButton = rootView.findViewById(R.id.recipe_step_prev);
            mNextButton = rootView.findViewById(R.id.recipe_step_next);

            mPrevButton.setOnClickListener(new PrevClickListener());
            mNextButton.setOnClickListener(new NextClickListener());
        }

        initializeView();

        return rootView;
    }

    private void initializeView() {
        releasePlayer();

        if (!getResources().getBoolean(R.bool.isTablet)) {
            if (mStep == 0) {
                mPrevButton.setVisibility(View.INVISIBLE);
            } else if (mStep == mSteps.size()-1) {
                mNextButton.setText(getResources().getText(R.string.done));
                mNextButton.setOnClickListener(new DoneClickListener());
            } else {
                mPrevButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mNextButton.setOnClickListener(new NextClickListener());
            }
        }

        if (mTextView != null) {
            mTextView.setText(mSteps.get(mStep).getDescription());
        }

        URL url = mSteps.get(mStep).getVideoUrl();
        if (url != null && !url.toString().isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(mSteps.get(mStep).getVideoUrl());
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    public void setStep(int step) {
        mStep = step;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }

    private void initializePlayer(URL mediaUrl) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            Uri mediaUri = Uri.parse(mediaUrl.toString());

            String userAgent = Util.getUserAgent(mContext, getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(mContext, userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(STEP, mStep);
        currentState.putParcelableArrayList(STEPS,(ArrayList<Step>) mSteps);

    }

    private class PrevClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mStep--;
            initializeView();
        }
    }

    private class NextClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mStep++;
            initializeView();
        }
    }

    private class DoneClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    }
}
