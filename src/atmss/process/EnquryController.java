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
public class EnquryController extends ProcessController{
	
	private String accountNumber;
	private double balance;
	
	private final String OPERATION_NAME = "Enquiry";
	private final String FAILED_FROM_BAMS = "Failed from BAMS";
	private final String FAILED_FROM_DISPLAY = "No response from display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_CHOOSE_ACCOUNT = "Failed to choose an account";
	private final String PROMPT_FOR_ACCOUNT = "Please choose your account";
	private final String SHOW_SUCCESS = "Your balance is ";
	private final String SHOW_FAILURE = "Failed! The enquiry operation failed.";
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	
	public EnquryController(Session session) {
		super(session);
	}
	
	public Boolean doEnqury() {
		
		if (!this.getAccountNumber()) {
			return failProcess(SHOW_FAILURE);
		}
		
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		
		this.balance = _atmssHandler.doBAMSCheckBalance(this.accountNumber, _session);		
		this.recordOperation();
		this._atmssHandler.doDisDisplayUpper(new String[] {SHOW_SUCCESS + balance});		
		return true;
	}
	
	private boolean getAccountNumber() {		

		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(_session);
		if (allAccountsInCard.length == 0){
			return this.failProcess(FAILED_FROM_BAMS);			
		}
			
		if (!this._atmssHandler.doDisDisplayUpper(allAccountsInCard)){
			return this.failProcess(FAILED_FROM_DISPLAY);
		}
		
		if (!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_ACCOUNT)) {
			return this.failProcess(FAILED_FROM_DISPLAY);
		}
		
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_ACCOUNT, allAccountsInCard))) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		
		while (true){		
			String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(20);
		
			if (accountSelectedByUser != null){
				try{
					int accountChosen = Integer.parseInt(accountSelectedByUser);
					if (accountChosen <= allAccountsInCard.length) {
						this.accountNumber = allAccountsInCard[accountChosen - 1];
						return true;
					}
				}
				catch(NumberFormatException e){
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
	
	private String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "-> " + i + ": " + Body[i-1];
		}
		return lines;
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
