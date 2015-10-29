/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class DisplayException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6810278142396478714L;

	/**
	 * 
	 */
	public DisplayException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[500~599])
	 * @param msg
	 */
	public DisplayException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
