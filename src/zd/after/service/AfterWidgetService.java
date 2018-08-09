package zd.after.service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
import android.widget.RemoteViews;
import zd.after.R;
import zd.after.provider.AfterWidgetProvider;
import zd.after.server.ClockServer;
import zd.after.server.impl.ClockServerImpl;

@SuppressLint("NewApi")
public class AfterWidgetService extends Service{
	
	private static ScheduledExecutorService executorService = null;
//	private Timer timer = null;
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	
	private static int next = 0;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		ClockServer clockServer = new ClockServerImpl(getApplicationContext());
		clockServer.initList();
		
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				sendAfterWidgetBroadcast();
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
		
//		timer = new Timer();
//		timer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				sendAfterWidgetBroadcast();
//			}
//		},0, 1000);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		executorService.shutdown();
		super.onDestroy();
	}
	
	private void sendAfterWidgetBroadcast(){
		if (0 == next) {
			next = 1;
			RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.after_widget);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
			ComponentName cn = new ComponentName(getApplicationContext(), AfterWidgetProvider.class);
			String time = df.format(new Date());
			remoteViews.setTextViewText(R.id.after_widget, time);
			appWidgetManager.updateAppWidget(cn, remoteViews);
			next = 0;
		}
	}
	
}
