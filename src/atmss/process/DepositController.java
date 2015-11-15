/**
 * 
 */
package atmss.process;

import atmss.MainController;

/**
 * @author Lihui
 *
 */
public class DepositController extends ProcessController {

	private int amount;
	private String _accountToDeposit;
	private double _amountToDeposit;

	/**
	 * 
	 */
	public DepositController(String CardNumber, MainController MainController) {
		// TODO Auto-generated constructor stub
		super(CardNumber, MainController);
	}

	public Boolean doDeopsit() {
		//boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */

		//prompt for account to deposit
		_accountToDeposit = doGetAccountToDeposit();
		
		
		//prompt for amount to deposit
		_amountToDeposit = doGetAmountToDeposit();
		
		if(true /*!this._mainController .doPrintReceipt(_accountToDeposit, _amountToDeposit)*/)
			return false;
		
		if(!this._mainController.doEjectEnvelop())
			return false;
		
		if(!this._mainController.doEatEnvelop())
			return false;
		return true;
	}
	
	public String doGetAccountToDeposit(){
		String accountToDeposit = "";
		//String[] allAccountsInCard = this._mainController.doBAMSCheckAccounts(this._cardNumber);
		String[] allAccountsInCard = {};
		boolean validInputByUser = false;
		while(!validInputByUser){
		try{
			this._mainController.doDisplay(allAccountsInCard);
			int accountChosenByUser = Integer.parseInt(this._mainController.doGetKeyInput());
			if(accountChosenByUser <= allAccountsInCard.length){
				accountToDeposit = allAccountsInCard[accountChosenByUser-1];
				validInputByUser = true;
			}
		}catch(NumberFormatException e){
			continue;
		}
		}
		return accountToDeposit;
	}
	
	public double doGetAmountToDeposit(){

		boolean confirmAmountToDeposit = false;
		String userInputAmountToDeposit = "";
		{
		
		boolean userHasInputDecimalPoint = false;
		int digitsAfterDecimalPoint = 0;

		inputAmount: while (true) {
			String currentButton = this._mainController.doGetKeyInput();
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
		}
		this._mainController.doDisplay(new String[] {"Confirm deposit amount: ", userInputAmountToDeposit});
		confirmAmountToDeposit = this._mainController.doGetKeyInput().equals("enter");
		}while(!confirmAmountToDeposit);
		
		return Double.parseDouble(userInputAmountToDeposit);
	}

}
