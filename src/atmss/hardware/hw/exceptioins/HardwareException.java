/**
 * 
 */
package atmss.hardware.hw.exceptioins;

/**
 * @author freeman
 *
 */
public abstract class HardwareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870838410632900921L;
	private int ExceptionCode;	//Range from 100~799, each HW has 99 codes
	private String ExceptioinMsg;

	/**
	 * 
	 */
	public HardwareException() {
		System.out
				.println("You must specify the ExceptionCode and ExceptionMsg");
	}

	public HardwareException(int code, String msg) {
		
		this.ExceptionCode = code;
		this.ExceptioinMsg = msg;
	}

	public int getExceptionCode() {
		return ExceptionCode;
	}

	public String getExceptionMsg() {
		return ExceptioinMsg;
	}

}
