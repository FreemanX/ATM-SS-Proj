/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayException.
 *
 * @author freeman
 */
public class DisplayException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 500;
	
	/** The Constant DIS_MSG. */
	private final static String[] DIS_MSG = { "" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6810278142396478714L;

	/**
	 * Instantiates a new display exception.
	 */
	public DisplayException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new display exception.
	 *
	 * @param code the code
	 */
	public DisplayException(int code) {
		super(code, DIS_MSG[code - NORMAL_CODE]);
	}

}
