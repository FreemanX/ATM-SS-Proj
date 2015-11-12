/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class DepositCollectorException extends HardwareException {
	private final static int NORMAL_CODE = 400;
	private final static String[] DC_MSG = { "", "Not Envelop" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 7591064697083917002L;

	/**
	 * 
	 */
	public DepositCollectorException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 * 
	 */
	public DepositCollectorException(int code) {
		super(code, DC_MSG[code - NORMAL_CODE]);
	}

}
