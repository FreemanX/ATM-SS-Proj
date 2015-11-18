/**
 *
 */
package atmss.hardware.view;

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

	public void displayUpper(String[] lines) {
		display.displayUpper(lines);
	}

	public void displayUpper(String line) {
		display.displayUpper(new String[]{line});
	}

	public void displayLower(String line) {
		display.displayLower(line);
	}

	public void appendUpper(String line) {
		List<String> list = display.getUpperContentList();
		list.add(line);
		display.displayUpper(list.toArray(new String[0]));
	}

	public void appendLower(String str) {
		display.displayLower(display.getLowerContent() + str);
	}

	public void clearUpper() {
		display.displayUpper(new String[0]);
	}

	public void clearLower() {
		display.displayLower("");
	}

	@Override
	public int checkStatus() throws HardwareException {
		// TODO Auto-generated method stub
		return 0;
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
	void throwException(int Code) throws HardwareException {
		// TODO Auto-generated method stub

	}

}
