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
	
	//private final String OPERATION_NAME = "Operation : Enquiry";
	private final String FAILED_FROM_DISPLAY = "Failure: no response from display";
	private final String FAILED_FROM_PRINTER = "Failure: no response from advice printer";
	private final String PROMPT_FOR_ACCOUNT = "Please choose your account to Enquiry";
	private final String SHOW_SUCCESS = "Your balance is $";
	private final String[] PRINT_NOTE_SELECTION = {
			 "Press 1-> Print advice", "Press 2-> Quit with out printing"
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
		//recordOperation("Enquiry : Clear the display", 0, "Success");
		
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
			String nextInput = this._atmssHandler.doKPGetSingleInput(10);
			
			if (nextInput == null || nextInput.equals("2")) {
				recordOperation("Enquiry : choose not to print the receipt", 0, "Success");
				break;
			} else if (nextInput.equals("1")) {
				if (!this.doPrintReceipt()) {
					return false;
				}
				recordOperation("Enquiry : choose to print the receipt", 0, "Success");
				break;
			} else {
				if (!this._atmssHandler.doDisDisplayUpper(new String[] {
						SHOW_SUCCESS + balance, PRINT_NOTE_SELECTION[0], 
						//PRINT_NOTE_SELECTION[1],PRINT_NOTE_SELECTION[2], "Wrong input!"
				})) {
					failProcess("Enquiry : display error message", 5, FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(" Enquiry : display error message", 0, "Success");
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
		
		String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(200);
		
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
	
	private String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "-> " + i + ": " + Body[i-1];
		}
		return lines;
	}
	
	private boolean doPrintReceipt(){
		if(!this._atmssHandler.doAPPrintStrArray(new String[] {
				new String("Operation Name: Enquiry"),
				new String("Card Number: " + _session.getCardNo()),
				new String("Account: " + accountNumber), 
				new String("Balance: $" + Double.toString(this.balance))
		})) {
			return failProcess("Enquiry : print the receipt", 1, FAILED_FROM_PRINTER);
		}
		recordOperation("Enquiry : print the receipt", 0, "Success");

		return true;
	}
	/*
	private boolean doPrintReceipt(String msg){
		if(!this._atmssHandler.doAPPrintStrArray(new String[] {msg})) {
			return failProcess("Print the receipt", 1);
		}
		recordOperation("Print the receipt", 0, "Success");
		return true;
	}
	*/
	private void recordOperation(String operation, int type, String result){
		/*
		String result = "";
		switch (type) {
			case 0:
				result = "Successful";				
				break;
			case 1:
				result = "Failed";
				doPrintReceipt("No response from advice printer.");
				break;
			case 5:
				result = "Failed";
				doPrintReceipt("No response from display.");
				break;
			case 7:
				result = "Failed";
				doPrintReceipt("No input from keypad.");
				break;
			case 8:
				result = "Failed";
				doPrintReceipt("Process cancelled.");
				break;
			case 10:
				result = "Failed";
				doPrintReceipt("No response from BAMS.");
				break;
		}
	    */
		operationCache.add(new Operation(operation, type, result));
	}
	
	private boolean failProcess(String operation, int type, String desc){
		//this._atmssHandler.doDisClearAll();
		//this._atmssHandler.doDisDisplayUpper(new String[] {operation});
		recordOperation(operation, type, desc);
		printOpCache();
		return false;
	}

}
