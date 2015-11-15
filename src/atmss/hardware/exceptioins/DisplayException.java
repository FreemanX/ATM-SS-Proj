/**
 * 
 */
package atmss.hardware.exceptioins;

/**
 * @author freeman
 *
 */
public class DisplayException extends HardwareException {
	private final static int NORMAL_CODE = 500;
	private final static String[] DIS_MSG = { "" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 6810278142396478714L;

	/**
	 * 
	 */
	public DisplayException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 */
	public DisplayException(int code) {
		super(code, DIS_MSG[code - NORMAL_CODE]);
	}

}
