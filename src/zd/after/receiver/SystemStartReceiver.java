package zd.after.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import zd.after.service.AfterWidgetService;

public class SystemStartReceiver extends BroadcastReceiver{

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(intent.setClass(context, AfterWidgetService.class));
	}

}
