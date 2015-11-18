/**
 *
 */
package atmss.hardware.view;

import atmss.Timer;
import atmss.hardware.exceptioins.DepositCollectorException;
import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.DepositCollector;
import hwEmulators.MBox;
import hwEmulators.Msg;

/**
 * @author freeman, tony
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

	public boolean collectEnvelop(int timeout) throws DepositCollectorException {
		checkStatus();
		Timer timer = Timer.getTimer();
		timer.initTimer(timeout, mbox);


		depositCollector.openSlot();
		if (!depositCollector.getHasEnvelop()) {
			// wait for envelop or timeout
			Msg msg = mbox.receive();

			if (msg.getType() == 999 && msg.getSender().equalsIgnoreCase("Timer:" + timer.getTimerId())) { // timeout
				// reject envelop and return false
				collectTimeout();

				return false;
			} else if (msg.getType() == 4 && msg.getSender().equalsIgnoreCase("DepositCollector")) { // user put in envelop
				depositCollector.closeSlot(false);

				if (depositCollector.isSlotOpen())
					throwException(402); // failed to close slot

				return !depositCollector.isSlotOpen();
			} else {
				// something else...
			}
		}
		return false;
	}

	// if timeout, reject the collection
	private boolean collectTimeout() throws DepositCollectorException {
		if (!depositCollector.getHasEnvelop()) { // has no envelop inside
			depositCollector.closeSlot(true);

			if (depositCollector.isSlotOpen())
				throwException(402); // failed to close slot

			return !depositCollector.isSlotOpen();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws DepositCollectorException {
		if (depositCollector.getDCStatus() % 100 != 0)
			throwException(depositCollector.getDCStatus());
		return depositCollector.getDCStatus();
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
		if (Code > 490)
			throw new DepositCollectorException();
		else
			throw new DepositCollectorException(Code);
	}

}
