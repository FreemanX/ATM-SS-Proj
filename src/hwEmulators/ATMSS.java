package hwEmulators;

import java.util.logging.Logger;

//======================================================================
// ATMSS
public class ATMSS extends Thread {
	private String id;
	private Logger log = null;
	private MBox mbox = null;
	private ATMSSDBugConsole console = null;
	private ExceptionEmulator exceptionEmulator = null;

	// HW components
	private AdvicePrinter advicePrinter = null; // 1
	private CardReader cardReader = null; // 2
	private CashDispenser cashDispenser = null; // 3
	private DepositCollector depositCollector = null; // 4
	private Display display = null; // 5
	private EnvelopDispenser envelopDispenser = null; // 6
	private Keypad keypad = null; // 7

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

	public void setExceptionEmulator(ExceptionEmulator ee) {
		this.exceptionEmulator = ee;
	}

	public void setHWStatus(int type, int statusCode) {
		switch (type) {
		case 1:
			advicePrinter.setAPStatus(statusCode);
			System.out.println(">>>>>>>>>AP changes to " + advicePrinter.getAPStatus());
			break;
		case 2:
			cardReader.setCRStatus(statusCode);
			System.out.println(">>>>>>>>>CR changes to " + cardReader.getCRStatus());
			break;
		case 3:
			cashDispenser.setCDStatus(statusCode);
			System.out.println(">>>>>>>>>cashDispenser changes to " + cashDispenser.getCDStatus());
			break;
		case 4:
			depositCollector.setDCStatus(statusCode);
			System.out.println(">>>>>>>>>depositCollector changes to " + depositCollector.getDCStatus());
			break;
		case 5:
			display.setDisStatus(statusCode);
			System.out.println(">>>>>>>>>display changes to " + display.getDisStatus());
			break;
		case 6:
			envelopDispenser.setEDStatus(statusCode);
			System.out.println(">>>>>>>>>envelopDispenser changes to " + envelopDispenser.getEDStatus());
			break;
		case 7:
			keypad.setKPStatus(statusCode);
			System.out.println(">>>>>>>>>keypad changes to " + keypad.getKPStatus());
			break;
		default:
			break;
		}
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