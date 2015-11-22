/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.EnvelopDispenserException;
import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.EnvelopDispenser;

// TODO: Auto-generated Javadoc
/**
 * The Class EnvelopDispenserView.
 *
 * @author freeman
 */
public class EnvelopDispenserView extends HardwareView {

	/** The envelop dispenser. */
	private EnvelopDispenser envelopDispenser;

	/**
	 * Instantiates a new envelop dispenser view.
	 *
	 * @param envelopDispenser the EnvelopDispenser emulator
	 */
	public EnvelopDispenserView(EnvelopDispenser envelopDispenser) {
		this.envelopDispenser = envelopDispenser;
	}

	/**
	 * Eject envelop.
	 *
	 * @return true, if successful
	 * @throws EnvelopDispenserException the envelop dispenser exception
	 */
	public boolean ejectEnvelop() throws EnvelopDispenserException {
		checkStatus();
		return envelopDispenser.ejectEnvelop();
	}

	/**
	 * Gets the envelop count.
	 *
	 * @return the envelop count
	 * @throws EnvelopDispenserException the envelop dispenser exception
	 */
	public int getEnvelopCount() throws EnvelopDispenserException {
		checkStatus();
		return envelopDispenser.getEnvelopCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws EnvelopDispenserException {
		if (envelopDispenser.getEDStatus() % 100 != 0)
			throwException(envelopDispenser.getEDStatus());
		return envelopDispenser.getEDStatus();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int code) throws EnvelopDispenserException {
		if (code > 690)
			throw new EnvelopDispenserException();
		else
			throw new EnvelopDispenserException(code);
	}

}
