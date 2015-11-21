/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class KeypadException.
 *
 * @author freeman
 */
public class KeypadException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 700;
	
	/** The Constant KP_MSG. */
	private final static String[] KP_MSG = { "" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3941607610590495555L;

	/**
	 * Instantiates a new keypad exception.
	 */
	public KeypadException() {
		// TODO Auto-generated constructor stub
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new keypad exception.
	 *
	 * @param code the code
	 */
	public KeypadException(int code) {
		super(code, KP_MSG[code - NORMAL_CODE]);
	}

}
