package atmss;

import java.util.Date;

import hwEmulators.MBox;
import hwEmulators.Msg;

public class Timer extends Thread {
	private static int timerId = 0;
	private volatile boolean running = true;
	private long elapsedTime = 0L;
	private long duration;
	private MBox _mbox;

	private Timer() {
		// TODO Auto-generated constructor stub
	}

	public void stopTimer() {
		this.running = false;
	}

	public int getTimerId() {
		return timerId;
	}

	public void initTimer(long DurationSecond, MBox MBox) {
		this.duration = DurationSecond * 1000;
		this._mbox = MBox;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		while (this.running && elapsedTime < duration) {
			elapsedTime = (new Date()).getTime() - startTime;
		}

		if (this.running)
			this._mbox.send(new Msg("Timer: " + timerId, 999, "Time out!"));
		timerId ++;
	}

	public static Timer getTimer() {
		return new Timer();
	}

}
