package atmss.bams;

/**
 * Created by ting on 2015/11/19.
 */
public class BAMSTester {

    public static void main(String[] args) {
        new BAMSTester();
    }

    public BAMSTester() {
        String prefix = "http://cs6063.comp.hkbu.edu.hk/~group05/";
        String cardNo = "981358459216";
        String pin = "321495";

        BAMSCommunicator con = new BAMSCommunicator(prefix);

        String cred = con.login(cardNo, pin);
        System.out.println(this.getClass().getSimpleName() + ">> cred: " + cred);

        String[] accounts = con.getAccounts(cardNo, cred);
        for (String account : accounts) {
            System.out.println(this.getClass().getSimpleName() + ">> account: " + account + ", balance: " + con.enquiry(cardNo, account, cred));
        }

        System.out.println(con.transfer(cardNo, cred + "&", accounts[0], accounts[1], "5"));
        accounts = con.getAccounts(cardNo, cred);
        for (String account : accounts) {
            System.out.println(this.getClass().getSimpleName() + ">> account: " + account + ", balance: " + con.enquiry(cardNo, account, cred));
        }
    }
}
