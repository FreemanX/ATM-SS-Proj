/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class DepositCollectorException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7591064697083917002L;

	/**
	 * 
	 */
	public DepositCollectorException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[400~499])
	 * @param msg
	 */
	public DepositCollectorException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
