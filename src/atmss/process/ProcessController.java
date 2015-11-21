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
	String _credential;
	LinkedList<Operation> operationCache;
	MainController _mainController;
	Session _session;
	ATMSSHandler _atmssHandler;

	public ProcessController() {
		System.err.println("You have to pass me AccountNumbre and Maincontroller...");
	}

	public ProcessController(Session CurrentSession) {
		// TODO Auto-generated constructor stub
		operationCache = new LinkedList<>();
		this._session = CurrentSession;
		this._atmssHandler = ATMSSHandler.getHandler();
	}

	public void setCardNumber() {
		this._cardNumber = _session.getCardNo();
	}

	public void setCred(){
		this._credential = _session.getCred();
	}
	
	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}

	void addOperation(Operation operation) {
		operationCache.add(operation);
		
	}

}
