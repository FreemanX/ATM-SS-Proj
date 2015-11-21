package hwEmulators;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class ATMSSLogFormatter.
 */
// ATMSSLogFormatter
public class ATMSSLogFormatter extends Formatter {
	// ------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	// format
	public String format(LogRecord rec) {
		Calendar cal = Calendar.getInstance();
		String str = "";

		// get date
		cal.setTimeInMillis(rec.getMillis());
		str += String.format("%02d%02d%02d-%02d:%02d:%02d ",
				cal.get(Calendar.YEAR) - 2000, cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

		// level of the log
		str += "[" + rec.getLevel() + "] -- ";

		// message of the log
		str += rec.getMessage();
		return str + "\n";
	} // format
} // ATMSSLogFormatter
