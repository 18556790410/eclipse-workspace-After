package zd.after.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast toast = null;
	
	public static void showToast(Context context,String message){
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}else{
			toast.setText(message);
		}
		toast.show();
	}

}
