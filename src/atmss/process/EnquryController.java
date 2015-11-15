/**
 * 
 */
package atmss.process;

import atmss.MainController;

/**
 * @author SXM
 *
 */
public class EnquryController extends ProcessController{
	
	private String accountNumber;
	private double balance = -1;
	/**
	 * 
	 */
	public EnquryController(String AccountNumber, MainController MainController) {
		// TODO Auto-generated constructor stub
		super(AccountNumber, MainController);
	}
	
	public Boolean doEnqury() {
		
		accountNumber = getAccountNumber();
		balance = this._mainController.doBAMSCheckBalance(accountNumber);
		
		if (balance != -1)		
			return true;
		else 
			return false;
	}
	
	private String getAccountNumber() {
		String accountNumber = "";
		String[] allAccountNumber = {};
		while(true){
			this._mainController.doDisplay(allAccountNumber);
			String currentInput = this._mainController.doGetKeyInput();
			if (currentInput.equals("CANCEL")) {
				return "";
			}
			if (isNumber(currentInput)) {
				int accountChosen = Integer.parseInt(currentInput);
				if(accountChosen <= allAccountNumber.length){
					accountNumber = allAccountNumber[accountChosen - 1];
					break;
				}
			}
		}
		return accountNumber;
	}
	
	private boolean isNumber(String currentInput) {
		if (currentInput.equals("1") || currentInput.equals("2") || 
			currentInput.equals("3") || currentInput.equals("4") || 
			currentInput.equals("5") || currentInput.equals("6") || 
			currentInput.equals("7") || currentInput.equals("8") ||
			currentInput.equals("9") || currentInput.equals("0"))
			return true;
		else
			return false;
	}

}
