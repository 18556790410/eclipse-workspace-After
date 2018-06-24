package zd.after.util;

import java.util.List;

import android.content.Context;
import zd.after.model.Clock;

public class AlarmOpenThread extends Thread{

	private Context context;
	private List<Clock> clocks;
	
	public AlarmOpenThread(Context context,List<Clock> clocks) {
		this.context = context;
		this.clocks = clocks;
	}
	
	@Override
	public void run() {
		for(final Clock clock : clocks){
			if (0 != clock.getState()) {
				AlarmUtil alarmUtil = new AlarmUtil(context);
				alarmUtil.closeAlarmServerWithoutTip(clock.getId());
				alarmUtil.openAlarmServerWithoutTip(clock);
			}
		}
	}
}
