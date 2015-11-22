/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.MBox;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class HardwareController.
 *
 * @author freeman
 */
public abstract class HardwareController {

	/** The status. */
	int status;

	/** The _maincontroller m box. */
	MBox _maincontrollerMBox;

	/**
	 * Instantiates a new hardware controller.
	 */
	public HardwareController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets the main controller m box.
	 *
	 * @param box the new main controller m box
	 */
	public void setMainControllerMBox(MBox box) {
		this._maincontrollerMBox = box;
	}

	/**
	 * Update status.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public abstract boolean updateStatus() throws Exception;

	/**
	 * Gets the status.
	 *
	 * @return the status
	 * @throws Exception the exception
	 */
	public int getStatus() throws Exception {
		updateStatus();
		return status;
	}

	/**
	 * Reset.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public abstract boolean reset() throws Exception;

	/**
	 * Shutdown.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public abstract boolean shutdown() throws Exception;

	/**
	 * Handle exception.
	 *
	 * @param ex the ex
	 * @throws Exception the exception
	 */
	abstract void HandleException(HardwareException ex) throws Exception;

	/**
	 * Report to main controller.
	 *
	 * @param ex the HardwareException
	 * @param type the type
	 */
	void reportToMainController(HardwareException ex, String type) {
		int exType = ex.getExceptionCode();
		System.err.println(ex);
		this._maincontrollerMBox.send(new Msg(type, exType, ex.getExceptionMsg()));
	}

}
