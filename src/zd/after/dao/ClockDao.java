package zd.after.dao;

import java.util.List;

import android.database.Cursor;

import zd.after.model.Clock;

public interface ClockDao {
	
	
	List<Clock> getClocks();
	
	Clock getLatestClock();
	
	Clock getClockById(int id);
	
	Clock add(String ringTime);
	
	void updateRingTime(int id,String ringTime);
	
	void updateFreq(int id,int freq);
	
	void updateVibrate(int id,int vibrate);
	
	void updateSound(int id,String url);
	
	void updateIntroduction(int id,String introduction);
	
	void updateState(int id,int state);
	
	void delete(int id);
	
	Clock getClockFromCursor(Cursor cursor);
}
