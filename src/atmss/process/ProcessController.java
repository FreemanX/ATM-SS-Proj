/**
 * 
 */
package atmss.process;

import atmss.ATMSSHandler;
import atmss.MainController;
import atmss.Operation;
import atmss.Session;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessController.
 *
 * @author Lihui
 */
public abstract class ProcessController {

	/** The operation cache. */
	LinkedList<Operation> operationCache;
	
	/** The _main controller. */
	MainController _mainController;
	
	/** The _session. */
	Session _session;
	
	/** The _atmss handler. */
	ATMSSHandler _atmssHandler;

	/**
	 * Instantiates a new process controller.
	 */
	public ProcessController() {
		System.err.println("You have to pass me AccountNumbre and Maincontroller...");
	}

	/**
	 * Instantiates a new process controller.
	 *
	 * @param CurrentSession the current session
	 */
	public ProcessController(Session CurrentSession) {
		// TODO Auto-generated constructor stub
		operationCache = new LinkedList<>();
		this._session = CurrentSession;
		this._atmssHandler = ATMSSHandler.getHandler();
	}
	
	/**
	 * Gets the operation cache.
	 *
	 * @return the operation cache
	 */
	public LinkedList<Operation> getOperationCache() {
		return this.operationCache;
	}
	
	/**
	 * Creates the option list.
	 *
	 * @param Header the header
	 * @param Body the body
	 * @return the string[]
	 */
	String[] createOptionList(String Header, String[] Body) {
		String[] lines = new String[Body.length + 1];
		lines[0] = Header;
		for (int i = 1; i < lines.length; i++) {
			lines[i] = "Press " + i + " -> " + Body[i-1];
		}
		return lines;
	}
	
	/**
	 * Record.
	 *
	 * @param OperationName the operation name
	 * @param Type the type
	 */
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
	
	/**
	 * Record success.
	 *
	 * @param OperationName the operation name
	 * @param detail the detail
	 */
	private void recordSuccess(String OperationName, String detail) {
		operationCache.add(new Operation(OperationName, 0, "Success: "+detail));
	}
	
	/**
	 * Record failure.
	 *
	 * @param OperationName the operation name
	 * @param Type the type
	 */
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
