/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.EnvelopDispenserView;
import hwEmulators.EnvelopDispenser;

/**
 * @author freeman
 *
 */
public class EnvelopDispenserController extends HardwareController {

	private EnvelopDispenserView envelopDispenserView;

	/**
	 *
	 */
	public EnvelopDispenserController(EnvelopDispenser envelopDispenser) {
		this.envelopDispenserView = new EnvelopDispenserView(envelopDispenser);
	}


	public boolean ejectEnvelop() {
		return envelopDispenserView.ejectEnvelop();
	}

	public int getEnvelopCount() {
		return envelopDispenserView.getEnvelopCount();
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
