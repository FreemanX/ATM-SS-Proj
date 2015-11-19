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
		private volatile boolean EDIsOk = true;
		private volatile boolean DCIsOk = true;
		int i = 1;
		String[] lines;

		public Processor() {
			// constructor...
			lines = new String[10];
		}

		protected void setEDIsOK(boolean b) {
			this.EDIsOk = b;
		}

		protected void setDCIsOK(boolean b) {
			this.DCIsOk = b;
		}

		protected void initProcessor() {
			checker.resumeCheck();
			this.isRunning = true;
			this.i = 1;
		}

		protected void processorPause() {
			this.isRunning = false;
			checker.pauseCheck();
		}

		private void clearLines() {
			for (int j = 0; j < lines.length; j++) {
				lines[j] = "";

			}
		}

		public void run() {
			// thread start...
			checker.start();

			// debug test
			while (true) {
				while (isRunning) {
					try {
						clearLines();
						if (EDIsOk && DCIsOk)
							lines[1] = "Welcome! Full functions";
						else
							lines[1] = "Welcome! Deposit function disabled";

						atmssHandler.doDisDisplayUpper(lines);
						System.out.println(">>>>Processor is running, iteration: " + i);
						i++;
						sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 *
	 */
	// TODO Singleton need to be implemented
	// public static MainController getInstance() { return self; }
	public MainController(AdvicePrinter AP, CardReader CR, CashDispenser CD, DepositCollector depositCollector,
			Display display, EnvelopDispenser envelopDispenser, Keypad KP) {
		this.isRunning = true;
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
		mainControllerMBox = new MBox("MainController");
		this.advicePrinterController.setMainControllerMBox(mainControllerMBox);
		this.cardReaderController.setMainControllerMBox(mainControllerMBox);
		this.cashDispenserController.setMainControllerMBox(mainControllerMBox);
		this.depositCollectorController.setMainControllerMBox(mainControllerMBox);
		this.displayController.setMainControllerMBox(mainControllerMBox);
		this.envelopDispenserController.setMainControllerMBox(mainControllerMBox);
		this.keypadController.setMainControllerMBox(mainControllerMBox);

		// start Processor
		this.processor = new Processor();
		processor.start();
	}

	@Override
	public void run() {
		while (true) {
			while (isRunning) {
				System.out.println(">>>>>>>>>>>>>Main controller is waiting for msg...");
				Msg msg = this.mainControllerMBox.receive();
				System.out.println(">>>>>>>>>>>>>Main controller receives: " + msg);
				String sender = msg.getSender();
				switch (sender) {
				case "AP":
					handleAPException(msg);
					break;
				case "CR":
					handleCRExceptioin(msg);
					break;
				case "CD":
					handleCDExceptioin(msg);
					break;
				case "DC":
					handleDCExceptioin(msg);
					break;
				case "Dis":
					handleDisExceptioin(msg);
					break;
				case "ED":
					handleEDExceptioin(msg);
					break;
				case "KP":
					handleKPExceptioin(msg);
					break;
				default:
					break;
				}
			}

			try {
				waitForRepair();
				sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean AutherizePassed(String cardNo, String pin) {
		String result = serverCommunicator.login(cardNo, pin);
		if (!result.equalsIgnoreCase("error")) {
			sessions.add(new Session(new Date().getTime(), result, cardNo));
			return true;
		}
		return false;
	}

	private void handleAPException(Msg msg) {
		handleFatalExceptions();
	}

	private void handleCRExceptioin(Msg msg) {
		handleFatalExceptions();
	}

	private void handleCDExceptioin(Msg msg) {
		if (msg.getType() == 301) {
			System.err.println("Warning: insufficent amount of cash");
		} else
			handleFatalExceptions();
	}

	private void handleDCExceptioin(Msg msg) {
		// TODO Not fatal, disable deposit function
		if (msg.getType() % 100 == 0)
			processor.setDCIsOK(true);
		else
			processor.setDCIsOK(false);
	}

	private void handleDisExceptioin(Msg msg) {
		handleFatalExceptions();
		// TODO emulate the display shutdown
	}

	private void handleEDExceptioin(Msg msg) {
		if (msg.getType() % 100 == 0)
			processor.setEDIsOK(true);
		else
			processor.setEDIsOK(false);
	}

	private void handleKPExceptioin(Msg msg) {
		handleFatalExceptions();
	}

	private void handleFatalExceptions() {
		this.isRunning = false;
		this.atmssHandler.doDisClearUpper();
		String[] lines = { "", "Out of service!" };
		this.atmssHandler.doDisDisplayUpper(lines);
		this.processor.processorPause();
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

	private void waitForRepair() {
		try {
			if (this.advicePrinterController.updateStatus() && this.cardReaderController.updateStatus()
					&& this.cashDispenserController.updateStatus() && this.depositCollectorController.updateStatus()
					&& this.displayController.updateStatus() && this.envelopDispenserController.updateStatus()
					&& this.keypadController.updateStatus())
				initAll();
		} catch (Exception e) {
		}
	}

	private void initAll() // Initiate all for serving next guest
	{
		this.mainControllerMBox.clearBox();
		this.cardReaderController.initCR();
		this.isRunning = true;
		this.atmssHandler.doDisClearAll();
		processor.initProcessor();
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
	private volatile boolean isRunning;
	// TODO Singleton need to be implemented
	// private static MainController self = new MainController();

}
