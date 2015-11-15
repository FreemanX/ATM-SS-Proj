/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public class CashDispenserController extends HardwareController {

	/**
	 * 
	 */
	public CashDispenserController() {
		// TODO Auto-generated constructor stub
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
