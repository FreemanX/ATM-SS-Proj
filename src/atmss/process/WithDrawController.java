/**
 *
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;
import atmss.Session;

/**
 * @author DJY
 *
 */
public class WithDrawController extends ProcessController{

	private final String OPERATION_NAME = "Withdraw Cash";
	private final String FAILED_FROM_BAMS_LOADING_ACCOUNTS = "Failed to read account information";
	private final String FAILED_FROM_BAMS_UPDATING_BALANCE = "Failed to update balance at BAMS";
	private final String FAILED_FROM_DISPLAY = "No response from the display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_CASHDISPENSER = "No response from the cash dispenser";
	private final String FAILED_FROM_CD_EJECTING = "The cash dispenser cannot eject the cash";
	private final String FAILED_FROM_CD_RETAINING = "The cash dispenser cannot retain the cash";
	private final String FAILED_FROM_USER_CANCELLING = "The operation has been cancelled";
	private final String FAILED_FROM_USER_COLLECTING = "The cash was not collected by the card holder";
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	private final String FAILED_FROM_INVENTORY = "Not enough inventory to withdraw";
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your account:";
	private final String PROMPT_FOR_CHOICE_ERR_HEADER = "Not a valid choice! Please choose your account:";
	private final String[] PROMPT_FOR_AMOUNT = {"You can only withdraw 100, 500, 1000 notes.","Please input your withdraw amount:"};
	private final String[] PROMPT_FOR_AMOUNT_ERR = {"Invalid amount!","The withdraw amount must end up with at least two 0s:"};
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final long TIME_LIMIT = 30 * 1000;
	private final String KP_CANCEL = "CANCEL";

	public WithDrawController(Session CurrentSession) {
		super(CurrentSession);
	}

	public Boolean doWithDraw() {
		String[] accountNumbers;
		String accountNumber = "";
		int withdrawAmount = 0;
		int[] withdrawPlan;
		int[] cashInventory;
		double balance = 0;
		boolean result = false;

		// get account numbers from BAMS
		accountNumbers = _atmssHandler.doBAMSGetAccounts(_session);
		if (accountNumbers == null || accountNumbers.length == 0) {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BAMS_LOADING_ACCOUNTS})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(FAILED_FROM_BAMS_LOADING_ACCOUNTS);
			return false;
		}

		// -> preparing the necessary information
		// get account choice from the user
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_CHOICE_HEADER,accountNumbers))) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		while (true) {
			String userInput = doKPGetChoice(TIME_LIMIT);
			if (userInput == null) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			} else if (userInput.equals(KP_CANCEL)) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_USER_CANCELLING);
				return false;
			}
			int choice = choiceFromString(userInput);
			if ( 0 < choice && choice <= accountNumbers.length ) {
				accountNumber = accountNumbers[choice-1];
				break;
			}
			if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_CHOICE_ERR_HEADER,accountNumbers))) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
		}

		// get withdraw amount from the user
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_AMOUNT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		while (true) {
			String userInput= _atmssHandler.doKPGetIntegerMoneyAmount(TIME_LIMIT);
			if (userInput == null) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			} else if (userInput.equals(KP_CANCEL)) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_USER_CANCELLING);
				return false;
			}
			int amount = amountFromString(userInput);
			if (amount != 0) {
				withdrawAmount = amount;
				break;
			}
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_AMOUNT_ERR)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
		}
		// <- preparing the necessary information

		// contact BAMS, 1. get current balance
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		balance = _atmssHandler.doBAMSCheckBalance(accountNumber, _session);
		
		// -> display the result
		// FAILED_FROM_BALANCE
		if (withdrawAmount > balance) {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BALANCE,"You can only withdraw $"+balance})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_BALANCE);
			return false;
		} 
		
		cashInventory = _atmssHandler.doCDCheckCashInventory();
		
		// FAILED_FROM_CASHDISPENSER
		if (cashInventory == null) {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_CASHDISPENSER})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CASHDISPENSER);
			return false;
		}
		
		withdrawPlan = getWithdrawPlan(cashInventory, withdrawAmount);
		
		// FAILED_FROM_INVENTORY
		if (withdrawPlan[0] == -1) {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_INVENTORY})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_INVENTORY);
			return false;
		}

		// eject the cash
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_COLLECTION)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		if (!_atmssHandler.doCDEjectCash(withdrawPlan)) {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_CD_EJECTING})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CD_EJECTING);
			return false;
		}

		if (result) { // TODO: if (_atmssHandler.doCDCollectInTime(TIME_LIMIT)) {
			if (!_atmssHandler.doBAMSUpdateBalance(accountNumber, -withdrawAmount,_session)) {
				recordOperation(accountNumber, withdrawAmount, FAILED_FROM_BAMS_UPDATING_BALANCE);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount);
			return true;
		} else { // otherwise not collect in time
			if (!_atmssHandler.doCDRetainCash()) {
				// cannot retain and will not inform BAMS
				recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CD_RETAINING);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_USER_COLLECTING);
			return false;
		}
		// <- display the result
	}

	private void recordOperation(String AccountNumber, int Amount, String FailedReason) {
		String description =
				"Card Number: " + _session.getCardNo() + "; " +
				"Account Number: " + AccountNumber + "; " +
				"Amount: " + Amount + "; " +
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}

	private void recordOperation(String AccountNumber, int Amount) {
		String description =
				"Card Number: " + _session.getCardNo() + "; " +
				"Account Number: " + AccountNumber + "; " +
				"Amount: " + Amount + "; " +
				"Result: " + "Succeeded; ";
		operationCache.add(new Operation(OPERATION_NAME, description));
	}

	private void recordOperation(String FailedReason) {
		String description =
				"Card Number: " + _session.getCardNo() + "; " +
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}
	
	private int amountFromString(String userInput) {
		try {
			int withdrawAmount = Integer.parseInt(userInput);
			if (withdrawAmount > 0 && withdrawAmount <= 10000 && withdrawAmount % 100 == 0){
				return withdrawAmount;
			} else {
				return 0;
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String doKPGetChoice(long Duration) {
		while (true) {
			String currentInput = _atmssHandler.doKPGetSingleInput(Duration);
			if (currentInput == null) {
				// user timeout or hardware failure
				return null;
			}
			switch (currentInput) {
				case "0":
				case ".":
				case "CLEAR":
				case "ENTER":break; // ignore those input
				default:return currentInput; // than it must be 1-9 or CANCEL
			}
		}
	}

	private int choiceFromString(String userInput) {
		// the choice can only be 1-9
		try {
			return Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "-> " + i + ": " + Body[i-1];
		}
		return lines;
	}
	
	private int[] getWithdrawPlan(int[] inventory, int withdrawAmount) {
		int[] plan = new int[3];
		plan[2] = withdrawAmount / 1000;
		plan[1] = (withdrawAmount - plan[2] * 1000 ) / 500;
		plan[0] = (withdrawAmount - plan[2] * 1000 - plan[1] * 500 ) / 100;
		while (plan[2] > inventory[2]) {
			plan[2] -= 1;
			plan[1] += 2;
		}
		while (plan[1] > inventory[1]) {
			plan[1] -= 1;
			plan[0] += 5;
		}
		if (plan[0] > inventory[0]) {
			plan[0] = -1;
		}
		return plan;
	}
}
