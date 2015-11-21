/**
 * 
 */
package atmss.process;

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
	private final String FAILED_FROM_BALANCE = "Not enough balance to withdraw";
	private final String PROMPT_FOR_AMOUNT = "Please type in your transfer amount";
	private final String PROMPT_FOR_SRCACCOUNT = "Please choose your account";
	private final String PROMPT_FOR_DESACCOUNT = "Please type in your target account";
	private final String[] PROMPT_FOR_CONFIRM = {
			"Please confirm your transfer amount", "Please confirm the account"
	};
	private final String SHOW_SUCCESS = "Succeeded! The transfer operation succeeds.";
	private final String[] PRINT_NOTE_SELECTION = {
			"Do you want to print note?", "Press ENTER to print", "Press CANCEL not to print"
	};
	
	public TransferController(Session session) {
		super(session);
	}
	
	public Boolean doTransfer() {	
		if(!this._atmssHandler.doDisClearAll()) {
			return failProcess("Clear the display", 5);
		}
		record(OPERATION_NAME+": clear the display", "");
		
		if (!this.getSrcAccountNumber()) {
			return false;
		}
		
		if (!this.getDesAccountNumber()) {
			return false;
		}
		
		if (!this.getAmountToTransfer()) {
			return false;
		}
				
		if (!this._atmssHandler.doBAMSTransfer(desAccountNumber, srcAccountNumber, amountToTransfer, _session)) {
			return false;
		}
		
		if (!this._atmssHandler.doDisDisplayUpper(new String[] {
				SHOW_SUCCESS, PRINT_NOTE_SELECTION[0], PRINT_NOTE_SELECTION[1],PRINT_NOTE_SELECTION[2]
		})) {
			failProcess("Display the result", 5);
			return false;
		}
		record(OPERATION_NAME+": display the result", "");
		
		while (true) {
			String nextInput = this._atmssHandler.doKPGetSingleInput(10);
			
			if (nextInput == null || nextInput.equals("2")) {
				record("Choose not to print the receipt", "");
			} else if (nextInput.equals("1")) {
				if (!this.doPrintReceipt()) {
					return false;
				}
				record("Choose to print the receipt", "");
				break;
			} else {
				if (!this._atmssHandler.doDisDisplayUpper(new String[] {
						SHOW_SUCCESS, PRINT_NOTE_SELECTION[0], 
						PRINT_NOTE_SELECTION[1],PRINT_NOTE_SELECTION[2], "Wrong input!"
				})) {
					failProcess("Display error message", 5);
					return false;
				}
				record("Display error message", "");
			}
		}
		record("", "");
		return true;
	}
	
	private boolean getSrcAccountNumber() {
		if(!this._atmssHandler.doDisClearAll()) {
			return failProcess("Clear the display", 5);
		}
		record("Clear the display", "");
		
		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if (allAccountsInCard.length == 0){
			return this.failProcess("Waiting for BAMS to get accounts", 10);			
		}
		record("Waiting for BAMS to get accounts", "");
			
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_SRCACCOUNT, allAccountsInCard))) {
			failProcess("Display all accounts in the card", 5);
			return false;
		}
		record("Display all accounts in the card", "");
		
		while (true){		
			String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(10);		
			if (accountSelectedByUser != null) {
				try{
					int accountChosen = Integer.parseInt(accountSelectedByUser);
					if (accountChosen <= allAccountsInCard.length) {
						this.srcAccountNumber = allAccountsInCard[accountChosen - 1];
						record("Select an account", srcAccountNumber);
						return true;
					}
				}
				catch(NumberFormatException e) {
					if(accountSelectedByUser.equals("CANCEL")) {
						return failProcess("User cancels the process", 8);
					}
				}
			}
			else {
				return this.failProcess("Select an account", 7);		
			}
		}
	}
	
	private boolean getDesAccountNumber() {
		String desAccountNumber ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				return failProcess("Clear the display", 5);
			}
			record("Clear the display", "");
			 
			if (!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_DESACCOUNT)) {
				return this.failProcess("Display message", 5);
			}
			record("Display message", "");
			
			desAccountNumber = this._atmssHandler.doKPGetAccountNum(30);
			if (desAccountNumber == null) {
				return failProcess("Input the destination account", 7);
			}
			
			if (desAccountNumber.equals("CANCEL")) {
				return failProcess("Input the destination account", 8);
			}
						
			if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM[1], desAccountNumber})) {
				return failProcess("Display the destination account to confirm", 5);
			}
			record("Display the destination account to confirm", desAccountNumber);
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(3);
			if (confirmInput != null) {
				switch(confirmInput) {
					case "ENTER":
						this.desAccountNumber = desAccountNumber;
						record("Confirm the desination account", "");
						return true;
					case "CANCEL":
						return failProcess("Confirm the desination account", 8);
				}
			} else {
				return failProcess("Confirm the desination account", 7);
			}
		}		
	}
	
	private boolean getAmountToTransfer() {		
		String amountToTransfer ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				return failProcess("Clear the display", 5);
			}
			record("Clear the display", "");
			
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT})) {
				return failProcess("Display the amount input require message", 5);
			}
			record("Display the amount input require message", "");
			
			amountToTransfer = this._atmssHandler.doKPGetDoubleMoneyAmount(30);
			if (amountToTransfer == null) {
				return failProcess("Input the transfer amount", 7);
			}
			
			else if (amountToTransfer.contains("CANCEL")) {
				return failProcess("Input the transfer amount", 8);
			}
			
			else if (amountToTransfer.contains("CLEAR")) {
				continue;
			}
			else {
				accountBalance = this._atmssHandler.doBAMSCheckBalance(this.srcAccountNumber, _session);
				if (Double.parseDouble(amountToTransfer) > accountBalance) {
					if (!_atmssHandler.doDisDisplayUpper(new String[]{
							FAILED_FROM_BALANCE, "You can only withdraw $" + accountBalance, 
							"Press any button to end"
							})) {					
						failProcess("Display not enough money message", 5);
						return false;
					}
					record("Display not enough money message", "");
					failProcess("Check money available", 10);
					return false;
				}
				
				if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM[0], amountToTransfer})) {
					return failProcess("Display transfer amonut to confirm", 5);
				}
				record("Display transfer amonut to confirm", amountToTransfer);
				
				String confirmInput = this._atmssHandler.doKPGetSingleInput(10);
				if (confirmInput != null) {
					switch(confirmInput) {
						case "ENTER":
							this.amountToTransfer = Double.parseDouble(amountToTransfer);
							record("Confirm the transfer amount", "");
							return true;
						case "CANCEL":
							return failProcess("Confirm the transfer amount", 8);
					}
				} else {
					return failProcess("Confirm the transfer amount",7);
				}
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
				new String("Card No.: " + _session.getCardNo()),
				new String("From:       " + srcAccountNumber), 
				new String("To:             " + desAccountNumber),
				new String("Amount:   " + Double.toString(this.amountToTransfer))
		})) {
			return failProcess("Print the receipt", 1);
		}
		record("Print the receipt", "");
		return true;
	}
	/*
	private boolean doPrintReceipt(String msg){
		if(!this._atmssHandler.doAPPrintStrArray(new String[] {msg})) {
			return failProcess("Print the receipt", 1);
		}
		record("Print the receipt", "");
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
		operationCache.add(new Operation(OPERATION_NAME + ": " + operation, type, result));
	}
	
	private boolean failProcess(String operation, int type){
		this._atmssHandler.doDisClearAll();
		this._atmssHandler.doDisDisplayUpper(new String[] {operation});
		recordOperation(operation, type, "Failed");
		return false;
	}
}
