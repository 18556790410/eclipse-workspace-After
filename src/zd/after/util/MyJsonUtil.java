package zd.after.util;

import com.google.gson.Gson;

public class MyJsonUtil {

	public synchronized static String getJson(Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
}
