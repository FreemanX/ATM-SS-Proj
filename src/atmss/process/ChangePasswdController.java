/**
 * 
 */
package atmss.process;

import atmss.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangePasswdController.
 *
 * @author DJY
 */
public class ChangePasswdController extends ProcessController {

	/** The display message when prompting for new password. */
	private final String[] PROMPT_FOR_NEW_PASSWORD = {"Please input your new password:"};
	
	/** The display message when prompting for new password err. */
	private final String[] PROMPT_FOR_NEW_PASSWORD_ERR = {"The new passwords do not equal", "Please input your new password:"};
	
	/** The display message when prompting for confirm password. */
	private final String[] PROMPT_FOR_CONFIRM_PASSWORD = {"Please input your new password again:"};
	
	/** The display message when showing please wait. */
	private final String[] SHOW_PLEASE_WAIT = {"Processing, please wait..."};
	
	/** The display message when failed from BAMS updating password. */
	private final String[] FAILED_FROM_BAMS_UPDATING_PW = {"Cannot get approval from BAMS", "The password may not be changed"};
	
	/** The display message when failed from keypad. */
	private final String[] FAILED_FROM_KEYPAD = {"No response from the keypad"};
	
	/** The display message when failed from user cancelling. */
	private final String[] FAILED_FROM_USER_CANCELLING = {"The operation has been cancelled"};
	
	/** The time limit for key press in seconds. */
	private final long TIME_LIMIT = 20;
	
	/** The key value for cancel. */
	private final String KP_CANCEL = "CANCEL";
	
	/** The operation name. */
	private final String OPERATION_NAME = "Change Password";
	
	/** The current step. */
	private String currentStep = OPERATION_NAME;
	
	/** The new password. */
	private String newPassword = "";
	
	/** The confirm password. */
	private String confirmPassword = "";
	
	/** The overall result. */
	private boolean result = false;
	
	/**
	 * Instantiates a new change password controller.
	 *
	 * @param CurrentSession the current session
	 */
	public ChangePasswdController(Session CurrentSession) {
		super(CurrentSession);
	}
	
	/**
	 * Do change password, The core method in ChangePasswdController.
	 * The method will access all the instance variables through several private methods.
	 * 
	 * @return true, if all the relevant hardwares work fine, the user does not cancel or get time out, and the process can get approval from the banking system over the network; otherwise return false.
	 */
	public boolean doChangePasswd() {
		while (true) {
			if (!doGetNewPassword()) return false;
			if (!doGetConfirmPassword()) return false;
			if (doCheckNewPassword()) break;
		}
		if (!doGetApproval()) return false;
		doPrintAdvice();
		return true;
	}
	
	/**
	 * Do get new password, a step of the overall process.
	 * It will change the current step, and access the newPassword.
	 * It will record the current step before return.
	 *
	 * @return true, if the newPassword gets a new value;
	 */
	private boolean doGetNewPassword() {
		currentStep = OPERATION_NAME+": getting new password";
		if (newPassword.isEmpty()) {
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD)) {
				record("Dis");
				return false;
			}
		} else {
			if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_NEW_PASSWORD_ERR)) {
				record("Dis");
				return false;
			}
		}
		newPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
		if (newPassword == null) {
			record("KP");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_KEYPAD)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		} else if (newPassword.equals(KP_CANCEL)) {
			record("USER");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_USER_CANCELLING)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
		record("new password typed");
		return true;
	}
	
	/**
	 * Do get confirm password, a step of the overall process.
	 * It will change the current step, and access the confirmPassword.
	 * It will record the current step before return.
	 *
	 * @return true, if the confirmPassword gets a new value;
	 */
	private boolean doGetConfirmPassword() {
		currentStep = OPERATION_NAME+": getting confirm password";
		if (!_atmssHandler.doDisDisplayUpper(PROMPT_FOR_CONFIRM_PASSWORD)) {
			record("Dis");
			return false;
		}
		confirmPassword = _atmssHandler.doKPGetPasswd(TIME_LIMIT);
		if (confirmPassword == null) {
			record("KP");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_KEYPAD)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		} else if (confirmPassword.equals(KP_CANCEL)) {
			record("USER");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_USER_CANCELLING)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
		record("confirm password typed");
		return true;
	}
	
	/**
	 * Do check new password, a step of the overall process.
	 * It will access the newPassword and the confirmPassword.
	 * It will record the current step before return.
	 *
	 * @return true, if the newPassword is equal to the confirmPassword;
	 */
	private boolean doCheckNewPassword() {
		if (!newPassword.equals(confirmPassword)) return false;
		record("new password checked");
		return true;
	}
	
	/**
	 * Do get approval, a step of the overall process.
	 * It will change the current step, and ask for approval from the BAMS.
	 * It will record the current step before return.
	 *
	 * @return true, if the BAMS approves the change;
	 */
	private boolean doGetApproval() {
		currentStep = OPERATION_NAME+": getting approval from BAMS";
		if (!_atmssHandler.doDisDisplayUpper(SHOW_PLEASE_WAIT)) {
			record("Dis");
			return false;
		}
		result = _atmssHandler.doBAMSUpdatePasswd(newPassword, _session);
		if (result) {
			record("password changed");
			return true;
		} else {
			record("BAMS");
			if (!_atmssHandler.doDisDisplayUpper(FAILED_FROM_BAMS_UPDATING_PW)) {
				record("Dis");
				return false;
			}
			pause(3);
			return false;
		}
	}
	
	/**
	 * Do print advice, a step of the overall process.
	 * It will change the current step, and ask user for printing the advice.
	 * It will record the current step before return.
	 */
	private void doPrintAdvice(){
		currentStep = OPERATION_NAME+": printing advice";
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
						"Result: succeeded"
				};
				if (!_atmssHandler.doAPPrintStrArray(toPrint)) record("AP");
				return;
			} else if (userInput.equals("2")) {
				return;
			}
		}
	}
	
	/**
	 * Record operation.
	 *
	 * @param resultType the type of result of the current step
	 */
	private void record(String resultType) {
		super.record(currentStep, resultType);
	}
		
	/**
	 * Pause the process
	 *
	 * @param waitingTimeInSeconds the pause time in seconds
	 */
	private void pause(int waitingTimeInSeconds) {
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime < waitingTimeInSeconds*1000){}
	}
}
