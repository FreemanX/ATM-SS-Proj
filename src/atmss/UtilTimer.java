package atmss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public abstract class UtilTimer extends Thread {
	private long startTime = System.currentTimeMillis();
	private long elapsedTime = 0L;
	private long duration; // Unit: second
	TimerThread tt;

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

	@Override
	public void run() {
		super.run();
		tt = new TimerThread();
		tt.start();
		if (tt.startTimer() && !isTaskFinished()) {
			System.err.println("Time out");
			System.err.println("Task not finished");
		} else {
			System.out.println("Finish on time");
		}

	}

	private class TimerThread extends Thread {
		boolean continues = true;

		protected void setContinues(boolean isContinue) {
			this.continues = isContinue;
		}

		public boolean startTimer() { // return true if timeout
			initTimmer();
			while (continues && elapsedTime < duration * 1000) {
				elapsedTime = (new Date()).getTime() - startTime;
			}
			return true;
			// try {
			// sleep(duration * 1000);
			// return true;
			// } catch (InterruptedException e) {
			// return false;
			// }

		}
	}

	private boolean isTaskFinished() {
		if (timerTask()) {
			tt.setContinues(false);
			tt.interrupt();
			return true;
		} else {
			return false;
		}

	}

	abstract boolean timerTask();

	/////////////////////////////////// Test /////////////////////////////////
	public static void main(String[] args) {

		UtilTimer tm = new UtilTimer(5) {

			@Override
			public boolean timerTask() {
				// TODO Auto-generated method stub
				return myTask();
			}
		};

		tm.start();

	}

	public boolean myTask() {
		boolean isSuccess = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			if (in.ready())
				isSuccess = true;
		} catch (IOException e) {
			isSuccess = false;
		}
		return isSuccess;
	}
	
}
