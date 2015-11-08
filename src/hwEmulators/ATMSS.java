package hwEmulators;

import java.util.logging.Logger;

//======================================================================
// ATMSS
public class ATMSS extends Thread {
	private String id;
	private Logger log = null;
	private MBox mbox = null;
	private ATMSSDBugConsole console = null;

	// HW components
	private AdvicePrinter advicePrinter = null;
	private CardReader cardReader = null;
	private CashDispenser cashDispenser = null;
	private DepositCollector depositCollector = null;
	private Display display = null;
	private EnvelopDispenser envelopDispenser = null;
	private Keypad keypad = null;
	
	// ------------------------------------------------------------
	// ATMSS
	public ATMSS(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();
		mbox = new MBox(id);
		console = new ATMSSDBugConsole("con", this);
	} // ATMSS

	// ------------------------------------------------------------
	// setters
	public void setAdvicePrinter(AdvicePrinter ap) {
		advicePrinter = ap;
	}
	
	public void setCardReader(CardReader cr) {
		cardReader = cr;
	}

	public void setCashDispenser(CashDispenser cd) {
		cashDispenser = cd;
	}

	public void setDepositCollector(DepositCollector dc) {
		depositCollector = dc;
	}
	
	public void setDisplay(Display ds) {
		display = ds;
	}
	
	public void setEnvelopDispenser(EnvelopDispenser ed) {
		envelopDispenser = ed;
	}
	
	public void setKeypad(Keypad kp) {
		keypad = kp;
	}

	// ------------------------------------------------------------
	// getters
	public MBox getMBox() {
		return mbox;
	}

	// ------------------------------------------------------------
	// run
	public void run() {
		while (true) {
			Msg msg = mbox.receive();
			console.println(id + " received " + msg);
		}
	} // run
} // ATMSS
