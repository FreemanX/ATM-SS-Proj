/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.DisplayException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.DisplayView;
import hwEmulators.Display;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayController.
 *
 * @author freeman
 */
public class DisplayController extends HardwareController {

	/** The display view. */
	private DisplayView displayView;

	/**
	 * Instantiates a new display controller.
	 *
	 * @param display the display
	 */
	public DisplayController(Display display) {
		displayView = new DisplayView(display);
	}

	/**
	 * Display upper.
	 *
	 * @param lines the lines
	 * @return true, if successful
	 * @throws DisplayException the display exception
	 */
	public boolean displayUpper(String[] lines) throws DisplayException {
		try {
			displayView.displayUpper(lines);
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * Display lower.
	 *
	 * @param line the line
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean displayLower(String line) throws Exception {
		try {
			displayView.displayLower(line);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Append upper.
	 *
	 * @param lines the lines
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean appendUpper(String[] lines) throws Exception {
		try {
			for (String line : lines) {
				appendUpper(line);
			}
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Append upper.
	 *
	 * @param line the line
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean appendUpper(String line) throws Exception {
		try {
			displayView.displayUpper(line);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Append lower.
	 *
	 * @param str the str
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean appendLower(String str) throws Exception {
		try {
			displayView.appendLower(str);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Clear all.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean clearAll() throws Exception {
		try {
			clearUpper();
			clearLower();
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Clear upper.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean clearUpper() throws Exception {
		try {
			displayView.clearUpper();
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	/**
	 * Clear lower.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean clearLower() throws Exception {
		try {
			displayView.clearLower();
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
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
			this.status = displayView.checkStatus();
			this._maincontrollerMBox.send(new Msg("Dis", status, "I'm OK"));
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
		if (ex instanceof DisplayException) {
			reportToMainController(ex, "Dis");
		} else
			throw ex;
	}

}
