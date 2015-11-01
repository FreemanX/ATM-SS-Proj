/**
 * 
 */
package atmss;

import java.util.LinkedList;

import atmss.bams.*;
import atmss.hardware.*;
import atmss.process.*;

/**
 * @author freeman
 *
 */
public class MainController {

	private CashDispenserController cashDispenserController = new CashDispenserController();
	private CardReaderController cardReaderController = new CardReaderController();
	private KeypadController keypadController = new KeypadController();
	private DepositCollectorController depositCollectorController = new DepositCollectorController();
	private AdvicePrinterController advicePrinterController = new AdvicePrinterController();
	private DisplayController displayController = new DisplayController();
	private EnvelopDispenserController envelopDispenserController = new EnvelopDispenserController();
	private EnquryController enquryController;
	private TransferController transferController;
	private ChangePasswdController changePasswdController;
	private WithDrawController withdrawController;
	private DepositController depositController;
	private BAMSCommunicater serverCommunicater;
	private String[] userAccounts;
	private int timmer;
	private LinkedList<Session> sessionLog;
	private MainController mainController = new MainController();

	
	/**
	 * 
	 */
	public MainController() {
		// TODO Auto-generated constructor stub
	}

	public boolean AutherizePassed()
	{
		boolean passwdIsRight = false;

		/*
		 * Implement the process here.
		 */

		return passwdIsRight;
	}
	
	public Double doBAMSCheckBalance(String accountNumber)
	{
		double balance = 0;
		
		/*
		 * Implement the process here.
		 */
		
		return balance;
	}
	
	public String doBAMSVerifyDestAccount(String desAccountNumber)
	{
		String verifiedInfo = "False"; 
		/*
		 * Implement the process here.
		 * If the account is OK then
		 * verifiedInfo = "<name>/<desAccountNumber>";
		 */
		return verifiedInfo;
	}
	
	public boolean doBAMSUpdateBalance(String accountNumber, double amount)
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doBAMSUpdatePasswd(String accountNumber, String newPasswd)
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doDisplay(String[] displayContent)
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doEjectCash(int amount) // Only eject 100, 500, 1000, must be int
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doEatCash()
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doBAMSTransfer(String accountNumber, String destAccountNumber, double amount)
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doEjectEnvelop()
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean doEatEnvelop()
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	public boolean printAdvice()
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private void handleUserRequest() {

	}

	private boolean createSession() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	private boolean killSession() {
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

	private boolean addSessionToLog(Session currentSession) {
		boolean isSuccess = false;
		/*
		 * Implement the process here.
		 */
		if (currentSession != null) // Session constrains 
			isSuccess = sessionLog.add(currentSession);

		return isSuccess;
	}
	
	private boolean initAll() //Initiate all for serving next guest
	{
		boolean isSuccess = false;

		/*
		 * Implement the process here.
		 */

		return isSuccess;
	}

}
