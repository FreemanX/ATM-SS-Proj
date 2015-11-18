/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.DisplayException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.DisplayView;
import hwEmulators.Display;

/**
 * @author freeman
 *
 */
public class DisplayController extends HardwareController {

	private DisplayView displayView;
	/**
	 *
	 */
	public DisplayController(Display display) {
		displayView = new DisplayView(display);
	}

	public void displayUpper(String[] lines) throws DisplayException {
		displayView.displayUpper(lines);
	}

	public void displayLower(String line) throws Exception {
		try {
			displayView.displayLower(line);
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void appendUpper(String[] lines) throws Exception {
		try {
			for (String line : lines) {
				appendUpper(line);
			}
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void appendUpper(String line) throws Exception {
		try {
			displayView.displayUpper(line);
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void appendLower(String str) throws Exception {
		try {
			displayView.appendLower(str);
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void clearAll() throws Exception {
		try {
			clearUpper();
			clearLower();
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void clearUpper() throws Exception {
		try {
			displayView.clearUpper();
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	public void clearLower() throws Exception {
		try {
			displayView.clearLower();
		} catch (DisplayException e) {
			HandleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */
	@Override
	public boolean updateStatus() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#reset()
	 */
	@Override
	public boolean reset() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#shutdonw()
	 */
	@Override
	public boolean shutdown() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#HandleException(atmss.hardware.hw.exceptioins.HardwareException)
	 */
	@Override
	void HandleException(HardwareException ex) throws Exception {
		if (ex instanceof DisplayException) {
			System.err.println(ex.getClass().getSimpleName() + "\n    " + ex.getExceptionCode() + ":" + ex.getMessage());
		}
	}

}
