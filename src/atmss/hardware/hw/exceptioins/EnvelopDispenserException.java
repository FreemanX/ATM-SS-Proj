/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public class EnvelopDispenserException extends HardwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4738851345421275638L;

	/**
	 * 
	 */
	public EnvelopDispenserException() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * @param code (range:[200~299])
	 * @param msg
	 */
	public EnvelopDispenserException(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}

}
