/**
 *
 */
package atmss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import atmss.bams.*;
import atmss.hardware.controller.*;
import atmss.process.*;
import hwEmulators.*;

/**
 * @author freeman
 *
 */
public class MainController extends Thread {

	// Processing thread
	// ----------------------------------------------------------------
	class Processor extends Thread {
		SystemCheckThread checker = new SystemCheckThread(advicePrinterController, cardReaderController,
				cashDispenserController, depositCollectorController, displayController, envelopDispenserController,
				keypadController, serverCommunicator);
		private volatile boolean isRunning = true;
		private volatile boolean EDIsOk = true;
		private volatile boolean DCIsOk = true;
		private volatile boolean isInProcess = false;
		int i = 1;
		private String[] lines;
		private final static String head = ">>>>>>>>>> ";
		private final static String tail = " <<<<<<<<<<";
		private int numOfWrongPassed = 0;

		public Processor() {
			// constructor...
			lines = new String[10];
		}

		protected boolean isInProcess() {
			return this.isInProcess;
		}

		protected void setEDIsOK(boolean b) {
			this.EDIsOk = b;
		}

		protected void setDCIsOK(boolean b) {
			this.DCIsOk = b;
		}

		protected void initProcessor() {
			numOfWrongPassed = 0;
			checker.resumeCheck();
			this.isRunning = true;
			this.i = 1;
		}

		private void endSession() {
			this.isInProcess = false;
			numOfWrongPassed = 0;
		}

		protected void processorPause() {
			this.isRunning = false;
			checker.pauseCheck();
		}

		private void clearLines() {
			for (int j = 0; j < lines.length; j++) {
				lines[j] = "";

			}
		}

		private boolean isBankCard(String s) {
			if (s != null && s.length() == 12)
				return atmssHandler.doBAMSCheckCardValid(s);
			return false;
		}

		public void run() {
			// thread start...
			checker.start();

			// debug test
			while (true) {
				while (isRunning) {
					this.endSession();
					System.out.println(">>>>Processor is running, iteration: " + i);
					i++;
					try {
						/*----------------------<Debug-------------------------*/
						clearLines();
						lines[0] = head + "Choose the controller you want to debug:" + tail;
						lines[1] = head + "1. Debug Change password" + tail;
						lines[2] = head + "2. Debug Deposit money" + tail;
						lines[3] = head + "3. Debug Enqury" + tail;
						lines[4] = head + "4. Debug Transfer money" + tail;
						lines[5] = head + "5. Debug Withdraw money" + tail;
						lines[6] = head + "6. Debug Main controller" + tail;
						atmssHandler.doDisDisplayUpper(lines);

						String choise = atmssHandler.doKPGetSingleInput(43200000);
						Session fakeSession = new Session(1, "612954853189", "981358459216");
						if (choise.equals("1")) {
							this.isInProcess = true;
							changePasswdController = new ChangePasswdController(fakeSession);
							System.out.println("Process finishes, result: " + changePasswdController.doChangePasswd());
							continue;
						} else if (choise.equals("2")) {
							this.isInProcess = true;
							depositController = new DepositController(fakeSession);
							System.out.println("Process finishes, result: " + depositController.doDeposit());
							continue;
						} else if (choise.equals("3")) {
							this.isInProcess = true;
							enquryController = new EnquryController(fakeSession);
							System.out.println("Process finishes, result: " + enquryController.doEnqury());
							continue;
						} else if (choise.equals("4")) {
							this.isInProcess = true;
							transferController = new TransferController(fakeSession);
							System.out.println("Process finishes, result: " + transferController.doTransfer());
							continue;
						} else if (choise.equals("5")) {
							this.isInProcess = true;
							withdrawController = new WithDrawController(fakeSession);
							System.out.println("Process finishes, result: " + withdrawController.doWithDraw());
							continue;
						} else if (choise.equals("6")) {

						} else {
							continue;
						}

						/*----------------------Debug>-------------------------*/

						clearLines();
						lines[1] = head + "Welcome!!!" + tail;
						atmssHandler.doDisDisplayUpper(lines);

						System.out.println("Waing for card...");
						String cardNum = atmssHandler.doCRReadCard();
						System.out.println("Rreceive card: " + cardNum);
						// TODO check if this card is valid card
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
								lines[0] = "Welcome! Please select the function you want to use, press CANCLE to exit";
								lines[1] = head + "1. Change password" + tail;
								lines[2] = head + "2. Withdraw money" + tail;
								lines[3] = head + "3. Enqury" + tail;
								lines[4] = head + "4. Transfer money" + tail;
								if (EDIsOk && DCIsOk) {
									lines[5] = head + "5. Deposit money" + tail;
								}
								atmssHandler.doDisDisplayUpper(lines);
								String userChoise = atmssHandler.doKPGetSingleInput(60);
								Session currentSession = getLastSession();
								if (userChoise.equals("1")) {
									this.isInProcess = true;
									changePasswdController = new ChangePasswdController(currentSession);
									boolean isSuccess = changePasswdController.doChangePasswd();
									LinkedList<Operation> processOperations = changePasswdController
											.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (!isSuccess) {
										Operation op = processOperations.getLast();
										if (op.getName().equalsIgnoreCase("cancel")) {
											break;
										}
										/*
										 * TODO do operation according to
										 * unified protocol
										 */
									}

								} else if (userChoise.equals("2")) {
									this.isInProcess = true;
									withdrawController = new WithDrawController(currentSession);
									boolean isSuccess = withdrawController.doWithDraw();
									LinkedList<Operation> processOperations = withdrawController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (!isSuccess) {
										Operation op = processOperations.getLast();
										if (op.getName().equalsIgnoreCase("cancel")) {
											break;
										}
										/*
										 * TODO do operation according to
										 * unified protocol
										 */
									}
								} else if (userChoise.equals("3")) {
									this.isInProcess = true;
									enquryController = new EnquryController(currentSession);
									boolean isSuccess = enquryController.doEnqury();
									LinkedList<Operation> processOperations = enquryController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (!isSuccess) {
										Operation op = processOperations.getLast();
										if (op.getName().equalsIgnoreCase("cancel")) {
											break;
										}
										/*
										 * TODO do operation according to
										 * unified protocol
										 */
									}
								} else if (userChoise.equals("4")) {
									this.isInProcess = true;
									transferController = new TransferController(currentSession);
									boolean isSuccess = transferController.doTransfer();
									LinkedList<Operation> processOperations = transferController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
									}
									if (!isSuccess) {
										Operation op = processOperations.getLast();
										if (op.getName().equalsIgnoreCase("cancel")) {
											break;
										}
										/*
										 * TODO do operation according to
										 * unified protocol
										 */
									}
								} else if (EDIsOk && DCIsOk && userChoise.equals("5")) {
									this.isInProcess = true;
									depositController = new DepositController(currentSession);
									boolean isSuccess = depositController.doDeposit();

									LinkedList<Operation> processOperations = depositController.getOperationCache();
									for (Operation op : processOperations) {
										currentSession.addOp(op);
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

							}
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
						e.printStackTrace();
					}
				}
			}
		}

	}

	// TODO Singleton need to be implemented
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

	public boolean authorizePassed(String cardNo, String pin) {
		String result = serverCommunicator.login(cardNo, pin);
		if (!result.equalsIgnoreCase("error")) {
			sessionLog.add(new Session(new Date().getTime(), result, cardNo));
			return true;
		}
		return false;
	}

	private void sendToBAMS(Msg msg) {
		SimpleDateFormat format = new SimpleDateFormat("H:mm:ss");
		_atmssMBox.send(new Msg("MainController", msg.getType(),
				msg.getDetails() + ": " + format.format(new Date().getTime())));
	}

	private void handleBAMSMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleAPMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleCRMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleCDMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() == 301) {
			System.err.println("Warning: insufficent amount of cash");
		} else if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleDCMsg(Msg msg) {
		if (msg.getType() % 100 == 0)
			processor.setDCIsOK(true);
		else
			processor.setDCIsOK(false);
	}

	private void handleDisMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleEDMsg(Msg msg) {
		if (msg.getType() % 100 == 0)
			processor.setEDIsOK(true);
		else
			processor.setEDIsOK(false);
	}

	private void handleKPMsg(Msg msg) {
		sendToBAMS(msg);
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleFatalExceptions(Msg msg) {
		this.isRunning = false;
		String card = this.atmssHandler.doCRGetCardNumebr();
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
		// _atmssMBox.send(new Msg("MainController", msg.getType(),
		// msg.getDetails()));
	}

	private void initAll() // Initiate all for serving next guest
	{
		SimpleDateFormat format = new SimpleDateFormat("H:mm:ss");
		_atmssMBox.send(new Msg("MainController", 0, "Everything is fine @ " + format.format(new Date().getTime())));
		this.mainControllerMBox.clearBox();
		this.cardReaderController.initCR();
		this.isRunning = true;
		this.atmssHandler.doDisClearAll();
		this.processor = new Processor();
		this.processor.start();
	}

	private Session getLastSession() {
		return sessionLog.get(sessionLog.size() - 1);
	}

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

			// if (true || b1 && b2 && b3 && b4 && b5) { //evil code
			if (b1 && b2 && b3 && b4 && b5) {
				initAll();
			}
		} catch (Exception e) {
		}
	}

	// -------------------------------------------------------------------------------------
	private CashDispenserController cashDispenserController;
	private CardReaderController cardReaderController;
	private KeypadController keypadController;
	private DepositCollectorController depositCollectorController;
	private AdvicePrinterController advicePrinterController;
	private DisplayController displayController;
	private EnvelopDispenserController envelopDispenserController;
	private EnquryController enquryController;
	private TransferController transferController;
	private ChangePasswdController changePasswdController;
	private WithDrawController withdrawController;
	private DepositController depositController;
	private BAMSCommunicator serverCommunicator;
	private Processor processor;
	private volatile List<Session> sessionLog = new ArrayList<Session>();
	private ATMSSHandler atmssHandler;
	private MBox mainControllerMBox;
	private volatile boolean isRunning;
	private MBox _atmssMBox;
	// TODO Singleton need to be implemented
	// private static MainController self = new MainController();

}
