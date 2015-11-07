/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.HardwareException;
import hwEmulators.Keypad;

/**
 * @author freeman
 *
 */
public class KeypadView extends HardwareView {

	private Keypad keypad;
	/**
	 * 
	 */
	public KeypadView(Keypad keypad) {
		this.keypad = keypad;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws HardwareException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		keypad.setKeypadEnable(false);
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code, String Msg) throws HardwareException {
		// TODO Auto-generated method stub

	}

}
