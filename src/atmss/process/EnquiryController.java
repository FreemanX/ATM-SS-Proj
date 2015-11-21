/**
 * 
 */
package atmss.process;

import atmss.Operation;
import atmss.Session;

/**
 * @author SXM
 *
 */
public class EnquiryController extends ProcessController{
	
	private final String FAILED_FROM_BAMS = "Failure: no response from BAMS";
	private final String FAILED_FROM_KEYPAD = "Failure: no response from BAMS";
	private final String CANCELED = "Failure: cancellation from user";
	private String accountNumber;
	private double balance;
	
	private final int    TIME_OUT_LIMIT = 30;
	private final String FAILED_FROM_DISPLAY = "Failure: no response from display";
	private final String FAILED_FROM_PRINTER = "Failure: no response from advice printer";
	private final String PROMPT_FOR_ACCOUNT = "Please choose your account to Enquiry";
	private final String SHOW_SUCCESS = "Your balance is $";
	private final String[] PRINT_NOTE_SELECTION = {
			 "Press 1 -> Print advice", "Press 2 -> Quit with out printing"
	};
	
	public void printOpCache(){
		for(Operation op: operationCache){
			System.out.println(op.getName() + " "+ op.getType() + " "+op.getDes());
		}
	}
	
	public EnquiryController(Session session) {
		super(session);
	}
	
	public Boolean doEnquiry() {	
		if (!this._atmssHandler.doDisClearAll()) {
			return failProcess("Enquiry : display accounts", 5, FAILED_FROM_DISPLAY);
		}
		
		if (!this.getAccountNumber()) {
			return false;
		}
		recordOperation("Enquiry : get account", 0, "Success");
		
		this.balance = _atmssHandler.doBAMSCheckBalance(this.accountNumber, _session);
		if (!this._atmssHandler.doDisDisplayUpper(new String[] {
				SHOW_SUCCESS + balance, PRINT_NOTE_SELECTION[0], PRINT_NOTE_SELECTION[1]
		})) {
			failProcess("Enquiry : display the balance", 5, FAILED_FROM_DISPLAY);
			return false;
		}
		recordOperation("Enquiry : display the balance", 0, Double.toString(balance));
		
		while (true) {
			String nextInput = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
			
			if (nextInput == null || nextInput.equals("2")) {
				recordOperation("Enquiry : choose not to print the receipt", 0, "Success");
				break;
			} else if (nextInput.equals("1")) {
				if (!this.doPrintReceipt()) {
					return false;
				}
				recordOperation("Enquiry : choose to print the receipt", 0, "Success");
				break;
			}		
		}
		recordOperation("Enquiry : ", 0, "Success");
		this.printOpCache();
		return true;
	}
	
	private boolean getAccountNumber() {		
		if (!this._atmssHandler.doDisClearAll()) {
			return failProcess("Enquiry : clear the diaplay", 5, FAILED_FROM_DISPLAY);
		}
		recordOperation("Enquiry : clear the display", 0, "Success");
		//==================================================
		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(_session);
		if (allAccountsInCard.length == 0){
			return failProcess("Enquiry : get accounts from BAMS", 10, FAILED_FROM_BAMS);			
		}
		recordOperation("Enquiry : get accounts from BAMS", 0, "Success");

		//==================================================
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_ACCOUNT, allAccountsInCard))) {
			failProcess("Enquiry : display accounts",5, FAILED_FROM_DISPLAY);
			return false;
		}
		recordOperation("Enquiry : display accounts", 0, "Success");
		
		System.out.println("===================================display accounts!!!!!!");
		int accountNoSelectedByUser = allAccountsInCard.length + 1;
		
		while(accountNoSelectedByUser > allAccountsInCard.length){
		
		String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
		
		if(accountSelectedByUser!=null){
			try{
				accountNoSelectedByUser = Integer.parseInt(accountSelectedByUser);
			}
			catch(NumberFormatException e){
				if(accountSelectedByUser.equals("CANCEL"))
					return failProcess( "get account", 8, this.CANCELED);
				
			}
		}
		else return this.failProcess("get account",7,this.FAILED_FROM_KEYPAD);
		
		}
		this.accountNumber = allAccountsInCard[accountNoSelectedByUser-1];


		return true;
	}

	
	private boolean doPrintReceipt(){
		if(!this._atmssHandler.doAPPrintStrArray(new String[] {
				new String("Operation Name: Enquiry"),
				new String("Card Number:      " + _session.getCardNo()),
				new String("Account:                " + accountNumber), 
				new String("Balance:                $" + Double.toString(this.balance))
		})) {
			return failProcess("Enquiry : print the receipt", 1, FAILED_FROM_PRINTER);
		}
		recordOperation("Enquiry : print the receipt", 0, "Success");

		return true;
	}
	private void recordOperation(String operation, int type, String result){
		operationCache.add(new Operation(operation, type, result));
	}
	
	private boolean failProcess(String operation, int type, String desc){
		recordOperation(operation, type, desc);
		printOpCache();
		return false;
	}

}
