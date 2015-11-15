/**
 * 
 */
package atmss;

import java.util.LinkedList;

import atmss.bams.*;
import atmss.hardware.*;
import atmss.process.*;
import hwEmulators.AdvicePrinter;

/**
 * @author freeman
 *
 */
public class MainController extends Thread {

	private CashDispenserController cashDispenserController = new CashDispenserController();
	private CardReaderController cardReaderController = new CardReaderController();
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
	private MainController(AdvicePrinter AP) {
		// TODO Auto-generated constructor stub
		advicePrinterController = new AdvicePrinterController(AP);
	}

	// TODO Singleton need to be implemented
	// public static MainController getInstance() { return self; }

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

	public boolean doDisplay(String[] displayContent) {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doEjectCash(int amount) // Only eject 100, 500, 1000, must be
											// int
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	public boolean doEatCash() {
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

	public boolean doPrintAdvice() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

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
			if (this.cardReaderController.getCard().length() > 0) {
				// TODO 2 Authenticate pin
				// TODO 2.1 Take user input from keypad and update display accordingly
				// TODO 2.2 Sent the pin and account number to server
				// TODO 2.3 Get the authentication result 
				// TODO	3 if right pin
					//TODO Display options and wait for user selection
					//TODO Create that process controller and wait for process finishes
					//TODO Display options and wait for user selecti on
					
				//TODO 3 else
					//TODO check if it has been 3 times' error
						//TODO if yes, retain card and display bank contact info
					//TODO else
						//TODO reinput the pin and input pin counter++
				
			} else {
				return;
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

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

}
