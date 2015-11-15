/**
 * 
 */
package atmss.hardware;

import atmss.hardware.hw.exceptioins.HardwareException;

/**
 * @author freeman
 *
 */
public class CardReaderController extends HardwareController {
	private String cardNumber = "";
	/**
	 * 
	 */
	public CardReaderController() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */
	
	public String getCard()
	{
		//TODO not finished
		return cardNumber;
	}
	
	@Override
	public boolean updateStatus() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#reset()
	 */
	@Override
	public boolean reset() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#shutdonw()
	 */
	@Override
	public boolean shutdown() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.HardwareController#HandleException(atmss.hardware.hw.exceptioins.HardwareException)
	 */
	@Override
	void HandleException(HardwareException ex) throws Exception {
		// TODO Auto-generated method stub

	}

}
