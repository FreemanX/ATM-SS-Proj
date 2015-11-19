/**
 * 
 */
package atmss.process;

import atmss.MainController;
import atmss.Operation;
import atmss.Session;

/**
 * @author DJY
 *
 */
public class ChangePasswdController extends ProcessController {

	private final String OPERATION_NAME = "Change Password";
	private final String FAILED_FROM_BAMS_UPDATING_PW = "Cannot get approval from BAMS";
	private final String FAILED_FROM_DISPLAY = "No response from the display";
	private final String FAILED_FROM_KEYPAD = "No response from the keypad";
	private final String FAILED_FROM_USER_CANCELLING = "The operation has been cancelled";
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please type your new password:"};
	private final String[] PROMPT_FOR_NEW_PASSWORD_ERR = {"The new passwords do not equal", "Please type your new password:"};
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please type your new password again:"};
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	private final String[] SHOW_SUCCESS = {"Succeeded!", "The password has been changed."};
	private final long TIME_LIMIT = 30 * 1000;
	private final String KP_CANCEL = "CANCEL";

	public ChangePasswdController(Session CurrentSession) {
		super(CurrentSession);
	}

	public boolean doChangePasswd() {
		String newPassword = "";
		String confirmPassword = "";
		boolean result = false;
		
		// -> preparing the necessary information
		// get new password from the user
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		while (true) {
			newPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
			if (newPassword == null) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			} else if (newPassword.equals(KP_CANCEL)) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_USER_CANCELLING);
				return false;
			}
			
			// get confirm password from the user
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_CONFIRM_PASSWORD)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			confirmPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
			if (confirmPassword == null) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_KEYPAD})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_KEYPAD);
				return false;
			} else if (confirmPassword.equals(KP_CANCEL)) {
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					recordOperation(FAILED_FROM_DISPLAY);
					return false;
				}
				recordOperation(FAILED_FROM_USER_CANCELLING);
				return false;
			}
			 
			// check the new and confirm password
			if (newPassword.equals(confirmPassword)) break; 
			
			// start again if not equal
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD_ERR)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
		}
		// <- preparing the necessary information
		
		// contact BAMS now
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			recordOperation(FAILED_FROM_DISPLAY);
			return false;
		}
		result = _atmssHandler.doBAMSUpdatePasswd(newPassword, _session);
		
		// display the result
		if (result) {
			if (!_atmssHandler.doDisDisplayUpper(SHOW_SUCCESS)) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation();
			return true;
		} else {
			if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_BAMS_UPDATING_PW,"The password may not be changed"})) {
				recordOperation(FAILED_FROM_DISPLAY);
				return false;
			}
			recordOperation(FAILED_FROM_BAMS_UPDATING_PW);
			return false;
		}
	}
	
	private void recordOperation() {
		String description = 
				"Card Number: " + _session.getCardNo() + "; " + 
				"Result: " + "Succeeded; ";
		operationCache.add(new Operation(OPERATION_NAME, description));
	}
	
	private void recordOperation(String FailedReason) {
		String description = 
				"Card Number: " + _session.getCardNo() + "; " + 
				"Result: " + "Failed; " +
				"Reason: " + FailedReason;
		operationCache.add(new Operation(OPERATION_NAME, description));
	}
}
