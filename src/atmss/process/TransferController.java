/**
 * 
 */
package atmss.process;

import atmss.MainController;

/**
 * @author SXM
 *
 */
public class TransferController extends ProcessController{

	private String desAccountNumber;
	/**
	 * 
	 */
	public TransferController(String AccountNumber, MainController MainController) {
		// TODO Auto-generated constructor stub
		super(AccountNumber, MainController);
	}
	
	public Boolean doTransfer() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

}
