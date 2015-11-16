/**
 *
 */
package atmss.hardware.view;

import atmss.Timer;
import atmss.hardware.exceptioins.DepositCollectorException;
import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.DepositCollector;
import hwEmulators.MBox;

/**
 * @author freeman
 *
 */
public class DepositCollectorView extends HardwareView {

	private DepositCollector depositCollector;
	private MBox mbox = new MBox("DepositCollectorView");

	/**
	 *
	 */
	public DepositCollectorView(DepositCollector depositCollector) {
		this.depositCollector = depositCollector;
		//this.depositCollector.setMBox(mbox);
	}

	public boolean prepareCollection() throws DepositCollectorException {
		checkStatus();
		depositCollector.openSlot();
		if (!depositCollector.isSlotOpen()) throwException(402);
		return depositCollector.isSlotOpen();
	}

	public boolean collectEnvelop() throws DepositCollectorException {
		checkStatus();
		if (depositCollector.getHasEnvelop()) { // has envelop inside
			depositCollector.closeSlot(false);
			if (depositCollector.isSlotOpen()) throwException(402);
			return !depositCollector.isSlotOpen();
		} else { // no envelop was placed
			throwException(401);
		}
		return false;
	}

	// if timeout, reject the collection
	public boolean collectTimeout() throws DepositCollectorException {
		checkStatus();
		if (!depositCollector.getHasEnvelop()) { // has no envelop inside
			depositCollector.closeSlot(true);
			if (depositCollector.isSlotOpen()) throwException(402);
			return !depositCollector.isSlotOpen();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws DepositCollectorException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws DepositCollectorException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws DepositCollectorException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws DepositCollectorException {
		throw new DepositCollectorException(Code);
	}

}
