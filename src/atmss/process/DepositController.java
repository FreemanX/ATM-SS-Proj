/**
 * 
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;
import atmss.Session;

/**
 * @author Lihui
 *
 */
public class DepositController extends ProcessController {

	//private int amount;
	private String _accountToDeposit;
	private int _amountToDeposit;

	private final String OPERATION_NAME = "Deposit";
	private final String FAILED_FROM_DISPLAY = "No response from display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_ENVELOPDISPENSER = "No response from envelop dispenser";
	private final String FAILED_FROM_ADVICEPRINTER = "No response from advice printer";
	private final String FAILED_FROM_BAMS = "Failed to get approval from BAMS";
	private final String[] ERROR_NOT_EQUAL = {"The new passwords do not equal", "Please type your old password:"};
	private final String[] PROMPT_FOR_OLD_PASSWORD = {"Please type your old password:"};
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please type your new password:"};
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please type your new password again:"};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final String[] SHOW_SUCCESS = {"Succeeded!", "The password has been changed."};
	private final String[] SHOW_FAILURE = {"Failed!", "The password may not be changed."};
	
	/**
	 * 
	 */
	public DepositController(Session Session, MainController MainController) {
		// TODO Auto-generated constructor stub
		super(Session, MainController);
	}

	public Boolean doDeopsit() {
		// boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */

		// prompt for account to deposit
		this._accountToDeposit = doGetAccountToDeposit();

		// prompt for amount to deposit
		this._amountToDeposit = (int) doGetAmountToDeposit();

		if (!this._atmssHandler .doPrintReceipt(_accountToDeposit, _amountToDeposit))
			return false;

		if (!this._atmssHandler.doEjectEnvelop())
			return false;

		if (!this._atmssHandler.doEatEnvelop())
			return false;
		return true;
	}

	private String doGetAccountToDeposit() {
		String accountToDeposit = "";
		String[] allAccountsInCard = this._atmssHandler.doBAMSCheckAccounts(this._session.getCardNo());
		if(allAccountsInCard.length == 0)
			recordOperation
		
		/*		String[] allAccountsInCard = {};
		boolean validInputByUser = false;
		while (!validInputByUser) {
			try {
				this._atmssHandler.doDisplay(allAccountsInCard);
				int accountChosenByUser = Integer.parseInt(this._atmssHandler.doGetKeyInput());
				if (accountChosenByUser <= allAccountsInCard.length) {
					accountToDeposit = allAccountsInCard[accountChosenByUser - 1];
					validInputByUser = true;
				}
			} catch (NumberFormatException e) {
				continue;
			}
		}*/
		return accountToDeposit;
	}

	private double doGetAmountToDeposit() {

		boolean confirmAmountToDeposit = false;
		String userInputAmountToDeposit = "";
		{

			/*boolean userHasInputDecimalPoint = false;
			int digitsAfterDecimalPoint = 0;

			inputAmount: while (true) {
				String currentButton = this._atmssHandler.doGetKeyInput();
				switch (currentButton) {
				case "enter":
					break inputAmount;
				case ".":
					if (userInputAmountToDeposit.length() != 0 && userHasInputDecimalPoint == false) {
						userHasInputDecimalPoint = true;
						userInputAmountToDeposit = userInputAmountToDeposit + ".";
					}
					break;
				default:
					if (digitsAfterDecimalPoint < 2 && userHasInputDecimalPoint == false) {
						userInputAmountToDeposit = userInputAmountToDeposit + currentButton;
					} else if (digitsAfterDecimalPoint < 2 && userHasInputDecimalPoint == true) {
						userInputAmountToDeposit = userInputAmountToDeposit + currentButton;
						digitsAfterDecimalPoint += 1;
					}
					break;
				}
			}*/
			this._atmssHandler.doDisplay(new String[] { "Confirm deposit amount: ", userInputAmountToDeposit });
			confirmAmountToDeposit = this._atmssHandler.doGetKeyInput().equals("enter");
		}
		while (!confirmAmountToDeposit);

		return Integer.parseInt(userInputAmountToDeposit);
	}
	
	private void recordOperation(){
		String description = 
				"Card Number: " + this._session.getCardNo() + ";" +
				"Result: " + "Succeeded; ";
		operationCache.add(new Operation(OPERATION_NAME,description));
	}
	
	private void recordOperation(String FailedReason){
		String description = 
				"Card Number: " + this._session.getCardNo() + ";" +
				"Result: " + "Succeeded; "+ 
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
		
	}

}
