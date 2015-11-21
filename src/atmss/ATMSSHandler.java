package atmss;

import atmss.bams.BAMSCommunicator;
import atmss.hardware.controller.*;

import java.util.LinkedList;

/**
 * The Class ATMSSHandler.
 */
public class ATMSSHandler {

	/** The atmss handler. */
	private static ATMSSHandler atmssHandler = null;
	
	/** The cash dispenser controller. */
	private CashDispenserController cashDispenserController;
	
	/** The card reader controller. */
	private CardReaderController cardReaderController;
	
	/** The keypad controller. */
	private KeypadController keypadController;
	
	/** The deposit collector controller. */
	private DepositCollectorController depositCollectorController;
	
	/** The advice printer controller. */
	private AdvicePrinterController advicePrinterController;
	
	/** The display controller. */
	private DisplayController displayController;
	
	/** The envelop dispenser controller. */
	private EnvelopDispenserController envelopDispenserController;
	
	/** The server communicator. */
	private BAMSCommunicator serverCommunicator;

	/**
	 * Instantiates a new ATMSS handler.
	 */
	private ATMSSHandler() {
	}

	/**
	 * Inits the handler.
	 *
	 * @param CashDispenserController the cash dispenser controller
	 * @param CardReaderController the card reader controller
	 * @param KeypadController the keypad controller
	 * @param DepositCollectorController the deposit collector controller
	 * @param AdvicePrinterController the advice printer controller
	 * @param DisplayController the display controller
	 * @param EnvelopDispenserController the envelop dispenser controller
	 * @param BAMSCommunicator the BAMS communicator
	 */
	void initHandler(CashDispenserController CashDispenserController, CardReaderController CardReaderController,
			KeypadController KeypadController, DepositCollectorController DepositCollectorController,
			AdvicePrinterController AdvicePrinterController, DisplayController DisplayController,
			EnvelopDispenserController EnvelopDispenserController, BAMSCommunicator BAMSCommunicator) {
		this.advicePrinterController = AdvicePrinterController;
		this.cardReaderController = CardReaderController;
		this.cashDispenserController = CashDispenserController;
		this.keypadController = KeypadController;
		this.depositCollectorController = DepositCollectorController;
		this.displayController = DisplayController;
		this.envelopDispenserController = EnvelopDispenserController;
		this.serverCommunicator = BAMSCommunicator;
	}

	/**
	 * Get the handler.
	 *
	 * @return the only handler
	 */
	public static ATMSSHandler getHandler() {
		if (atmssHandler == null) {
			synchronized (ATMSSHandler.class) {
				if (atmssHandler == null)
					atmssHandler = new ATMSSHandler();
			}
		}

		return atmssHandler;
	}

	// >>>>>>>>>>>>>>>>>>>>0. functions of BAMS Handler<<<<<<<<<<<<<<<<<<<

	/**
	 * Do bams check balance.
	 *
	 * @param accNo the account number
	 * @param currentSession the current session
	 * @return the balance
	 */
	// get input params from Session
	public double doBAMSCheckBalance(String accNo, Session currentSession) {
		return serverCommunicator.enquiry(currentSession.getCardNo(), accNo, currentSession.getCred());
	}

	/**
	 * Do bams get accounts.
	 *
	 * @param currentSession the current session
	 * @return the accounts in this card
	 */
	public String[] doBAMSGetAccounts(Session currentSession) {
		return serverCommunicator.getAccounts(currentSession.getCardNo(), currentSession.getCred());
	}

	/**
	 * Do bams update balance.
	 *
	 * @param accNumber the account number
	 * @param amount the amount
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSUpdateBalance(String accNumber, double amount, Session currentSession) {
		long resultAmount = 0;

		if (amount > 0) // deposit
			resultAmount = Math.round(serverCommunicator.deposit(currentSession.getCardNo(), accNumber,
					currentSession.getCred(), (int) amount)); // round down

		if (amount < 0) // withdraw
			resultAmount = Math.round(serverCommunicator.cashWithdraw(currentSession.getCardNo(), accNumber,
					currentSession.getCred(), String.valueOf(amount * -1)));

		if (resultAmount != 0 && resultAmount == Math.abs(amount))
			return true;

		return false;
	}

	/**
	 * Do bams update password.
	 *
	 * @param newPin the new pin
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSUpdatePasswd(String newPin, Session currentSession) {
		if (serverCommunicator.changePin(currentSession.getCardNo(), currentSession.getCred(), newPin) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Do bams transfer.
	 *
	 * @param toAccNo the to account number
	 * @param destAccNo the destination account number
	 * @param amount the amount
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSTransfer(String toAccNo, String destAccNo, double amount, Session currentSession) {
		double resultAmount = serverCommunicator.transfer(currentSession.getCardNo(), currentSession.getCred() + "&",
				destAccNo, toAccNo, String.valueOf(amount));

		if (resultAmount == amount)
			return true;
		return false;
	}

	/**
	 * Do bams check card valid.
	 *
	 * @param cardNo the card number
	 * @return true, if successful
	 */
	public boolean doBAMSCheckCardValid(String cardNo) {
		return serverCommunicator.isCardExist(cardNo);
	}

	/**
	 * Do Advice Printer print advice.
	 *
	 * @param operations the operations
	 * @return true, if successful
	 */
	// >>>>>>>>>>>>>>>>>>1 Functions of advice printer <<<<<<<<<<<<<<<<<<<
	public boolean doAPPrintAdvice(LinkedList<Operation> operations) {
		try {
			return advicePrinterController.printOperations(operations);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	/**
	 * Do Advice Printer print string array.
	 *
	 * @param toPrint the to print
	 * @return true, if successful
	 */
	public boolean doAPPrintStrArray(String[] toPrint) {
		try {
			return this.advicePrinterController.printStrArray(toPrint);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	/**
	 * Do Advice Printer check inventory.
	 *
	 * @return the number of paper remains
	 */
	public int doAPCheckInventory() {
		try {
			return this.advicePrinterController.checkInventory();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with AP
		}
	}

	/**
	 * Do Advice Printer get status.
	 *
	 * @return the status of Advice Printer
	 */
	public int doAPGetStatus() {
		try {
			return this.advicePrinterController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with AP
		}
	}

	// >>>>>>>>>>>>>>>>>>2 Functions of card reader <<<<<<<<<<<<<<<<<<<

	/**
	 * Do Card Reader get card number.
	 *
	 * @return the card number
	 */
	public String doCRGetCardNumber() {
		try {
			return this.cardReaderController.getCardNumber();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	/**
	 * Do Card Reader read card.
	 *
	 * @return the string
	 */
	public String doCRReadCard() {
		try {
			return this.cardReaderController.readCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	/**
	 * Do Card Reader get status.
	 *
	 * @return the int
	 */
	public int doCRGetStatus() {
		try {
			return this.cardReaderController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with CR
		}
	}

	/**
	 * Do Card Reader eject card.
	 *
	 * @return true, if successful
	 */
	public boolean doCREjectCard() {
		try {
			return this.cardReaderController.ejectCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	/**
	 * Do Card Reader retain card.
	 *
	 * @return true, if successful
	 */
	public boolean doCRRetainCard() {
		try {
			return this.cardReaderController.retainCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	// >>>>>>>>>>>>>>>>>>3 Functions of Cash dispenser <<<<<<<<<<<<<<<<<<<

	/**
	 * Do Cash dispenser eject cash.
	 *
	 * @param ejectPlan the eject plan
	 * @return true, if successful
	 */
	public boolean doCDEjectCash(int[] ejectPlan) {
		try {
			return this.cashDispenserController.ejectCash(ejectPlan);
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	/**
	 * Do Cash dispenser check cash inventory.
	 *
	 * @return the int[], 0: number of 100, 1: number of 500, 2:number of 1000
	 */
	public int[] doCDCheckCashInventory() {
		try {
			return this.cashDispenserController.checkCashInvetory();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	/**
	 * Do Cash dispenser get status.
	 *
	 * @return the status
	 */
	public int doCDGetStatus() {
		try {
			return this.cashDispenserController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1;
		}
	}

	// >>>>>>>>>>>>>>>>>>4 Functions of Deposit collector <<<<<<<<<<<<<<<<<<<

	/**
	 * Do Deposit collector collect envelop.
	 *
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	public boolean doDCCollectEnvelop(int timeout) {
		try {
			return depositCollectorController.collectEnvelop(timeout);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	/**
	 * Do Display display upper.
	 *
	 * @param lines the lines to display
	 * @return true, if successful
	 */
	// >>>>>>>>>>>>>>>>>>5 Functions of Display <<<<<<<<<<<<<<<<<<<
	public boolean doDisDisplayUpper(String[] lines) {
		try {
			return displayController.displayUpper(lines);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	/**
	 * Do Display append upper.
	 *
	 * @param append the lines in the upper area
	 * @return true, if successful
	 */
	public boolean doDisAppendUpper(String[] lines) {
		try {
			return displayController.appendUpper(lines);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Display append upper.
	 *
	 * @param append a line in the upper area
	 * @return true, if successful
	 */
	public boolean doDisAppendUpper(String line) {
		try {
			return displayController.appendUpper(line);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	/**
	 * Do Display display lower.
	 *
	 * @param display the line in the lower area
	 * @return true, if successful
	 */
	public boolean doDisDisplayLower(String line) {
		try {
			return displayController.displayLower(line);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Display append lower.
	 *
	 * @param str the string to append
	 * @return true, if successful
	 */
	public boolean doDisAppendLower(String str) {
		try {
			return displayController.appendLower(str);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Display clear all.
	 *
	 * @return true, if successful
	 */
	public boolean doDisClearAll() {
		try {
			return displayController.clearAll();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Display clear upper.
	 *
	 * @return true, if successful
	 */
	public boolean doDisClearUpper() {
		try {
			return displayController.clearUpper();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Display clear lower.
	 *
	 * @return true, if successful
	 */
	public boolean doDisClearLower() {
		try {
			return displayController.clearLower();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}

		return false;
	}

	/**
	 * Do Envelop Dispenser eject envelop.
	 *
	 * @return true, if successful
	 */
	// >>>>>>>>>>>>>>>>>>6 Functions of Envelop dispenser <<<<<<<<<<<<<<<<<<<
	public boolean doEDEjectEnvelop() {
		try {
			return envelopDispenserController.ejectEnvelop();
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	/**
	 * Do Envelop Dispenser eat envelop.
	 *
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	public boolean doEDEatEnvelop(int timeout) {
		try {
			return depositCollectorController.collectEnvelop(timeout);
		} catch (Exception e) {
			handleUnknownExceptions(e);
		}
		return false;
	}

	// >>>>>>>>>>>>>>>>>>7 Functions of Function of keypad <<<<<<<<<<<<<<<<<<<

	/**
	 * Do keypad get single input.
	 *
	 * @param Duration the time out duration
	 * @return the string
	 */
	public String doKPGetSingleInput(long Duration) {
		try {
			String input = this.keypadController.readUserInput(Duration);
			if (input.contains("Time out!"))
				return null;
			else
				return input;
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	/**
	 * Do keypad get password.
	 *
	 * @param Duration the time out duration
	 * @return the password
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
			} else if (currentInput.equals("ENTER")) {
				if (inputPasswd.length() == lengthLimit) {
					doDisClearLower();
					break;
				} else
					continue;
			} else if (inputPasswd.length() == lengthLimit) {
				continue;
			} else {
				doDisAppendLower("*");
				inputPasswd += currentInput;
			}
		}

		return inputPasswd;
	}

	/**
	 * Do keypad get integer money amount.
	 *
	 * @param Duration the time out duration
	 * @return the money amount
	 */
	public String doKPGetIntegerMoneyAmount(long Duration) {
		String moneyAmount = "";
		int lengthLimit = 8; // Max 999,000 per day

		while (true) {
			String currentInput = doKPGetSingleInput(Duration);
			if (currentInput == null) {
				return null;
			}

			if (currentInput.equals(".")) {
				continue;
			}

			if (currentInput.equals("CANCEL")) {
				doDisClearLower();
				moneyAmount = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				doDisClearLower();
				moneyAmount = "";
			} else if (currentInput.equals("ENTER")) {
				if (moneyAmount.length() > 0 && moneyAmount.length() <= lengthLimit) {
					doDisClearLower();
					break;
				} else
					continue;
			} else if (moneyAmount.length() == lengthLimit) {
				continue;
			} else {
				doDisAppendLower(currentInput);
				moneyAmount += currentInput;
				continue;
			}
		}

		return moneyAmount;
	}

	/**
	 * Do keypad get double money amount.
	 *
	 * @param Duration the time out duration
	 * @return the money amount
	 */
	public String doKPGetDoubleMoneyAmount(long Duration) {
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
					moneyAmount += currentInput;
					lengthLimit = moneyAmount.length() + 3;
					inputDot = true;
				}
				continue;
			}
			System.out.println("-------------Money Length : " + moneyAmount.length());
			System.out.println("-------------Length limit: " + lengthLimit);

			if (currentInput.equals("CANCEL")) {
				doDisClearLower();
				moneyAmount = "CANCEL";
				break;
			} else if (currentInput.equals("CLEAR")) {
				inputDot = false;
				doDisClearLower();
				moneyAmount = "";
			} else if (currentInput.equals("ENTER")) {
				if (moneyAmount.length() > 0 && moneyAmount.length() <= lengthLimit) {
					doDisClearLower();
					break;
				} else
					continue;
			} else if (moneyAmount.length() == lengthLimit) {
				continue;
			} else {
				doDisAppendLower(currentInput);
				moneyAmount += currentInput;
				continue;
			}
		}

		return moneyAmount;
	}

	/**
	 * Do keypad get account number.
	 *
	 * @param Duration the duration
	 * @return the account number
	 */
	public String doKPGetAccountNum(long Duration) {
		String inputAccountNum = "";
		int lengthLimit = 11;
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
			} else if (currentInput.equals("ENTER")) {
				if (inputAccountNum.length() == lengthLimit) {
					doDisClearLower();
					break;
				} else
					continue;
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

	/**
	 * Handle unknown exceptions.
	 *
	 * @param Exception e
	 */
	private void handleUnknownExceptions(Exception e) {
		e.printStackTrace();
	}

}
