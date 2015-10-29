/**
 * 
 */
package atmss.process;

import java.util.LinkedList;
import atmss.*;

/**
 * @author freeman
 *
 */
public abstract class ProcessController {

	String _accountNumber;
	LinkedList<Operation> operationCache;
	MainController _mainController;

	public ProcessController() {
		System.err
				.println("You have to pass me AccountNumbre and Maincontroller...");
	}

	public ProcessController(String AccountNumber, MainController MainController) {
		// TODO Auto-generated constructor stub
		this.initialization(AccountNumber, MainController);
	}

	void initialization(String AccountNumber, MainController MainController) {
		this._accountNumber = AccountNumber;
		this._mainController = MainController;
		operationCache = new LinkedList<>();
	}

	public void setAccountNumber(String AccountNumber) {
		this._accountNumber = AccountNumber;
	}

	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}

	void addOperation(Operation operation) {
		operationCache.add(operation);
	}

}
