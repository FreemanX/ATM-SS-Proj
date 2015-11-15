/**
 * 
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public class CashDispenser extends HardwareView {

	/**
	 * 
	 */
	public CashDispenser() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.view.HardwareView#checkStatus()
	 */
	@Override
	public int checkStatus() throws HardwareException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.view.HardwareView#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.view.HardwareView#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.view.HardwareView#throwException(int)
	 */
	@Override
	void throwException(int Code) throws HardwareException {
		// TODO Auto-generated method stub

	}

}
