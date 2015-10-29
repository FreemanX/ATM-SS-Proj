/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class KeypadException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3941607610590495555L;

	/**
	 * 
	 */
	public KeypadException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[700~799])
	 * @param msg
	 */
	public KeypadException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
