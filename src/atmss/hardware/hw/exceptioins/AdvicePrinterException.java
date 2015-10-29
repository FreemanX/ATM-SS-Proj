/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class AdvicePrinterException extends HardwareException {

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
	 * @param code (range:[100~199])
	 * @param msg
	 */
	public AdvicePrinterException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
