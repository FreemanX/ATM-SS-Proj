/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.MBox;
import hwEmulators.Msg;

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

	void reportToMainController(HardwareException ex, String type) {
		int exType = ex.getExceptionCode();
		System.err.println(ex);
		this._maincontrollerMBox.send(new Msg(type, exType, ex.getExceptionMsg()));
	}

}
