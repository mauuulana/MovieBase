package com.maul.moviebase.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.maul.moviebase.R;

public class Widget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "com.maul.moviebase.EXTRA_ITEM";
    private static final String TOAST_ACTION = "com.maul.moviebase.TOAST_ACTION";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {

        Intent intent = new Intent(context, StackWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view);
        remoteViews.setRemoteAdapter(R.id.stack_view, intent);
        remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toast = new Intent(context, Widget.class);
        toast.setAction(Widget.TOAST_ACTION);
        toast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, toast, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stack_view, pending);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int appWidget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 1);
                Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
