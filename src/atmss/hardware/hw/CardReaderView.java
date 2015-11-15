/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.CardReaderException;
import hwEmulators.CardReader;

/**
 * @author freeman
 *
 */
public class CardReaderView extends HardwareView {
	private CardReader _cardReader;

	/**
	 * 
	 */
	public CardReaderView(CardReader CR) {
		// TODO Auto-generated constructor stub
		this._cardReader = CR;
	}

	
	
	public String getCardNumer() {
		return this._cardReader.getCard();
	}

	public void ejectCard() {
		this._cardReader.ejectCard();
	}

	public void retainCard() {
		this._cardReader.eatCard();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws CardReaderException {
		// TODO Auto-generated method stub
		return this._cardReader.getCRStatus();
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
