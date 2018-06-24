package zd.after.util;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import zd.after.model.Clock;

@SuppressLint("NewApi")
public class AlarmUtil {

	private static final String DEFAULTBROADCASTLEVEL = "zd.after.openAlarmServer";
	
	private Context context;
	
	public AlarmUtil(Context context){
		this.context = context;
	}
	
	public boolean joinWhiteList(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			if (null != pm && !pm.isIgnoringBatteryOptimizations(context.getPackageName())) {
				context.startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
				
				Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + context.getPackageName()));
				context.startActivity(intent);
			}
		}
		return false;
	}
	
	public void openAlarmServer(Clock clock){
		Intent intent = new Intent();
		intent.setAction(DEFAULTBROADCASTLEVEL);
		intent.putExtra("id", clock.getId());
		PendingIntent pi = PendingIntent.getBroadcast(context, clock.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		long delay = RingTimeUtil.getCalendar(clock.getRingTime()).getTimeInMillis();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,delay,pi);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			am.setExact(AlarmManager.RTC_WAKEUP, delay, pi);
		} else {
			am.set(AlarmManager.RTC_WAKEUP, delay, pi);
		}
		Date date = RingTimeUtil.getNowDate();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long now = c.getTimeInMillis();
		long d = delay - now;
		String message = "距离下一次响铃还有"+(d/1000/60/60)+"小时"+(((d/1000/60)%60))+"分钟";
		ToastUtil.showToast(context, message);
	}
	
	public void closeAlarmServer(int id){
		Intent intent = new Intent();
		intent.setAction(DEFAULTBROADCASTLEVEL);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
		ToastUtil.showToast(context, "闹铃已关闭");
	}
	
	public void closeAlarmServerWithoutTip(int id){
		Intent intent = new Intent();
		intent.setAction(DEFAULTBROADCASTLEVEL);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
	
	public void openAlarmServerWithoutTip(Clock clock){
		Intent intent = new Intent();
		intent.setAction(DEFAULTBROADCASTLEVEL);
		intent.putExtra("id", clock.getId());
		PendingIntent pi = PendingIntent.getBroadcast(context, clock.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		long delay = RingTimeUtil.getCalendar(clock.getRingTime()).getTimeInMillis();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,delay,pi);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			am.setExact(AlarmManager.RTC_WAKEUP, delay, pi);
		} else {
			am.set(AlarmManager.RTC_WAKEUP, delay, pi);
		}
	}
	
}
