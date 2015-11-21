package atmss.bams;

import atmss.Session;
import hwEmulators.MBox;

// TODO: Auto-generated Javadoc
/**
 * Created by ting on 2015/11/19.
 */
public class BAMSTester {
	
	/** The mybox. */
	private MBox mybox = new MBox("BAMSTester");
	
	/** The server communicator. */
	private BAMSCommunicator serverCommunicator = new BAMSCommunicator(mybox);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new BAMSTester();
	}

	/**
	 * Instantiates a new BAMS tester.
	 */
	public BAMSTester() {
		String cardNo = "981358459216";
		String pin = "321495";
		String cred = new StringBuilder(cardNo).reverse().toString();

		Session session = new Session(99, cred, cardNo);

		String[] accounts = doBAMSGetAccounts(session);

		printAcc(accounts, session);
		System.out.println("Transfer $10 from " + accounts[0] + " to " + accounts[1]);
		System.out.println(doBAMSTransfer(accounts[0], accounts[1], 10.0, session));
		printAcc(accounts, session);

		// withdraw 10
		System.out.println("Withdraw 10: " + doBAMSUpdateBalance(accounts[0], -10, session));
		printAcc(accounts, session);

		// deposit 10
		System.out.println("Deposit 10: " + doBAMSUpdateBalance(accounts[0], 10, session));
		printAcc(accounts, session);
	}

	/**
	 * Prints the acc.
	 *
	 * @param accounts the accounts
	 * @param session the session
	 */
	private void printAcc(String[] accounts, Session session) {
		for (String account : accounts) {
			System.out.println("Acc:" + account + ", $" + doBAMSCheckBalance(account, session));
		}
	}

	/**
	 * Do bams check balance.
	 *
	 * @param accNo the acc no
	 * @param currentSession the current session
	 * @return the double
	 */
	// get input params from Session
	public double doBAMSCheckBalance(String accNo, Session currentSession) {
		return serverCommunicator.enquiry(currentSession.getCardNo(), accNo, currentSession.getCred());
	}

	/**
	 * Do bams get accounts.
	 *
	 * @param currentSession the current session
	 * @return the string[]
	 */
	public String[] doBAMSGetAccounts(Session currentSession) {
		return serverCommunicator.getAccounts(currentSession.getCardNo(), currentSession.getCred());
	}

	/*
	 * dest account verification is done within transfer public String
	 * doBAMSVerifyDestAccount(String desAccountNumber) { String verifiedInfo =
	 * "False";
	 *
	 * return verifiedInfo; }
	 */

	/**
	 * Do bams update balance.
	 *
	 * @param accNumber the acc number
	 * @param amount the amount
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSUpdateBalance(String accNumber, double amount, Session currentSession) {
		long resultAmount = 0;

		if (amount > 0) // deposit
			resultAmount = Math.round(serverCommunicator.deposit(currentSession.getCardNo(), accNumber,
					currentSession.getCred(), (int) amount)); // round down

		if (amount < 0) // withdraw
			resultAmount = Math.round(serverCommunicator.cashWithdraw(currentSession.getCardNo(), accNumber,
					currentSession.getCred(), String.valueOf(amount * -1)));

		if (resultAmount != 0 && resultAmount == Math.abs(amount))
			return true;

		return false;
	}

	/**
	 * Do bams update passwd.
	 *
	 * @param newPin the new pin
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSUpdatePasswd(String newPin, Session currentSession) {
		if (serverCommunicator.changePin(currentSession.getCardNo(), currentSession.getCred(), newPin) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Do bams transfer.
	 *
	 * @param toAccNo the to acc no
	 * @param destAccNo the dest acc no
	 * @param amount the amount
	 * @param currentSession the current session
	 * @return true, if successful
	 */
	public boolean doBAMSTransfer(String toAccNo, String destAccNo, double amount, Session currentSession) {
		double resultAmount = serverCommunicator.transfer(currentSession.getCardNo(), currentSession.getCred() + "&",
				destAccNo, toAccNo, String.valueOf(amount));

		if (resultAmount == amount)
			return true;
		return false;
	}

	/**
	 * Do bams check card valid.
	 *
	 * @param cardNo the card no
	 * @return true, if successful
	 */
	public boolean doBAMSCheckCardValid(String cardNo) {
		return serverCommunicator.isCardExist(cardNo);
	}
}
