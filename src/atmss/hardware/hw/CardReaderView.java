/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.CardReaderException;

/**
 * @author freeman
 *
 */
public class CardReaderView extends HardwareView {
	
	/**
	 * 
	 */
	public CardReaderView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws CardReaderException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws CardReaderException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws CardReaderException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws CardReaderException {
		// TODO Auto-generated method stub

	}

}
