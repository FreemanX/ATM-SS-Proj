/**
 * 
 */
package atmss.hardware.hw;

import atmss.hardware.hw.exceptioins.AdvicePrinterException;

/**
 * @author freeman
 *
 */
public class AdvicePrinter extends Hardware {

	/**
	 * 
	 */
	public AdvicePrinter() {
		// TODO Auto-generated constructor stub
	}

	public boolean print(String[] advice) throws AdvicePrinterException{
		boolean isSuccess = false;
		/*
		 * TODO call the hardware to do
		 */
		// try{
		// Call the printer to print out things
		// isSuccess = true;
		// }catch(Some Exceptons ex from hardware)
		// {
		// isSuccess = false;
		// swich(ex){
		// case: ex1
		// {
		// this.throwException(Code1, Msg1);
		// break;
		// }
		// case: ex2
		// {
		// this.throwException(Code2, Msg2);
		// break;
		// }
		// default:
		// throw ex;
		// break;
		// }
		// }
		return isSuccess;
	}

	public boolean checkInventory() throws AdvicePrinterException {
		boolean isSuccess = false;
		/*
		 * TODO call the hardware to do
		 */
		return isSuccess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */
	@Override
	public int checkStatus() throws AdvicePrinterException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws AdvicePrinterException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws AdvicePrinterException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code, String Msg) throws AdvicePrinterException {
		// TODO Auto-generated method stub
		throw new AdvicePrinterException(Code, Msg);
	}

}
