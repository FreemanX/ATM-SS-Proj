/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.CardReaderException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.CardReaderView;
import hwEmulators.CardReader;

/**
 * @author freeman
 *
 */
public class CardReaderController extends HardwareController {
	private String cardNumber = "";
	private CardReaderView cardReaderView;

	/**
	 * 
	 */
	public CardReaderController(CardReader CR) {
		this.cardReaderView = new CardReaderView(CR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */

	public String readCard() throws Exception {
		try {
			return this.cardReaderView.readCard();
		} catch (CardReaderException e) {
			HandleException(e);
			return "";
		}

	}

	public void initCR() {
		this.cardNumber = "";
	}

	public String getCardNumber() {
		// TODO not finished
		return cardNumber;
	}

	public boolean ejectCard() throws Exception {
		try {
			this.cardReaderView.ejectCard();
			return true;
		} catch (CardReaderException e) {
			HandleException(e);
			return false;
		}
	}

	public boolean retainCard() throws Exception {
		try {
			this.cardReaderView.retainCard();
			return true;
		} catch (CardReaderException e) {
			HandleException(e);
			return false;
		}
	}

	@Override
	public boolean updateStatus() throws Exception {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		try {
			this.status = this.cardReaderView.checkStatus();
			isSuccess = true;
		} catch (CardReaderException e) {
			// TODO Auto-generated catch block
			isSuccess = false;
			HandleException(e);
		}
		return isSuccess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#reset()
	 */
	@Override
	public boolean reset() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#shutdonw()
	 */
	@Override
	public boolean shutdown() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.HardwareController#HandleException(atmss.hardware.hw.
	 * exceptioins.HardwareException)
	 */
	@Override
	void HandleException(HardwareException ex) throws Exception {
		// TODO Auto-generated method stub
		if (ex instanceof HardwareException) {
			int exType = ex.getExceptionCode();
			// TODO handle ex and report to MainController;
			switch (exType) {
			case 201:
				// No need for our system to handle
				System.err.println(">>>>>>>>>>>Can not recorgnize card");
				break;
			case 202:
				System.err.println(">>>>>>>>>>>Card storage full");
				break;
			case 299:
				System.err.println(">>>>>>>>>>>Hardware failure");
				break;
			default:
				throw ex;
			}

		} else {
			throw ex;
		}
	}

}
