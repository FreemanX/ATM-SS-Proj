/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvicePrinterException.
 *
 * @author freeman
 */
public class AdvicePrinterException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 100;
	
	/** The Constant AP_MSG. */
	private final static String[] AP_MSG = { "", "Out of resources", "Out of resources", "Paper Jamed" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2024333288115566500L;

	/**
	 * Instantiates a new advice printer exception.
	 */
	public AdvicePrinterException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new advice printer exception.
	 *
	 * @param code the code
	 */
	public AdvicePrinterException(int code) {
		super(code, AP_MSG[code - NORMAL_CODE]);
	}

}
