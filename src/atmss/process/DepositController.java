/**
 * 
 */
package atmss.process;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

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

	private final String OPERATION_NAME = "DEPOSIT: ";
	private final String FAILED_FROM_DISPLAY = "No response from display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_ENVELOPDISPENSER = "No response from envelop dispenser";
	private final String FAILED_FROM_DEPOSITCOLLECTOR = "No response from deposit collector";
	private final String FAILED_FROM_ADVICEPRINTER = "No response from advice printer";
	private final String FAILED_FROM_BAMS = "Failed from BAMS";
	private final String PROMPT_FOR_ACCOUNT = ">Please select account to deposit :";
	private final String PROMPT_FOR_AMOUNT = ">Please type in your deposit amount :";
	private final String PROMPT_FOR_CONFIRM1 = ">Please confirm your deposit amount by ENTER.";
	private final String PROMPT_FOR_CONFIRM2 = ">Press 0 to reinput amount.";
	private final String PROMPT_FOR_COLLECT_ENVELOP = ">Please collect the envelop and put cheque/cash and receipt into the envelop";
	private final String PROMPT_FOR_RETURN_ENVELOP = ">Please put the envelop with cheque/cash to deposit collector";
	private final String CANCELED = "Canceled!";
	
	/**
	 * 
	 */
	public DepositController(Session Session) {
		// TODO Auto-generated constructor stub
		super(Session);
	}

	public void printOpCache(){
		String[] operations = this.operationCache.toString().split(",");
		for(String operation: operations){
			System.out.println(operation);
		}
	}
	public Boolean doDeposit() {
		// boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */
		// prompt for account to deposit
		
		//this.operationCache.add(new Operation("User selects enters DEPOSIT process", 0 , "Success"));
		
		if (!this.doGetAccountToDeposit())
			return false;
		this.operationCache.add(new Operation("DEPOSIT : Select account",0, this.accountToDeposit));
		
		if (!this. doGetAmountToDeposit())
			return false;
		
		if (!this.doPrintReceipt())
			return false;
		this.operationCache.add(new Operation("DEPOSIT : Print reiceipt", 0, "Success"));
		
		if (!this.doEjectEnvelop())
			return false;
		this.operationCache.add(new Operation("DEPOSIT : Eject envelop", 0 , "Success"));
		
		if (!this.doEatEnvelop())
			return false;
		this.operationCache.add(new Operation("DEPOSIT : Eat envelop", 0 ,"Success"));
		

		this.printLastOperation();
		printOpCache();
		return true;
	}
	
	private boolean doEatEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_RETURN_ENVELOP}))
			return failProcess("Eat envelop",5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEatEnvelop())
			return failProcess("Eat envelop",4,this.FAILED_FROM_DEPOSITCOLLECTOR);
		
		return true;
	}
	
	private boolean doEjectEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_COLLECT_ENVELOP}))
			return failProcess("Eject envelop", 5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEjectEnvelop())
			return failProcess("Eject envelop", 6, this.FAILED_FROM_ENVELOPDISPENSER);

		return true;
	}
	
		
	private boolean doPrintReceipt(){
		if(!this._atmssHandler .doAPPrintStrArray(new String[] {"Account to deposit: "+accountToDeposit, "Amount to deposit: $"+ Integer.toString(this.amountToDeposit)}))
			return failProcess("Print receipt",1,this.FAILED_FROM_ADVICEPRINTER);
		return true;
	}
	private boolean doGetAccountToDeposit() {

		if(!this._atmssHandler.doDisClearAll())
			return this.failProcess("Get account", 5, this.FAILED_FROM_DISPLAY);
		
		List<String> accountsToChooseDisplay = new ArrayList<String>(Arrays.asList(new String[] {PROMPT_FOR_ACCOUNT}));

		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if(allAccountsInCard.length == 0){
			return this.failProcess("Get account", 10, this.FAILED_FROM_BAMS);		
		}
		
		int index = 1;
		for (String account:allAccountsInCard){
			accountsToChooseDisplay.add("Account "+index+": "+account );
			index += 1;
		}
		

		
		
		if(!this._atmssHandler.doDisDisplayUpper(accountsToChooseDisplay.toArray(new String[0])))
			return this.failProcess("Get account", 5, FAILED_FROM_DISPLAY);
		
		int accountNoSelectedByUser = allAccountsInCard.length + 1;
		
		while(accountNoSelectedByUser > allAccountsInCard.length){
		
		String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(3000);
		
		if(accountSelectedByUser!=null){
			try{
				accountNoSelectedByUser = Integer.parseInt(accountSelectedByUser);
			}
			catch(NumberFormatException e){
				if(accountSelectedByUser.equals("CANCEL"))
					return failProcess( "Get account", 8, this.CANCELED);
				
			}
		}
		else return this.failProcess("Get account",7,this.FAILED_FROM_KEYPAD);
		
		}
		
		this.accountToDeposit = allAccountsInCard[accountNoSelectedByUser-1];


		return true;

	}

	private boolean doGetAmountToDeposit() {
		


		boolean confirmAmountToDeposit = false;
		String userInputAmountToDeposit ="";
		
		 while (!confirmAmountToDeposit){
			System.out.println("new turn=========================================");
			if(!this._atmssHandler.doDisClearAll())
				return failProcess("Get amount", 5,this.FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT}))
				return failProcess("Get amount", 5,FAILED_FROM_DISPLAY);
			
			userInputAmountToDeposit = this._atmssHandler.doKPGetIntegerMoneyAmount(300);
			if(userInputAmountToDeposit == null)
				return failProcess("Get amount",7,this.FAILED_FROM_KEYPAD);
			else if(userInputAmountToDeposit.equals("CANCEL"))
				return failProcess("Get amount", 8,this.CANCELED);
			
			String desc = "$" + userInputAmountToDeposit;
			this.operationCache.add(new Operation("DEPOSIT : Input amount", 0, desc));
			
			if(!this._atmssHandler.doDisClearAll())
				return failProcess("Get amount", 5, this.FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM1,PROMPT_FOR_CONFIRM2, "$" + userInputAmountToDeposit}))
				return failProcess("Get amount",5, FAILED_FROM_DISPLAY);
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(300);
			while (confirmInput != null){
				if (confirmInput.equals("ENTER")){
					this.operationCache.add(new Operation("DEPOSIT : Confirm amount", 0, "Confirm"));
					confirmAmountToDeposit = true;
					break;
				}
				else if(confirmInput.equals("0")){
					this.operationCache.add(new Operation("DEPOSIT : Confirm amount", 0, "Not confirm"));
					break;
				}
				else if(confirmInput.equals("CANCEL")){
						return failProcess("Get amount",8, this.CANCELED);
				}
				confirmInput = this._atmssHandler.doKPGetSingleInput(300);
			}

		}
		this.amountToDeposit = Integer.parseInt(userInputAmountToDeposit);
		return true;

	}
	
//	private void recordOperation(){
//		String description = 
//				"Card Number: " + this._session.getCardNo() + ";" +
//				"Destination account: "+ this.accountToDeposit + ";" +
//				"Amount: " + this.amountToDeposit + ";" + 
//				"Result: " + "Succeeded";
//		
//		operationCache.add(new Operation(OPERATION_NAME,0,description));
//	}
	
	
	void printLastOperation(){
		Operation lastOperation = this.operationCache.getLast();
		
		if(lastOperation.getType() == 0){
		if(this._atmssHandler.doDisClearAll() && this._atmssHandler.doDisDisplayUpper(new String[] {">Print advice?",">Print advice by press ENTER.", ">Skip by press 0"}))
		{
			String inputFromKeypad = _atmssHandler.doKPGetSingleInput(300);
			inputLoop: 
				while(inputFromKeypad!=null){
				switch (inputFromKeypad){
				case "ENTER":
					this._atmssHandler.doAPPrintStrArray(linesToPrintWhenSucceeded(lastOperation));
					break inputLoop;

				case "0":
					break inputLoop;
				}
				inputFromKeypad = this._atmssHandler.doKPGetSingleInput(300);
			}
		}
		}
		else this._atmssHandler.doAPPrintStrArray(linesToPrintWhenFailed(lastOperation));
	}
	

	
	private String[] linesToPrintWhenSucceeded(Operation lastOp){
		String[] lines = new String[5];
		
		lines[0] = "Operation Name ; DEPOSIT";
		lines[1] = "Card Number : "+this._session.getCardNo();
		lines[2] = "To Account : " + this.accountToDeposit;
		lines[3] = "Amount : $"+ this.amountToDeposit;
		lines[4] = "Success";
		
		return lines;
	}
	
	private String[] linesToPrintWhenFailed(Operation lastOp){
		String[] lines = new String[3];
		lines[0] = "Operation Name : DEPOSIT";
		lines[1] = "Card Number : " + this._session.getCardNo();
		lines[2] = lastOp.getDes();
				
		return lines;
	}
	
	
	private boolean failProcess(String failedStep, int type, String desc){
		this.operationCache.add(new Operation("DEPOSIT : "+failedStep, type, "Failure"));
		this.printLastOperation();
		this.printOpCache();
		return false;
	}

}
