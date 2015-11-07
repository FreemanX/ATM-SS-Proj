/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public abstract class HardwareView {

	/**
	 * 
	 */
	public HardwareView() {
		// TODO Auto-generated constructor stub
	}

	public abstract int checkStatus()throws HardwareException;
	
	public abstract boolean reset()throws HardwareException;
	
	public abstract boolean shutdown()throws HardwareException;
	
	// To be discussed how to create and throw Exceptions
	// This method here is not necessary, just my current thoughts -- Freeman
	abstract void throwException(int Code,String Msg ) throws HardwareException;
}
