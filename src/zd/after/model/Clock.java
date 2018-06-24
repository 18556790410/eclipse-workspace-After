package zd.after.model;

public class Clock {
	private int id;
	private String ringTime;
	private int freq;
	private int vibrate;
	private String sound;
	private String introduction;
	private int state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRingTime() {
		return ringTime;
	}
	public void setRingTime(String ringTime) {
		this.ringTime = ringTime;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public int getVibrate() {
		return vibrate;
	}
	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
