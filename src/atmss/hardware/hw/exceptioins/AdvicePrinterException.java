/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class AdvicePrinterException extends HardwareException {
	private final static int AP_NORMAL_CODE = 100;
	private final static String[] AP_MSG = { "", "Out of resources", "Out of resources", "Paper Jamed" };
	/**
	 * 
	 */
	private static final long serialVersionUID = -2024333288115566500L;

	/**
	 * 
	 */
	public AdvicePrinterException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code
	 *            (range:[100~199])
	 * @param msg
	 */
	public AdvicePrinterException(int code) {
		super(code, AP_MSG[code - AP_NORMAL_CODE]);
		// TODO Auto-generated constructor stub
	}

}
