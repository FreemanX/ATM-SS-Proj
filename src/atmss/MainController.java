/**
 *
 */
package atmss;

import java.util.LinkedList;

import atmss.bams.*;
import atmss.hardware.controller.*;
import atmss.process.*;
import hwEmulators.*;

/**
 * @author freeman
 *
 */
public class MainController extends Thread {

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
	}

	// >>>>>>>>>>>>>>>>>>>>0. functions of BAMS Handler<<<<<<<<<<<<<<<<<<<
	public boolean AutherizePassed(String cardNo, String pin) {
		if (!serverCommunicator.login(cardNo, pin).equalsIgnoreCase("error")) { // not
																				// "error"
			return true;
		}
		return false;
	}

	// get input params from Session
	public Double doBAMSCheckBalance(String cardNo, String accNo, String cred) {
		return serverCommunicator.enquiry(cardNo, accNo, cred);
	}

	/*
	 * dest account verification is done within transfer public String
	 * doBAMSVerifyDestAccount(String desAccountNumber) { String verifiedInfo =
	 * "False";
	 * 
	 * return verifiedInfo; }
	 */

	public boolean doBAMSUpdateBalance(String cardNo, String accNumber, String cred, int amount) {
		long resultAmount = 0;

		if (amount > 0) // deposit
			resultAmount = Math.round(serverCommunicator.deposit(cardNo, accNumber, cred, amount));

		if (amount < 0) // withdraw
			resultAmount = Math
					.round(serverCommunicator.cashWithdraw(cardNo, accNumber, cred, String.valueOf(amount * -1)));

		if (resultAmount != 0 && resultAmount == Math.abs(amount))
			return true;

		return false;
	}

	public boolean doBAMSUpdatePasswd(String cardNo, String cred, String newPin) {
		if (serverCommunicator.changePin(cardNo, cred, newPin) == 1) {
			return true;
		}
		return false;
	}

	public boolean doBAMSTransfer(String cardNo, String toAccNo, String destAccNo, String cred, double amount) {
		double resultAmount = serverCommunicator.transfer(cardNo, cred, destAccNo, toAccNo, String.valueOf(amount));

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

	public boolean doPrepareCollectEnvelop() {
		try {
			return depositCollectorController.prepareCollection();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	public boolean doCollectEnvelop() {
		try {
			return depositCollectorController.collectEnvelop();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	public boolean doTimeoutRejectEnvelop() {
		try {
			return depositCollectorController.collectTimeout();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

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
				// TODO Call display function to clear screen
				inputPasswd = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				// TODO Call display function to clear screen
				inputPasswd = "";
			} else if (currentInput.equals("ENTER") && inputPasswd.length() == lengthLimit) {
				break;
			} else if (inputPasswd.length() == lengthLimit) {
				continue;
			} else {
				// TODO Call display function to append a *
				inputPasswd += currentInput;
			}
		}

		return inputPasswd;
	}

	public String doKPGetMoneyAmount(long Duration) {
		String inputAccountNum = "";
		int lengthLimit = 8; // Max 999,000 per day

		boolean inputDot = false;

		return inputAccountNum;
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
				inputAccountNum = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				// TODO Call display function to clear screen
				inputAccountNum = "";
			} else if (currentInput.equals("ENTER") && inputAccountNum.length() == lengthLimit) {
				break;
			} else if (inputAccountNum.length() == lengthLimit) {
				continue;
			} else {
				// TODO Call display function to append a the current input
				inputAccountNum += currentInput;
			}
		}
		return inputAccountNum;
	}

	// >>>>>>>>>>>>>>>>>>> End of functions <<<<<<<<<<<<<<<<<<

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
		e.printStackTrace();
	}

}
