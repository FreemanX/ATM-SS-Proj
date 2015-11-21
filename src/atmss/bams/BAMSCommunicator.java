package atmss.bams;

import hwEmulators.MBox;
import hwEmulators.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class BAMSCommunicator.
 */
// TestBASMHandler
public class BAMSCommunicator extends BAMSHandler {

	/** The Constant prefix. */
	private final static String prefix = "http://cs6063.comp.hkbu.edu.hk/~group05/";
	
	/** The maincontroller box. */
	private MBox maincontrollerBox  = null;
	
	/** The timeout. */
	private int timeout = 3000;

	/**
	 * Instantiates a new BAMS communicator.
	 *
	 * @param maincontrollerBox the maincontroller box
	 */
	public BAMSCommunicator(MBox maincontrollerBox) {
		super(prefix);
		this.maincontrollerBox = maincontrollerBox;
	}

	/* (non-Javadoc)
	 * @see atmss.bams.BAMSHandler#login(java.lang.String, java.lang.String)
	 */
	public String login(String cardNo, String pin) {
		if (ping()) {
			return super.login(cardNo, pin);
		}
		return "ERROR";
	}

	/* (non-Javadoc)
	 * @see atmss.bams.BAMSHandler#cashWithdraw(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int cashWithdraw(String cardNo, String accNo, String cred, String amount) {
		if (ping()) {
			return super.cashWithdraw(cardNo, accNo, cred, amount);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see atmss.bams.BAMSHandler#deposit(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public double deposit(String cardNo, String accNo, String cred, int amount) {
		if (ping()) {
			return super.deposit(cardNo, accNo, cred, amount);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see atmss.bams.BAMSHandler#enquiry(java.lang.String, java.lang.String, java.lang.String)
	 */
	public double enquiry(String cardNo, String accNo, String cred) {
		if (ping()) {
			return super.enquiry(cardNo, accNo, cred);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see atmss.bams.BAMSHandler#transfer(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public double transfer(String cardNo, String cred, String fromAcc, String toAcc, String amount) {
		if (ping()) {
			return super.transfer(cardNo, cred, fromAcc, toAcc, amount);
		}
		return -1;
	}

	/**
	 * Change pin.
	 *
	 * @param cardNo the card no
	 * @param cred the cred
	 * @param newPin the new pin
	 * @return the int
	 */
	public int changePin(String cardNo, String cred, String newPin) {
		if (ping()) {
			String urlStr = prefix + "changePIN.php?cardNo=" + cardNo + "&cred=" + cred + "&newPin=" + newPin;
			return Integer.parseInt(sendRequest(urlStr));
		}
		return 0;
	}

	/**
	 * Gets the accounts.
	 *
	 * @param cardNo the card no
	 * @param cred the cred
	 * @return the accounts
	 */
	public String[] getAccounts(String cardNo, String cred) {
		if (ping()) {
			String urlStr = prefix + "accounts.php?cardNo=" + cardNo + "&cred=" + cred;
			String result = sendRequest(urlStr);

			if (!result.equalsIgnoreCase("error")) {
				return result.split(",");
			}
		}
		return new String[0];
	}

	/**
	 * Checks if is card exist.
	 *
	 * @param cardNo the card no
	 * @return true, if is card exist
	 */
	public boolean isCardExist(String cardNo) {
		String urlStr = prefix + "cardExist.php?cardNo=" + cardNo;
		String result = sendRequest(urlStr);

		if (result.equalsIgnoreCase("true"))
			return true;

		return false;
	}

	/**
	 * Ping.
	 *
	 * @return true, if successful
	 */
	public boolean ping() { // timeout in ms
		String urlStr = prefix + "ping.php"; // expect "ERROR"
		Msg m = null;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
			con.setConnectTimeout(timeout);
			int code = con.getResponseCode();
			m = new Msg("BAMS", 800, "ResponseCode: " + code);

			return true;
		} catch (SocketTimeoutException e) {
			// timeout
			m = new Msg("BAMS", 899, "Connection error...");
		} catch (IOException e) {
			m = new Msg("BAMS", 899, "Connection error...");
		}
		System.out.println("BAMS sending: " + m);
		maincontrollerBox.send(m);
		return false;
	}

	// ------------------------------------------------------------
	/**
	 * Send request.
	 *
	 * @param urlStr the url str
	 * @return the string
	 */
	// sendRequest
	private String sendRequest(String urlStr) {
		String reply = "-1";

		try {
			URL url = new java.net.URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			reply = in.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return reply;
	} // sendRequest
} // TestBAMSHandler
