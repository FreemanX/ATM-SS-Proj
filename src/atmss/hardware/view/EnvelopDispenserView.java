/**
 * 
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public class EnvelopDispenserView extends HardwareView {

	/**
	 * 
	 */
	public EnvelopDispenserView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws HardwareException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

}