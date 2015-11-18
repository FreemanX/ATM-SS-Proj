/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.EnvelopDispenserException;
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


	public boolean ejectEnvelop() throws Exception {
		try {
			return envelopDispenserView.ejectEnvelop();
		} catch (EnvelopDispenserException e) {
			HandleException(e);
		}
		return false;
	}

	public int getEnvelopCount() throws Exception {
		try {
			return envelopDispenserView.getEnvelopCount();
		} catch (EnvelopDispenserException e) {
			HandleException(e);
		}
		return -1;
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
		if (ex instanceof EnvelopDispenserException) {
			int exType = ex.getExceptionCode();

			switch (exType) {
				case 601:
					System.err.println(">>>>>>>>>>>No envelop error.");
					break;
				case 699:
					System.err.println(">>>>>>>>>>>Unknown EnvelopDispenser error.");
					break;
			}
		}

		throw ex;
	}

}
