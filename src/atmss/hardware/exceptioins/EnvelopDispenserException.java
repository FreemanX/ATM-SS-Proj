/**
 * 
 */
package atmss.hardware.exceptioins;

/**
 * @author freeman
 *
 */
public class EnvelopDispenserException extends HardwareException {
	private final static int NORMAL_CODE = 600;
	private final static String[] ED_MSG = { "", "No envelop" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 4738851345421275638L;

	/**
	 * 
	 */
	public EnvelopDispenserException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 * 
	 */
	public EnvelopDispenserException(int code) {
		super(code, ED_MSG[code - NORMAL_CODE]);
	}

}
