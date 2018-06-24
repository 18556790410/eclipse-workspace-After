package zd.after.server;

import zd.after.model.Clock;

public interface ClockServer {


	String initList();
	
	String getClocks();
	
	String getLatestClock();
	
	Clock getClockById(int id);
	
	void add(String ringTime);
	
	void updateRingTime(int id,String ringTime);
	
	void updateFreq(int id,int freq);
	
	void updateVibrate(int id,int vibrate);
	
	void updateSound(int id,String sound,String name);
	
	void updateIntroduction(int id,String introduction);
	
	void updateState(int id,int state);
	
	void removeClock(int id);
	
}
