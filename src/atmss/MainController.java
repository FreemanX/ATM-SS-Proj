/**
 *
 */
package atmss;

import atmss.bams.BAMSCommunicator;
import atmss.hardware.controller.*;
import atmss.process.*;
import hwEmulators.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class MainController.
 *
 * @author freeman
 */
public class MainController extends Thread {

	// Processing thread
	/**
	 * The Class Processor.
	 */
	// ----------------------------------------------------------------
	class Processor extends Thread {

		/** The checker. */
		SystemCheckThread checker = new SystemCheckThread(advicePrinterController, cardReaderController,
				cashDispenserController, depositCollectorController, displayController, envelopDispenserController,
				keypadController, serverCommunicator);

		/** The processor is running. */
		private volatile boolean isRunning = true;

		/** The Envelop Dispenser works fine. */
		private volatile boolean EDIsOk = true;

		/** The Deposit collector works fine. */
		private volatile boolean DCIsOk = true;

		/** There is a process in process. */
		private volatile boolean isInProcess = false;

		/** The i, count for iteration. */
		int i = 1;

		/** The lines to be display. */
		private String[] lines;

		/** The Constant head. */
		private final static String head = ">>>>>>>>>> ";

		/** The Constant tail. */
		private final static String tail = " <<<<<<<<<<";

		/** The number of typing in wrong passed. */
		private int numOfWrongPassed = 0;

		/**
		 * Instantiates a new processor.
		 */
		public Processor() {
			lines = new String[10];
		}

		/**
		 * Checks if is in process.
		 *
		 * @return true, if is in process
		 */
		protected boolean isInProcess() {
			return this.isInProcess;
		}

		/**
		 * Sets the Envelop Dispenser is OK.
		 *
		 * @param b
		 *            the Envelop Dispenser is OK
		 */
		protected void setEDIsOK(boolean b) {
			this.EDIsOk = b;
		}

		/**
		 * Sets the Deposit collector is OK.
		 *
		 * @param b
		 *            the Deposit collector is OK
		 */
		protected void setDCIsOK(boolean b) {
			this.DCIsOk = b;
		}

		/**
		 * Inits the processor.
		 */
		protected void initProcessor() {
			numOfWrongPassed = 0;
			checker.resumeCheck();
			this.isRunning = true;
			this.i = 1;
		}

		/**
		 * End session.
		 */
		private void endSession() {
			this.isInProcess = false;
			numOfWrongPassed = 0;
		}

		/**
		 * Processor pause.
		 */
		protected void processorPause() {
			this.isRunning = false;
			checker.pauseCheck();
		}

		/**
		 * Clear lines.
		 */
		private void clearLines() {
			for (int j = 0; j < lines.length; j++) {
				lines[j] = "";

			}
		}

		/**
		 * Checks if is a card of our bank.
		 *
		 * @param cardNumber
		 * @return true, if is bank card
		 */
		private boolean isBankCard(String cardNumber) {
			if (cardNumber != null && cardNumber.length() == 12)
				return atmssHandler.doBAMSCheckCardValid(cardNumber);
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			// thread start...
			checker.start();

			while (true) {
				while (isRunning) {
					boolean success = serverCommunicator.ping();
					System.out.println("ping check..." + success);
					if (!success)
						break;
					_atmssMBox.send(new Msg("MainController", 800, "OK"));

					this.endSession();
					System.out.println(">>>>Processor is running, iteration: " + i);
					i++;
					try {
						clearLines();
						lines[1] = head + "Welcome!!!" + tail;
						atmssHandler.doDisDisplayUpper(lines);

						System.out.println("Waing for card...");
						String cardNum = atmssHandler.doCRReadCard();
						System.out.println("Rreceive card: " + cardNum);
						if (!isBankCard(cardNum)) {
							clearLines();
							lines[0] = head + "Invalid card, please insert the card from our bank" + tail;
							lines[1] = head + "Card ejected" + tail;
							lines[2] = "Please take your card...";
							atmssHandler.doDisDisplayUpper(lines);
							if (atmssHandler.doCREjectCard())
								continue;
							else {
								clearLines();
								lines[1] = head + "Your card has been retained, please contact +852 51740740" + tail;
								atmssHandler.doDisDisplayUpper(lines);
								sleep(5000);
								continue;
							}
						}

						clearLines();
						lines[1] = head + "Card inserted" + tail;
						lines[2] = "Please input your password (20 sec/key)";
						atmssHandler.doDisDisplayUpper(lines);
						String pin = "";
						while (numOfWrongPassed < 3) {
							pin = atmssHandler.doKPGetPasswd(20);
							if (pin == null || pin.equals("CANCEL")) {
								break;
							}

							if (authorizePassed(cardNum, pin)) {
								break;
							} else {
								clearLines();
								lines[1] = head + "Wrong password" + tail;
								lines[2] = "Please input your password again(20 sec/key)";
								atmssHandler.doDisDisplayUpper(lines);
								numOfWrongPassed++;
							}
						}

						if (pin == null || pin.equals("CANCEL")) {
							clearLines();
							lines[1] = head + "Card ejected" + tail;
							lines[2] = "Please take your card...";
							atmssHandler.doDisDisplayUpper(lines);
							if (atmssHandler.doCREjectCard())
								continue;
							else {
								clearLines();
								lines[1] = head + "Your card has been retained, please contact +852 51740740" + tail;
								atmssHandler.doDisDisplayUpper(lines);
								sleep(5000);
								continue;
							}
						}

						if (numOfWrongPassed < 3) {
							numOfWrongPassed = 0;
							while (true) {
								clearLines();
								lines[0] = "Welcome! Please select the function you want to use, press CANCEL to exit";
								lines[1] = head + "1. Change password" + tail;
								lines[2] = head + "2. Withdraw money" + tail;
								lines[3] = head + "3. Enquiry" + tail;
								lines[4] = head + "4. Transfer money" + tail;
								if (EDIsOk && DCIsOk) {
									lines[5] = head + "5. Deposit money" + tail;
								}
								atmssHandler.doDisDisplayUpper(lines);
								String userChoise = atmssHandler.doKPGetSingleInput(30);
								if (userChoise == null) {
									atmssHandler.doCRRetainCard();
									break;
								}
								Session currentSession = getLastSession();
								if (userChoise.equals("1")) {
									this.isInProcess = true;
									changePasswdController = new ChangePasswdController(currentSession);
									changePasswdController.doChangePasswd();
									LinkedList<Operation> processOperations = changePasswdController
											.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (processOperations.getLast().getType() != 0) {
										clearLines();
										lines[1] = head + "Card ejected" + tail;
										lines[2] = "Please take your card...";
										atmssHandler.doDisDisplayUpper(lines);
										if (atmssHandler.doCREjectCard()) {
										} else {
											clearLines();
											lines[1] = head
													+ "Your card has been retained, please contact +852 51740740"
													+ tail;
											atmssHandler.doDisDisplayUpper(lines);
										}
										break;
									}

								} else if (userChoise.equals("2")) {
									this.isInProcess = true;
									withdrawController = new WithdrawController(currentSession);
									withdrawController.doWithdraw();
									LinkedList<Operation> processOperations = withdrawController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (processOperations.getLast().getType() != 0) {
										clearLines();
										lines[1] = head + "Card ejected" + tail;
										lines[2] = "Please take your card...";
										atmssHandler.doDisDisplayUpper(lines);
										if (atmssHandler.doCREjectCard()) {
										} else {
											clearLines();
											lines[1] = head
													+ "Your card has been retained, please contact +852 51740740"
													+ tail;
											atmssHandler.doDisDisplayUpper(lines);
										}
										break;
									}

								} else if (userChoise.equals("3")) {
									this.isInProcess = true;
									enquryController = new EnquiryController(currentSession);
									enquryController.doEnquiry();
									LinkedList<Operation> processOperations = enquryController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (processOperations.getLast().getType() != 0) {
										clearLines();
										lines[1] = head + "Card ejected" + tail;
										lines[2] = "Please take your card...";
										atmssHandler.doDisDisplayUpper(lines);
										if (atmssHandler.doCREjectCard()) {
										} else {
											clearLines();
											lines[1] = head
													+ "Your card has been retained, please contact +852 51740740"
													+ tail;
											atmssHandler.doDisDisplayUpper(lines);
										}
										break;
									}

								} else if (userChoise.equals("4")) {
									this.isInProcess = true;
									transferController = new TransferController(currentSession);
									transferController.doTransfer();
									LinkedList<Operation> processOperations = transferController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (processOperations.getLast().getType() != 0) {
										clearLines();
										lines[1] = head + "Card ejected" + tail;
										lines[2] = "Please take your card...";
										atmssHandler.doDisDisplayUpper(lines);
										if (atmssHandler.doCREjectCard()) {
										} else {
											clearLines();
											lines[1] = head
													+ "Your card has been retained, please contact +852 51740740"
													+ tail;
											atmssHandler.doDisDisplayUpper(lines);
										}
										break;
									}

								} else if (EDIsOk && DCIsOk && userChoise.equals("5")) {
									this.isInProcess = true;
									depositController = new DepositController(currentSession);
									depositController.doDeposit();

									LinkedList<Operation> processOperations = depositController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (processOperations.getLast().getType() != 0) {
										clearLines();
										lines[1] = head + "Card ejected" + tail;
										lines[2] = "Please take your card...";
										atmssHandler.doDisDisplayUpper(lines);
										if (atmssHandler.doCREjectCard()) {
										} else {
											clearLines();
											lines[1] = head
													+ "Your card has been retained, please contact +852 51740740"
													+ tail;
											atmssHandler.doDisDisplayUpper(lines);
										}
										break;
									}

								} else if (userChoise.equals("CANCEL")) {
									clearLines();
									lines[1] = head + "Card ejected, please take your card" + tail;
									atmssHandler.doDisDisplayUpper(lines);

									if (!atmssHandler.doCREjectCard()) {
										clearLines();
										lines[1] = head + "Your card has been retained, please contact +852 51740740"
												+ tail;
										atmssHandler.doDisDisplayUpper(lines);
										sleep(10000);
									}
									sleep(1000);
									break;
								}

							} // End of while

							/*---------------------Write out log----------------------------*/
							try (PrintWriter out = new PrintWriter(
									new BufferedWriter(new FileWriter("SessionLog.txt", true)))) {
								Session s = getLastSession();
								out.println();
								out.println("=====================Session: " + s.getSid() + "=====================");
								SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
								out.println("Time: " + f.format(new Date().getTime()));
								out.println("Card number: " + s.getCardNo());
								List<Operation> ops = s.getOps();
								for (Operation op : ops) {
									out.println(op.getName() + " [" + op.getType() + "]");
									out.println("\t Result: " + op.getDes());
								}
							} catch (IOException e) {
								System.err.println(">>>>>>>> IOException: write out log");
							}

							this.endSession();
						} else {
							clearLines();
							lines[1] = head + "Card ejected" + tail;
							lines[2] = "You have input wrong password 3 times, please take your card";
							atmssHandler.doDisDisplayUpper(lines);
							this.initProcessor();
							if (atmssHandler.doCREjectCard())
								continue;
							else {
								clearLines();
								lines[1] = head + "Your card has been retained, please contact +852 51740740" + tail;
								atmssHandler.doDisDisplayUpper(lines);
								sleep(10000);
								continue;
							}
						}

					} catch (InterruptedException e) {

					}
				}
			}
		}

	}

	/**
	 * Instantiates a new main controller.
	 *
	 * @param AP
	 *            the Advice Printer
	 * @param CR
	 *            the Card Reader
	 * @param CD
	 *            the Cash dispenser
	 * @param depositCollector
	 *            the deposit collector
	 * @param display
	 *            the display
	 * @param envelopDispenser
	 *            the envelop dispenser
	 * @param KP
	 *            the keypad
	 * @param AtmssMbox
	 *            the mbox from atmss
	 */
	// public static MainController getInstance() { return self; }
	public MainController(AdvicePrinter AP, CardReader CR, CashDispenser CD, DepositCollector depositCollector,
			Display display, EnvelopDispenser envelopDispenser, Keypad KP, MBox AtmssMbox) {
		this._atmssMBox = AtmssMbox;
		this.isRunning = true;
		mainControllerMBox = new MBox("MainController");
		this.advicePrinterController = new AdvicePrinterController(AP);
		this.cardReaderController = new CardReaderController(CR);
		this.cashDispenserController = new CashDispenserController(CD);
		this.depositCollectorController = new DepositCollectorController(depositCollector);
		this.displayController = new DisplayController(display);
		this.envelopDispenserController = new EnvelopDispenserController(envelopDispenser);
		this.keypadController = new KeypadController(KP);
		this.serverCommunicator = new BAMSCommunicator(mainControllerMBox);
		this.atmssHandler = ATMSSHandler.getHandler();
		this.atmssHandler.initHandler(cashDispenserController, cardReaderController, keypadController,
				depositCollectorController, advicePrinterController, displayController, envelopDispenserController,
				serverCommunicator);

		this.advicePrinterController.setMainControllerMBox(mainControllerMBox);
		this.cardReaderController.setMainControllerMBox(mainControllerMBox);
		this.cashDispenserController.setMainControllerMBox(mainControllerMBox);
		this.depositCollectorController.setMainControllerMBox(mainControllerMBox);
		this.displayController.setMainControllerMBox(mainControllerMBox);
		this.envelopDispenserController.setMainControllerMBox(mainControllerMBox);
		this.keypadController.setMainControllerMBox(mainControllerMBox);

		// start Processor
		this.processor = new Processor();
		processor.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			while (isRunning) {

				Msg msg = this.mainControllerMBox.receive();
				String sender = msg.getSender();
				switch (sender) {
				case "AP":
					handleAPMsg(msg);
					break;
				case "CR":
					handleCRMsg(msg);
					break;
				case "CD":
					handleCDMsg(msg);
					break;
				case "DC":
					handleDCMsg(msg);
					break;
				case "Dis":
					handleDisMsg(msg);
					break;
				case "ED":
					handleEDMsg(msg);
					break;
				case "KP":
					handleKPMsg(msg);
					break;
				case "BAMS":
					handleBAMSMsg(msg);
					break;
				default:
					break;
				}
			}
			try {
				waitForRepair();
				sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Authorize pin.
	 *
	 * @param cardNo
	 *            the card number
	 * @param pin
	 *            the card password
	 * @return true, if successful
	 */
	public boolean authorizePassed(String cardNo, String pin) {
		String result = serverCommunicator.login(cardNo, pin);
		if (!result.equalsIgnoreCase("error")) {
			sessionLog.add(new Session(new Date().getTime(), result, cardNo));
			return true;
		}
		return false;
	}

	/**
	 * Send to bams.
	 *
	 * @param msg
	 *            the msg
	 */
	private void sendToBAMS(Msg msg) {
		SimpleDateFormat format = new SimpleDateFormat("H:mm:ss");
		_atmssMBox.send(new Msg("MainController", msg.getType(),
				msg.getDetails() + ": " + format.format(new Date().getTime())));
	}

	/**
	 * Handle bams msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleBAMSMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle Advice Printer msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleAPMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle Card Reader msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleCRMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle Cash dispenser msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleCDMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() == 301) {
			System.err.println("Warning: insufficent amount of cash");
		} else if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle Deposit collector msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleDCMsg(Msg msg) {
		if (msg.getType() % 100 == 0)
			processor.setDCIsOK(true);
		else
			processor.setDCIsOK(false);
	}

	/**
	 * Handle Display msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleDisMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle Envelop Dispenser msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleEDMsg(Msg msg) {
		if (msg.getType() % 100 == 0)
			processor.setEDIsOK(true);
		else
			processor.setEDIsOK(false);
	}

	/**
	 * Handle keypad msg.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleKPMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	/**
	 * Handle fatal exceptions.
	 *
	 * @param msg
	 *            the msg
	 */
	private void handleFatalExceptions(Msg msg) {
		this.isRunning = false;
		String card = this.atmssHandler.doCRGetCardNumber();
		if (card != null && card.length() == 12) {
			this.atmssHandler.doDisClearUpper();
			String[] lines = { "", "This ATM is out of service, please take your card" };
			this.atmssHandler.doDisDisplayUpper(lines);
			if (!atmssHandler.doCREjectCard()) {
				lines[1] = "Your card has been retained, please contact +852 51740740";
				atmssHandler.doDisDisplayUpper(lines);
				try {
					sleep(10000);
				} catch (InterruptedException e) {
				}
			}
		}
		while (this.processor.isInProcess) {
			// wait for the current process finishes
		}
		this.atmssHandler.doDisClearUpper();
		String[] lines = { "", "This ATM is out of service!!!~" };
		this.atmssHandler.doDisDisplayUpper(lines);
		this.processor.stop();
	}

	/**
	 * Inits all variable for serving next guest after repairing.
	 */
	private void initAll() {
		SimpleDateFormat format = new SimpleDateFormat("H:mm:ss");
		_atmssMBox.send(new Msg("MainController", 0, "Everything is fine @ " + format.format(new Date().getTime())));
		this.mainControllerMBox.clearBox();
		this.cardReaderController.initCR();
		this.isRunning = true;
		this.atmssHandler.doDisClearAll();
		this.processor = new Processor();
		this.processor.start();
	}

	/**
	 * Gets the last session.
	 *
	 * @return the last session
	 */
	private Session getLastSession() {
		return sessionLog.get(sessionLog.size() - 1);
	}

	/**
	 * Wait for repair.
	 */
	private void waitForRepair() {
		Msg msg = null;

		try {
			boolean b1 = this.advicePrinterController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleAPMsg(msg);
			sleep(100);

			boolean b2 = this.cardReaderController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleCRMsg(msg);
			sleep(100);

			boolean b3 = this.cashDispenserController.updateStatus();
			if (b3 == false && msg.getType() == 301) {
				b3 = true;
			}
			msg = this.mainControllerMBox.receive();
			handleCDMsg(msg);
			sleep(100);

			boolean b4 = this.displayController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleDisMsg(msg);
			sleep(100);

			boolean b5 = this.keypadController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleKPMsg(msg);

			boolean b6 = this.serverCommunicator.ping();
			msg = this.mainControllerMBox.receive();
			handleBAMSMsg(msg);
			// if (true || b1 && b2 && b3 && b4 && b5) { //evil code
			if (b1 && b2 && b3 && b4 && b5 && b6) {
				initAll();
			}
		} catch (Exception e) {
		}
	}

	/** The cash dispenser controller. */
	private CashDispenserController cashDispenserController;

	/** The card reader controller. */
	private CardReaderController cardReaderController;

	/** The keypad controller. */
	private KeypadController keypadController;

	/** The deposit collector controller. */
	private DepositCollectorController depositCollectorController;

	/** The advice printer controller. */
	private AdvicePrinterController advicePrinterController;

	/** The display controller. */
	private DisplayController displayController;

	/** The envelop dispenser controller. */
	private EnvelopDispenserController envelopDispenserController;

	/** The enqury controller. */
	private EnquiryController enquryController;

	/** The transfer controller. */
	private TransferController transferController;

	/** The change passwd controller. */
	private ChangePasswdController changePasswdController;

	/** The withdraw controller. */
	private WithdrawController withdrawController;

	/** The deposit controller. */
	private DepositController depositController;

	/** The server communicator. */
	private BAMSCommunicator serverCommunicator;

	/** The processor. */
	private Processor processor;

	/** The session log. */
	private volatile List<Session> sessionLog = new ArrayList<Session>();

	/** The atmss handler. */
	private ATMSSHandler atmssHandler;

	/** The main controller MBox. */
	private MBox mainControllerMBox;

	/** The is running. */
	private volatile boolean isRunning;

	/** The _atmss MBox. */
	private MBox _atmssMBox;
}
