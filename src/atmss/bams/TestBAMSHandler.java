package atmss.bams;


// TestBASMHandler
public class TestBAMSHandler {
    //------------------------------------------------------------
    // main
    public static void main(String[] args) {
	String urlPrefix = "http://cs6067.comp.hkbu.edu.hk/~group01/";

	// try logging in
	BAMSHandler bams = new BAMSHandler(urlPrefix);
	String cred = bams.login("123456789", "456123789");
	System.out.println("Credential = " + cred);
	return;
    } // main
} // TestBAMSHandler
