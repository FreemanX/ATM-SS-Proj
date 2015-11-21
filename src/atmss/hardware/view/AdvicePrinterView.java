/**
 * 
 */
package atmss.hardware.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import atmss.hardware.exceptioins.AdvicePrinterException;
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
		this._advicePrinter = ap;
	}

	public boolean print(String[] advice) throws AdvicePrinterException {
		boolean isSuccess = false;
		for (int i = 0; i < advice.length; i++) {
			checkStatus();
			if (i == 0) {
				this._advicePrinter.println("");
				SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
				this._advicePrinter.println("=====" + f.format(new Date().getTime()) + "=====");
			}
			this._advicePrinter.println(advice[i]);
		}
		isSuccess = true;

		return isSuccess;
	}

	public int checkInventory() throws AdvicePrinterException {

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
	public int checkStatus() throws AdvicePrinterException {

		int currStatus = this._advicePrinter.getAPStatus();
		if (currStatus % 100 != 0)
			throwException(currStatus);
		return currStatus;

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
		if (Code > 190)
			throw new AdvicePrinterException();
		else
			throw new AdvicePrinterException(Code);
	}

}
