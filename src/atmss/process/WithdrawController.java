/**
 *
 */
package atmss.process;

import atmss.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class WithdrawController.
 *
 * @author DJY
 */
public class WithdrawController extends ProcessController{

	/** The display message when prompting for choice header. */
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your withdraw account:";
	
	/** The display message when prompting for choice err header. */
	private final String PROMPT_FOR_CHOICE_ERR_HEADER = "Not a valid choice! Please choose your account:";
	
	/** The display message when prompting for amount. */
	private final String[] PROMPT_FOR_AMOUNT = {"You can only withdraw 100, 500, 1000 notes.","Please input your withdraw amount:"};
	
	/** The display message when prompting for amount err. */
	private final String[] PROMPT_FOR_AMOUNT_ERR = {"Invalid amount!","The withdraw amount must be divisible by 100 and less or equal to $10000","Please input your withdraw amount again:"};
	
	/** The display message when prompting for collection. */
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	
	/** The display message when showing please wait. */
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	
	/** The display message when failed from BAMS loading accounts. */
	private final String[] FAILED_FROM_BAMS_LOADING_ACCOUNTS = {"Failed to read account information"};
	
	/** The display message when failed from keypad. */
	private final String[] FAILED_FROM_KEYPAD = {"No response from the keypad"};
	
	/** The display message when failed from cash dispenser. */
	private final String[] FAILED_FROM_CASHDISPENSER = {"No response from the cash dispenser"};
	
	/** The display message when failed from user cancelling. */
	private final String[] FAILED_FROM_USER_CANCELLING = {"The operation has been cancelled"};
	
	/** The display message when failed from balance. */
	private final String[] FAILED_FROM_BALANCE = {"Not enough balance to withdraw", "You can only withdraw $"};
	
	/** The display message when failed from inventory. */
	private final String[] FAILED_FROM_INVENTORY = {"Not enough inventory to withdraw"};
	
	/** The time limit for key press in seconds. */
	private final long TIME_LIMIT = 20;
	
	/** The withdraw amount limit per operation. */
	private final int AMOUNT_LIMIT = 10000;
	
	/** The key value for cancel. */
	private final String KP_CANCEL = "CANCEL";
	
	/** The operation name. */
	private final String OPERATION_NAME = "Withdraw";
	
	/** The current step. */
	private String currentStep = OPERATION_NAME;
	
	/** The account numbers associated with the card. */
	private String[] accountNumbers;
	
	/** The chosen withdraw account. */
	private String accountNumber = "";
	
	/** The withdraw amount. */
	private int withdrawAmount = 0;
	
	/** The balance in the account. */
	private double balance = 0;
	
	/** The cash inventory in the cash dispenser, [0] for 100 note, [1] for 500 note, [2] for 1000 note. */
	private int[] cashInventory;
	
	/** The withdraw plan for the cash dispenser, [0] for 100 note, [1] for 500 note, [2] for 1000 note. */
	private int[] withdrawPlan;
	
	/** The overall result. */
	private boolean result = false;

	/**
	 * Instantiates a new withdraw controller.
	 *
	 * @param CurrentSession the current session
	 */
	public WithdrawController(Session CurrentSession) {
		super(CurrentSession);
	}

	/**
	 * Do withdraw cash, the core method in WithdrawController.
	 * The method will access all the instance variables through several private methods.
	 *
	 * @return true, if all the relevant hardwares work fine, the user does not cancel or get time out, and the process can get approval from the banking system over the network; otherwise return false.
	 */
	public Boolean doWithdraw() {
		if (!doLoadAccounts()) return false;
		if (!doGetAccountChoice()) return false;
		if (!doGetWithdrawAmount()) return false;		
		if (!doCheckBalance()) return false;
		if (!doCheckInventory()) return false;
		if (!doGetWithdrawPlan()) return false;
		if (!doDispenseCash()) return false;
		doPrintAdvice();
		return true;
	}
	
	/**
	 * Do load accounts, a step of the overall process.
	 * It will change the current step, and access the accountNumbers.
	 * It will record the current step before return.
	 *
	 * @return true, if the accountNumbers gets a new value;
	 */
	private boolean doLoadAccounts() {
		currentStep = OPERATION_NAME+": loading accounts";
		accountNumbers = _atmssHandler.doBAMSGetAccounts(_session);
		if (accountNumbers == null || accountNumbers.length == 0) {
			record("BAMS");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_BAMS_LOADING_ACCOUNTS)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
		record("accounts loaded");
		return true;
	}
	
	/**
	 * Do get account choice, a step of the overall process.
	 * It will change the current step, and access the accountNumbers and accountNumber.
	 * It will record the current step before return.
	 *
	 * @return true, if the accountNumber is chosen by the user;
	 */
	private boolean doGetAccountChoice() {
		currentStep = OPERATION_NAME+": getting account choice from user";
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_CHOICE_HEADER,accountNumbers))) {
			record("Dis");
			return false;
		}
		while (true) {
			String userInput = doKPGetChoice(TIME_LIMIT);
			if (userInput == null) {
				record("KP");
				if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_KEYPAD)) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			} else if (userInput.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_USER_CANCELLING)) {
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
		return true;
	}
	
	/**
	 * Do get withdraw amount, a step of the overall process.
	 * It will change the current step, and access the withdrawAmount.
	 * It will record the current step before return.
	 *
	 * @return true, if the withdrawAmount gets a new value;
	 */
	private boolean doGetWithdrawAmount() {
		currentStep = OPERATION_NAME+": getting withdraw amount from user";
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_AMOUNT)) {
			record("Dis");
			return false;
		}
		while (true) {
			String userInput= _atmssHandler.doKPGetIntegerMoneyAmount(TIME_LIMIT);
			if (userInput == null) {
				record("KP");
				if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_KEYPAD)) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			} else if (userInput.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_USER_CANCELLING)) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
			int amount = amountFromString(userInput, AMOUNT_LIMIT);
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
		return true;
	}
	
	/**
	 * Do check balance, a step of the overall process.
	 * It will change the current step, and access the balance.
	 * It will record the current step before return.
	 *
	 * @return true, if the balance is enough for withdraw;
	 */
	private boolean doCheckBalance() {
		currentStep = OPERATION_NAME+": checking withdraw amount against balance";
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			record("Dis");
			return false;
		}
		balance = _atmssHandler.doBAMSCheckBalance(accountNumber, _session);
		if (withdrawAmount > balance) {
			record("BAMS");
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BALANCE[0],FAILED_FROM_BALANCE[1]+balance})) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
		record("balance is enough");
		return true;
	}
	
	/**
	 * Do check inventory, a step of the overall process.
	 * It will change the current step, and access the cashInventory.
	 * It will record the current step before return.
	 *
	 * @return true, if the cashInventory is enough for withdraw;
	 */
	private boolean doCheckInventory() {
		currentStep = OPERATION_NAME+": checking cash inventory";
		cashInventory = _atmssHandler.doCDCheckCashInventory();
			if (cashInventory == null) {
				record("CD");
				if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_CASHDISPENSER)) {
					record("Dis");
					return false;
				}
				pause(3);
				return false;
			}
		record("inventory seems enough");
		return true;
	}
	
	/**
	 * Do get withdraw plan, a step of the overall process.
	 * It will change the current step, and access the withdrawPlan.
	 * It will record the current step before return.
	 *
	 * @return true, if the withdrawPlan is valid for the cash dispenser;
	 */
	private boolean doGetWithdrawPlan() {
		currentStep = OPERATION_NAME+": getting withdraw plan";
		withdrawPlan = getWithdrawPlan(cashInventory, withdrawAmount);
		if (withdrawPlan[0] == -1) {
			record("CD");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_INVENTORY)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
		record("withdraw plan loaded");
		return true;
	}
	
	/**
	 * Do dispense cash, a step of the overall process.
	 * It will change the current step, and access the result.
	 * It will record the current step before return.
	 *
	 * @return true, if the cash is collected by the user before timeout;
	 */
	private boolean doDispenseCash() {
		currentStep = OPERATION_NAME+": waiting for collecting cash";
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_COLLECTION)) {
			record("Dis");
			return false;
		}
		result = _atmssHandler.doCDEjectCash(withdrawPlan); 
		if (result) {
			record("cash collected");
			if (!_atmssHandler.doBAMSUpdateBalance(accountNumber, -withdrawAmount, _session)) {
				record("BAMS");
				return false;
			}
			return true;
		} else {
			record("CD");
			return false;
		}
	}
	
	/**
	 * Do print advice, a step of the overall process.
	 * It will change the current step, and ask user for printing the advice.
	 * It will record the current step before return.
	 */
	private void doPrintAdvice(){
		currentStep = OPERATION_NAME+": printing advice";
		String[] toDisplay = {
				"Operation succeeded!",
				"You have withdrawn $" + withdrawAmount + " from account: " + accountNumber,				
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
						"Account Number: " + accountNumber,
						"Amount: $" + withdrawAmount
				};
				if (!_atmssHandler.doAPPrintStrArray(toPrint)) record("AP");
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
	
	/**
	 * Record operation.
	 *
	 * @param resultType the type of result of the current step
	 */
	private void record(String resultType) {
		super.record(currentStep, resultType);
	}
		
	/**
	 * Pause the process
	 *
	 * @param waitingTimeInSeconds the pause time in seconds
	 */
	private void pause(int waitingTimeInSeconds) {
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime < waitingTimeInSeconds*1000){}
	}
	
	/**
	 * Do keypad get choice.
	 *
	 * @param waitingTimeInSeconds the time limit for each key press
	 * @return the number choice in String format
	 */
	private String doKPGetChoice(long waitingTimeInSeconds) {
		while (true) {
			String currentInput = _atmssHandler.doKPGetSingleInput(waitingTimeInSeconds);
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
	 * @param userInput the user input string
	 * @return the integer converted from the input string; -1 if the input string is invalid
	 */
	private int choiceFromString(String userInput) {
		try {
			return Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Amount from string.
	 *
	 * @param userInput the user input string
	 * @return the integer converted from the input string; 0 if the input string is invalid
	 */
	private int amountFromString(String userInput, int amountLimit) {
		try {
			int amount = Integer.parseInt(userInput);
			if (amount > 0 && amount <= amountLimit && amount % 100 == 0){
				return amount;
			} else {
				return 0;
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Gets the withdraw plan.
	 * Both inventory and withdrawPlan are integer arrays of size 3.
	 * [0] for 100 note, [1] for 500 note, [2] for 1000 note
	 *
	 * @param inventory the cash inventory of the cash dispenser
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
