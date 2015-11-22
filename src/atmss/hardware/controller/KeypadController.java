/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.exceptioins.KeypadException;
import atmss.hardware.view.KeypadView;
import hwEmulators.Keypad;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class KeypadController.
 *
 * @author freeman
 */
public class KeypadController extends HardwareController {

	/** The keypad view. */
	private KeypadView keypadView;

	/**
	 * Instantiates a new keypad controller.
	 *
	 * @param KP the KeypadView
	 */
	public KeypadController(Keypad KP) {
		this.keypadView = new KeypadView(KP);
	}

	/**
	 * Read user input.
	 *
	 * @param Duration the duration
	 * @return the string
	 * @throws Exception the exception
	 */
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
			this._maincontrollerMBox.send(new Msg("KP", status, "I'm OK"));
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
			reportToMainController(ex, "KP");
		} else {
			throw ex;
		}

	}

}
