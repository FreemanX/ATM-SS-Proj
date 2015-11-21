/**
 * 
 */
package atmss.hardware.exceptioins;

// TODO: Auto-generated Javadoc
/**
 * The Class HardwareException.
 *
 * @author freeman
 */
public abstract class HardwareException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1870838410632900921L;
	
	/** The Exception code. */
	private int ExceptionCode; // Range from 100~799, each HW has 99 codes
	
	/** The Exceptioin msg. */
	private String ExceptioinMsg;

	/**
	 * Instantiates a new hardware exception.
	 */
	public HardwareException() {
		System.out.println("You must specify the ExceptionCode and ExceptionMsg");
	}

	/**
	 * Instantiates a new hardware exception.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	public HardwareException(int code, String msg) {

		this.ExceptionCode = code;
		this.ExceptioinMsg = msg;
	}

	/**
	 * Gets the exception code.
	 *
	 * @return the exception code
	 */
	public int getExceptionCode() {
		return ExceptionCode;
	}

	/**
	 * Gets the exception msg.
	 *
	 * @return the exception msg
	 */
	public String getExceptionMsg() {
		return ExceptioinMsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return "================= " + ExceptionCode + ": " + ExceptioinMsg;
	}
}
