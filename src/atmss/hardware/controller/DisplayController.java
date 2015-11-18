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

	public boolean displayUpper(String[] lines) throws DisplayException {
		try {
			displayView.displayUpper(lines);
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	public boolean displayLower(String line) throws Exception {
		try {
			displayView.displayLower(line);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

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

	public boolean appendUpper(String line) throws Exception {
		try {
			displayView.displayUpper(line);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	public boolean appendLower(String str) throws Exception {
		try {
			displayView.appendLower(str);
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

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

	public boolean clearUpper() throws Exception {
		try {
			displayView.clearUpper();
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
	}

	public boolean clearLower() throws Exception {
		try {
			displayView.clearLower();
			return true;
		} catch (DisplayException e) {
			HandleException(e);
		}
		return false;
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
			int exType = ex.getExceptionCode();

			switch (exType) {
				case 599:
					System.err.println(">>>>>>>>>>>Unknown display error.");
					break;
			}
		}
		throw ex;
	}

}
