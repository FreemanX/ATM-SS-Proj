/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.EnvelopDispenser;

/**
 * @author freeman
 *
 */
public class EnvelopDispenserView extends HardwareView {

	private EnvelopDispenser envelopDispenser;

	/**
	 *
	 */
	public EnvelopDispenserView(EnvelopDispenser envelopDispenser) {
		this.envelopDispenser = envelopDispenser;
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
