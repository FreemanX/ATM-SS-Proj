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
		CashDispenser cashDispenser = new CashDispenser("cd");
		CardReader cardReader = new CardReader("cr");
		Keypad keypad = new Keypad("kp");
		AdvicePrinter advicePrinter = new AdvicePrinter("ap");
		DepositCollector depositCollector = new DepositCollector("dc");
		ATMSS atmss = new ATMSS("atmss");

		EnvelopDispenser envelopDispenser = new EnvelopDispenser("ed");

		// connect components
		atmss.setCashDispenser(cashDispenser);
		atmss.setCardReader(cardReader);
		atmss.setKeypad(keypad);
		atmss.setDepositCollector(depositCollector);
		cardReader.setATMSS(atmss);
		keypad.setATMSS(atmss);
		advicePrinter.setATMSS(atmss);
		cashDispenser.setATMSS(atmss);
		depositCollector.setATMSS(atmss);
		envelopDispenser.setATMSS(atmss);
		
		// start the components
		cashDispenser.start();
		keypad.start();
		cardReader.start();
		atmss.start();
		advicePrinter.start();
		depositCollector.start();
		envelopDispenser.start();
	} // main

	// ------------------------------------------------------------
	// getLogger
	public static Logger getLogger() {
		return log;
	} // getLogger
} // ATMKickstarter
