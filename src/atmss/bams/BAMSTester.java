package atmss.bams;

import atmss.ATMSSHandler;
import atmss.MainController;
import atmss.Session;
import hwEmulators.MBox;

/**
 * Created by ting on 2015/11/19.
 */
public class BAMSTester {
	private MBox mybox = new MBox("BAMSTester");
	private BAMSCommunicator serverCommunicator = new BAMSCommunicator(mybox);

	public static void main(String[] args) {
		new BAMSTester();
	}

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

	private void printAcc(String[] accounts, Session session) {
		for (String account : accounts) {
			System.out.println("Acc:" + account + ", $" + doBAMSCheckBalance(account, session));
		}
	}

	// get input params from Session
	public double doBAMSCheckBalance(String accNo, Session currentSession) {
		return serverCommunicator.enquiry(currentSession.getCardNo(), accNo, currentSession.getCred());
	}

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

	public boolean doBAMSUpdatePasswd(String newPin, Session currentSession) {
		if (serverCommunicator.changePin(currentSession.getCardNo(), currentSession.getCred(), newPin) == 1) {
			return true;
		}
		return false;
	}

	public boolean doBAMSTransfer(String toAccNo, String destAccNo, double amount, Session currentSession) {
		double resultAmount = serverCommunicator.transfer(currentSession.getCardNo(), currentSession.getCred() + "&",
				destAccNo, toAccNo, String.valueOf(amount));

		if (resultAmount == amount)
			return true;
		return false;
	}

	public boolean doBAMSCheckCardValid(String cardNo) {
		return serverCommunicator.isCardExist(cardNo);
	}
}
