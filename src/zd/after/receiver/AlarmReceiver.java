package zd.after.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import zd.after.activity.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startActivity(intent.setClass(context, AlarmActivity.class));
	}

}
