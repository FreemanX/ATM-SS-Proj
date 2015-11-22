/**
 * 
 */
package atmss.process;

import atmss.Operation;
import atmss.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class DepositController.
 *
 * @author Lihui
 */
public class DepositController extends ProcessController {

	/** The account to deposit. */
	//private int amount;
	private String accountToDeposit;
	
	/** The amount to deposit. */
	private int amountToDeposit;

	/** The display message when failure from display. */
	//private final String OPERATION_NAME = "DEPOSIT: ";
	private final String FAILED_FROM_DISPLAY = "Failure: no response from display";
	
	/** The display message when failure from keypad. */
	private final String FAILED_FROM_KEYPAD = "Failure: no response from the keypad";
	
	/** The display message when failure from envelop dispenser. */
	private final String FAILED_FROM_ENVELOPDISPENSER = "Failure: no response from envelop dispenser";
	
	/** The display message when failure from deposit collector. */
	private final String FAILED_FROM_DEPOSITCOLLECTOR = "Failure: no response from deposit collector";
	
	/** The display message when failure from advice printer. */
	private final String FAILED_FROM_ADVICEPRINTER = "Failure: no response from advice printer";
	
	/** The display message when failure from BAMS. */
	private final String FAILED_FROM_BAMS = "Failure: no response from bank system (BAMS)";
	
	/** The display message prompt for account. */
	private final String PROMPT_FOR_ACCOUNT = "Please choose an account to deposit:";
	
	/** The display message prompt for amount. */
	private final String PROMPT_FOR_AMOUNT = "Please input your deposit amount:";
	
	/** The display message prompt for confirmation line1. */
	private final String PROMPT_FOR_CONFIRM1 = "Press 1 -> Confirm your deposit amount";
	
	/** The display message prompt for confirmation line2. */
	private final String PROMPT_FOR_CONFIRM2 = "Press 2 -> Reinput your deposit amount";
	
	/** The display message prompt for confirmation line3. */
	private final String PROMPT_FOR_CONFIRM3 = "Press CANCEL -> Quit process";
	
	/** The display message prompt for collecting envelop. */
	private final String PROMPT_FOR_COLLECT_ENVELOP = "Please collect the envelop and put cheque/cash and receipt into the envelop";
	
	/** The prompt for return envelop. */
	private final String PROMPT_FOR_RETURN_ENVELOP = "Please put the envelop with cheque/cash to deposit collector";
	
	/** The cancellation from. */
	private final String CANCELED = "Failure: cancellation from user";
	
	/**
	 * Instantiates a new deposit controller.
	 *
	 * @param Session the session communicates between different parts.
	 */
	public DepositController(Session Session) {
		// TODO Auto-generated constructor stub
		super(Session);
	}

	/**
	 * Prints the whole operation cache.
	 */
	public void printOpCache(){
		for(Operation op: operationCache){
			System.out.println(op.getName() + " "+ op.getType() + " "+op.getDes());
		}
	}
	
	/**
	 * Process deposit operation.
	 *
	 * @return  true if the process succeeds, otherwise return false
	 */
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
	
	/**
	 * Eat envelop
	 * If fail, record the operation message and its result to operationCache
	 * 
	 * @return true if eat the envelop successfully, otherwise return false
	 */
	private boolean doEatEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_RETURN_ENVELOP}))
			return failProcess("eat envelop",5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEatEnvelop(20))
			return failProcess("eat envelop",4,this.FAILED_FROM_DEPOSITCOLLECTOR);
		
		return true;
	}
	
	/**
	 * Eject envelop
	 *If fail, record the operation message and its result to operationCache
	 *
	 * @return true, if eject an envelop successfully, otherwise return false
	 */
	private boolean doEjectEnvelop(){
		if(!this._atmssHandler.doDisClearAll() || !this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_COLLECT_ENVELOP}))
			return failProcess("eject envelop", 5, this.FAILED_FROM_DISPLAY);
		
		if(!this._atmssHandler.doEDEjectEnvelop())
			return failProcess("eject envelop", 6, this.FAILED_FROM_ENVELOPDISPENSER);

		return true;
	}
	
		
	/**
	 * Print receipt
	 *If fail, record the operation message and its result to operationCache
	 *
	 * @return true, if print the receipt successfully, otherwise return false
	 */
	private boolean doPrintReceipt(){
		if(!this._atmssHandler .doAPPrintStrArray(new String[] {"Account to deposit: "+accountToDeposit, "Amount to deposit: $"+ Integer.toString(this.amountToDeposit)}))
			return failProcess("print receipt",1,this.FAILED_FROM_ADVICEPRINTER);
		return true;
	}
	
	/**
	 * Prompt for the account to deposit
	 *If fail, record the operation message and its result to operationCache
	 *
	 * @return true, if get the account to deposit successfully, otherwise false.
	 */
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
			accountsToChooseDisplay.add("Press "+index+ "-> "+account );
			index += 1;
		}
		

		
		
		if(!this._atmssHandler.doDisDisplayUpper(accountsToChooseDisplay.toArray(new String[0])))
			return this.failProcess("get account", 5, FAILED_FROM_DISPLAY);
		
		int accountNoSelectedByUser = allAccountsInCard.length + 1;
		
		while(accountNoSelectedByUser > allAccountsInCard.length){
		
		String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(20);
		
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

	/**
	 * Prompt for the amount to deposit.
	 * If fail, record the operation message and its result to operationCache
	 *
	 * @return true, if get the amount to deposit successfully, otherwise return false.
	 */
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
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {" Your deposit amount: $" + userInputAmountToDeposit, PROMPT_FOR_CONFIRM1,PROMPT_FOR_CONFIRM2, PROMPT_FOR_CONFIRM3}))
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
	

	
	/**
	 * Prints the advice after the whole process end.
	 * If deposit succeed, user choose to print or not
	 * If the process fails, print the advice automatically
	 */
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
	

	
	/**
	 * Generate lines to print when the process succeeds.
	 *
	 * @param lastOp the last operation in the operationCache
	 * @return the string[] that contains the content to print
	 */
	private String[] linesToPrintWhenSucceeded(Operation lastOp){
		String[] lines = new String[4];
		
		lines[0] = "Operation Name: DEPOSIT";
		lines[1] = "Card Number: "+this._session.getCardNo();
		lines[2] = "To Account: " + this.accountToDeposit;
		lines[3] = "Amount: $"+ this.amountToDeposit;
		//lines[4] = "Success";
		
		return lines;
	}
	
	/**
	 * Generate lines to print when the process fails.
	 *
	 * @param lastOperation the last operation in the operationCache
	 * @return the string[] that contains the content to print
	 */
	private String[] linesToPrintWhenFailed(Operation lastOp){
		String[] lines = new String[2];
		lines[0] = "Operation Name: "+ lastOp.getName();
		//lines[1] = "Card Number    : " + this._session.getCardNo();
		lines[1] = lastOp.getDes();
				
		return lines;
	}
	
	
	/**
	 * To process essential tasks when a process fails.
	 *
	 * @param failedStep the specific failed step
	 * @param type the error type of the failure
	 * @param desc the detailed description of the failure
	 * @return false indicates the failure of the process
	 */
	private boolean failProcess(String failedStep, int type, String desc){
		this.operationCache.add(new Operation("DEPOSIT: "+failedStep, type, desc));
		this.printLastOperation();
		this.printOpCache();
		return false;
	}

}
