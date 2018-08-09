package zd.after.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RingTimeUtil {

	@SuppressLint("SimpleDateFormat") 
	public static String getRightTime(String ringTime,int freq){
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = null;
		try {
			Date now = c.getTime();
			Date targetDate = df.parse(ringTime);
			while (now.after(targetDate)) {
				c.setTime(targetDate);
				c.add(Calendar.DATE, freq);
				targetDate = c.getTime();
			}
			time = df.format(targetDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String waitMs(String ringTime,int freq,int ms){
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = null;
		try {
			Date now = c.getTime();
			Date targetDate = df.parse(ringTime);
			if (now.before(targetDate)) {
				c.setTime(targetDate);
				c.add(Calendar.DAY_OF_MONTH, -freq);
				targetDate = c.getTime();
			}
			while (now.after(targetDate)) {
				c.setTime(targetDate);
				c.add(Calendar.MINUTE, ms);
				targetDate = c.getTime();
			}
			time = df.format(targetDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public static Date getNowDate(){
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static Date getFormatDate(String ringTime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date time = null;
		try {
			time = df.parse(ringTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Calendar getCalendar(String ringTime){
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = df.parse(ringTime);
			c.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
}
