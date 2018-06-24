package zd.after.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.content.Context;
import android.os.Environment;

public class FileUtil {
	
	private static final String ROOTPATH = "zdAfter" +File.separator +"sound";
	private static final String DEFAULTSOUND = "Sakura Tears.mp3";
	private static final String DEFAULTSOUNDPATH = "apps/After/www/sound/Sakura Tears.mp3";
	
	public static synchronized String getSoundPath(String wholeSoundName){
		File sdPath = Environment.getExternalStorageDirectory();
		String path = ROOTPATH + File.separator + wholeSoundName;
		File targetFile = new File(sdPath,path);
		return targetFile.getAbsolutePath();
	}
	
	public static synchronized void init(Context context){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdPath = Environment.getExternalStorageDirectory();
			File file = new File(sdPath, ROOTPATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			checkSound(context,file);
		}
	}
	
	public static synchronized void checkSound(Context context,File file){
		File[] files = file.listFiles();
		if (0 == files.length) {
			try {
				InputStream is = context.getAssets().open(DEFAULTSOUNDPATH);
				BufferedInputStream bis = new BufferedInputStream(is);
				File targetFile = new File(file, DEFAULTSOUND);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
				byte[] buffer = new byte[1024];
				while(-1 != (bis.read(buffer))){
					bos.write(buffer, 0, buffer.length);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized void moveSound(String sound,String name){
		File sdPath = Environment.getExternalStorageDirectory();
		String path = ROOTPATH + File.separator + name;
		File targetFile = new File(sdPath,path);
		if (!targetFile.exists()) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(sound.getBytes("ISO-8859-1"));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
				byte[] buffer = new byte[1024];
				while(-1 != (bis.read(buffer))){
					bos.write(buffer, 0, buffer.length);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
