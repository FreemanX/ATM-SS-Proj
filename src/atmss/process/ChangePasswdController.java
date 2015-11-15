/**
 * 
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;

/**
 * @author DJY
 *
 */
public class ChangePasswdController extends ProcessController {

	private final String OPERATION_NAME = "Change Password";
	private final String FAILED_FROM_DISPLAY = "No response from the display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_BAMS = "Failed to get approval from BAMS";
	private final String[] ERROR_NOT_EQUAL = {"The new passwords do not equal", "Please type your old password:"};
	private final String[] PROMPT_FOR_OLD_PASSWORD = {"Please type your old password:"};
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please type your new password:"};
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please type your new password again:"};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final String[] SHOW_SUCCESS = {"Succeeded!", "The password has been changed."};
	private final String[] SHOW_FAILURE = {"Failed!", "The password may not be changed."};

	public ChangePasswdController(String CardNumber, MainController MainController) {
		super(CardNumber, MainController);
	}

	public boolean doChangePasswd() {
		String oldPassword = "";
		String newPassword = "";
		String confirmPassword = "";
		boolean result = false;
		
		// -> preparing the necessary information
		// get old password from the user
		if (!_mainController.doDisplay(PROMPT_FOR_OLD_PASSWORD)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		while (true) {
			oldPassword = _mainController.getPasswordFromUser();
			if (oldPassword.isEmpty()) {
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			}
			
			// get new password from the user
			if (!_mainController.doDisplay(PROMPT_FOR_NEW_PASSWORD)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			newPassword = _mainController.getPasswordFromUser();
			if (newPassword.isEmpty()) {
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			}
			
			// get confirm password from the user
			if (!_mainController.doDisplay(PROMPT_FOR_CONFIRM_PASSWORD)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			confirmPassword = _mainController.getPasswordFromUser();
			if (confirmPassword.isEmpty()) {
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			}
			 
			// check the new and confirm password
			if (newPassword.equals(confirmPassword)) break; 
			
			// start again if not equal
			if (!_mainController.doDisplay(ERROR_NOT_EQUAL)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
		}
		// <- preparing the necessary information
		
		// contact BAMS now
		if (!_mainController.doDisplay(SHOW_PLEASE_WAIT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		result = _mainController.doBAMSUpdatePasswd(_cardNumber, oldPassword, newPassword);
		
		// display the result
		if (result) {
			if (!_mainController.doDisplay(SHOW_SUCCESS)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation();
			return true;
		} else {
			if (!_mainController.doDisplay(SHOW_FAILURE)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(FAILED_FROM_BAMS);
			return false;
		}
	}
	
	private void recordOperation() {
		String description = 
				"Card Number: " + _cardNumber + "; " + 
				"Result: " + "Succeeded; ";
		operationCache.add(new Operation(OPERATION_NAME, description));
	}
	
	private void recordOperation(String FailedReason) {
		String description = 
				"Card Number: " + _cardNumber + "; " + 
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}
}
