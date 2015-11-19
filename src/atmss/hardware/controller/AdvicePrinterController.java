/**
 * 
 */
package atmss.hardware.controller;

import java.util.LinkedList;

import atmss.Operation;
import atmss.hardware.exceptioins.AdvicePrinterException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.AdvicePrinterView;
import hwEmulators.AdvicePrinter;
import hwEmulators.Msg;

/**
 * @author freeman
 *
 */
public class AdvicePrinterController extends HardwareController {

	private AdvicePrinterView advicePrinter;

	/**
	 * 
	 */
	public AdvicePrinterController(AdvicePrinter AP) {
		// TODO Auto-generated constructor stub
		advicePrinter = new AdvicePrinterView(AP);
	}

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

	public boolean printStrArray(String[] toPrint) throws Exception {
		boolean isSuccess = false;

		try {
			advicePrinter.print(toPrint);
		} catch (AdvicePrinterException e) {
			this.HandleException(e);
			isSuccess = false;
		}

		return isSuccess;
	}

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
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		try {
			this.status = advicePrinter.checkStatus();
			isSuccess = true;
		} catch (HardwareException e) {
			// TODO Auto-generated catch block
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
		if (ex instanceof HardwareException) {
			int exType = ex.getExceptionCode();
			// TODO handle ex and report to MainController;
			switch (exType) {
			case 101:
				// TODO notify the main controller
				System.err.println(">>>>>>>>>>>Out of paper");
				this._maincontrollerMBox.send(new Msg("AP", 101, "Out of paper"));
				break;
			case 102:
				System.err.println(">>>>>>>>>>>Out of ink");
				this._maincontrollerMBox.send(new Msg("AP", 102, "Out of ink"));
				break;
			case 103:
				System.err.println(">>>>>>>>>>>Paper jam");
				this._maincontrollerMBox.send(new Msg("AP", 103, "Paper jam"));
				break;
			case 199:
				System.err.println(">>>>>>>>>>>Hardware failure");
				this._maincontrollerMBox.send(new Msg("AP", 199, "Hardware failure"));
				break;
			default:
				throw ex;
			}
		} else {
			throw ex;
		}
	}

}
