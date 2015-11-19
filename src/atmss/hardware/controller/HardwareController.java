/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.MBox;

/**
 * @author freeman
 *
 */
public abstract class HardwareController {

	int status;
	MBox _maincontrollerMBox;

	/**
	 * 
	 */
	public HardwareController() {
		// TODO Auto-generated constructor stub
	}

	public void setMainControllerMBox(MBox box) {
		this._maincontrollerMBox = box;
	}

	public abstract boolean updateStatus() throws Exception;

	public int getStatus() throws Exception {
		updateStatus();
		return status;
	}

	public abstract boolean reset() throws Exception;

	public abstract boolean shutdown() throws Exception;

	abstract void HandleException(HardwareException ex) throws Exception;

}
