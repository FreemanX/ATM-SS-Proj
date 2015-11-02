package atmss.bams;

// TestBASMHandler
public class TestBAMSHandler {
	// ------------------------------------------------------------
	// main
	public static void main(String[] args) {
		String urlPrefix = "http://192.168.111.100/";

		// try logging in
		BAMSHandler bams = new BAMSHandler(urlPrefix);
		String cred = bams.login("123456789", "987654");
		System.out.println("Credential = " + cred);
		return;
	} // main
} // TestBAMSHandler
