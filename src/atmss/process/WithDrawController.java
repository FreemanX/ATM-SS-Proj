/**
 *
 */
package atmss.process;

import atmss.Operation;
import atmss.Session;

/**
 * @author DJY
 *
 */
public class WithDrawController extends ProcessController{

	private final String OPERATION_NAME = "Withdraw";
	private final String FAILED_FROM_BAMS_LOADING_ACCOUNTS = "Failed to read account information";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_CASHDISPENSER = "No response from the cash dispenser";
	private final String FAILED_FROM_USER_CANCELLING = "The operation has been cancelled";
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	private final String FAILED_FROM_INVENTORY = "Not enough inventory to withdraw";
	private final String PROMPT_FOR_CHOICE_HEADER = "Please choose your account:";
	private final String PROMPT_FOR_CHOICE_ERR_HEADER = "Not a valid choice! Please choose your account:";
	private final String[] PROMPT_FOR_AMOUNT = {"You can only withdraw 100, 500, 1000 notes.","Please input your withdraw amount:"};
	private final String[] PROMPT_FOR_AMOUNT_ERR = {"Invalid amount!","The withdraw amount must be divisible by 100 and less or equal to 10000","Please input your withdraw amount again:"};
	private final String[] PROMPT_FOR_COLLECTION = {"Operatoin succeeded!", "Please collect your money."};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final int TIME_LIMIT = 10; // seconds
	private final int AMOUNT_LIMIT = 10000;
	private final String KP_CANCEL = "CANCEL";
	private String _currentStep = OPERATION_NAME;

	public WithDrawController(Session CurrentSession) {
		super(CurrentSession);
	}

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
				try { this.wait(3000);} catch (InterruptedException e) {}
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
					try { this.wait(3000);} catch (InterruptedException e) {}
					return false;
				} else if (userInput.equals(KP_CANCEL)) {
					record("USER");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
						record("Dis");
						return false;
					}
					try { this.wait(3000);} catch (InterruptedException e) {}
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
					try { this.wait(3000);} catch (InterruptedException e) {}
					return false;
				} else if (userInput.equals(KP_CANCEL)) {
					record("USER");
					if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
						record("Dis");
						return false;
					}
					try { this.wait(3000);} catch (InterruptedException e) {}
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
				try { this.wait(3000);} catch (InterruptedException e) {}
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
				try { this.wait(3000);} catch (InterruptedException e) {}
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
				try { this.wait(3000);} catch (InterruptedException e) {}
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
	
	private void record(String Type) {
		switch(Type) {
			case "AP" : recordFailure(1);break;
			case "CR" : recordFailure(2);break;
			case "CD" : recordFailure(3);break;
			case "DC" : recordFailure(4);break;
			case "Dis": recordFailure(5);break;
			case "ED" : recordFailure(6);break;
			case "KP" : recordFailure(7);break;
			case "USER" : recordFailure(8);break;
			case "BAMS" : recordFailure(10);break;
			default: recordSuccess(Type);break;
		}
	}
	
	private void recordSuccess(String detail) {
		operationCache.add(new Operation(_currentStep, 0, "Success: "+detail));
	}
	
	private void recordFailure(int Type) {
		String description;
		switch(Type) {
			case 1: description = "Failure: no response from advice printer";break;
			case 2: description = "Failure: no response from card reader";break;
			case 3: description = "Failure: no response from cash dispenser";break;
			case 4: description = "Failure: no response from deposit collector";break;
			case 5: description = "Failure: no response from display";break;
			case 6: description = "Failure: no response from evelop dispenser";break;
			case 7: description = "Failure: no response from keypad";break;
			case 8: description = "Failure: cancellation from user";break;
			case 10: description = "Failure: disapproval from bank system (BAMS)";break;
			default:description = "Failure: unknown reason";break;
		}
		operationCache.add(new Operation(_currentStep, Type, description));
		_atmssHandler.doAPPrintStrArray(new String[]{_currentStep,description});
	}
	
	private void askForPrinting(String AccountNumber, int Amount){
		String[] toDisplay = {
				"Operation succeeded!",
				"You have withdrawn $" + Amount + "from account: " + AccountNumber,
				
				
				
				"Please choose an account to withdraw:",
				"Press 1 -> Print the advice",
				"Press 2 -> Quit without printing",
				"Press CANCEL -> Quit process"

		};
		if (!_atmssHandler.doDisDisplayUpper(toDisplay)) return;
		while (true) {
			String userInput = _atmssHandler.doKPGetSingleInput(TIME_LIMIT);
			if (userInput == null) return;
			if (userInput.equals("1")) {
				String[] toPrint = {
						"Operation Name : " + OPERATION_NAME,
						"Card Number    : " + _session.getCardNo(),
						"Account Number : " + AccountNumber,
						"Amount         : $" + Amount
				};
				_atmssHandler.doAPPrintStrArray(toPrint);
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
	
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
