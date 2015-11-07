package hwEmulators;

import java.util.logging.Logger;


public class Display {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	
	
	public Display(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		
	} // Keypad

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS
}
