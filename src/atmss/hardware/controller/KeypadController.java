/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.exceptioins.KeypadException;
import atmss.hardware.view.KeypadView;
import hwEmulators.Keypad;

/**
 * @author freeman
 *
 */
public class KeypadController extends HardwareController {
	private KeypadView keypadView;

	/**
	 * 
	 */
	public KeypadController(Keypad KP) {
		this.keypadView = new KeypadView(KP);
	}

	public String readUserInput(long Duration) throws Exception {
		try {
			return this.keypadView.readUserInput(Duration);
		} catch (KeypadException e) {
			HandleException(e);
			return null;
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
			this.status = this.keypadView.checkStatus();
			isSuccess = true;
		} catch (KeypadException e) {
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
	public boolean reset() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#shutdonw()
	 */
	@Override
	public boolean shutdown() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#HandleException(atmss.hardware.hw.
	 * exceptioins.HardwareException)
	 */
	@Override
	void HandleException(HardwareException ex) throws Exception {
		// TODO Auto-generated method stub
		if (ex instanceof HardwareException) {
			int exType = ex.getExceptionCode();
			// TODO handle ex and report to MainController;
			switch (exType) {
			case 799:
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
