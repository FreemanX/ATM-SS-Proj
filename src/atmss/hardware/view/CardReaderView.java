/**
 * 
 */
package atmss.hardware.view;

import atmss.Timer;
import atmss.hardware.exceptioins.CardReaderException;
import hwEmulators.CardReader;
import hwEmulators.MBox;
import hwEmulators.Msg;

/**
 * @author freeman
 *
 */
public class CardReaderView extends HardwareView {
	private CardReader _cardReader;
	private MBox cardReaderViewMBox = new MBox("cardReaderView");

	/**
	 * 
	 */
	public CardReaderView(CardReader CR) {
		// TODO Auto-generated constructor stub
		this._cardReader = CR;
		this._cardReader.setCRViewBox(this.cardReaderViewMBox);
	}

	public String readCard() throws CardReaderException {
		checkStatus();
		Msg msg = this.cardReaderViewMBox.receive();
		if (msg.getSender().equals("CardReader") && msg.getType() == 2)
			return msg.getDetails();
		else
			return null;
	}

	public String getCardNumer() throws CardReaderException {
		checkStatus();
		return this._cardReader.getCard();
	}

	public boolean ejectCard() throws CardReaderException {
		checkStatus();
		Timer timer = Timer.getTimer();
		timer.initTimer(3, cardReaderViewMBox);
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

	void retainCard() throws CardReaderException {
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
