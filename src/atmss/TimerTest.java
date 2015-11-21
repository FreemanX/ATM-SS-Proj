package atmss;

import hwEmulators.MBox;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class TimerTest.
 */
public class TimerTest extends Thread {

	/** The my box. */
	private MBox myBox;

	/**
	 * Instantiates a new timer test.
	 */
	public TimerTest() {
		// TODO Auto-generated constructor stub
		myBox = new MBox("TimerTest");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		System.out.println(">>>>>>>>>>>>> Task 1 <<<<<<<<<<<<<<<<<");
		// Get a timer
		Timer _timer1 = Timer.getTimer();
		// Get a task, your task must have a stop method
		TestTask testTask1 = new TestTask(myBox, 5);
		// You must initiate the timer, 1st param: time, 2nd param: your mailBox
		_timer1.initTimer(3, myBox); // Time out in 5 seconds
		// Start task and timer
		testTask1.start();
		_timer1.start();
		Msg receivedMsg1 = myBox.receive();

		if (receivedMsg1.getType() == 999) {
			// If time out stop your task, for details how to stop task refer to
			// the testTask
			testTask1.stopTask();
			System.err.println(">>>>>>>>Time out!");
			System.out.println(receivedMsg1);
		} else {
			// If task finished, stop the timer
			_timer1.stopTimer();
			System.out.println(">>>>>>>>Task finished");
			System.out.println(receivedMsg1);
		}
		/*
		 * If you don't stop your timer or task accordingly you will run into
		 * trouble next time you use timer !!!
		 */

		System.out.println("\n\n>>>>>>>>>>>>> Task 2 <<<<<<<<<<<<<<<<<");
		Timer _timer2 = Timer.getTimer();
		TestTask testTask2 = new TestTask(myBox, 1);

		_timer2.initTimer(3, myBox); // Time out in 5 seconds

		testTask2.start();
		_timer2.start();
		Msg receivedMsg2 = myBox.receive();

		if (receivedMsg2.getType() == 999) {
			testTask1.stopTask();
			System.err.println(">>>>>>>>Time out!");
			System.out.println(receivedMsg2);
		} else {
			_timer1.stopTimer();
			System.out.println(">>>>>>>>Task finished");
			System.out.println(receivedMsg2);
		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		TimerTest t1 = new TimerTest();
		t1.start();
	}

}

class TestTask extends Thread {
	private volatile boolean running = true;
	private static int id = 0;
	private MBox _mbox;
	private long sleepTime;

	public TestTask(MBox MBox, long SleepTime) {
		this._mbox = MBox;
		this.sleepTime = SleepTime * 1000;
	}

	public void stopTask() {
		this.running = false;
	}

	@Override
	public void run() {
		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			System.out.println("I don't want to wake up!");
		}
		if (this.running)
			this._mbox.send(new Msg("Test Task: " + id, 911, "Test task finish!!!"));
		id++;
	}
}