/**
 * 
 */
package atmss;

import atmss.bams.BAMSCommunicator;
import atmss.hardware.controller.*;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemCheckThread.
 *
 * @author freeman
 */
class SystemCheckThread extends Thread {

	/** The is running. */
	private volatile boolean isRunning = true;

	/** The _advice printer controller. */
	AdvicePrinterController _advicePrinterController;
	
	/** The _card reader controller. */
	CardReaderController _cardReaderController;
	
	/** The _cash dispener controller. */
	CashDispenserController _cashDispenerController;
	
	/** The _deposit collector controller. */
	DepositCollectorController _depositCollectorController;
	
	/** The _display controller. */
	DisplayController _displayController;
	
	/** The _envelop dispenser controller. */
	EnvelopDispenserController _envelopDispenserController;
	
	/** The _keypad controller. */
	KeypadController _keypadController;
	
	/** The _ bams communicater. */
	BAMSCommunicator _BAMSCommunicater; // Or just the handler

	/**
	 * Instantiates a new system check thread.
	 */
	public SystemCheckThread() {
		System.out.println("Defualt constructor! Pass me the controllers");
	}

	/**
	 * Instantiates a new system check thread.
	 *
	 * @param ap the ap
	 * @param cr the cr
	 * @param cd the cd
	 * @param dc the dc
	 * @param dp the dp
	 * @param ed the ed
	 * @param kp the kp
	 * @param ba the ba
	 */
	public SystemCheckThread(AdvicePrinterController ap, CardReaderController cr, CashDispenserController cd,
			DepositCollectorController dc, DisplayController dp, EnvelopDispenserController ed, KeypadController kp,
			BAMSCommunicator ba) {
		this._advicePrinterController = ap;
		this._cardReaderController = cr;
		this._cashDispenerController = cd;
		this._depositCollectorController = dc;
		this._displayController = dp;
		this._envelopDispenserController = ed;
		this._keypadController = kp;
		this._BAMSCommunicater = ba;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		while (true) {
			while (isRunning) {
				try {
					this._BAMSCommunicater.ping();
					sleep(100);
					this._advicePrinterController.updateStatus();
					sleep(100);
					this._cardReaderController.updateStatus();
					sleep(100);
					this._cashDispenerController.updateStatus();
					sleep(100);
					this._depositCollectorController.updateStatus();
					sleep(100);
					this._displayController.updateStatus();
					sleep(100);
					this._envelopDispenserController.updateStatus();
					sleep(100);
					this._keypadController.updateStatus();
					sleep(5000); // Statuses will be checked every 60 seconds
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Pause check.
	 */
	public void pauseCheck() {
		isRunning = false;
	}

	/**
	 * Resume check.
	 */
	public void resumeCheck() {
		isRunning = true;
	}

}
