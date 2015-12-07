/**
 *
 */
package atmss.hardware.view;

import atmss.hardware.exceptioins.DisplayException;
import atmss.hardware.exceptioins.HardwareException;
import hwEmulators.Display;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayView.
 *
 * @author freeman
 */
public class DisplayView extends HardwareView {

	/** The display. */
	private Display display;

	/**
	 * Instantiates a new display view.
	 *
	 * @param display
	 *            the Display emulator
	 */
	public DisplayView(Display display) {
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#checkStatus()
	 */

	/**
	 * Display upper.
	 *
	 * @param lines
	 *            the lines
	 * @throws DisplayException
	 *             the display exception
	 */
	public void displayUpper(String[] lines) throws DisplayException {
		checkStatus();
		display.displayUpper(lines);
	}

	/**
	 * Display upper.
	 *
	 * @param line
	 *            the line
	 * @throws DisplayException
	 *             the display exception
	 */
	public void displayUpper(String line) throws DisplayException {
		checkStatus();
		display.displayUpper(new String[] { line });
	}

	/**
	 * Display lower.
	 *
	 * @param line
	 *            the line
	 * @throws DisplayException
	 *             the display exception
	 */
	public void displayLower(String line) throws DisplayException {
		checkStatus();
		display.displayLower(line);
	}

	/**
	 * Append upper.
	 *
	 * @param line
	 *            the line
	 * @throws DisplayException
	 *             the display exception
	 */
	public void appendUpper(String line) throws DisplayException {
		checkStatus();
		List<String> list = display.getUpperContentList();
		list.add(line);
		display.displayUpper(list.toArray(new String[list.size()]));
	}

	/**
	 * Append lower.
	 *
	 * @param str
	 *            the str
	 * @throws DisplayException
	 *             the display exception
	 */
	public void appendLower(String str) throws DisplayException {
		display.displayLower(display.getLowerContent() + str);
	}

	/**
	 * Clear upper.
	 *
	 * @throws DisplayException
	 *             the display exception
	 */
	public void clearUpper() throws DisplayException {
		display.displayUpper(new String[0]);
	}

	/**
	 * Clear lower.
	 *
	 * @throws DisplayException
	 *             the display exception
	 */
	public void clearLower() throws DisplayException {
		checkStatus();
		display.displayLower("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atmss.hardware.view.HardwareView#checkStatus()
	 */
	@Override
	public int checkStatus() throws DisplayException {
		if (display.getDisStatus() % 100 != 0)
			throwException(display.getDisStatus());
		return display.getDisStatus();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#reset()
	 */
	@Override
	public boolean reset() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#shutdown()
	 */
	@Override
	public boolean shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see atmss.hardware.hw.Hardware#throwException(int, java.lang.String)
	 */
	@Override
	void throwException(int Code) throws DisplayException {
		if (Code > 590)
			throw new DisplayException();
		else
			throw new DisplayException(Code);
	}

}
