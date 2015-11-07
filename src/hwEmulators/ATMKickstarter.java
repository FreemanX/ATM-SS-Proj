package hwEmulators;

import java.util.logging.Logger;

import atmss.MainController;
import atmss.hardware.hw.KeypadView;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

//======================================================================
// ATMKickstarter
public class ATMKickstarter {
	private static Logger log = null;

	// HW components
	CashDispenser cashDispenser;
	CardReader cardReader;
	Keypad keypad;
	AdvicePrinter advicePrinter;
	DepositCollector depositCollector;
	ATMSS atmss;
	
	// view components
	KeypadView keypadView;

	// ------------------------------------------------------------
	// main
	public static void main(String args[]) {
		new ATMKickstarter();
	} // main

	public ATMKickstarter() {
		// create and configure logger
		ConsoleHandler conHd = new ConsoleHandler();
		conHd.setFormatter(new ATMSSLogFormatter());
		log = Logger.getLogger(ATMKickstarter.class.getName());
		log.setUseParentHandlers(false);
		log.addHandler(conHd);
		log.setLevel(Level.INFO);

		// create components
		cashDispenser = new CashDispenser("cd");
		cardReader = new CardReader("cr");
		keypad = new Keypad("kp");
		advicePrinter = new AdvicePrinter("ap");
		depositCollector = new DepositCollector("dc");
		atmss = new ATMSS("atmss");

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
		
		// setup views
		keypadView = new KeypadView(keypad);
	}

	// ------------------------------------------------------------
	// getLogger
	public static Logger getLogger() {
		return log;
	} // getLogger
} // ATMKickstarter
