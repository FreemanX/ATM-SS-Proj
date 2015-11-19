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
	private String accountToDeposit;
	private int amountToDeposit;

	private final String OPERATION_NAME = "Deposit";
	private final String FAILED_FROM_DISPLAY = "No response from display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_ENVELOPDISPENSER = "No response from envelop dispenser";
	private final String FAILED_FROM_DEPOSITCOLLECTOR = "No response from deposit collector";
	private final String FAILED_FROM_ADVICEPRINTER = "No response from advice printer";
	private final String FAILED_FROM_BAMS = "Failed from BAMS";
	private final String FAILED_CHOOSE_ACCOUNT = "Failed to choose an account";
	private final String FAILED_INPUT_AMOUNT = "Failed to input deposit amount";
	private final String FAILED_CONFIRM_AMOUNT = "Failed to confirm deposit amount";
	private final String PROMPT_FOR_ACCOUNT = "Please choose your account";
	private final String PROMPT_FOR_AMOUNT = "Please type in your deposit amount";
	private final String PROMPT_FOR_CONFIRM = "Please confirm your deposit amount";
	private final String PROMPT_FOR_COLLECT_ENVELOP = "Please collect the envelop and put cheque/cash and receipt into the envelop";
	private final String PROMPT_FOR_RETURN_ENVELOP = "Please put the envelop with cheque/cash to deposit collector";
	private final String SHOW_SUCCESS = "Succeeded! The deposit operation succeeds.";
	private final String SHOW_FAILURE = "Failed! The deposit operation failed.";
	
	/**
	 * 
	 */
	public DepositController(Session Session) {
		// TODO Auto-generated constructor stub
		super(Session);
	}

	public Boolean doDeopsit() {
		// boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */

		// prompt for account to deposit
		if (!this.doGetAccountToDeposit())
			return failProcess(SHOW_FAILURE);

		// prompt for amount to deposit
		if (!this. doGetAmountToDeposit())
			return failProcess(SHOW_FAILURE);

		if (!this.doPrintReceipt())
			return failProcess(SHOW_FAILURE);

		if (!this.doEjectEnvelop())
			return failProcess(SHOW_FAILURE);

		if (!this.doEatEnvelop())
			return failProcess(SHOW_FAILURE);
		
		this.recordOperation();
		this._atmssHandler.doDisAppendUpper(SHOW_SUCCESS);
		return true;
	}
	
	private boolean doEatEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_RETURN_ENVELOP}))
			return failProcess(FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEatEnvelop())
			return failProcess(FAILED_FROM_DEPOSITCOLLECTOR);
		
		return true;
	}
	
	private boolean doEjectEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_COLLECT_ENVELOP}))
			return failProcess(FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEjectEnvelop())
			return failProcess(FAILED_FROM_ENVELOPDISPENSER);

		return true;
	}
	
		
	private boolean doPrintReceipt(){
		if(!this._atmssHandler .doAPPrintStrArray(new String[] {accountToDeposit, Integer.toString(this.amountToDeposit)}))
			return failProcess(FAILED_FROM_ADVICEPRINTER);
		return true;
	}
	private boolean doGetAccountToDeposit() {
		if(this._atmssHandler.doDisClearAll())
			return this.failProcess(FAILED_FROM_DISPLAY);

		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if(allAccountsInCard.length == 0){
			return this.failProcess(FAILED_FROM_BAMS);
			
		}
			
		if(!this._atmssHandler.doDisDisplayUpper(allAccountsInCard)){
			return this.failProcess(FAILED_FROM_DISPLAY);
		}
		
		if(!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_ACCOUNT))
			return this.failProcess(FAILED_FROM_DISPLAY);
		
		int accountNoSelectedByUser = allAccountsInCard.length + 1;
		
		while(accountNoSelectedByUser > allAccountsInCard.length){
		
		String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(50000);
		
		if(accountSelectedByUser!=null){
			try{
				accountNoSelectedByUser = Integer.parseInt(accountSelectedByUser);
			}
			catch(NumberFormatException e){
				if(accountSelectedByUser.equals("CANCEL"))
					return failProcess(FAILED_CHOOSE_ACCOUNT);
				
			}
		}
		else return this.failProcess(FAILED_FROM_KEYPAD);
		
		}
		
		this.accountToDeposit = allAccountsInCard[accountNoSelectedByUser-1];

		return true;

	}

	private boolean doGetAmountToDeposit() {

		boolean confirmAmountToDeposit = false;
		String userInputAmountToDeposit ="";
		
		{
			if(!this._atmssHandler.doDisClearAll())
				return failProcess(FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT}))
				return failProcess(FAILED_FROM_DISPLAY);
			
			userInputAmountToDeposit = this._atmssHandler.doKPGetIntegerMoneyAmount(5000);
			if(userInputAmountToDeposit == null)
				return failProcess(FAILED_INPUT_AMOUNT);
			
			if(!this._atmssHandler.doDisClearAll())
				return failProcess(FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM}))
				return failProcess(FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisAppendUpper(userInputAmountToDeposit))
				return failProcess(FAILED_FROM_DISPLAY);
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(5000);
			if(confirmInput != null){
				switch(confirmInput){
				case "ENTER":
					confirmAmountToDeposit = true;
					break;
				case  "CANCEL":
					return failProcess(SHOW_FAILURE);
				}
			}
			else return failProcess(FAILED_CONFIRM_AMOUNT);
		}while (!confirmAmountToDeposit);
		
		this.amountToDeposit = Integer.parseInt(userInputAmountToDeposit);
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
				"Result: " + "Failed;"+ 
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
