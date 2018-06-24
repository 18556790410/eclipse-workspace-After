package zd.after;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class AfterWidgetProvider extends AppWidgetProvider{
	
	private static final String AWAKENAPP = "zd.after.awakenApp";

	@SuppressLint("NewApi")
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		context.stopService(new Intent(context,AfterWidgetService.class));
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		context.startService(new Intent(context,AfterWidgetService.class));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (AWAKENAPP.equals(intent.getAction())) {
			intent.setClass(context, SDK_WebApp.class);
			context.startActivity(intent);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
		super.onRestored(context, oldWidgetIds, newWidgetIds);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.after_widget);
		Intent intent = new Intent(AWAKENAPP);
		PendingIntent pi = PendingIntent.getBroadcast(context, R.id.after_widget, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.after_widget, pi);
		appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
		
	}

	
}
