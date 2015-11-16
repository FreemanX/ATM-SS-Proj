/**
 * 
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.CashDispenserException;
import hwEmulators.CashDispenser;

/**
 * @author freeman
 *
 */
public class CashDispenserView extends HardwareView {
	private CashDispenser _cashDispenser;

	/**
	 * 
	 */
	public CashDispenserView(CashDispenser CD) {
		// TODO Auto-generated constructor stub
		this._cashDispenser = CD;
	}

	public int[] checkCashInventory() throws CashDispenserException {
		checkStatus();
		int[] cashInventory = { this._cashDispenser.getNumOf100(), this._cashDispenser.getNumOf500(),
				this._cashDispenser.getNumOf1000() };
		return cashInventory;
	}

	public boolean ejectCash(int[] ejectPlan) throws CashDispenserException {
		checkStatus();
		return this._cashDispenser.ejectCash(ejectPlan[0], ejectPlan[1], ejectPlan[2]);
	}

	public void retainCash() throws CashDispenserException {
		checkStatus();
		this._cashDispenser.retainCash();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.view.HardwareView#checkStatus()
	 */
	@Override
	public int checkStatus() throws CashDispenserException {
		// TODO Auto-generated method stub
		int currStatus = this._cashDispenser.getCDStatus();
		if (currStatus % 100 != 0)
			throwException(currStatus);
		return currStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.view.HardwareView#reset()
	 */
	@Override
	public boolean reset() throws CashDispenserException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.view.HardwareView#shutdown()
	 */
	@Override
	public boolean shutdown() throws CashDispenserException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.view.HardwareView#throwException(int)
	 */
	@Override
	void throwException(int Code) throws CashDispenserException {
		// TODO Auto-generated method stub
		if (Code > 390)
			throw new CashDispenserException();
		else
			throw new CashDispenserException(Code);
	}

}
