/**
 *
 */
package atmss;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import atmss.bams.*;
import atmss.hardware.controller.*;
import atmss.process.*;
import hwEmulators.*;

/**
 * @author freeman
 *
 */
public class MainController extends Thread {

	// Processing thread
	// ----------------------------------------------------------------
	class Processor extends Thread {
		SystemCheckThread checker = new SystemCheckThread(advicePrinterController, cardReaderController,
				cashDispenserController, depositCollectorController, displayController, envelopDispenserController,
				keypadController, serverCommunicator);
		private volatile boolean isRunning = true;
		

		public void Processor() {
			// constructor...
		}

		public void run() {
			// thread start...
			checker.start();
			int i = 1;

			// debug test
			while (true) {
				try {
					System.err.println("Processor is running, iteration: " + i);
					i++;
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// -------------------------------------------------------------------------------------

	private CashDispenserController cashDispenserController;
	private CardReaderController cardReaderController;
	private KeypadController keypadController;
	private DepositCollectorController depositCollectorController;
	private AdvicePrinterController advicePrinterController;
	private DisplayController displayController;
	private EnvelopDispenserController envelopDispenserController;
	private EnquryController enquryController;
	private TransferController transferController;
	private ChangePasswdController changePasswdController;
	private WithDrawController withdrawController;
	private DepositController depositController;
	private BAMSCommunicator serverCommunicator;
	private LinkedList<Session> sessionLog;
	private Processor processor;
	private List<Session> sessions = new ArrayList<Session>();
	private ATMSSHandler atmssHandler;
	private MBox mainControllerMBox;
	// TODO Singleton need to be implemented
	// private static MainController self = new MainController();

	/**
	 *
	 */
	// TODO Singleton need to be implemented
	// public static MainController getInstance() { return self; }
	public MainController(AdvicePrinter AP, CardReader CR, CashDispenser CD, DepositCollector depositCollector,
			Display display, EnvelopDispenser envelopDispenser, Keypad KP) {
		this.advicePrinterController = new AdvicePrinterController(AP);
		this.cardReaderController = new CardReaderController(CR);
		this.cashDispenserController = new CashDispenserController(CD);
		this.depositCollectorController = new DepositCollectorController(depositCollector);
		this.displayController = new DisplayController(display);
		this.envelopDispenserController = new EnvelopDispenserController(envelopDispenser);
		this.keypadController = new KeypadController(KP);
		this.atmssHandler = ATMSSHandler.getHandler();
		this.atmssHandler.initHandler(cashDispenserController, cardReaderController, keypadController,
				depositCollectorController, advicePrinterController, displayController, envelopDispenserController,
				serverCommunicator);
		// start Processor
		this.processor = new Processor();
		processor.start();
	}

	public boolean AutherizePassed(String cardNo, String pin) {
		String result = serverCommunicator.login(cardNo, pin);
		if (!result.equalsIgnoreCase("error")) {
			sessions.add(new Session(new Date().getTime(), result, cardNo));
			return true;
		}
		return false;
	}

	private Session getLastSession() {
		return sessions.get(sessions.size() - 1);
	}

	private void handleUserRequest() {

	}

	private boolean createSession() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	private boolean killSession() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	private boolean addSessionToLog(Session currentSession) {
		boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */
		if (currentSession != null) // Session constrains
			isSuccess = sessionLog.add(currentSession);

		return isSuccess;
	}

	private boolean initAll() // Initiate all for serving next guest
	{
		boolean isSuccess = false;
		this.cardReaderController.initCR();
		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

}
