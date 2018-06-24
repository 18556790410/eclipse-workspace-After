package zd.after.server.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.webkit.JavascriptInterface;
import zd.after.dao.ClockDao;
import zd.after.dao.impl.ClockDaoImpl;
import zd.after.model.Clock;
import zd.after.server.ClockServer;
import zd.after.util.AlarmOpenThread;
import zd.after.util.AlarmUtil;
import zd.after.util.FileUtil;
import zd.after.util.MyJsonUtil;
import zd.after.util.RingTimeUtil;

public class ClockServerImpl implements ClockServer{

	
	private Context context;
	
	public ClockServerImpl(Context context){
		this.context = context;
	}

	@JavascriptInterface
	@Override
	public String initList() {
		ClockDao clockDao = new ClockDaoImpl(context);
		List<Clock> clocks = new ArrayList<Clock>();
		clocks = clockDao.getClocks();
		int isNeed = 0;
		for (Clock clock : clocks) {
			Date ringTime = RingTimeUtil.getFormatDate(clock.getRingTime());
			Date now = RingTimeUtil.getNowDate();
			if (now.after(ringTime)) {
				isNeed = 1;
				String time = RingTimeUtil.getRightTime(clock.getRingTime(), clock.getFreq());
				updateRingTime(clock.getId(), time);
			}else {
				break;
			}
		}
		if (0 != isNeed) {
			clocks = clockDao.getClocks();
		}
		Thread thread = new AlarmOpenThread(context, clocks);
		thread.start();
		return MyJsonUtil.getJson(clocks);
	}

	@Override
	public String getClocks() {
		ClockDao clockDao = new ClockDaoImpl(context);
		return MyJsonUtil.getJson(clockDao.getClocks());
	}

	@JavascriptInterface
	@Override
	public String getLatestClock() {
		ClockDao clockDao = new ClockDaoImpl(context);
		Clock clock = clockDao.getLatestClock();
		return MyJsonUtil.getJson(clock);
	}

	@Override
	public Clock getClockById(int id) {
		ClockDao clockDao = new ClockDaoImpl(context);
		Clock clock = clockDao.getClockById(id);
		return clock;
	}

	@JavascriptInterface
	@Override
	public void add(String ringTime) {
		ClockDao clockDao = new ClockDaoImpl(context);
		Clock clock = clockDao.add(ringTime);
		
		//开启闹铃服务
		if (null != clock) {
			AlarmUtil alarmUtil = new AlarmUtil(context);
			alarmUtil.openAlarmServer(clock);
		}
	}

	@JavascriptInterface
	@Override
	public void updateRingTime(int id, String ringTime) {
		ClockDao clockDao = new ClockDaoImpl(context);
		clockDao.updateRingTime(id, ringTime);
		Clock clock = clockDao.getClockById(id);
		if (0 != clock.getState()) {
			AlarmUtil alarmUtil = new AlarmUtil(context);
			alarmUtil.closeAlarmServer(clock.getId());
			alarmUtil.openAlarmServer(clock);
		}
	}

	@JavascriptInterface
	@Override
	public void updateFreq(int id, int freq) {
		ClockDao clockDao = new ClockDaoImpl(context);
		clockDao.updateFreq(id, freq);
	}

	@JavascriptInterface
	@Override
	public void updateVibrate(int id, int vibrate) {
		ClockDao clockDao = new ClockDaoImpl(context);
		clockDao.updateVibrate(id, vibrate);
	}

	@JavascriptInterface
	@Override
	public void updateSound(int id, final String sound,final String name) {
		ClockDao clockDao = new ClockDaoImpl(context);
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileUtil.moveSound(sound,name);				
			}
		}).start();
		clockDao.updateSound(id, name);
	}
	
	@JavascriptInterface
	@Override
	public void updateIntroduction(int id, String introduction) {
		ClockDao clockDao = new ClockDaoImpl(context);
		clockDao.updateIntroduction(id, introduction);
	}

	@JavascriptInterface
	@Override
	public void updateState(int id, int state) {
		ClockDao clockDao = new ClockDaoImpl(context);
		clockDao.updateState(id, state);
		Clock clock = clockDao.getClockById(id);
		AlarmUtil alarmUtil = new AlarmUtil(context);
		if (0 == state) {
			//关闭闹铃服务
			if (null != clock) {
				alarmUtil.closeAlarmServer(id);
			}
		}else{
			//开启闹铃服务
			if (null != clock) {
				alarmUtil.openAlarmServer(clock);
			}
		}
	}

	@JavascriptInterface
	@Override
	public void removeClock(int id) {
		ClockDao clockDao = new ClockDaoImpl(context);
		Clock clock = clockDao.getClockById(id);
		if (1 == clock.getState()) {
			//关闭闹铃服务
			AlarmUtil alarmUtil = new AlarmUtil(context);
			alarmUtil.closeAlarmServer(id);
		}
		clockDao.delete(id);
	}

}
