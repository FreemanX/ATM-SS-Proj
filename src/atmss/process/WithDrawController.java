/**
 *
 */
package atmss.process;

import atmss.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class WithDrawController.
 *
 * @author DJY
 */
public class WithDrawController extends ProcessController{

	/** The operation name. */
	private final String OPERATION_NAME = "Withdraw";
	
	/** The failed from bams loading accounts. */
	private final String FAILED_FROM_BAMS_LOADING_ACCOUNTS = "Failed to read account information";
	
	/** The failed from keypad. */
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	
	/** The failed from cashdispenser. */
	private final String FAILED_FROM_CASHDISPENSER = "No response from the cash dispenser";
	
	/** The failed from user cancelling. */
	private final String FAILED_FROM_USER_CANCELLING = "The operation has been cancelled";
	
	/** The failed from balance. */
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	
	/** The failed from inventory. */
	private final String FAILED_FROM_INVENTORY = "Not enough inventory to withdraw";
	
	/** The prompt for choice header. */
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your withdraw account:";
	
	/** The prompt for choice err header. */
	private final String PROMPT_FOR_CHOICE_ERR_HEADER = "Not a valid choice! Please choose your account:";
	
	/** The prompt for amount. */
	private final String[] PROMPT_FOR_AMOUNT = {"You can only withdraw 100, 500, 1000 notes.","Please input your withdraw amount:"};
	
	/** The prompt for amount err. */
	private final String[] PROMPT_FOR_AMOUNT_ERR = {"Invalid amount!","The withdraw amount must be divisible by 100 and less or equal to 10000","Please input your withdraw amount again:"};
	
	/** The prompt for collection. */
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	
	/** The show please wait. */
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	
	/** The time limit. */
	private final int TIME_LIMIT = 20; // seconds
	
	/** The amount limit. */
	private final int AMOUNT_LIMIT = 10000;
	
	/** The kp cancel. */
	private final String KP_CANCEL = "CANCEL";
	
	/** The _current step. */
	private String _currentStep = OPERATION_NAME;

	/**
	 * Instantiates a new with draw controller.
	 *
	 * @param CurrentSession the current session
	 */
	public WithDrawController(Session CurrentSession) {
		super(CurrentSession);
	}

	/**
	 * Do with draw.
	 *
	 * @return the boolean
	 */
	public Boolean doWithDraw() {
		String[] accountNumbers;
		String accountNumber = "";
		int withdrawAmount = 0;
		double balance = 0;
		int[] cashInventory;
		int[] withdrawPlan;
		boolean result = false;

		_currentStep = OPERATION_NAME+": loading accounts";
			accountNumbers = _atmssHandler.doBAMSGetAccounts(_session);
			if (accountNumbers == null || accountNumbers.length == 0) {
				record("BAMS");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BAMS_LOADING_ACCOUNTS})) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
		record("accounts loaded");

		// -> preparing the necessary information
		_currentStep = OPERATION_NAME+": getting account choice from user";
			if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_CHOICE_HEADER,accountNumbers))) {
				record("Dis");
				return false;
			}
			while (true) {
				String userInput = doKPGetChoice(TIME_LIMIT);
				if (userInput == null) {
					record("KP");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
						record("Dis");
						return false;
					}
					pause(3);
					return false;
				} else if (userInput.equals(KP_CANCEL)) {
					record("USER");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
						record("Dis");
						return false;
					}
					pause(3);
					return false;
				}
				int choice = choiceFromString(userInput);
				if ( 0 < choice && choice <= accountNumbers.length ) {
					accountNumber = accountNumbers[choice-1];
					break;
				}
				if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_CHOICE_ERR_HEADER,accountNumbers))) {
					record("Dis");
					return false;
				}
			}
		record("account chosen " + accountNumber);

		_currentStep = OPERATION_NAME+": getting withdraw amount from user";
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_AMOUNT)) {
				record("Dis");
				return false;
			}
			while (true) {
				String userInput= _atmssHandler.doKPGetIntegerMoneyAmount(TIME_LIMIT);
				if (userInput == null) {
					record("KP");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
						record("Dis");
						return false;
					}
					pause(3);
					return false;
				} else if (userInput.equals(KP_CANCEL)) {
					record("USER");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
						record("Dis");
						return false;
					}
					pause(3);
					return false;
				}
				int amount = amountFromString(userInput);
				if (amount != 0) {
					withdrawAmount = amount;
					break;
				}
				if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_AMOUNT_ERR)) {
					record("Dis");
					return false;
				}
			}
		record("withdraw amount typed $" + withdrawAmount);
		// <- preparing the necessary information

		// -> processing
		_currentStep = OPERATION_NAME+": checking withdraw amount against balance";
			if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
				record("Dis");
				return false;
			}
			balance = _atmssHandler.doBAMSCheckBalance(accountNumber, _session);
			if (withdrawAmount > balance) {
				record("BAMS");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BALANCE,"You can only withdraw $"+balance})) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
		record("balance is enough");
		
		_currentStep = OPERATION_NAME+": checking cash inventory";
		cashInventory = _atmssHandler.doCDCheckCashInventory();
			if (cashInventory == null) {
				record("CD");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_CASHDISPENSER})) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
		record("inventory seems enough");
		
		_currentStep = OPERATION_NAME+": getting withdraw plan";
			withdrawPlan = getWithdrawPlan(cashInventory, withdrawAmount);
			if (withdrawPlan[0] == -1) {
				record("CD");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_INVENTORY})) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
		record("withdraw plan loaded");
		// <- processing

		// -> display the result
		_currentStep = OPERATION_NAME+": waiting for collecting cash";
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_COLLECTION)) {
			record("Dis");
			return false;
		}
		result = _atmssHandler.doCDEjectCash(withdrawPlan); 
		if (result) {
			record("cash collected");
			if (!_atmssHandler.doBAMSUpdateBalance(accountNumber, -withdrawAmount,_session)) {
				record("BAMS");
				return false;
			}
			askForPrinting(accountNumber, withdrawAmount);
			return true;
		} else {
			record("CD");
			return false;
		}
		// <- display the result
	}
	
	/**
	 * Pause.
	 *
	 * @param Seconds the seconds
	 */
	private void pause(int Seconds) {
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime < Seconds*1000){}
	}
	
	/**
	 * Record.
	 *
	 * @param Type the type
	 */
	private void record(String Type) {
		super.record(_currentStep, Type);
	}
		
	/**
	 * Ask for printing.
	 *
	 * @param AccountNumber the account number
	 * @param Amount the amount
	 */
	private void askForPrinting(String AccountNumber, int Amount){
		String[] toDisplay = {
				"Operation succeeded!",
				"You have withdrawn $" + Amount + " from account: " + AccountNumber,				
				"Press 1 -> Print the advice",
				"Press 2 -> Quit without printing"
		};
		if (!_atmssHandler.doDisDisplayUpper(toDisplay)) {
			record("Dis");
			return;
		}
		while (true) {
			String userInput = _atmssHandler.doKPGetSingleInput(TIME_LIMIT);
			if (userInput == null) return;
			if (userInput.equals("1")) {
				String[] toPrint = {
						"Operation Name: " + OPERATION_NAME,
						"Card Number: " + _session.getCardNo(),
						"Account Number: " + AccountNumber,
						"Amount: $" + Amount
				};
				if (!_atmssHandler.doAPPrintStrArray(toPrint)) record("AP");
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
	
	/**
	 * Amount from string.
	 *
	 * @param userInput the user input
	 * @return the int
	 */
	private int amountFromString(String userInput) {
		try {
			int withdrawAmount = Integer.parseInt(userInput);
			if (withdrawAmount > 0 && withdrawAmount <= AMOUNT_LIMIT && withdrawAmount % 100 == 0){
				return withdrawAmount;
			} else {
				return 0;
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Do kp get choice.
	 *
	 * @param Duration the duration
	 * @return the string
	 */
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

	/**
	 * Choice from string.
	 *
	 * @param userInput the user input
	 * @return the int
	 */
	private int choiceFromString(String userInput) {
		// the choice can only be 1-9
		try {
			return Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Gets the withdraw plan.
	 *
	 * @param inventory the inventory
	 * @param withdrawAmount the withdraw amount
	 * @return the withdraw plan
	 */
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
