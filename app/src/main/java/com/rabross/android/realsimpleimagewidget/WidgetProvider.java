package com.rabross.android.realsimpleimagewidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by RabRoss on 28/04/2016.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds)
            new WidgetManager(appWidgetManager, context.getApplicationContext()).restoreWidgetImage(appWidgetId);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
