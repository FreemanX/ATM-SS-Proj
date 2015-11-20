package atmss.bams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// TestBASMHandler
public class BAMSCommunicator extends BAMSHandler {

	private final static String prefix = "http://cs6063.comp.hkbu.edu.hk/~group05/";

	public BAMSCommunicator() {
		super(prefix);
	}

	public boolean ping() {
		String urlStr = prefix + "login.php?"; // expect "ERROR"
		if (sendRequest(urlStr).equalsIgnoreCase("error")) {
			return true;
		}
		return false;
	}

	public int changePin(String cardNo, String cred, String newPin) {
		String urlStr = prefix + "changePIN.php?cardNo=" + cardNo + "&cred=" + cred + "&newPin=" + newPin;
		return Integer.parseInt(sendRequest(urlStr));
	}

	public String[] getAccounts(String cardNo, String cred) {
		String urlStr = prefix + "accounts.php?cardNo=" + cardNo + "&cred=" + cred;
		String result = sendRequest(urlStr);

		if (!result.equalsIgnoreCase("error")) {
			return result.split(",");
		}

		return new String[0];
	}

	/**
	 * @param cardNo
	 *            The card number read from the inserted card
	 * @return true - belongs to our bank, false - doesn't belongs to our bank
	 */
	public boolean isCardExist(String cardNo) {
		String urlStr = prefix + "cardExist.php?cardNo=" + cardNo;
		String result = sendRequest(urlStr);

		if (result.equalsIgnoreCase("true"))
			return true;

		return false;
	}

	// ------------------------------------------------------------
	// sendRequest
	/**
	 *
	 * @param urlStr
	 *            The url (string) for making the request
	 * @return The reply string received from BAMS.
	 */
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
