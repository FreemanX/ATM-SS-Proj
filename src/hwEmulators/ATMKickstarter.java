package hwEmulators;

import java.util.logging.Logger;

import atmss.MainController;
import atmss.hardware.view.KeypadView;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

//======================================================================
// ATMKickstarter
public class ATMKickstarter {
	private static Logger log = null;

	// HW components
	AdvicePrinter advicePrinter;
	CardReader cardReader;
	CashDispenser cashDispenser;
	DepositCollector depositCollector;
	Display display;
	EnvelopDispenser envelopDispenser;
	Keypad keypad;	
	ATMSS atmss;
	ExceptionEmulator exceptionEmulator;
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
		advicePrinter = new AdvicePrinter("ap");
		cardReader = new CardReader("cr");
		cashDispenser = new CashDispenser("cd");
		depositCollector = new DepositCollector("dc");
		display = new Display("ds");
		envelopDispenser = new EnvelopDispenser("ed");
		keypad = new Keypad("kp");
		atmss = new ATMSS("atmss");
		exceptionEmulator = new ExceptionEmulator("ee", atmss);
		// connect components
		atmss.setAdvicePrinter(advicePrinter);
		atmss.setCardReader(cardReader);
		atmss.setCashDispenser(cashDispenser);
		atmss.setDepositCollector(depositCollector);
		atmss.setDisplay(display);
		atmss.setKeypad(keypad);
		atmss.setEnvelopDispenser(envelopDispenser);
		atmss.setExceptionEmulator(exceptionEmulator);
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
		exceptionEmulator.start();
		
		// setup views
		keypadView = new KeypadView(keypad);

		// MainController threading test - Tony
		MainController mc = new MainController(advicePrinter, cardReader, cashDispenser, depositCollector, display, envelopDispenser, keypad);
	}

	// ------------------------------------------------------------
	// getLogger
	public static Logger getLogger() {
		return log;
	} // getLogger
} // ATMKickstarter
