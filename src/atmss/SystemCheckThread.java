/**
 * 
 */
package atmss;

import atmss.bams.*;
import atmss.hardware.*;

/**
 * @author freeman
 *
 */
class SystemCheckThread extends Thread {

	private boolean isRunning = true;

	AdvicePrinterController _advicePrinterController;
	CardReaderController _cardReaderController;
	CashDispenserController _cashDispenerController;
	DepositCollectorController _depositCollectorController;
	DisplayController _displayController;
	EnvelopDispenserController _envelopDispenserController;
	KeypadController _keypadController;
	BAMSCommunicater _BAMSCommunicater; // Or just the handler

	/**
	 * 
	 */
	public SystemCheckThread() {
		// TODO Auto-generated constructor stub
		System.out.println("Defualt constructor! Pass me the controllers");
	}

	public SystemCheckThread(AdvicePrinterController ap,
			CardReaderController cr, CashDispenserController cd,
			DepositCollectorController dc, DisplayController dp,
			EnvelopDispenserController ed, KeypadController kp,
			BAMSCommunicater ba) {
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

		while (isRunning) {
			/*
			 * Hardware checking schedule here
			 */
		}
	}

	public void pauseCheck() {
		isRunning = false;
	}

	public void resumeCheck() {
		isRunning = true;
	}

}
