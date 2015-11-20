/**
 * 
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;
import atmss.Session;

/**
 * @author SXM
 *
 */
public class TransferController extends ProcessController{

	private String srcAccountNumber;
	private String desAccountNumber;
	private double amountToTransfer;
	private double accountBalance;
	
	private final String OPERATION_NAME = "Transfer";
	private final String FAILED_FROM_DISPLAY = "No response from display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_BAMS = "Failed from BAMS";
	private final String FAILED_FROM_ADVICEPRINTER = "No response from advice printer";
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	private final String FAILED_CHOOSE_ACCOUNT = "Failed to choose an account";
	private final String FAILED_INPUT_ACCOUNT = "Failed to input an account";
	private final String FAILED_INPUT_AMOUNT = "Failed to input transfer amount";
	private final String FAILED_CONFIRM_AMOUNT = "Failed to confirm transfer amount";
	private final String PROMPT_FOR_AMOUNT = "Please type in your transfer amount";
	private final String PROMPT_FOR_SRCACCOUNT = "Please choose your account";
	private final String PROMPT_FOR_DESACCOUNT = "Please type in your target account";
	private final String PROMPT_FOR_CONFIRM = "Please confirm your transfer amount";
	private final String SHOW_SUCCESS = "Succeeded! The transfer operation succeeds.";
	private final String SHOW_FAILURE = "Failed! The transfer operation failed.";
	
	public TransferController(Session session) {
		super(session);
	}
	
	public Boolean doTransfer() {		
		if (!this.getSrcAccountNumber()) {
			return failProcess(SHOW_FAILURE);
		}
		
		if (!this.getDesAccountNumber()) {
			return failProcess(SHOW_FAILURE);
		}
		
		if (!this.getAmountToTransfer()) {
			return failProcess(SHOW_FAILURE);
		}

		if (!this.doPrintReceipt()) {
			return failProcess(SHOW_FAILURE);
		}
		
//		System.out.println("srcAccountNumber: " + srcAccountNumber);
//		System.out.println("desAccountNumber: " + desAccountNumber);
//		System.out.println("amountToTransfer: " + amountToTransfer);
//		System.out.println(_session.getCardNo());
				
		if (!this._atmssHandler.doBAMSTransfer(desAccountNumber, srcAccountNumber, amountToTransfer, _session)) {
//			System.out.println("Line 63"); 
			return failProcess(SHOW_FAILURE);
		}
		
		this.recordOperation();
		this._atmssHandler.doDisAppendUpper(SHOW_SUCCESS);
		return true;
	}
	
	private boolean getSrcAccountNumber() {

		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if (allAccountsInCard.length == 0){
			return this.failProcess(FAILED_FROM_BAMS);			
		}
			
		if (!this._atmssHandler.doDisDisplayUpper(allAccountsInCard)){
			return this.failProcess(FAILED_FROM_DISPLAY);
		}
		
		if (!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_SRCACCOUNT)) {
			return this.failProcess(FAILED_FROM_DISPLAY);
		}
		
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_SRCACCOUNT, allAccountsInCard))) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		
		while (true){		
			String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(20);		
			if (accountSelectedByUser != null) {
				try{
					int accountChosen = Integer.parseInt(accountSelectedByUser);
					if (accountChosen <= allAccountsInCard.length) {
						this.srcAccountNumber = allAccountsInCard[accountChosen - 1];
						return true;
					}
				}
				catch(NumberFormatException e) {
					if(accountSelectedByUser.equals("CANCEL")) {
						return failProcess(FAILED_CHOOSE_ACCOUNT);
					}
				}
			}
			else {
				return this.failProcess(FAILED_FROM_KEYPAD);		
			}
		}
	}
	
	private boolean getDesAccountNumber() {
		String desAccountNumber ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT})) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if (!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_DESACCOUNT))
				return this.failProcess(FAILED_FROM_DISPLAY);
			
			desAccountNumber = this._atmssHandler.doKPGetAccountNum(30);
			if (desAccountNumber == null) {
				return failProcess(FAILED_INPUT_ACCOUNT);
			}
			
			if (!this._atmssHandler.doDisClearAll()) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM})) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if (!this._atmssHandler.doDisAppendUpper(desAccountNumber)) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(10);
			if (confirmInput != null) {
				switch(confirmInput) {
					case "ENTER":
						this.desAccountNumber = desAccountNumber;
						return true;
					case "CANCEL":
						return failProcess(SHOW_FAILURE);
				}
			} else {
				return failProcess(FAILED_CONFIRM_AMOUNT);
			}
		}		
	}
	
	private boolean getAmountToTransfer() {		
		String amountToTransfer ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT})) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			amountToTransfer = this._atmssHandler.doKPGetDoubleMoneyAmount(30);
			accountBalance = this._atmssHandler.doBAMSCheckBalance(this.srcAccountNumber, _session);
			if (Double.parseDouble(amountToTransfer) > accountBalance) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{
						FAILED_FROM_BALANCE, "You can only withdraw $" + accountBalance
						})) {
					System.out.println("You can only withdraw $" + accountBalance);
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
			}
			if (amountToTransfer == null) {
				return failProcess(FAILED_INPUT_AMOUNT);
			}
			
			if (!this._atmssHandler.doDisClearAll()) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM})) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			if (!this._atmssHandler.doDisAppendUpper(amountToTransfer)) {
				return failProcess(FAILED_FROM_DISPLAY);
			}
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(10);
			if (confirmInput != null) {
				switch(confirmInput) {
					case "ENTER":
						this.amountToTransfer = Double.parseDouble(amountToTransfer);
						return true;
					case "CANCEL":
						return failProcess(SHOW_FAILURE);
				}
			} else {
				return failProcess(FAILED_CONFIRM_AMOUNT);
			}
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
	
	private boolean doPrintReceipt(){
		if(!this._atmssHandler .doAPPrintStrArray(new String[] {
				srcAccountNumber, desAccountNumber, Double.toString(this.amountToTransfer)}))
			return failProcess(FAILED_FROM_ADVICEPRINTER);
		return true;
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
				"Result: " + "Failed;" + 
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));		
	}
	
	private boolean failProcess(String FailedReason){
		this._atmssHandler.doDisClearAll();
		this._atmssHandler.doDisDisplayUpper(new String[] {FailedReason});
		recordOperation(FailedReason);
		return false;
	}
}
