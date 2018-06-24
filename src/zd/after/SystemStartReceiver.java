package zd.after;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemStartReceiver extends BroadcastReceiver{

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(intent.setClass(context, AfterWidgetService.class));
	}

}
