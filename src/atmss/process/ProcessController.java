/**
 * 
 */
package atmss.process;

import java.util.*;
import atmss.*;

/**
 * @author Lihui
 *
 */
public abstract class ProcessController {

	String _cardNumber;
	LinkedList<Operation> operationCache;
	MainController _mainController;

	public ProcessController() {
		System.err.println("You have to pass me AccountNumbre and Maincontroller...");
	}

	public ProcessController(String CardNumber, MainController MainController) {
		// TODO Auto-generated constructor stub
		this.initialization(CardNumber, MainController);
	}

	void initialization(String CardNumber, MainController MainController) {
		this._cardNumber = CardNumber;
		this._mainController = MainController;
		operationCache = new LinkedList<>();
	}

	public void setCardNumber(String CardNumber) {
		this._cardNumber = CardNumber;
	}

	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}

	void addOperation(Operation operation) {
		operationCache.add(operation);
	}

}
