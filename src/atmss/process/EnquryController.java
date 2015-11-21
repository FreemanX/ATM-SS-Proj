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
public class EnquryController extends ProcessController{
	
	private final String FAILED_FROM_BAMS = "No response from BAMS";
	private final String FAILED_FROM_KEYPAD = "No response from BAMS";
	private final String CANCELED = "Canceled!";
	private String accountNumber;
	private double balance;
	
	//private final String OPERATION_NAME = "Operation : ENQURY";
	private final String FAILED_FROM_DISPLAY = "No response from display"
			+ "No response from advice printer";
	private final String PROMPT_FOR_ACCOUNT = "Please choose your account";
	private final String SHOW_SUCCESS = "Your balance is ";
	private final String[] PRINT_NOTE_SELECTION = {
			"Do you want to print note?", "Press ENTER to print", "Press CANCEL not to print"
	};
	
	public void printOpCache(){
		for(Operation op: operationCache){
			System.out.println(op.getName() + " "+ op.getType() + " "+op.getDes());
		}
	}
	
	public EnquryController(Session session) {
		super(session);
	}
	
	public Boolean doEnqury() {	
		if (!this._atmssHandler.doDisClearAll()) {
			return failProcess("ENQURY : Display accounts", 5, FAILED_FROM_DISPLAY);
		}
		//recordOperation("ENQURY : Clear the display", 0, "Success");
		
		if (!this.getAccountNumber()) {
			return false;
		}
		recordOperation("ENQURY : Get account", 0, "Success");
		
		this.balance = _atmssHandler.doBAMSCheckBalance(this.accountNumber, _session);
		if (!this._atmssHandler.doDisDisplayUpper(new String[] {
				SHOW_SUCCESS + balance, PRINT_NOTE_SELECTION[0], PRINT_NOTE_SELECTION[1],PRINT_NOTE_SELECTION[2]
		})) {
			failProcess("ENQURY : Display the balance", 5, FAILED_FROM_DISPLAY);
			return false;
		}
		recordOperation("Display the balance", 0, Double.toString(balance));
		
		while (true) {
			String nextInput = this._atmssHandler.doKPGetSingleInput(10);
			
			if (nextInput == null || nextInput.equals("2")) {
				recordOperation("Choose not to print the receipt", 0, "Success");
				break;
			} else if (nextInput.equals("1")) {
				if (!this.doPrintReceipt()) {
					return false;
				}
				recordOperation("Choose to print the receipt", 0, "Success");
				break;
			} else {
				if (!this._atmssHandler.doDisDisplayUpper(new String[] {
						SHOW_SUCCESS + balance, PRINT_NOTE_SELECTION[0], 
						PRINT_NOTE_SELECTION[1],PRINT_NOTE_SELECTION[2], "Wrong input!"
				})) {
					failProcess("ENQURY : Display error message", 5, FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(" ENQURY : Display error message", 0, "Success");
			}		
		}
		recordOperation("ENQURY : ", 0, "Success");
		this.printOpCache();
		return true;
	}
	
	private boolean getAccountNumber() {		
		if (!this._atmssHandler.doDisClearAll()) {
			return failProcess("ENQURY : Clear the diaplay", 5, FAILED_FROM_DISPLAY);
		}
		recordOperation("ENQURY : Clear the display", 0, "Success");
		//==================================================
		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(_session);
		if (allAccountsInCard.length == 0){
			return failProcess("ENQURY : Get accounts from BAMS", 10, FAILED_FROM_BAMS);			
		}
		recordOperation("ENQURY : Get accounts from BAMS", 0, "Success");

		//==================================================
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_ACCOUNT, allAccountsInCard))) {
			failProcess("ENQURY : Display accounts",5, FAILED_FROM_DISPLAY);
			return false;
		}
		recordOperation("ENQURY : Display accounts", 0, "Success");

		//==================================================
		while (true){		
			String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(10);	
			if (accountSelectedByUser != null){
				try{
					int accountChosen = Integer.parseInt(accountSelectedByUser);
					if (accountChosen <= allAccountsInCard.length) {
						this.accountNumber = allAccountsInCard[accountChosen - 1];
						recordOperation("Select an account", 0, accountNumber);
						return true;
					}
				}
				catch(NumberFormatException e){
					if(accountSelectedByUser.equals("CANCEL")) {
						return failProcess("ENQURY : Select account", 8, CANCELED);
					}
				}
			}
			else {
				return failProcess("ENQURY : Select account", 7, FAILED_FROM_KEYPAD);		
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
		if(!this._atmssHandler.doAPPrintStrArray(new String[] {
				new String("Operation Name : ENQURY"),
				new String("Card Number     : " + _session.getCardNo()),
				new String("Account Number : " + accountNumber), 
				new String("Balance          : $" + Double.toString(this.balance))
		})) {
			return failProcess("ENQURY : Print the receipt", 1, FAILED_FROM_KEYPAD);
		}
		recordOperation("ENQURY : Print the receipt", 0, "Success");

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
