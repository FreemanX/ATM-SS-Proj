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
	private final String FAILED_FROM_BAMS = "Failed to get approval from BAMS";
	private final String FAILED_FROM_CASHDISPENSER = "No response from the cash dispenser";
	private final String FAILED_FROM_CD_COLLECTION = "The cash dispenser cannot retain the cash";
	private final String FAILED_FROM_USER_COLLECTION = "The cash was not collected by the card holder";
	private final String ERROR_BAD_CHOICE_HEADER = "Not a valid choice! Please choose your account:";
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your account:";
	private final String[] PROMPT_FOR_AMOUNT = {"Please input your withdraw amount:"};
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final String[] SHOW_FAILURE = {"Operation Failed!"};

	public WithDrawController(String CardNumber, MainController MainController) {
		super(CardNumber, MainController);
	}

	public Boolean doWithDraw() {
		String[] accountNumbers = new String[4];
		String accountNumber = "";
		int withdrawAmount = 0;
		boolean result = false;

		// get account numbers from the CardReader
		// TODO: accountNumbers = this._mainController.doBAMSCheckAccounts();
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
			String userInput = _mainController.doGetKeyInput();
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
		// TODO: result = _mainController.doBAMSWithdraw(accountNumber, withdrawAmount);

		// -> display the result
		// failed
		if (!result) {
			if (!_mainController.doDisplay(SHOW_FAILURE)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_BAMS);
			return false;
		}
		// succeeded
		if (!_mainController.doDisplay(PROMPT_FOR_COLLECTION)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		// TODO int[] plan = getPlan(withdrawAmount);
		int[] plan = {1,1,1};
		if (!_mainController.doCDEjectCash(plan)) {
			recordOperation(accountNumber, withdrawAmount, FAILED_FROM_CASHDISPENSER);
			return false;
		}

		if (result) { // if (_mainController.collectInTime()) {
			// TODO _mainController.doCDCheckInventory();
			recordOperation(accountNumber, withdrawAmount);
			return true;
		} else {
			if (!_mainController.doCDRetainCash()) {
				// TODO _mainController.doCDCheckInventory();
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
}
