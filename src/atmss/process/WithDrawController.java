/**
 *
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;

/**
 * @author DJY
 *
 */
public class WithDrawController extends ProcessController{

	private final String OPERATION_NAME = "Withdraw Cash";
	private final String FAILED_FROM_CARDREADER = "No response from the card reader";
	private final String FAILED_FROM_DISPLAY = "No response from the display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	private final String FAILED_FROM_INVENTORY = "Not enough inventory to withdraw";
	private final String FAILED_FROM_CASHDISPENSER = "No response from the cash dispenser";
	private final String FAILED_FROM_CD_COLLECTION = "The cash dispenser cannot retain the cash";
	private final String FAILED_FROM_USER_COLLECTION = "The cash was not collected by the card holder";
	private final String FAILED_FROM_BAMS = "No response from the from BAMS";
	private final String ERROR_BALANCE_HEADER = "Not enough balance!";
	private final String ERROR_BAD_CHOICE_HEADER = "Not a valid choice! Please choose your account:";
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your account:";
	private final String[] PROMPT_FOR_AMOUNT = {"Please input your withdraw amount:"};
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final String ERROR_BALANCE_HEADER2 = "Operation Failed!";

	public WithDrawController(String CardNumber, MainController MainController) {
		super(CardNumber, MainController);
	}

	public Boolean doWithDraw() {
		String[] accountNumbers = new String[4];
		String accountNumber = "";
		int withdrawAmount = 0;
		int[] withdrawPlan;
		boolean result = false;

		// get account numbers from the CardReader
		// TODO: accountNumbers = _mainController.doBAMSCheckAccounts();
		if (accountNumbers == null || accountNumbers.length == 0) {
			recordOperation(FAILED_FROM_CARDREADER);
			return false;
		}

		// -> preparing the necessary information
		// get account choice from the user
		if (!_mainController.doDisplay(createOptionList(PROMPT_FOR_CHOICE_HEADER,accountNumbers))) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		while (true) {
			String userInput = _mainController.doKPGetSingleInput(30000);
			if (userInput.isEmpty()) {
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			}

			int choice = choiceFromString(userInput);
			if ( 0 < choice && choice <= accountNumbers.length ) {
				accountNumber = accountNumbers[choice-1];
				break;
			}

			if (!_mainController.doDisplay(createOptionList(ERROR_BAD_CHOICE_HEADER,accountNumbers))) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
		}

		// get withdraw amount from the user
		if (!_mainController.doDisplay(PROMPT_FOR_AMOUNT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		// TODO: withdrawAmount = _mainController.getWithdrawAmountFromUser();
		if (withdrawAmount == 0) {
			recordOperation(FAILED_FROM_KEYPAD);
			return false;
		}
		// <- preparing the necessary information

		// contact BAMS now
		if (!_mainController.doDisplay(SHOW_PLEASE_WAIT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		// TODO: Do not know what is cred...
		// TODO: double balance = _mainController.doBAMSCheckBalance(_cardNumber, accountNumber);
		double balance = 500.00;
		
		// -> display the result
		// failed:1
		if (withdrawAmount > balance) {
			String[] displayLines = {ERROR_BALANCE_HEADER, "You can only withdraw $"+balance};
			if (!_mainController.doDisplay(displayLines)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_BALANCE);
			return false;
		} 
		
		int[] cashInventory = _mainController.doCDCheckCashInventory();
		
		// failed:2
		if (cashInventory == null) {
			String[] displayLines = {FAILED_FROM_CASHDISPENSER};
			if (!_mainController.doDisplay(displayLines)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CASHDISPENSER);
			return false;
		}
		
		withdrawPlan = getWithdrawPlan(cashInventory, withdrawAmount);
		
		// failed:3
		if (withdrawPlan[0] == -1) {
			String[] displayLines = {FAILED_FROM_INVENTORY};
			if (!_mainController.doDisplay(displayLines)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_INVENTORY);
			return false;
		}

		// succeeded:
		if (!_mainController.doDisplay(PROMPT_FOR_COLLECTION)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		if (!_mainController.doCDEjectCash(withdrawPlan)) {
			String[] displayLines = {FAILED_FROM_CASHDISPENSER};
			if (!_mainController.doDisplay(displayLines)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CASHDISPENSER);
			return false;
		}

		if (result) { // TODO: if (_mainController.collectInTime()) {
//			if (!_mainController.doBAMSWithdraw(accountNumber, withdrawAmount)) {
//				recordOperation(accountNumber, withdrawAmount, FAILED_FROM_BAMS);
//				return false;
//			}
			recordOperation(accountNumber, withdrawAmount);
			return true;
		} else { // otherwise not collect in time
			if (!_mainController.doCDRetainCash()) {
				// cannot retain and will not inform BAMS
				recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CD_COLLECTION);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_USER_COLLECTION);
			return false;
		}
		// <- display the result
	}

	private void recordOperation(String AccountNumber, int Amount, String FailedReason) {
		String description =
				"Card Number: " + _cardNumber + "; " +
				"Account Number: " + AccountNumber + "; " +
				"Amount: " + Amount + "; " +
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}

	private void recordOperation(String AccountNumber, int Amount) {
		String description =
				"Card Number: " + _cardNumber + "; " +
				"Account Number: " + AccountNumber + "; " +
				"Amount: " + Amount + "; " +
				"Result: " + "Succeeded; ";
		operationCache.add(new Operation(OPERATION_NAME, description));
	}

	private void recordOperation(String FailedReason) {
		String description =
				"Card Number: " + _cardNumber + "; " +
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}

	private String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "-> " + i + ": " + Body[i-1];
		}
		return lines;
	}

	private int choiceFromString(String userInput) {
		int choice = 0;
		try {
			choice = Integer.parseInt(userInput);
			return choice;
		} catch (NumberFormatException e) {
			return 0;
		}
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
