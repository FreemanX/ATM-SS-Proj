/**
 * 
 */
package atmss.hardware;

import atmss.hardware.hw.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public abstract class HardwareController {

	int status;

	/**
	 * 
	 */
	public HardwareController() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract boolean updateStatus() throws Exception;
	
	public int getStatus() throws Exception{
		updateStatus();
		return status;
	}
	
	public abstract boolean reset() throws Exception;
	
	public abstract boolean shutdonw() throws Exception;
	
	abstract void HandleException(HardwareException ex) throws Exception;

}
