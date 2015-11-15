/**
 * 
 */
package atmss.hardware;

import java.util.LinkedList;

import atmss.Operation;
import atmss.hardware.hw.AdvicePrinterView;
import atmss.hardware.hw.exceptioins.AdvicePrinterException;
import atmss.hardware.hw.exceptioins.HardwareException;
import hwEmulators.AdvicePrinter;

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
	public boolean printAdvice(LinkedList<Operation> operations) throws Exception {
		boolean isSuccess = false;
		try {
			String[] strLines = new String[operations.size()];
			/*
			 * TODO get the strings from operation list and store them in to a
			 * String array, then pass the the string array to the printer
			 */
			advicePrinter.print(strLines);
			isSuccess = true;
		} catch (AdvicePrinterException ex) {
			this.HandleException(ex);
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
		} catch (AdvicePrinterException e) {
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
		if (ex.getClass().getName().equals("AdvicePrinterException")) {
			int exType = ex.getExceptionCode();
			// TODO handle ex and report to MainController;
			switch (exType) {
			case 101:
				System.err.println(">>>>>>>>>>>Out of paper");
				break;
			case 102:
				System.err.println(">>>>>>>>>>>Out of ink");
				break;
			case 103:
				System.err.println(">>>>>>>>>>>Paper jam");
				break;
			case 199:
				System.err.println(">>>>>>>>>>>Hardware failure");
				break;
			default:
				throw ex;
			}

		} else {
			throw ex;
		}
	}

}
