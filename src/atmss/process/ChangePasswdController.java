/**
 * 
 */
package atmss.process;

import atmss.MainController;

/**
 * @author DJY
 *
 */
public class ChangePasswdController extends ProcessController {

	private int newPasswd1;
	private int newPasswd2;
	/**
	 * 
	 */
	public ChangePasswdController(String AccountNumber,
			MainController MainController) {
		// TODO Auto-generated constructor stub
		super(AccountNumber, MainController);
	}

	public Boolean doChangePasswd() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	
	private boolean checkNewPasswd() {
		return newPasswd1 == newPasswd2;
	}

}
