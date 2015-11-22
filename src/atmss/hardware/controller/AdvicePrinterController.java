/**
 *
 */
package atmss.hardware.controller;

import atmss.Operation;
import atmss.hardware.exceptioins.AdvicePrinterException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.AdvicePrinterView;
import hwEmulators.AdvicePrinter;
import hwEmulators.Msg;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvicePrinterController.
 *
 * @author freeman
 */
public class AdvicePrinterController extends HardwareController {

	/** The advice printer. */
	private AdvicePrinterView advicePrinter;

	/**
	 * Instantiates a new advice printer controller.
	 *
	 * @param AP the AdvicePrinter view
	 */
	public AdvicePrinterController(AdvicePrinter AP) {
		// TODO Auto-generated constructor stub
		advicePrinter = new AdvicePrinterView(AP);
	}

	/**
	 * Prints the operations.
	 *
	 * @param operations the operations
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	// Non-overrided methods:
	public boolean printOperations(LinkedList<Operation> operations) throws Exception {

		String[] strLines = new String[operations.size()];
		/*
		 * TODO get the strings from operation list and store them in to a
		 * String array, then pass the the string array to the printer
		 */
		for (int i = 0; i < strLines.length; i++) {
			strLines[i] = operations.get(i).toString();
		}
		return printStrArray(strLines);
	}

	/**
	 * Prints the str array.
	 *
	 * @param toPrint the array to print
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean printStrArray(String[] toPrint) throws Exception {
		boolean isSuccess = false;

		try {
			advicePrinter.print(toPrint);
			isSuccess = true;
		} catch (AdvicePrinterException e) {
			this.HandleException(e);
			isSuccess = false;
		}

		return isSuccess;
	}

	/**
	 * Check inventory.
	 *
	 * @return the inventory count
	 * @throws Exception the exception
	 */
	public int checkInventory() throws Exception {
		try {
			return advicePrinter.checkInventory();
		} catch (AdvicePrinterException ex) {
			this.HandleException(ex);
			return -1;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */
	@Override
	public boolean updateStatus() throws Exception {
		boolean isSuccess = false;
		try {
			this.status = advicePrinter.checkStatus();
			this._maincontrollerMBox.send(new Msg("AP", status, "I'm OK"));
			isSuccess = true;
		} catch (HardwareException e) {
			isSuccess = false;
			HandleException(e);
		}

		return isSuccess;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#reset()
	 */
	@Override
	public boolean reset() {
		// TODO Auto-generated method stub

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#shutdonw()
	 */
	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#HandleException()
	 */
	@Override
	void HandleException(HardwareException ex) throws Exception {
		// TODO Auto-generated method stub
		if (ex instanceof AdvicePrinterException) {
			reportToMainController(ex, "AP");
		} else {
			throw ex;
		}
	}

}
