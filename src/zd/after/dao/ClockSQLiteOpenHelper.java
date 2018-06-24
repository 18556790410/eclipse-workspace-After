package zd.after.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClockSQLiteOpenHelper extends SQLiteOpenHelper{

	public ClockSQLiteOpenHelper(Context context){
		super(context, "Clocks.db", null, 5);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table clock("
						+"id integer primary key autoincrement,"
						+"ringTime varchar(30),"
						+"freq int default 1,"
						+"vibrate int default 1 check(vibrate = 0 or vibrate = 1),"
						+"sound varchar(50),"
						+"introduction varchar(40),"
						+"state int default 1 check(state = 0 or state = 1)"
					+")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
