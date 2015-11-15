/**
 * 
 */
package atmss.hardware.exceptioins;

/**
 * @author freeman
 *
 */
public class CardReaderException extends HardwareException {
	private final static int NORMAL_CODE = 200;
	private final static String[] CR_MSG = { "", "Can not recognize Card", "Card storage full",
			"Damaged card or not right card" };
	/**
	 * 
	 */
	private static final long serialVersionUID = 6246473880025089704L;

	/**
	 * 
	 */
	public CardReaderException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * @param code
	 * 
	 */
	public CardReaderException(int code) {
		super(code, CR_MSG[code - NORMAL_CODE]);
	}

}
