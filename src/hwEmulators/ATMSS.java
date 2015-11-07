package hwEmulators;

import java.util.logging.Logger;

//======================================================================
// ATMSS
public class ATMSS extends Thread {
	private String id;
	private Logger log = null;
	private CardReader cardReader = null;
	private Keypad keypad = null;
	private EnvelopDispenser envelopDispenser = null;
	private ATMSSDBugConsole console = null;
	private MBox mbox = null;	

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
	public void setCardReader(CardReader cr) {
		cardReader = cr;
	}

	public void setKeypad(Keypad kp) {
		keypad = kp;
	}
	
	public void setEnvelopDispenser(EnvelopDispenser ed) {
		envelopDispenser = ed;
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
