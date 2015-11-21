/**
 * 
 */
package atmss.process;

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
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please input your new password:"};
	private final String[] PROMPT_FOR_NEW_PASSWORD_ERR = {"The new passwords do not equal", "Please input your new password:"};
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please input your new password again:"};
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
				pause(3);
				return false;
			} else if (newPassword.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					record("Dis");
					return false;
				}
				pause(3);
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
				pause(3);
				return false;
			} else if (confirmPassword.equals(KP_CANCEL)) {
				record("USER");
				if (!_atmssHandler.doDisDisplayUpper(new String[]{FAILED_FROM_USER_CANCELLING})) {
					record("Dis");
					return false;
				}
				pause(3);
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
			pause(3);
			return false;
		}
	}
	
	private void pause(int Seconds) {
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime < Seconds*1000){}
	}
	
	private void record(String Type) {
		super.record(_currentStep, Type);
	}
	
	private void askForPrinting(){
		String[] toDisplay = {
				"Operation succeeded!",
				"You have changed your password",
				"Press 1 -> Print the advice",
				"Press 2 -> Quit without printing"
		};
		if (!_atmssHandler.doDisDisplayUpper(toDisplay)) {
			record("Dis");
			return;
		}
		while (true) {
			String userInput = _atmssHandler.doKPGetSingleInput(TIME_LIMIT);
			if (userInput == null) return;
			if (userInput.equals("1")) {
				String[] toPrint = {
						"Operation name: " + OPERATION_NAME,
						"Card Number: " + _session.getCardNo(),
						"Result: Succeeded"
				};
				if (!_atmssHandler.doAPPrintStrArray(toPrint)) record("AP");
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
}
