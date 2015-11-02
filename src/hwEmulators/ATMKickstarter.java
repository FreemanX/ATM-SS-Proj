package hwEmulators;

import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

//======================================================================
// ATMKickstarter
public class ATMKickstarter {
	private static Logger log = null;

	// ------------------------------------------------------------
	// main
	public static void main(String args[]) {
		// create and configure logger
		ConsoleHandler conHd = new ConsoleHandler();
		conHd.setFormatter(new ATMSSLogFormatter());
		log = Logger.getLogger(ATMKickstarter.class.getName());
		log.setUseParentHandlers(false);
		log.addHandler(conHd);
		log.setLevel(Level.INFO);

		// create components
		CardReader cardReader = new CardReader("cr");
		Keypad keypad = new Keypad("kp");
		ATMSS atmss = new ATMSS("atmss");

		// connect components
		atmss.setCardReader(cardReader);
		atmss.setKeypad(keypad);
		cardReader.setATMSS(atmss);
		keypad.setATMSS(atmss);

		// start the components
		keypad.start();
		cardReader.start();
		atmss.start();
	} // main

	// ------------------------------------------------------------
	// getLogger
	public static Logger getLogger() {
		return log;
	} // getLogger
} // ATMKickstarter
