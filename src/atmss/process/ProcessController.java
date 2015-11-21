/**
 * 
 */
package atmss.process;

import atmss.ATMSSHandler;
import atmss.MainController;
import atmss.Operation;
import atmss.Session;

import java.util.LinkedList;

/**
 * @author Lihui
 *
 */
public abstract class ProcessController {

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
	
	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}
	
	String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "Press " + i + " -> " + Body[i-1];
		}
		return lines;
	}
	
	void record(String OperationName, String Type) {
		switch(Type) {
			case "AP" : recordFailure(OperationName, 1);break;
			case "CR" : recordFailure(OperationName, 2);break;
			case "CD" : recordFailure(OperationName, 3);break;
			case "DC" : recordFailure(OperationName, 4);break;
			case "Dis": recordFailure(OperationName, 5);break;
			case "ED" : recordFailure(OperationName, 6);break;
			case "KP" : recordFailure(OperationName, 7);break;
			case "USER" : recordFailure(OperationName, 8);break;
			case "BAMS" : recordFailure(OperationName, 10);break;
			default: recordSuccess(OperationName, Type);break;
		}
	}
	
	private void recordSuccess(String OperationName, String detail) {
		operationCache.add(new Operation(OperationName, 0, "Success: "+detail));
	}
	
	private void recordFailure(String OperationName, int Type) {
		String description;
		switch(Type) {
			case 1 : description = "Failure: no response from advice printer";break;
			case 2 : description = "Failure: no response from card reader";break;
			case 3 : description = "Failure: no response from cash dispenser";break;
			case 4 : description = "Failure: no response from deposit collector";break;
			case 5 : description = "Failure: no response from display";break;
			case 6 : description = "Failure: no response from evelop dispenser";break;
			case 7 : description = "Failure: no response from keypad";break;
			case 8 : description = "Failure: cancellation from user";break;
			case 10: description = "Failure: disapproval from bank system (BAMS)";break;
			default: description = "Failure: unknown reason";break;
		}
		operationCache.add(new Operation(OperationName, Type, description));
		_atmssHandler.doAPPrintStrArray(new String[]{OperationName,description});
	}
}
