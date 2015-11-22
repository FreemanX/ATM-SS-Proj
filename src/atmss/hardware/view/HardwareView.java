/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.HardwareException;

// TODO: Auto-generated Javadoc
/**
 * The Class HardwareView.
 *
 * @author freeman
 */
public abstract class HardwareView{

	/**
	 * Instantiates a new hardware view.
	 */
	public HardwareView() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Check status.
	 *
	 * @return the status code
	 * @throws HardwareException the hardware exception
	 */
	public abstract int checkStatus() throws HardwareException;

	/**
	 * Reset.
	 *
	 * @return true, if successful
	 * @throws HardwareException the hardware exception
	 */
	public abstract boolean reset() throws HardwareException;

	/**
	 * Shutdown.
	 *
	 * @return true, if successful
	 * @throws HardwareException the hardware exception
	 */
	public abstract boolean shutdown() throws HardwareException;

	// To be discussed how to create and throw Exceptions
	/**
	 * Throw exception.
	 *
	 * @param Code the exception code
	 * @throws HardwareException the hardware exception
	 */
	// This method here is not necessary, just my current thoughts -- Freeman
	abstract void throwException(int Code) throws HardwareException;
}
