package zd.after;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import zd.after.model.Clock;
import zd.after.server.ClockServer;
import zd.after.server.impl.ClockServerImpl;
import zd.after.util.FileUtil;
import zd.after.util.RingTimeUtil;

public class AlarmActivity extends Activity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

		Intent intent = getIntent();
		final int id = intent.getIntExtra("id",-1);
		
		final ClockServer server = new ClockServerImpl(this);
		Clock clock = server.getClockById(id);
		
		final String ringTime = clock.getRingTime();
		final int freq = clock.getFreq();
		int vibrate = clock.getVibrate();
		String wholeSoundName = clock.getSound();
		String introduction = clock.getIntroduction();
		//-----------------------------------------唤醒屏幕-------------------------------------
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		final WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.SCREEN_DIM_WAKE_LOCK,"AlarmReceiver");
		wl.acquire();
		
		//-----------------------------------------设置媒体音量-------------------------------------
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		final int currentVolume = getMediaVolume(audioManager);
		setMediaVolume(audioManager, 100);
		//--------------------------------------------震动---------------------------------------
		final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		long[] v = {1000,1000};
		if (0 != vibrate) {
			vibrator.vibrate(v, 0);
		}
		//--------------------------------------------音乐---------------------------------------
		final MediaPlayer player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource(FileUtil.getSoundPath(wholeSoundName));
			player.setLooping(true);
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//-------------------------------------------对话框---------------------------------------
		final Timer t = new Timer();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(ringTime.split(" ")[1]);
		builder.setMessage(introduction);
		builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String rightTime =  RingTimeUtil.getRightTime(ringTime, freq);
				server.updateRingTime(id, rightTime);
				closePlayer(player);
				vibrator.cancel();
				setMediaVolume(audioManager, currentVolume);
				wl.release();
				t.cancel();
				onDestroy();
			}
		});
		builder.setNegativeButton("等5分钟", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String rightTime = RingTimeUtil.waitMs(ringTime, 5);
				server.updateRingTime(id, rightTime);
				closePlayer(player);
				vibrator.cancel();
				setMediaVolume(audioManager, currentVolume);
				wl.release();
				t.cancel();
				onDestroy();
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show();
		
		//-----------------------------------------等待1分钟30秒-----------------------------------
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (dialog.isShowing()) {
					String rightTime = RingTimeUtil.waitMs(ringTime, 5);
					server.updateRingTime(id, rightTime);
					dialog.dismiss();
					closePlayer(player);
					vibrator.cancel();
					setMediaVolume(audioManager, currentVolume);
					wl.release();
					onDestroy();
				}
				
			}
		}, 1000*90);
	}
	
	//关闭音乐播放器
	private void closePlayer(MediaPlayer player){
		player.stop();
		player.release();
		player = null;
	}
	//获取系统媒体音量
	private int getMediaVolume(AudioManager audioManager){
		int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		return currentVolume;
	}
	//设置系统媒体音量
	private void setMediaVolume(AudioManager audioManager,int volume){
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
	}
		

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	
}
