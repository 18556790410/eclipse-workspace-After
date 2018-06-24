package zd.after.dao.impl;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import zd.after.dao.ClockDao;
import zd.after.dao.ClockSQLiteOpenHelper;
import zd.after.model.Clock;

public class ClockDaoImpl implements ClockDao{
	
	private static final String DEFAULTSOUND = "Sakura Tears.mp3";

	private Context context;
	
	public ClockDaoImpl(Context context){
		this.context = context;
	}
	
	@Override
	public List<Clock> getClocks() {
		String sql = "select * from clock order by state desc,ringTime asc";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		List<Clock> clocks = new ArrayList<Clock>();
		Clock clock = null;
		while(cursor.moveToNext()){
			clock = getClockFromCursor(cursor);
			clocks.add(clock);
		}
		cursor.close();
		db.close();
		return clocks;
	}
	
	@Override
	public Clock getLatestClock() {
		String sql = "select * from clock where state = 1 order by ringTime asc limit 0,1";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
	 	SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		Clock clock = null;
		if (cursor.moveToNext()) {
			clock = getClockFromCursor(cursor);
		}
		cursor.close();
		db.close();
		return clock;
	}

	@Override
	public Clock getClockById(int id) {
		String sql = "select * from clock where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[]{""+id});
		Clock clock = null;
		while(cursor.moveToNext()){
			clock = getClockFromCursor(cursor);
		}
		cursor.close();
		db.close();
		return clock;
	}

	@Override
	public Clock add(String ringTime) {
		String sql = "insert into clock(ringTime,sound)values(?,?)";
		String sql2 = "select last_insert_rowid() from clock";
		String sql3 = "select * from clock where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Clock clock = null;
		Cursor cursor = null;
		db.beginTransaction();
		try {
			db.execSQL(sql, new Object[]{ringTime,DEFAULTSOUND});
			cursor = db.rawQuery(sql2, null);
			cursor.moveToNext();
			int id = cursor.getInt(0);
			cursor = db.rawQuery(sql3, new String[]{""+id});
			cursor.moveToNext();
			clock = getClockFromCursor(cursor);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return clock;
	}
	
	

	@Override
	public void updateRingTime(int id, String ringTime) {
		String sql = "update clock set ringTime = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{ringTime,id});
		db.close();
	}

	@Override
	public void updateFreq(int id, int freq) {
		String sql = "update clock set freq = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{freq,id});
		db.close();
	}

	@Override
	public void updateVibrate(int id, int vibrate) {
		String sql = "update clock set vibrate = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{vibrate,id});
		db.close();
	}

	@Override
	public void updateSound(int id, String url) {
		String sql = "update clock set sound = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{url,id});
		db.close();
	}

	@Override
	public void updateIntroduction(int id, String introduction) {
		String sql = "update clock set introduction = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{introduction,id});
		db.close();
	}

	@Override
	public void updateState(int id, int state) {
		String sql = "update clock set state = ? where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{state,id});
		db.close();
	}
	
	@Override
	public void delete(int id) {
		String sql = "delete from clock where id = ?";
		SQLiteOpenHelper helper = new ClockSQLiteOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new Object[]{id});
		db.close();
	}

	@Override
	public Clock getClockFromCursor(Cursor cursor) {
		Clock clock = new Clock();
		clock.setId(cursor.getInt(cursor.getColumnIndex("id")));
		clock.setRingTime(cursor.getString(cursor.getColumnIndex("ringTime")));
		clock.setFreq(cursor.getInt(cursor.getColumnIndex("freq")));
		clock.setSound(cursor.getString(cursor.getColumnIndex("sound")));
		clock.setVibrate(cursor.getInt(cursor.getColumnIndex("vibrate")));
		clock.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
		clock.setState(cursor.getInt(cursor.getColumnIndex("state")));
		return clock;
	}

}
