/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.HardwareException;

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
	
	public abstract boolean shutdown() throws Exception;
	
	abstract void HandleException(HardwareException ex) throws Exception;

}
