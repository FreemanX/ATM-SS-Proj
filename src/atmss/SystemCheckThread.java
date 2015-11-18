/**
 * 
 */
package atmss;

import atmss.bams.*;
import atmss.hardware.controller.*;

import java.util.Date;
import java.util.Random;

/**
 * @author freeman
 *
 */
class SystemCheckThread extends Thread {

	private volatile boolean isRunning = true;

	AdvicePrinterController _advicePrinterController;
	CardReaderController _cardReaderController;
	CashDispenserController _cashDispenerController;
	DepositCollectorController _depositCollectorController;
	DisplayController _displayController;
	EnvelopDispenserController _envelopDispenserController;
	KeypadController _keypadController;
	BAMSCommunicator _BAMSCommunicater; // Or just the handler

	/**
	 * 
	 */
	public SystemCheckThread() {
		System.out.println("Defualt constructor! Pass me the controllers");
	}

	public void setIsRunning(boolean b) {
		this.isRunning = b;
	}

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

	@Override
	public void run() {

		while (true) {
			while (isRunning) {
				try {
					System.out.println(">>>>>>>>>>>>>>>>>System Check Thread Running!");
					this._advicePrinterController.updateStatus();
					this._cardReaderController.updateStatus();
					this._cashDispenerController.updateStatus();
					this._depositCollectorController.updateStatus();
					this._displayController.updateStatus();
					this._envelopDispenserController.updateStatus();
					this._keypadController.updateStatus();
					sleep(6000); // Statuses will be checked every 60 seconds
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.err.println(">>>>>>>>>>>>>>>>>System Check Thread paused!");
		}
	}

	public void pauseCheck() {
		isRunning = false;
	}

	public void resumeCheck() {
		isRunning = true;
	}

}
