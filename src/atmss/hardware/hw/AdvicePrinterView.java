/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.AdvicePrinterException;
import hwEmulators.AdvicePrinter;

/**
 * @author freeman
 *
 */
public class AdvicePrinterView extends HardwareView {
	private AdvicePrinter _advicePrinter;

	/**
	 * 
	 */
	public AdvicePrinterView(AdvicePrinter ap) {
		// TODO Auto-generated constructor stub
		this._advicePrinter = ap;
		checkStatus();
	}

	public boolean print(String[] advice) throws AdvicePrinterException {
		boolean isSuccess = false;
		/*
		 * TODO call the hardware to do
		 */
		int currentStatus = checkStatus();
		if (currentStatus == 100) {
			for (String str : advice) {
				this._advicePrinter.println(str);
			}
			isSuccess = true;
		} else {
			throwException(currentStatus);
		}

		return isSuccess;
	}

	public int checkInventory() throws AdvicePrinterException {
		/*
		 * TODO call the hardware to do
		 */
		int res = this._advicePrinter.getResource();
		if (res < 1) {
			throwException(this._advicePrinter.getAPStatus());
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() {
		// TODO Auto-generated method stub
		return this._advicePrinter.getAPStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws AdvicePrinterException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws AdvicePrinterException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws AdvicePrinterException {
		// TODO Auto-generated method stub
		throw new AdvicePrinterException(Code);
	}

}
