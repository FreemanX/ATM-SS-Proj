/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class AdvicePrinterException extends HardwareException {
	private final static int NORMAL_CODE = 100;
	private final static String[] AP_MSG = { "", "Out of resources", "Out of resources", "Paper Jamed" };
	/**
	 * 
	 */
	private static final long serialVersionUID = -2024333288115566500L;

	/**
	 * 
	 */
	public AdvicePrinterException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 * 
	 */
	public AdvicePrinterException(int code) {
		super(code, AP_MSG[code - NORMAL_CODE]);
	}

}
