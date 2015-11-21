package atmss;

import hwEmulators.MBox;
import hwEmulators.Msg;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Timer.
 */
public class Timer extends Thread {
	
	/** The timer id. */
	private static int timerId = 0;
	
	/** The running. */
	private volatile boolean running = true;
	
	/** The elapsed time. */
	private long elapsedTime = 0L;
	
	/** The duration. */
	private long duration;
	
	/** The _mbox. */
	private MBox _mbox;
	
	/** The my id. */
	private int myId;

	/**
	 * Instantiates a new timer.
	 */
	private Timer() {
		myId = timerId;
	}

	/**
	 * Stop timer.
	 */
	public void stopTimer() {
		this.running = false;
	}

	/**
	 * Gets the timer id.
	 *
	 * @return the timer id
	 */
	public int getTimerId() {
		return myId;
	}

	/**
	 * Inits the timer.
	 *
	 * @param DurationSecond the duration second
	 * @param MBox the m box
	 */
	public void initTimer(long DurationSecond, MBox MBox) {
		this.duration = DurationSecond * 1000;
		this._mbox = MBox;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		while (this.running && elapsedTime < duration) {
			elapsedTime = (new Date()).getTime() - startTime;
		}
		if (this.running)
			this._mbox.send(new Msg("Timer:" + timerId, 999, timerId + ":" + "Time out!"));
	}

	/**
	 * Gets the timer.
	 *
	 * @return the timer
	 */
	public static Timer getTimer() {
		timerId++;
		return new Timer();
	}

}
