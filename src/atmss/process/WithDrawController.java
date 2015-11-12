/**
 * 
 */
package atmss.process;

import atmss.MainController;

/**
 * @author DJY
 *
 */
public class WithDrawController extends ProcessController{

	private int amount;
	/**
	 * 
	 */
	public WithDrawController(String AccountNumber,
			MainController MainController) {
		// TODO Auto-generated constructor stub
		super(AccountNumber, MainController);
	}

	public Boolean doWithDraw() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
}
