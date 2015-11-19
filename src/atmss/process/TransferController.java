/**
 * 
 */
package atmss.process;

import atmss.MainController;
import atmss.Session;

/**
 * @author SXM
 *
 */
public class TransferController extends ProcessController{

	private String srcAccountNumber;
	private String desAccountNumber;
	private double amount;
	/**
	 * 
	 */
	public TransferController(Session currentSession) {
		// TODO Auto-generated constructor stub
		super(currentSession);
	}
	
	public Boolean doTransfer() {
		
		srcAccountNumber = getSrcAccountNumber();
		desAccountNumber = getDesAccountNumber();
		amount = getAmountToTransfer();
		
		if (!this._mainController.doBAMSTransfer(srcAccountNumber, desAccountNumber, amount)) {
			return false;
		}
		if (!this._mainController.doBAMSUpdateBalance(srcAccountNumber, amount)) {
			return false;
		}
		if (!this._mainController.doBAMSUpdateBalance(desAccountNumber, amount)) {
			return false;
		}
		return true;
	}
	
	private String getSrcAccountNumber() {
		String srcAccountNumber = "";
		String[] allAccountNumber = {};
		while(true){
			this._mainController.doDisplay(allAccountNumber);
			String currentInput = this._mainController.doGetKeyInput();
			if (isNumber(currentInput)) {
				int accountChosen = Integer.parseInt(currentInput);
				if(accountChosen <= allAccountNumber.length){
					srcAccountNumber = allAccountNumber[accountChosen - 1];
					break;
				}
			}
		}
		return srcAccountNumber;
	}
	
	private String getDesAccountNumber() {
		String desAccountNumber = "";
		while (true) {
			String currentInput = this._mainController.doGetKeyInput();
			if (currentInput.equals("ENTER")) {
				return desAccountNumber;
			} else if (currentInput.equals("CLEAR")) {
				desAccountNumber = "";
			} else if (currentInput.equals("CANCEL")) {
				return "";
			} else {
				if (validInputFormat(desAccountNumber, currentInput)) {
					desAccountNumber += currentInput;
				}				
			}
		}
	}
	
	private double getAmountToTransfer() {
		String amountToTransfer = "";
		while (true) {
			String currentInput = this._mainController.doGetKeyInput();
			if (currentInput.equals("ENTER")) {
				return Double.parseDouble(amountToTransfer);
			} else if (currentInput.equals("CLEAR")) {
				amountToTransfer = "";
			} else if (currentInput.equals("CANCEL")) {
				return 0;
			} else {
				if (validInputFormat(amountToTransfer, currentInput)) {
					amountToTransfer += currentInput;
				}				
			}
		}
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
	
	private boolean validInputFormat(String currentString, String currentInput) {
		if (currentInput.equals("0") && currentString.length() == 0) 
			return false;
		if (currentInput.equals(".") && currentString.contains(".")) 
			return false;
		if (currentString.contains(".") && currentString.length() == currentString.indexOf("." + 2))
			return false;
		return true;
	}

}
