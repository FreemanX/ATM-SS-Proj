/**
 *
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.CashDispenserException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.CashDispenserView;
import hwEmulators.CashDispenser;
import hwEmulators.Msg;

// TODO: Auto-generated Javadoc
/**
 * The Class CashDispenserController.
 *
 * @author freeman
 */
public class CashDispenserController extends HardwareController {

	/** The cash dispenser view. */
	private CashDispenserView cashDispenserView;

	/**
	 * Instantiates a new cash dispenser controller.
	 *
	 * @param CD the CardReaderView
	 */
	public CashDispenserController(CashDispenser CD) {
		// TODO Auto-generated constructor stub
		this.cashDispenserView = new CashDispenserView(CD);
	}

	/**
	 * Check cash invetory.
	 *
	 * @return the int[]
	 * @throws Exception the exception
	 */
	public int[] checkCashInvetory() throws Exception {
		try {
			return this.cashDispenserView.checkCashInventory();
		} catch (CashDispenserException e) {
			HandleException(e);
			return null;
		}
	}

	/**
	 * Eject cash.
	 *
	 * @param ejectPlan the eject plan
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean ejectCash(int[] ejectPlan) throws Exception {
		try {
			return this.cashDispenserView.ejectCash(ejectPlan);
		} catch (CashDispenserException e) {
			HandleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.HardwareController#updateStatus()
	 */
	@Override
	public boolean updateStatus() throws Exception {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		try {
			this.status = this.cashDispenserView.checkStatus();
			this._maincontrollerMBox.send(new Msg("CD", status, "I'm OK"));
			isSuccess = true;
		} catch (CashDispenserException e) {
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
			reportToMainController(ex, "CD");
		} else {
			throw ex;
		}
	}

}
