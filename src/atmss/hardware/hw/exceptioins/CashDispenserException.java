/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class CashDispenserException extends HardwareException {
	private final static int NORMAL_CODE = 300;
	private final static String[] CD_MSG = { "", "Insuffient number of 500 notes", "No cash" };
	/**
	 * 
	 */
	private static final long serialVersionUID = -4613252398503065307L;

	/**
	 * 
	 */
	public CashDispenserException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 * 
	 */
	public CashDispenserException(int code) {
		super(code, CD_MSG[code - NORMAL_CODE]);
	}

}
