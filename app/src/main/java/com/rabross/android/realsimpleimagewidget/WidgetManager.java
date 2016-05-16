package com.rabross.android.realsimpleimagewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.Target;

/**
 * Created by RabRoss on 02/05/2016.
 */
class WidgetManager {

    private static final String TAG = WidgetManager.class.getSimpleName();

    private final Context mContext;
    private final SharedPreferences mPreferences;
    private AppWidgetManager mAppWidgetManager;

    public WidgetManager(AppWidgetManager appWidgetManager, Context context) {
        this(context);
        mAppWidgetManager = appWidgetManager;
    }

    public WidgetManager(Context context) {
        mContext = context;
        mAppWidgetManager = AppWidgetManager.getInstance(context);
        mPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void restoreWidgetImage(int widgetId) {
        setWidget(widgetId, restore(widgetId));
    }

    public void setWidget(int widgetId, @NonNull String uri) {
        setWidget(widgetId, Uri.parse(uri));
    }

    public void setWidget(int widgetId, @NonNull Uri uri){
        final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.widget);

        mAppWidgetManager.updateAppWidget(widgetId, remoteViews);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int height;
        int width;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        Log.d(TAG, "Render: "+String.format("%s %s %s %s", widgetId, uri.toString(), width, height));

        Glide.with( mContext ) // safer!
                .load(uri)
                .asBitmap()
                .fitCenter()
                .override(width, height)
                .listener(new RequestListener<Uri, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                        remoteViews.setTextViewText(R.id.widget_text, mContext.getString(R.string.error_failed));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        remoteViews.setViewVisibility(R.id.widget_text, View.GONE);
                        return false;
                    }
                })
                .into(new AppWidgetTarget(mContext, remoteViews, R.id.widget_image, new int[] {widgetId}));

        save(widgetId, uri);

        //Add configure on touch
        Intent configIntent = new Intent(mContext, ConfigurationActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(mContext, widgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_image, configPendingIntent);
    }

    private void save(int widgetId, Uri uri){
        Log.d(TAG, "Save: "+widgetId+" "+uri.toString());
        mPreferences.edit().putString(String.valueOf(widgetId), uri.toString()).apply();
    }

    private Uri restore(int widgetId){
        Log.d(TAG, "Restore: "+widgetId);
        Uri uri = Uri.parse(mPreferences.getString(String.valueOf(widgetId), String.valueOf(AppWidgetManager.INVALID_APPWIDGET_ID)));
        Log.d(TAG, "Restore: "+widgetId+" "+uri.toString());
        return uri;
    }
}