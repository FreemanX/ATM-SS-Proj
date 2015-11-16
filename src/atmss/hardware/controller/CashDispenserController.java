/**
 * 
 */
package atmss.hardware.controller;

import atmss.hardware.exceptioins.CashDispenserException;
import atmss.hardware.exceptioins.HardwareException;
import atmss.hardware.view.CashDispenserView;
import hwEmulators.CashDispenser;

/**
 * @author freeman
 *
 */
public class CashDispenserController extends HardwareController {
	private CashDispenserView cashDispenserView;

	/**
	 * 
	 */
	public CashDispenserController(CashDispenser CD) {
		// TODO Auto-generated constructor stub
		this.cashDispenserView = new CashDispenserView(CD);
	}

	public int[] checkCashInvetory() throws Exception {
		try {
			return this.cashDispenserView.checkCashInventory();
		} catch (CashDispenserException e) {
			HandleException(e);
			return null;
		}
	}

	public boolean ejectCash(int[] ejectPlan) throws Exception {
		try {
			return this.cashDispenserView.ejectCash(ejectPlan);
		} catch (CashDispenserException e) {
			HandleException(e);
			return false;
		}
	}

	public boolean retainCash() throws Exception {
		try {
			this.cashDispenserView.retainCash();
			return true;
		} catch (CashDispenserException e) {
			// TODO Auto-generated catch block
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
		if (ex.getClass().getName().equals("CashDispenser")) {
			int exType = ex.getExceptionCode();
			// TODO handle ex and report to MainController;
			switch (exType) {
			case 301:
				// No need for our system to handle
				System.err.println(">>>>>>>>>>>Insuffient number of 500 notes");
				break;
			case 302:
				System.err.println(">>>>>>>>>>>No cash");
				break;
			case 399:
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
