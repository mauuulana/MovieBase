package com.maul.moviebase.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.maul.moviebase.R;
import com.maul.moviebase.modal.Movie;

import java.util.concurrent.ExecutionException;

import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI;

public class StackRemote implements RemoteViewsService.RemoteViewsFactory {

    int appWidgetId;
    private Context context;
    private Cursor cursor;

    public StackRemote (Context context, Intent intent) {
        this.context = context;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(
                CONTENT_URI, null, null, null, null
        );
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Error");
        }
        return new Movie(cursor);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movies = getItem(position);
        String poster = movies.getPosterPathFav();
        RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .asBitmap()
                    .load(poster)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        remote.setImageViewBitmap(R.id.img_widget, bmp);

        Bundle bundle = new Bundle();
        bundle.putInt(Widget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remote.setOnClickFillInIntent(R.id.img_widget, intent);
        return remote;
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
