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
public class MainController {

	// Processing thread
	// ----------------------------------------------------------------
	class Processor extends Thread implements CheckerListener {
		SystemCheckThread checker = new SystemCheckThread(this);

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

		@Override
		public void componentStatusNotify(String componentName, int status, String description) {
			System.err.println(componentName + ">> status: " + status + ", desc: " + description);
		}
	}

	interface CheckerListener {
		public void componentStatusNotify(String componentName, int status, String description);
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
	private String[] userAccounts;
	private int timmer;
	private LinkedList<Session> sessionLog;
	private Processor processor;
	private List<Session> sessions = new ArrayList<Session>();

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

		// start Processor
		this.processor = new Processor();
		processor.start();
	}

	private Session getLastSession() {
		return sessions.get(sessions.size() - 1);
	}

	// >>>>>>>>>>>>>>>>>>>>0. functions of BAMS Handler<<<<<<<<<<<<<<<<<<<
	public boolean AutherizePassed(String cardNo, String pin) {
		String result = serverCommunicator.login(cardNo, pin);
		if (!result.equalsIgnoreCase("error")) {
			sessions.add(new Session(new Date().getTime(), result, cardNo));
			return true;
		}
		return false;
	}

	// get input params from Session
	public double doBAMSCheckBalance(String accNo) {
		return serverCommunicator.enquiry(getLastSession().getCardNo(), accNo, getLastSession().getCred());
	}

	/*
	 * dest account verification is done within transfer public String
	 * doBAMSVerifyDestAccount(String desAccountNumber) { String verifiedInfo =
	 * "False";
	 *
	 * return verifiedInfo; }
	 */

	public boolean doBAMSUpdateBalance(String accNumber, double amount) {
		long resultAmount = 0;

		if (amount > 0) // deposit
			resultAmount = Math.round(
					serverCommunicator.deposit(getLastSession().getCardNo(), accNumber, getLastSession().getCred(), 0));

		if (amount < 0) // withdraw
			resultAmount = Math.round(serverCommunicator.cashWithdraw(getLastSession().getCardNo(), accNumber,
					getLastSession().getCred(), String.valueOf(amount * -1)));

		if (resultAmount != 0 && resultAmount == Math.abs(amount))
			return true;

		return false;
	}

	public boolean doBAMSUpdatePasswd(String newPin) {
		if (serverCommunicator.changePin(getLastSession().getCardNo(), getLastSession().getCred(), newPin) == 1) {
			return true;
		}
		return false;
	}

	public boolean doBAMSTransfer(String toAccNo, String destAccNo, double amount) {
		double resultAmount = serverCommunicator.transfer(getLastSession().getCardNo(), getLastSession().getCred(),
				destAccNo, toAccNo, String.valueOf(amount));

		if (resultAmount == amount)
			return true;
		return false;
	}

	// >>>>>>>>>>>>>>>>>>1 Functions of advice printer <<<<<<<<<<<<<<<<<<<
	public boolean doAPPrintAdvice(LinkedList<Operation> operations) {
		try {
			return advicePrinterController.printOperations(operations);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	public boolean doAPPrintStrArray(String[] toPrint) {
		try {
			return this.advicePrinterController.printStrArray(toPrint);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	public int doAPCheckInventory() {
		try {
			return this.advicePrinterController.checkInventory();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with AP
		}
	}

	public int doAPGetStatus() {
		try {
			return this.advicePrinterController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with AP
		}
	}

	// >>>>>>>>>>>>>>>>>>2 Functions of card reader <<<<<<<<<<<<<<<<<<<

	public String doCRGetCardNumebr() {
		return this.cardReaderController.getCardNumber();
	}

	public int doCRGetstatus() {
		try {
			return this.cardReaderController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with CR
		}
	}

	// >>>>>>>>>>>>>>>>>>3 Functions of Cash dispenser <<<<<<<<<<<<<<<<<<<

	/*
	 * doCDEjectCash:
	 *
	 * @param ejectPlan integer array in size of 3 ejectPlan[0]: the number of
	 * 100 notes you want to eject ejectPlan[1]: the number of 500 notes you
	 * want to eject ejectPlan[2]: the number of 1000 notes you want to eject
	 */
	public boolean doCDEjectCash(int[] ejectPlan) {
		try {
			return this.cashDispenserController.ejectCash(ejectPlan);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	public boolean doCDRetainCash() {
		try {
			return this.cashDispenserController.retainCash();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	/*
	 * doCDEjectCash:
	 *
	 * @return cashInventry integer array in size of 3 cashInventry[0]: the
	 * number of 100 cashInventry[1]: the number of 500 cashInventry[2]: the
	 * number of 1000
	 */
	public int[] doCDCheckCashInventory() {
		try {
			return this.cashDispenserController.checkCashInvetory();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	public int doCDGetStatus() {
		try {
			return this.cashDispenserController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1;
		}
	}

	// >>>>>>>>>>>>>>>>>>4 Functions of Deposit collector <<<<<<<<<<<<<<<<<<<

	public boolean doDCCollectEnvelop(int timeout) {
		try {
			return depositCollectorController.collectEnvelop(timeout);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	// >>>>>>>>>>>>>>>>>>5 Functions of Display <<<<<<<<<<<<<<<<<<<
	public boolean doDisDisplayUpper(String[] lines) {
		try {
			return displayController.displayUpper(lines);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	public boolean doDisAppendUpper(String[] lines) {
		try {
			return displayController.appendUpper(lines);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisAppendUpper(String line) {
		try {
			return displayController.appendUpper(line);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisDisplayLower(String line) {
		try {
			return displayController.displayLower(line);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisAppendLower(String str) {
		try {
			return displayController.appendLower(str);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisClearAll() {
		try {
			return displayController.clearAll();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisClearUpper() {
		try {
			return displayController.clearUpper();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	public boolean doDisClearLower() {
		try {
			return displayController.clearLower();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	// >>>>>>>>>>>>>>>>>>6 Functions of Envelop dispenser <<<<<<<<<<<<<<<<<<<
	public boolean doEDEjectEnvelop() {
		try {
			return envelopDispenserController.ejectEnvelop();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	public boolean doEDEatEnvelop() {
		int timeout = 10000; // dummy
		try {
			return depositCollectorController.collectEnvelop(timeout);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	// >>>>>>>>>>>>>>>>>>7 Functions of Function of keypad <<<<<<<<<<<<<<<<<<<

	/*
	 * @param Duration time for timeout
	 */
	public String doKPGetSingleInput(long Duration) {
		try {
			String input = this.keypadController.readUserInput(Duration);
			if (input.equals("Time out!"))
				return null;
			else
				return input;
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	/*
	 * @param Duration time for timeout
	 *
	 * @return null means that something wrong during input
	 *
	 * @return "CANCLE" means that user cancel input
	 *
	 * @return inputPasswd the passwd user types in
	 */

	public String doKPGetPasswd(long Duration) {
		String inputPasswd = "";
		int lengthLimit = 6;
		while (true) {
			String currentInput = doKPGetSingleInput(Duration);
			if (currentInput == null) {
				return null;
			}

			if (currentInput.equals("."))
				continue;

			if (currentInput.equals("CANCEL")) {
				doDisClearLower();
				inputPasswd = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				doDisClearLower();
				inputPasswd = "";
			} else if (currentInput.equals("ENTER") && inputPasswd.length() == lengthLimit) {
				break;
			} else if (inputPasswd.length() == lengthLimit) {
				continue;
			} else {
				doDisAppendLower("*");
				inputPasswd += currentInput;
			}
		}

		return inputPasswd;
	}

	public String doKPGetMoneyAmount(long Duration) {
		String moneyAmount = "";
		int lengthLimit = 8; // Max 999,000 per day
		boolean inputDot = false;

		while (true) {
			String currentInput = doKPGetSingleInput(Duration);
			if (currentInput == null) {
				return null;
			}

			if (currentInput.equals(".")) {
				if (!inputDot && moneyAmount.length() > 0) {
					// User can input 3 more digits
					doDisAppendLower(currentInput);
					lengthLimit = moneyAmount.length() + 3;
					inputDot = true;
					moneyAmount += currentInput;
				}
				continue;
			}

			if (currentInput.equals("CANCEL")) {
				doDisClearLower();
				currentInput = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				doDisClearLower();
				currentInput = "";
			} else if (currentInput.equals("ENTER") && currentInput.length() == lengthLimit) {
				break;
			} else if (currentInput.length() == lengthLimit) {
				continue;
			} else {
				doDisAppendLower(currentInput);
				moneyAmount += currentInput;
				continue;
			}
		}

		return moneyAmount;
	}

	public String doKPGetAccountNum(long Duration) {
		String inputAccountNum = "";
		int lengthLimit = 12;
		while (true) {
			String currentInput = doKPGetSingleInput(Duration);
			if (currentInput == null) {
				return null;
			}

			if (currentInput.equals("."))
				continue;

			if (currentInput.equals("CANCEL")) {
				// TODO Call display function to clear screen
				doDisClearLower();
				inputAccountNum = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				// TODO Call display function to clear screen
				doDisClearLower();
				inputAccountNum = "";
			} else if (currentInput.equals("ENTER") && inputAccountNum.length() == lengthLimit) {
				break;
			} else if (inputAccountNum.length() == lengthLimit) {
				continue;
			} else {
				// TODO Call display function to append a the current input
				doDisAppendLower(currentInput);
				inputAccountNum += currentInput;
			}
		}
		return inputAccountNum;
	}

	// >>>>>>>>>>>>>>>>>>> End of functions <<<<<<<<<<<<<<<<<<
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

	private void handleUnknownExceptions(Exception e) {
		// TODO handles unexpected exceptions
		e.printStackTrace();
	}
}
