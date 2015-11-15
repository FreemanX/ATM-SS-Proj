/**
 * 
 */
package atmss;

import java.util.LinkedList;

import atmss.bams.*;
import atmss.hardware.*;
import atmss.process.*;
import hwEmulators.*;

/**
 * @author freeman
 *
 */
public class MainController extends Thread {

	private CashDispenserController cashDispenserController = new CashDispenserController();
	private CardReaderController cardReaderController;
	private KeypadController keypadController = new KeypadController();
	private DepositCollectorController depositCollectorController = new DepositCollectorController();
	private AdvicePrinterController advicePrinterController;
	private DisplayController displayController = new DisplayController();
	private EnvelopDispenserController envelopDispenserController = new EnvelopDispenserController();
	private EnquryController enquryController;
	private TransferController transferController;
	private ChangePasswdController changePasswdController;
	private WithDrawController withdrawController;
	private DepositController depositController;
	private BAMSCommunicater serverCommunicater;
	private String[] userAccounts;
	private int timmer;
	private LinkedList<Session> sessionLog;

	// TODO Singleton need to be implemented
	// private static MainController self = new MainController();

	/**
	 * 
	 */
	// TODO Singleton need to be implemented
	// public static MainController getInstance() { return self; }
	public MainController(AdvicePrinter AP, CardReader CR) {
		// TODO Auto-generated constructor stub
		this.advicePrinterController = new AdvicePrinterController(AP);
		this.cardReaderController = new CardReaderController(CR);
	}

	public boolean AutherizePassed() {
		boolean passwdIsRight = false;

		/*
		 * Implement the process here.
		 */

		return passwdIsRight;
	}

	public Double doBAMSCheckBalance(String accountNumber) {
		double balance = 0;

		/*
		 * Implement the process here.
		 */

		return balance;
	}

	public String doBAMSVerifyDestAccount(String desAccountNumber) {
		String verifiedInfo = "False";
		/*
		 * Implement the process here. If the account is OK then verifiedInfo =
		 * "<name>/<desAccountNumber>";
		 */
		return verifiedInfo;
	}

	public boolean doBAMSUpdateBalance(String accountNumber, double amount) {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doBAMSUpdatePasswd(String accountNumber, String newPasswd) {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doBAMSTransfer(String accountNumber, String destAccountNumber, double amount) {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
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
	public boolean doEjectCash(int amount) // Only eject 100, 500, 1000, must be
	// int
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doRetainCash() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	// >>>>>>>>>>>>>>>>>>4 Functions of Deposit collector <<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>5 Functions of Display <<<<<<<<<<<<<<<<<<<
	public boolean doDisplay(String[] displayContent) {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	// >>>>>>>>>>>>>>>>>>6 Functions of Envelop dispenser <<<<<<<<<<<<<<<<<<<
	public boolean doEjectEnvelop() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doEatEnvelop() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	// >>>>>>>>>>>>>>>>>>7 Functions of Function of keypad <<<<<<<<<<<<<<<<<<<

	public String doGetKeyInput() {
		String userInput = "";

		/*
		 * Implement the process here.
		 */

		return userInput;
	}

	@Override
	public void run() {
		while (true) {
			// 1 Wait for card insert
			if (this.cardReaderController.getCardNumber().length() > 0) {
				// TODO 2 Authenticate pin
				// TODO 2.1 Take user input from keypad and update display
				// accordingly
				// TODO 2.2 Sent the pin and account number to server
				// TODO 2.3 Get the authentication result
				// TODO 3 if right pin
				// TODO Display options and wait for user selection
				// TODO Create that process controller and wait for process
				// finishes
				// TODO Display options and wait for user selection
				// TODO If user does not choose eject card
				// TODO Create corresponding process for user
				// TODO Else
				// TODO call cardReader controller to eject card
				// TODO 3 else
				// TODO check if it has been 3 times' error
				// TODO if yes, retain card and display bank contact info
				// TODO else
				// TODO reinput the pin and input pin counter++

			} else {
				continue;
			}
		}
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

	private void handleUnknownExceptions(Exception e) {
		// TODO handles unexpected exceptions
	}

}
