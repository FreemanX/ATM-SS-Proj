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
	
	private final int    TIME_OUT_LIMIT = 30;
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
			"Press 1 -> Print advice", "Press 2 -> Quit with out printing"
	};
	
	public TransferController(Session session) {
		super(session);
	}
	
	public Boolean doTransfer() {
		if(!this._atmssHandler.doDisClearAll()) {
			record(OPERATION_NAME+": clear the display", "Dis");
			return false;
		}
		record(OPERATION_NAME+": clear the display", "");
		
		if (!this.getSrcAccountNumber()) return false;
		if (!this.getDesAccountNumber()) return false;
		if (!this.getAmountToTransfer()) return false;
		if (!this._atmssHandler.doBAMSTransfer(desAccountNumber, srcAccountNumber, amountToTransfer, _session)) {
			record(OPERATION_NAME+": waiting response from bank", "BAMS");
			return false;
		}
		// if succeeded:
		record(OPERATION_NAME+": waiting response from bank", "approved");
		
		if (!this._atmssHandler.doDisDisplayUpper(new String[] {
				SHOW_SUCCESS, PRINT_NOTE_SELECTION[0],PRINT_NOTE_SELECTION[1]
		})) {
			record(OPERATION_NAME+": display the result", "Dis");
			return false;
		}
		while (true) {
			String nextInput = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
			if (nextInput == null || nextInput.equals("2")) {
				return true;
			} else if (nextInput.equals("1")) {
				doPrintReceipt();
				return true;
			} 
		}
	}
	
	private boolean getSrcAccountNumber() {
		if(!this._atmssHandler.doDisClearAll()) {
			record(OPERATION_NAME+": clear the display", "Dis");
			return false;
		}
		record(OPERATION_NAME+": clear the display", "");
		
		String[] allAccountsInCard = this._atmssHandler.doBAMSGetAccounts(this._session);
		if (allAccountsInCard.length == 0){
			record(OPERATION_NAME+": waiting for BAMS to get accounts", "BAMS");
			return false;
		}
		record(OPERATION_NAME+": waiting for BAMS to get accounts", "");
			
		if (!_atmssHandler.doDisDisplayUpper(createOptionList(PROMPT_FOR_SRCACCOUNT, allAccountsInCard))) {
			record(OPERATION_NAME+": display all accounts in the card", "Dis");
			return false;
		}
		record(OPERATION_NAME+": display all accounts in the card", "");
		
		while (true){		
			String accountSelectedByUser = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);		
			if (accountSelectedByUser != null) {
				try{
					int accountChosen = Integer.parseInt(accountSelectedByUser);
					if (accountChosen <= allAccountsInCard.length) {
						this.srcAccountNumber = allAccountsInCard[accountChosen - 1];
						record(OPERATION_NAME+": select an account", srcAccountNumber);
						return true;
					}
				}
				catch(NumberFormatException e) {
					if(accountSelectedByUser.equals("CANCEL")) {
						record(OPERATION_NAME+": user cancels the process", "USER");
						return false;
					}
				}
			}
			else {
				record(OPERATION_NAME+": select an account", "KP");
				return false;
			}
		}
	}
	
	private boolean getDesAccountNumber() {
		String desAccountNumber ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				record(OPERATION_NAME+": clear the display", "Dis");
				return false;
			}
			record(OPERATION_NAME+": clear the display", "");
			 
			if (!this._atmssHandler.doDisAppendUpper(PROMPT_FOR_DESACCOUNT)) {
				record(OPERATION_NAME+": display message", "Dis");
				return false;
			}
			record(OPERATION_NAME+": display message", "");
			
			desAccountNumber = this._atmssHandler.doKPGetAccountNum(TIME_OUT_LIMIT);
			if (desAccountNumber == null) {
				record(OPERATION_NAME+": input the destination account", "KP");
				return false;
			}
			
			if (desAccountNumber.equals("CANCEL")) {
				record(OPERATION_NAME+": input the destination account", "USER");
				return false;
			}
						
			if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM[1], desAccountNumber})) {
				record(OPERATION_NAME+": display the destination account to confirm", "Dis");
				return false;
			}
			record(OPERATION_NAME+": display the destination account to confirm", desAccountNumber);
			
			String confirmInput = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
			if (confirmInput != null) {
				switch(confirmInput) {
					case "ENTER":
						this.desAccountNumber = desAccountNumber;
						record(OPERATION_NAME+": confirm the desination account", "");
						return true;
					case "CANCEL":
						record(OPERATION_NAME+": confirm the desination account", "USER");
						return false;
				}
			} else {
				record(OPERATION_NAME+": confirm the desination account", "KP");
				return false;
			}
		}		
	}
	
	private boolean getAmountToTransfer() {		
		String amountToTransfer ="";
		
		while (true) {
			if(!this._atmssHandler.doDisClearAll()) {
				record(OPERATION_NAME+": clear the display", "Dis");
				return false;
			}
			record(OPERATION_NAME+": clear the display", "");
			
			if(!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_AMOUNT})) {
				record(OPERATION_NAME+": Display the amount input require message", "Dis");
				return false;
			}
			record(OPERATION_NAME+": display the amount input require message", "");
			
			amountToTransfer = this._atmssHandler.doKPGetDoubleMoneyAmount(TIME_OUT_LIMIT);
			if (amountToTransfer == null) {
				record(OPERATION_NAME+": input the transfer amount", "KP");
				return false;
			}
			
			else if (amountToTransfer.contains("CANCEL")) {
				record(OPERATION_NAME+": input the transfer amount", "USER");
				return false;
			}
			
			else if (amountToTransfer.contains("CLEAR")) {
				continue;
			}
			else {
				accountBalance = this._atmssHandler.doBAMSCheckBalance(this.srcAccountNumber, _session);
				if (Double.parseDouble(amountToTransfer) > 200000) {
					if (!_atmssHandler.doDisDisplayUpper(new String[]{
							FAILED_FROM_BALANCE, "You can only transfer $" + 200000 + " at one time", 
							"Press any button to end"
							})) {					
						record(OPERATION_NAME+": display over max transfer amount message", "Dis");
						return false;
					}
					record(OPERATION_NAME+": display over max transfer amount message", "");
					record(OPERATION_NAME+": check max transfer amount", "BAMS");
					this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
					return false;
				}
				
				if (Double.parseDouble(amountToTransfer) > accountBalance) {
					if (!_atmssHandler.doDisDisplayUpper(new String[]{
							FAILED_FROM_BALANCE, "You can only transfer $" + accountBalance, 
							"Press any button to end"
							})) {					
						record(OPERATION_NAME+": display not enough money message", "Dis");
						return false;
					}
					record(OPERATION_NAME+": display not enough money message", "");
					record(OPERATION_NAME+": check money available", "BAMS");
					this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
					return false;
				}
				
				if (!this._atmssHandler.doDisDisplayUpper(new String[] {PROMPT_FOR_CONFIRM[0], amountToTransfer})) {
					record(OPERATION_NAME+": display transfer amonut to confirm", "Dis");
					return false;
				}
				record(OPERATION_NAME+": display transfer amonut to confirm", amountToTransfer);
				
				String confirmInput = this._atmssHandler.doKPGetSingleInput(TIME_OUT_LIMIT);
				if (confirmInput != null) {
					switch(confirmInput) {
						case "ENTER":
							this.amountToTransfer = Double.parseDouble(amountToTransfer);
							record(OPERATION_NAME+": confirm the transfer amount", "");
							return true;
						case "CANCEL":
							record(OPERATION_NAME+": confirm the transfer amount", "USER");
							return false;
					}
				} else {
					record(OPERATION_NAME+": confirm the transfer amount","KP");
					return false;
				}
			}
		}
	}
	
	private void doPrintReceipt(){
		boolean result = _atmssHandler .doAPPrintStrArray(new String[] {
				"Operation Name: " + OPERATION_NAME,
				"Card Number   : " + _session.getCardNo(),
				"From Account  : " + srcAccountNumber,
				"To Account    : " + desAccountNumber,
				"Amount        : $" + Double.toString(this.amountToTransfer)
		});
		if (!result) record(OPERATION_NAME+": choose to print the receipt", "AP");
	}
}
