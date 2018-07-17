package me.jimmyhuang.bakingtime.utility;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import me.jimmyhuang.bakingtime.DetailActivity;
import me.jimmyhuang.bakingtime.MainActivity;
import me.jimmyhuang.bakingtime.R;
import me.jimmyhuang.bakingtime.adapter.RecipeListAdapter;
import me.jimmyhuang.bakingtime.model.Recipe;

// https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/
public class RecipeWidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<CharSequence> mWidgetItemList = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private Recipe mRecipe;

    public RecipeWidgetListProvider(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        CharSequence recipe_content = MainActivity.loadTitlePref(context, mAppWidgetId);
        mRecipe = MainActivity.loadObjectPref(context, mAppWidgetId);
        if (recipe_content.length() != 0) {
            mWidgetItemList.add(recipe_content);
        } else {
            mWidgetItemList.add(context.getResources().getString(R.string.appwidget_text));
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews( mContext.getPackageName(),
                R.layout.recipe_widget_list_item);
        CharSequence content = mWidgetItemList.get(position);
        remoteView.setTextViewText(R.id.recipe_widget_list_item_tv, content);

//        Intent intent = new Intent(mContext, DetailActivity.class);
//        intent.putExtra(RecipeListAdapter.RECIPE_INTENT_EXTRA, mRecipe);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        remoteView.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);
        Bundle extras = new Bundle();
        extras.putParcelable(RecipeListAdapter.RECIPE_INTENT_EXTRA, mRecipe);
        Intent intent = new Intent();
        intent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.recipe_widget_list_item_container, intent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
