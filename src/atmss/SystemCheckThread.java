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

	private boolean isRunning = true;

	AdvicePrinterController _advicePrinterController;
	CardReaderController _cardReaderController;
	CashDispenserController _cashDispenerController;
	DepositCollectorController _depositCollectorController;
	DisplayController _displayController;
	EnvelopDispenserController _envelopDispenserController;
	KeypadController _keypadController;
	BAMSCommunicater _BAMSCommunicater; // Or just the handler

	// listener for component status notification
	MainController.CheckerListener listener;

	/**
	 * 
	 */
	public SystemCheckThread(MainController.CheckerListener listener) {
		this.listener = listener;
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
			// debug test
			// randomly send out component failure message to test listener
			int failPos = 30; // 30%
			Random randGen = new Random(new Date().getTime());
			int rand = 0;
			int i = 1;

			while (true) {
				try {
					System.err.println("SystemCheck iteration: " + i);
					rand = randGen.nextInt(100) + 1; // 1 - 100
					if (rand > failPos)
						notifyListener("DebugComponent", 123, "Debug component failed, congratulation!");
					else
						System.err.println("DebugComponent working well!");
					i++;
					sleep(5000);
				} catch (InterruptedException e) {
					// nothing...
				}
			}
		}
	}

	public void pauseCheck() {
		isRunning = false;
	}

	public void resumeCheck() {
		isRunning = true;
	}

	public void notifyListener(String componentName, int status, String description) {
		listener.componentStatusNotify(componentName, status, description);
	}
}
