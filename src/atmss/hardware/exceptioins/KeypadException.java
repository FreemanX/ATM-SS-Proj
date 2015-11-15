/**
 * 
 */
package atmss.hardware.exceptioins;

/**
 * @author freeman
 *
 */
public class KeypadException extends HardwareException {
	private final static int NORMAL_CODE = 700;
	private final static String[] KP_MSG = { "" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 3941607610590495555L;

	/**
	 * 
	 */
	public KeypadException() {
		// TODO Auto-generated constructor stub
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 */
	public KeypadException(int code) {
		super(code, KP_MSG[code - NORMAL_CODE]);
	}

}
