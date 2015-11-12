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

		while (elapsedTime < duration * 1000) {
			elapsedTime = (new Date()).getTime() - startTime;
		}

		return true;
	}

	public static void main(String[] args) {

		UtilTimer t1 = new UtilTimer(5); // Set the duration as 5s
		System.out.println("Start...");
		if (t1.startTimer()) {
			System.out.println("Time out");
		}
	}

}
