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
	String _cretencial;
	LinkedList<Operation> operationCache;
	MainController _mainController;
	Session _session;
	ATMSSHandler _atmssHandler;

	public ProcessController() {
		System.err.println("You have to pass me AccountNumbre and Maincontroller...");
	}

	public ProcessController(Session currentSession, MainController MainController) {
		// TODO Auto-generated constructor stub
		operationCache = new LinkedList<>();
		this._session = currentSession;
		this._atmssHandler = ATMSSHandler.getHandler();
	}

	public void setCardNumber(String CardNumber) {
		this._cardNumber = CardNumber;
	}

	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}

	void addOperation(Operation operation) {
		operationCache.add(operation);
		this._session.addOp(operation);
	}

}
