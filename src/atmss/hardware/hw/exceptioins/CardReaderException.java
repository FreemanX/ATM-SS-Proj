/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class CardReaderException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6246473880025089704L;

	/**
	 * 
	 */
	public CardReaderException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[300~399])
	 * @param msg
	 */
	public CardReaderException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
