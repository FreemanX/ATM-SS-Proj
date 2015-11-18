/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.EnvelopDispenserException;
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

	public boolean ejectEnvelop() throws EnvelopDispenserException {
		checkStatus();
		return envelopDispenser.ejectEnvelop();
	}

	public int getEnvelopCount() throws EnvelopDispenserException {
		checkStatus();
		return envelopDispenser.getEnvelopCount();
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws EnvelopDispenserException {
		if (envelopDispenser.getEDStatus() % 100 != 0)
			throwException(envelopDispenser.getEDStatus());
		return envelopDispenser.getEDStatus();
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
	void throwException(int code) throws EnvelopDispenserException {
		throw new EnvelopDispenserException(code);
	}

}
