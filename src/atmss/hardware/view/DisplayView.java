/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.DisplayException;
import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.Display;

import java.util.List;

/**
 * @author freeman
 *
 */
public class DisplayView extends HardwareView {

	private Display display;

	/**
	 *
	 */
	public DisplayView(Display display) {
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */

	public void displayUpper(String[] lines) throws DisplayException {
		checkStatus();
		display.displayUpper(lines);
	}

	public void displayUpper(String line) throws DisplayException {
		checkStatus();
		display.displayUpper(new String[]{line});
	}

	public void displayLower(String line) throws DisplayException {
		checkStatus();
		display.displayLower(line);
	}

	public void appendUpper(String line) throws DisplayException {
		checkStatus();
		List<String> list = display.getUpperContentList();
		list.add(line);
		display.displayUpper(list.toArray(new String[0]));
	}

	public void appendLower(String str) throws DisplayException {
		display.displayLower(display.getLowerContent() + str);
	}

	public void clearUpper() throws DisplayException {
		display.displayUpper(new String[0]);
	}

	public void clearLower() throws DisplayException {
		checkStatus();
		display.displayLower("");
	}

	@Override
	public int checkStatus() throws DisplayException {
		if (display.getStatus() % 100 != 0)
			throwException(display.getStatus());
		return display.getStatus();
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws DisplayException {
		throw new DisplayException(Code);
	}

}
