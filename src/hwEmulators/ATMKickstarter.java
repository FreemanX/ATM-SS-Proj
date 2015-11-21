package hwEmulators;

import atmss.MainController;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class ATMKickstarter.
 */
// ATMKickstarter
public class ATMKickstarter {
	
	/** The log. */
	private static Logger log = null;

	/** The advice printer. */
	// HW components
	AdvicePrinter advicePrinter;
	
	/** The card reader. */
	CardReader cardReader;
	
	/** The cash dispenser. */
	CashDispenser cashDispenser;
	
	/** The deposit collector. */
	DepositCollector depositCollector;
	
	/** The display. */
	Display display;
	
	/** The envelop dispenser. */
	EnvelopDispenser envelopDispenser;
	
	/** The keypad. */
	Keypad keypad;
	
	/** The atmss. */
	ATMSS atmss;
	
	/** The ex emu. */
	NewExceptionEmulator exEmu;
	// view components

	// ------------------------------------------------------------
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	// main
	public static void main(String args[]) {
		new ATMKickstarter();
	} // main

	/**
	 * Instantiates a new ATM kickstarter.
	 */
	public ATMKickstarter() {
		// create and configure logger
		ConsoleHandler conHd = new ConsoleHandler();
		conHd.setFormatter(new ATMSSLogFormatter());
		log = Logger.getLogger(ATMKickstarter.class.getName());
		log.setUseParentHandlers(false);
		log.addHandler(conHd);
		try {
			log.addHandler(new FileHandler("atmss_log"));
		} catch (IOException e) {
			System.err.println("Failed to set log file... @ " + this.getClass().getSimpleName());
		}
		log.setLevel(Level.INFO);

		// create components
		advicePrinter = new AdvicePrinter("ap");
		cardReader = new CardReader("cr");
		cashDispenser = new CashDispenser("cd");
		depositCollector = new DepositCollector("dc");
		display = new Display("ds");
		envelopDispenser = new EnvelopDispenser("ed");
		keypad = new Keypad("kp");
		atmss = new ATMSS("atmss");
		// connect components
		atmss.setAdvicePrinter(advicePrinter);
		atmss.setCardReader(cardReader);
		atmss.setCashDispenser(cashDispenser);
		atmss.setDepositCollector(depositCollector);
		atmss.setDisplay(display);
		atmss.setKeypad(keypad);
		atmss.setEnvelopDispenser(envelopDispenser);
		advicePrinter.setATMSS(atmss);
		cardReader.setATMSS(atmss);
		cashDispenser.setATMSS(atmss);
		depositCollector.setATMSS(atmss);
		display.setATMSS(atmss);
		envelopDispenser.setATMSS(atmss);
		keypad.setATMSS(atmss);

		// start the components
		atmss.start();
		advicePrinter.start();
		cardReader.start();
		cashDispenser.start();
		depositCollector.start();
		display.start();
		keypad.start();
		envelopDispenser.start();

		// MainController threading test - Tony
		MainController mc = new MainController(advicePrinter, cardReader, cashDispenser, depositCollector, display, envelopDispenser, keypad, atmss.getMBox());
		mc.start();
		// NewExceptionEmu debug test
		exEmu = new NewExceptionEmulator("new Exception", atmss);
		atmss.setNewExEmu(exEmu);
	}

	// ------------------------------------------------------------
	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	// getLogger
	public static Logger getLogger() {
		return log;
	} // getLogger
} // ATMKickstarter
