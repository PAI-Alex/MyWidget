package com.example.mywidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyWidget extends AppWidgetProvider {
    InfoRepository repository;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH");
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
        repository = InfoRepository.createInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Date date = repository.getDate();
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        System.out.println(now);
        String result = "Нажми на меня";
        if(now.getTime() < date.getTime()) {
            long differenceBetweenDates = date.getTime() - now.getTime();
            long differenceSeconds = (differenceBetweenDates / 1000) % 60;
            long differenceMinutes = (differenceBetweenDates / (1000*60)) %60;
            long differenceHours = (differenceBetweenDates / (1000*60*60)) % 24;
            long differenceDays = (differenceBetweenDates / (1000*60*60*24));
            result = differenceDays + " дней " + differenceHours + " часов " + differenceMinutes + " минут "
                    + differenceSeconds + "  секунд" ;
        }
        views.setTextViewText(R.id.date_text, result);
        ComponentName componentName = new ComponentName(context, MyWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button

            views.setOnClickPendingIntent(R.id.date_text, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    static void updateWidget(Context ctx, AppWidgetManager appWidgetManager,
                             int widgetID) {
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(),
                R.layout.widget);
        Intent configIntent = new Intent(ctx, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetID,
                configIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.date_text, pIntent);
    }

}
