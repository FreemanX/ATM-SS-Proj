/**
 *
 */
package atmss.hardware.view;

import atmss.Timer;
import atmss.hardware.exceptioins.CardReaderException;
import hwEmulators.CardReader;
import hwEmulators.MBox;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class CardReaderView.
 *
 * @author freeman
 */
public class CardReaderView extends HardwareView {

	/** The _card reader. */
	private CardReader _cardReader;

	/** The card reader view m box. */
	private MBox cardReaderViewMBox = new MBox("cardReaderView");

	/**
	 * Instantiates a new card reader view.
	 *
	 * @param CR the CardReader emulator
	 */
	public CardReaderView(CardReader CR) {
		// TODO Auto-generated constructor stub
		this._cardReader = CR;
		this._cardReader.setCRViewBox(this.cardReaderViewMBox);
	}

	/**
	 * Read card.
	 *
	 * @return the string
	 * @throws CardReaderException the card reader exception
	 */
	public String readCard() throws CardReaderException {
		checkStatus();
		Msg msg = this.cardReaderViewMBox.receive();
		if (msg.getSender().equals("CardReader") && msg.getType() == 2)
			return msg.getDetails();
		else
			return null;
	}

	/**
	 * Gets the card numer.
	 *
	 * @return the card numer
	 * @throws CardReaderException the card reader exception
	 */
	public String getCardNumber() throws CardReaderException {
		checkStatus();
		return this._cardReader.getCard();
	}

	/**
	 * Eject card.
	 *
	 * @return true, if successful
	 * @throws CardReaderException the card reader exception
	 */
	public boolean ejectCard() throws CardReaderException {
		checkStatus();
		Timer timer = Timer.getTimer();
		timer.initTimer(30, cardReaderViewMBox);
		this._cardReader.ejectCard();
		timer.start();
		this.cardReaderViewMBox.clearBox();
		Msg msg = this.cardReaderViewMBox.receive();
		if (msg.getType() == 999) {
			retainCard();
			return false;
		} else {
			timer.stopTimer();
			return true;
		}
	}

	/**
	 * Retain card.
	 *
	 * @throws CardReaderException the card reader exception
	 */
	public void retainCard() throws CardReaderException {
		checkStatus();
		this._cardReader.eatCard();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws CardReaderException {
		int currStatus = this._cardReader.getCRStatus();
		if (currStatus % 100 != 0)
			throwException(currStatus);
		return currStatus;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws CardReaderException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws CardReaderException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws CardReaderException {
		if (Code > 290)
			throw new CardReaderException();
		else
			throw new CardReaderException(Code);
	}

}
