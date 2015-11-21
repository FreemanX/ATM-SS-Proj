/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class CashDispenserException.
 *
 * @author freeman
 */
public class CashDispenserException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 300;
	
	/** The Constant CD_MSG. */
	private final static String[] CD_MSG = { "", "Insuffient number of 500 notes", "No cash" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4613252398503065307L;

	/**
	 * Instantiates a new cash dispenser exception.
	 */
	public CashDispenserException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new cash dispenser exception.
	 *
	 * @param code the code
	 */
	public CashDispenserException(int code) {
		super(code, CD_MSG[code - NORMAL_CODE]);
	}

}
