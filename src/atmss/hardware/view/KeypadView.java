/**
 *
 */
package atmss.hardware.view;

import atmss.Timer;
import atmss.hardware.exceptioins.KeypadException;
import hwEmulators.Keypad;
import hwEmulators.MBox;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class KeypadView.
 *
 * @author freeman
 */
public class KeypadView extends HardwareView {

	/** The _keypad. */
	private Keypad _keypad;

	/** The keypad view m box. */
	private MBox keypadViewMBox;

	/**
	 * Instantiates a new keypad view.
	 *
	 * @param KP the Keypad emulator
	 */
	public KeypadView(Keypad KP) {
		this._keypad = KP;
		keypadViewMBox = new MBox("KeypadView");
		this._keypad.setViewMBox(keypadViewMBox);
	}

	/**
	 * Read user input.
	 *
	 * @param timeout the timeout duration
	 * @return the string
	 * @throws KeypadException the keypad exception
	 */
	public String readUserInput(long timeout) throws KeypadException {
		checkStatus();
		String buf = "";
		Timer timer = Timer.getTimer();
		timer.initTimer(timeout, this.keypadViewMBox);

		long inputId = System.currentTimeMillis();
		this._keypad.setKeypadEnable(true, inputId);
		timer.start();
		while (true) {
			keypadViewMBox.clearBox();
			Msg msg = keypadViewMBox.receive();

			if (msg.getType() == 7) {
				String[] msgDetail = msg.getDetails().split(":");
				if (Long.parseLong(msgDetail[0]) != inputId)
					continue;

				timer.stopTimer();
				this._keypad.setKeypadEnable(false, 0);
				buf = msgDetail[1];
				break;
			} else if (msg.getType() == 999) {
				this._keypad.setKeypadEnable(false, 0);
				buf = msg.getDetails();
				break;
			}
		}

		return buf;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws KeypadException {
		int currStatus = this._keypad.getKPStatus();
		if (currStatus % 100 != 0)
			throwException(currStatus);
		return currStatus;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws KeypadException {
		// TODO Auto-generated method stub
		if (Code > 790)
			throw new KeypadException();
		else
			throw new KeypadException(Code);
	}

}
