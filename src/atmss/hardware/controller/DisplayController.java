/**
 *
 */
package atmss.hardware.controller;

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

	public void displayUpper(String[] lines) {
		displayView.displayUpper(lines);
	}

	public void displayLower(String line) {
		displayView.displayLower(line);
	}

	public void appendUpper(String[] lines) {
		for (String line : lines) {
			appendUpper(line);
		}
	}

	public void appendUpper(String line) {
		displayView.displayUpper(line);
	}

	public void appendLower(String str) {
		displayView.appendLower(str);
	}

	public void clearAll() {
		clearUpper();
		clearLower();
	}

	public void clearUpper() {
		displayView.clearUpper();
	}

	public void clearLower() {
		displayView.clearLower();
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
		// TODO Auto-generated method stub

	}

}
