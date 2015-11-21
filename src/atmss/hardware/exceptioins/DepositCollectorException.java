/**
 *
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class DepositCollectorException.
 *
 * @author freeman
 */
public class DepositCollectorException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 400;
	
	/** The Constant DC_MSG. */
	private final static String[] DC_MSG = { "", "Not Envelop", "Slot jam"};
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7591064697083917002L;

	/**
	 * Instantiates a new deposit collector exception.
	 */
	public DepositCollectorException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new deposit collector exception.
	 *
	 * @param code the code
	 */
	public DepositCollectorException(int code) {
		super(code, DC_MSG[code - NORMAL_CODE]);
	}

}
