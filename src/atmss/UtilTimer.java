package atmss;

import java.util.Date;

public class UtilTimer {
	private long startTime = System.currentTimeMillis();
	private long elapsedTime = 0L;
	private long duration; // Unit: second

	public UtilTimer(int Duration) {
		this.duration = Duration;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuratioin(long newDuration) {
		this.duration = newDuration;
	}

	public void initTimmer() {
		this.startTime = System.currentTimeMillis();
	}

	public boolean startTimer() {
		initTimmer();
		System.out.println("Start...");
		while (elapsedTime < duration * 1000) {
			elapsedTime = (new Date()).getTime() - startTime;
		}
		System.out.println("Time out");
		return true;
	}

	public static void main(String[] args) {

		UtilTimer t1 = new UtilTimer(5);
		t1.startTimer();
	}

}
