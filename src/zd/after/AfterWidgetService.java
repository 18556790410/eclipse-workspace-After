package zd.after;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
import android.widget.RemoteViews;
import zd.after.server.ClockServer;
import zd.after.server.impl.ClockServerImpl;

@SuppressLint("NewApi")
public class AfterWidgetService extends Service{
	
	
	private Timer timer = null;
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	
	@Override
	public void onCreate() {
		super.onCreate();
		ClockServer clockServer = new ClockServerImpl(getApplicationContext());
		clockServer.initList();
		
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				sendAfterWidgetBroadcast();
			}
		},0, 1000);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer = null;
	}
	
	private void sendAfterWidgetBroadcast(){
		RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.after_widget);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		ComponentName cn = new ComponentName(getApplicationContext(), AfterWidgetProvider.class);
		String time = df.format(new Date());
		remoteViews.setTextViewText(R.id.after_widget, time);
		appWidgetManager.updateAppWidget(cn, remoteViews);
	}
	
}
