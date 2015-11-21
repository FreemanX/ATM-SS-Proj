/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class EnvelopDispenserException.
 *
 * @author freeman
 */
public class EnvelopDispenserException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 600;
	
	/** The Constant ED_MSG. */
	private final static String[] ED_MSG = { "", "No envelop" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4738851345421275638L;

	/**
	 * Instantiates a new envelop dispenser exception.
	 */
	public EnvelopDispenserException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new envelop dispenser exception.
	 *
	 * @param code the code
	 */
	public EnvelopDispenserException(int code) {
		super(code, ED_MSG[code - NORMAL_CODE]);
	}

}
