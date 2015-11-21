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

	//private final String OPERATION_NAME = "DEPOSIT: ";
	private final String FAILED_FROM_DISPLAY = "Failure: no response from display";
	private final String FAILED_FROM_KEYPAD = "Failure: no response from the keypad";
	private final String FAILED_FROM_ENVELOPDISPENSER = "Failure: no response from envelop dispenser";
	private final String FAILED_FROM_DEPOSITCOLLECTOR = "Failure: no response from deposit collector";
	private final String FAILED_FROM_ADVICEPRINTER = "Failure: no response from advice printer";
	private final String FAILED_FROM_BAMS = "Failure: no response from bank system (BAMS)";
	private final String PROMPT_FOR_ACCOUNT = "Please choose an account to deposit:";
	private final String PROMPT_FOR_AMOUNT = "Please inpur your deposit amount:";
	private final String PROMPT_FOR_CONFIRM1 = "Press 1 -> Confirm your deposit amount";
	private final String PROMPT_FOR_CONFIRM2 = "Press 2 -> Reinput your deposit amount";
	private final String PROMPT_FOR_CONFIRM3 = "Press CANCEL -> Quit process";
	private final String PROMPT_FOR_COLLECT_ENVELOP = "Please collect the envelop and put cheque/cash and receipt into the envelop";
	private final String PROMPT_FOR_RETURN_ENVELOP = "Please put the envelop with cheque/cash to deposit collector";
	private final String CANCELED = "Failure: cancellation from user";
	
	/**
	 * 
	 */
	public DepositController(Session Session) {
		// TODO Auto-generated constructor stub
		super(Session);
	}

	public void printOpCache(){
		for(Operation op: operationCache){
			System.out.println(op.getName() + " "+ op.getType() + " "+op.getDes());
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
		this.operationCache.add(new Operation("DEPOSIT: select account",0, this.accountToDeposit));
		
		if (!this. doGetAmountToDeposit())
			return false;
		
		if (!this.doPrintReceipt())
			return false;
		this.operationCache.add(new Operation("DEPOSIT: print reiceipt", 0, "Success"));
		
		if (!this.doEjectEnvelop())
			return false;
		this.operationCache.add(new Operation("DEPOSIT: eject envelop", 0 , "Success"));
		
		if (!this.doEatEnvelop())
			return false;
		this.operationCache.add(new Operation("DEPOSIT: eat envelop", 0 ,"Success"));
		

		this.printLastOperation();
		printOpCache();
		return true;
	}
	
	private boolean doEatEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_RETURN_ENVELOP}))
			return failProcess("eat envelop",5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEatEnvelop(20))
			return failProcess("eat envelop",4,this.FAILED_FROM_DEPOSITCOLLECTOR);
		
		return true;
	}
	
	private boolean doEjectEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_COLLECT_ENVELOP}))
			return failProcess("eject envelop", 5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEjectEnvelop())
			return failProcess("eject envelop", 6, this.FAILED_FROM_ENVELOPDISPENSER);

		return true;
	}
	
		
	private boolean doPrintReceipt(){
		if(!this._atmssHandler .doAPPrintStrArray(new String[] {"Account to deposit: "+accountToDeposit, "Amount to deposit: $"+ Integer.toString(this.amountToDeposit)}))
			return failProcess("print receipt",1,this.FAILED_FROM_ADVICEPRINTER);
		return true;
	}
	private boolean doGetAccountToDeposit() {

		if(!this._atmssHandler.doDisClearAll())
			return this.failProcess("get account", 5, this.FAILED_FROM_DISPLAY);
		
		List<String> accountsToChooseDisplay = new ArrayList<String>(Arrays.asList(new String[] {PROMPT_FOR_ACCOUNT}));

		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if(allAccountsInCard.length == 0){
			return this.failProcess("get account", 10, this.FAILED_FROM_BAMS);		
		}
		
		int index = 1;
		for (String account:allAccountsInCard){
			accountsToChooseDisplay.add("-> "+index+ ": "+account );
			index += 1;
		}
		

		
		
		if(!this._atmssHandler.doDisDisplayUpper(accountsToChooseDisplay.toArray(new String[0])))
			return this.failProcess("get account", 5, FAILED_FROM_DISPLAY);
		
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
		
		this.accountToDeposit = allAccountsInCard[accountNoSelectedByUser-1];


		return true;

	}

	private boolean doGetAmountToDeposit() {
		


		boolean confirmAmountToDeposit = false;
		String userInputAmountToDeposit ="";
		
		 while (!confirmAmountToDeposit){
			//System.out.println("new turn=========================================");
			if(!this._atmssHandler.doDisClearAll())
				return failProcess("get amount", 5,this.FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT}))
				return failProcess("get amount", 5,FAILED_FROM_DISPLAY);
			
			userInputAmountToDeposit = this._atmssHandler.doKPGetIntegerMoneyAmount(20);
			if(userInputAmountToDeposit == null)
				return failProcess("get amount",7,this.FAILED_FROM_KEYPAD);
			else if(userInputAmountToDeposit.equals("CANCEL"))
				return failProcess("get amount", 8,this.CANCELED);
			
			String desc = "$" + userInputAmountToDeposit;
			this.operationCache.add(new Operation("DEPOSIT: input amount", 0, desc));
			
			if(!this._atmssHandler.doDisClearAll())
				return failProcess("get amount", 5, this.FAILED_FROM_DISPLAY);
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {" Your deposit amoun-: $" + userInputAmountToDeposit, PROMPT_FOR_CONFIRM1,PROMPT_FOR_CONFIRM2, PROMPT_FOR_CONFIRM3}))
				return failProcess("get amount",5, FAILED_FROM_DISPLAY);
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(20);
			while (confirmInput != null){
				if (confirmInput.equals("1")){
					this.operationCache.add(new Operation("DEPOSIT: confirm amount", 0, "Confirm"));
					confirmAmountToDeposit = true;
					break;
				}
				else if(confirmInput.equals("2")){
					this.operationCache.add(new Operation("DEPOSIT: confirm amount", 0, "Not confirm"));
					break;
				}
				else if(confirmInput.equals("CANCEL")){
						return failProcess("get amount",8, this.CANCELED);
				}
				confirmInput = this._atmssHandler.doKPGetSingleInput(20);
			}

		}
		this.amountToDeposit = Integer.parseInt(userInputAmountToDeposit);
		
		return true;

	}
	

	
	void printLastOperation(){
		Operation lastOperation = this.operationCache.getLast();
		
		if(lastOperation.getType() == 0){
			
		if(this._atmssHandler.doDisClearAll() && this._atmssHandler.doDisDisplayUpper(new String[] {"You have deposited "+this.amountToDeposit+ " to account" + this.accountToDeposit,"Press 1 -> Print advice", "Press 2 -> Quit without printing"}))
		{
			String inputFromKeypad = _atmssHandler.doKPGetSingleInput(20);
			inputLoop: while(inputFromKeypad!=null){
				switch (inputFromKeypad){
				case "1":
					if(!this._atmssHandler.doAPPrintStrArray(linesToPrintWhenSucceeded(lastOperation)))
						this.operationCache.add(new Operation("DEPOSIT: print receipt", 1, "Failure: no response from advice printer"));
					else this.operationCache.add(new Operation("DEPOSIT: print receipt", 0, "Succeess"));
					break inputLoop;

				case "2":
					this.operationCache.add(new Operation("DEPOSIT: print receipt", 0, "Do not print"));
					break inputLoop;
				}
				inputFromKeypad = this._atmssHandler.doKPGetSingleInput(20);
			}
		}
		}
		else this._atmssHandler.doAPPrintStrArray(linesToPrintWhenFailed(lastOperation));
	}
	

	
	private String[] linesToPrintWhenSucceeded(Operation lastOp){
		String[] lines = new String[4];
		
		lines[0] = "Operation Name: DEPOSIT";
		lines[1] = "Card Number: "+this._session.getCardNo();
		lines[2] = "To Account: " + this.accountToDeposit;
		lines[3] = "Amount: $"+ this.amountToDeposit;
		//lines[4] = "Success";
		
		return lines;
	}
	
	private String[] linesToPrintWhenFailed(Operation lastOp){
		String[] lines = new String[2];
		lines[0] = "Operation Name: "+ lastOp.getName();
		//lines[1] = "Card Number    : " + this._session.getCardNo();
		lines[1] = lastOp.getDes();
				
		return lines;
	}
	
	
	private boolean failProcess(String failedStep, int type, String desc){
		this.operationCache.add(new Operation("DEPOSIT: "+failedStep, type, desc));
		this.printLastOperation();
		this.printOpCache();
		return false;
	}

}
