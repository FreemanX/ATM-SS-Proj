package atmss.bams;

import hwEmulators.MBox;
import hwEmulators.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

// TestBASMHandler
public class BAMSCommunicator extends BAMSHandler {

	private final static String prefix = "http://cs6063.comp.hkbu.edu.hk/~group05/";
	private MBox maincontrollerBox  = null;
	private int timeout = 3000;

	public BAMSCommunicator(MBox maincontrollerBox) {
		super(prefix);
		this.maincontrollerBox = maincontrollerBox;
	}

	public String login(String cardNo, String pin) {
		if (ping()) {
			return super.login(cardNo, pin);
		}
		return "ERROR";
	}

	public int cashWithdraw(String cardNo, String accNo, String cred, String amount) {
		if (ping()) {
			return super.cashWithdraw(cardNo, accNo, cred, amount);
		}
		return -1;
	}

	public double deposit(String cardNo, String accNo, String cred, int amount) {
		if (ping()) {
			return super.deposit(cardNo, accNo, cred, amount);
		}
		return -1;
	}

	public double enquiry(String cardNo, String accNo, String cred) {
		if (ping()) {
			return super.enquiry(cardNo, accNo, cred);
		}
		return -1;
	}

	public double transfer(String cardNo, String cred, String fromAcc, String toAcc, String amount) {
		if (ping()) {
			return super.transfer(cardNo, cred, fromAcc, toAcc, amount);
		}
		return -1;
	}

	public int changePin(String cardNo, String cred, String newPin) {
		if (ping()) {
			String urlStr = prefix + "changePIN.php?cardNo=" + cardNo + "&cred=" + cred + "&newPin=" + newPin;
			return Integer.parseInt(sendRequest(urlStr));
		}
		return 0;
	}

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

	public boolean isCardExist(String cardNo) {
		String urlStr = prefix + "cardExist.php?cardNo=" + cardNo;
		String result = sendRequest(urlStr);

		if (result.equalsIgnoreCase("true"))
			return true;

		return false;
	}

	public boolean ping() { // timeout in ms
		String urlStr = prefix + "login.php?"; // expect "ERROR"
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
