/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class CardReaderException.
 *
 * @author freeman
 */
public class CardReaderException extends HardwareException {
	
	/** The Constant NORMAL_CODE. */
	private final static int NORMAL_CODE = 200;
	
	/** The Constant CR_MSG. */
	private final static String[] CR_MSG = { "", "Can not recognize Card", "Card storage full",
			"Damaged card or not right card" };
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6246473880025089704L;

	/**
	 * Instantiates a new card reader exception.
	 */
	public CardReaderException() {
		super(NORMAL_CODE + 99, "Fatal Error");
	}

	/**
	 * Instantiates a new card reader exception.
	 *
	 * @param code the code
	 */
	public CardReaderException(int code) {
		super(code, CR_MSG[code - NORMAL_CODE]);
	}

}
