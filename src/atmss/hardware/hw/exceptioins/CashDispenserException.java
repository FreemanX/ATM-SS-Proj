/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class CashDispenserException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4613252398503065307L;

	/**
	 * 
	 */
	public CashDispenserException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[600~699])
	 * @param msg
	 */
	public CashDispenserException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
