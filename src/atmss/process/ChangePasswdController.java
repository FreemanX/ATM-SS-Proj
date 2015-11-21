/**
 * 
 */
package atmss.process;

import atmss.Operation;
import atmss.Session;

/**
 * @author DJY
 *
 */
public class ChangePasswdController extends ProcessController {

	private final String OPERATION_NAME = "Change Password";
	private final String FAILED_FROM_BAMS_UPDATING_PW = "Cannot get approval from BAMS";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_USER_CANCELLING = "The operation has been cancelled";
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please type your new password:"};
	private final String[] PROMPT_FOR_NEW_PASSWORD_ERR = {"The new passwords do not equal", "Please type your new password:"};
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please type your new password again:"};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final long TIME_LIMIT = 10; // seconds
	private final String KP_CANCEL = "CANCEL";
	private String _currentStep = OPERATION_NAME;

	public ChangePasswdController(Session CurrentSession) {
		super(CurrentSession);
	}

	public boolean doChangePasswd() {
		String newPassword = "";
		String confirmPassword = "";
		boolean result = false;
		
		// -> preparing the necessary information
		_currentStep = OPERATION_NAME+": getting new password";
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD)) {
			record("Dis");
			return false;
		}
		while (true) {
			newPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
			if (newPassword == null) {
				record("KP");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					record("Dis");
					return false;
				}
				try { this.wait(3000);} catch (InterruptedException e) {}
				return false;
			} else if (newPassword.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					record("Dis");
					return false;
				}
				try { this.wait(3000);} catch (InterruptedException e) {}
				return false;
			}
			record("new password typed");
			
			_currentStep = OPERATION_NAME+": getting confirm password";
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_CONFIRM_PASSWORD)) {
				record("Dis");
				return false;
			}
			confirmPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
			if (confirmPassword == null) {
				record("KP");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					record("Dis");
					return false;
				}
				try { this.wait(3000);} catch (InterruptedException e) {}
				return false;
			} else if (confirmPassword.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					record("Dis");
					return false;
				}
				try { this.wait(3000);} catch (InterruptedException e) {}
				return false;
			}
			record("confirm password typed");
			 
			// check new password
			if (newPassword.equals(confirmPassword)) break; 
			
			_currentStep = OPERATION_NAME+": getting new password";
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD_ERR)) {
				record("Dis");
				return false;
			}
		}
		record("new password checked");
		// <- preparing the necessary information
		
		// contact BAMS now
		_currentStep = OPERATION_NAME+": getting approval from BAMS";
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			record("Dis");
			return false;
		}
		result = _atmssHandler.doBAMSUpdatePasswd(newPassword, _session);
		
		// display the result
		if (result) {
			record("password changed");
			askForPrinting();
			return true;
		} else {
			record("BAMS");
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BAMS_UPDATING_PW,"The password may not be changed"})) {
				record("Dis");
				return false;
			}
			try { this.wait(3000);} catch (InterruptedException e) {}
			return false;
		}
	}
	
	private void record(String Type) {
		switch(Type) {
			case "AP" : recordFailure(1);break;
			case "CR" : recordFailure(2);break;
			case "CD" : recordFailure(3);break;
			case "DC" : recordFailure(4);break;
			case "Dis": recordFailure(5);break;
			case "ED" : recordFailure(6);break;
			case "KP" : recordFailure(7);break;
			case "USER" : recordFailure(8);break;
			case "BAMS" : recordFailure(10);break;
			default: recordSuccess(Type);break;
		}
	}
	
	private void recordSuccess(String detail) {
		operationCache.add(new Operation(_currentStep, 0, "Success: "+detail));
	}
	
	private void recordFailure(int Type) {
		String description;
		switch(Type) {
			case 1: description = "Failure: no response from advice printer";break;
			case 2: description = "Failure: no response from card reader";break;
			case 3: description = "Failure: no response from cash dispenser";break;
			case 4: description = "Failure: no response from deposit collector";break;
			case 5: description = "Failure: no response from display";break;
			case 6: description = "Failure: no response from evelop dispenser";break;
			case 7: description = "Failure: no response from keypad";break;
			case 8: description = "Failure: cancelled by user";break;
			case 10: description = "Failure: disapproved by the bank system(BAMS)";break;
			default:description = "Failure: unknown reason";break;
		}
		operationCache.add(new Operation(_currentStep, Type, description));
		_atmssHandler.doAPPrintStrArray(new String[]{_currentStep,description});
	}
	
	private void askForPrinting(){
		String[] toDisplay = {
				"Operation succeeded!",
				"You have changed your password",
				"Press button 1 to print the advice,",
				"button 2 to quit without printing"
		};
		if (!_atmssHandler.doDisDisplayUpper(toDisplay)) return;
		while (true) {
			String userInput = _atmssHandler.doKPGetSingleInput(TIME_LIMIT);
			if (userInput == null) return;
			if (userInput.equals("1")) {
				String[] toPrint = {
						"Operation name: " + OPERATION_NAME,
						"Card Number: " + _session.getCardNo(),
						"Result: Succeeded"
				};
				_atmssHandler.doAPPrintStrArray(toPrint);
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
}
