/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.CardReaderException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.CardReaderView;
import hwEmulators.CardReader;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class CardReaderController.
 *
 * @author freeman
 */
public class CardReaderController extends HardwareController {

	/** The card number. */
	private String cardNumber = "";

	/** The card reader view. */
	private CardReaderView cardReaderView;

	/**
	 * Instantiates a new card reader controller.
	 *
	 * @param CR the cr
	 */
	public CardReaderController(CardReader CR) {
		this.cardReaderView = new CardReaderView(CR);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */

	/**
	 * Read card.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	public String readCard() throws Exception {
		try {
			this.cardNumber = this.cardReaderView.readCard();
			return cardNumber;
		} catch (CardReaderException e) {
			HandleException(e);
			return "";
		}

	}

	/**
	 * Inits the cr.
	 */
	public void initCR() {
		this.cardNumber = "";
	}

	/**
	 * Gets the card number.
	 *
	 * @return the card number
	 * @throws Exception the exception
	 */
	public String getCardNumber() throws Exception {
		// TODO not finished
		try {
			return this.cardReaderView.getCardNumber();
		} catch (CardReaderException e) {
			HandleException(e);
			return null;
		}
	}

	/**
	 * Eject card.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean ejectCard() throws Exception {
		try {
			return this.cardReaderView.ejectCard();
		} catch (CardReaderException e) {
			HandleException(e);
			return false;
		}
	}

	/**
	 * Retain card.
	 *
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean retainCard() throws Exception {
		try {
			this.cardReaderView.retainCard();
			return true;
		} catch (CardReaderException e) {
			HandleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.controller.HardwareController#updateStatus()
	 */
	@Override
	public boolean updateStatus() throws Exception {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		try {
			this.status = this.cardReaderView.checkStatus();
			this._maincontrollerMBox.send(new Msg("CR", status, "I'm OK"));
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
			reportToMainController(ex, "CR");
		} else {
			throw ex;
		}
	}

}
