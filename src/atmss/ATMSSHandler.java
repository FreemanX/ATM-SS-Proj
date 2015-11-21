package atmss;

import java.util.LinkedList;

import atmss.bams.*;
import atmss.hardware.controller.*;

public class ATMSSHandler {

	private static ATMSSHandler atmssHandler = null;
	private CashDispenserController cashDispenserController;
	private CardReaderController cardReaderController;
	private KeypadController keypadController;
	private DepositCollectorController depositCollectorController;
	private AdvicePrinterController advicePrinterController;
	private DisplayController displayController;
	private EnvelopDispenserController envelopDispenserController;
	private BAMSCommunicator serverCommunicator;

	private ATMSSHandler() {
		// TODO Auto-generated constructor stub
	}

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

	// get input params from Session
	public double doBAMSCheckBalance(String accNo, Session currentSession) {
		return serverCommunicator.enquiry(currentSession.getCardNo(), accNo, currentSession.getCred());
	}

	public String[] doBAMSGetAccounts(Session currentSession) {
		return serverCommunicator.getAccounts(currentSession.getCardNo(), currentSession.getCred());
	}

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

	public boolean doBAMSUpdatePasswd(String newPin, Session currentSession) {
		if (serverCommunicator.changePin(currentSession.getCardNo(), currentSession.getCred(), newPin) == 1) {
			return true;
		}
		return false;
	}

	public boolean doBAMSTransfer(String toAccNo, String destAccNo, double amount, Session currentSession) {
		double resultAmount = serverCommunicator.transfer(currentSession.getCardNo(), currentSession.getCred() + "&",
				destAccNo, toAccNo, String.valueOf(amount));

		if (resultAmount == amount)
			return true;
		return false;
	}

	public boolean doBAMSCheckCardValid(String cardNo) {
		return serverCommunicator.isCardExist(cardNo);
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
		try {
			return this.cardReaderController.getCardNumber();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	public String doCRReadCard() {
		try {
			return this.cardReaderController.readCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return null;
		}
	}

	public int doCRGetstatus() {
		try {
			return this.cardReaderController.getStatus();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return -1; // Returns -1 means that there's a problem with CR
		}
	}

	public boolean doCREjectCard() {
		try {
			return this.cardReaderController.ejectCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
		}
	}

	public boolean doCRRetainCard() {
		try {
			return this.cardReaderController.retainCard();
		} catch (Exception e) {
			handleUnknownExceptions(e);
			return false;
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

	public boolean doEDEatEnvelop(int timeout) {
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
			if (input.contains("Time out!"))
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

	private void handleUnknownExceptions(Exception e) {
		// TODO handles unexpected exceptions
		e.printStackTrace();
	}

}
