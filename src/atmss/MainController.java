/**
 *
 */
package atmss;

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
		int i = 1;
		private String[] lines;
		private final static String head = ">>>>>>>>>> ";
		private final static String tail = " <<<<<<<<<<";
		private int numOfWrongPassed = 0;

		public Processor() {
			// constructor...
			lines = new String[10];
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
							changePasswdController = new ChangePasswdController(fakeSession);
							System.out.println("Process finishes, result: " + changePasswdController.doChangePasswd());
							continue;
						} else if (choise.equals("2")) {
							depositController = new DepositController(fakeSession);
							System.out.println("Process finishes, result: " + depositController.doDeposit());
							continue;
						} else if (choise.equals("3")) {
							enquryController = new EnquryController(fakeSession);
							System.out.println("Process finishes, result: " + enquryController.doEnqury());
							continue;
						} else if (choise.equals("4")) {
							transferController = new TransferController(fakeSession);
							System.out.println("Process finishes, result: " + transferController.doTransfer());
							continue;
						} else if (choise.equals("5")) {
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
							/*----------------------<Debug-------------------------*/
							clearLines();
							lines[1] = head + cardNum + tail;
							lines[2] = head + pin + tail;
							atmssHandler.doDisDisplayUpper(lines);
							sleep(3000);
							/*----------------------Debug>-------------------------*/
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
							while (true) {
								String userChoise = atmssHandler.doKPGetSingleInput(60);
								Session currentSession = getLastSession();
								if (userChoise.equals("1")) {

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
									depositController = new DepositController(currentSession);
									boolean isSuccess = depositController.doDeposit();
									LinkedList<Operation> processOperations = depositController.getOperationCache();
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
								} else if (userChoise.equals("CANCEL")) {
									clearLines();
									lines[1] = head + "Card ejected, please take your card" + tail;
									atmssHandler.doDisDisplayUpper(lines);

									if (!atmssHandler.doCREjectCard()) {
										clearLines();
										lines[1] = head + "Your card has been retained, please contact +852 51740740"
												+ tail;
										atmssHandler.doDisDisplayUpper(lines);
									}
									sleep(10000);
									break;
								}

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

	/**
	 *
	 */
	// TODO Singleton need to be implemented
	// public static MainController getInstance() { return self; }
	public MainController(AdvicePrinter AP, CardReader CR, CashDispenser CD, DepositCollector depositCollector,
			Display display, EnvelopDispenser envelopDispenser, Keypad KP, MBox AtmssMbox) {
		this._atmssMBox = AtmssMbox;
		this.isRunning = true;
		this.advicePrinterController = new AdvicePrinterController(AP);
		this.cardReaderController = new CardReaderController(CR);
		this.cashDispenserController = new CashDispenserController(CD);
		this.depositCollectorController = new DepositCollectorController(depositCollector);
		this.displayController = new DisplayController(display);
		this.envelopDispenserController = new EnvelopDispenserController(envelopDispenser);
		this.keypadController = new KeypadController(KP);
		this.serverCommunicator = new BAMSCommunicator();
		this.atmssHandler = ATMSSHandler.getHandler();
		this.atmssHandler.initHandler(cashDispenserController, cardReaderController, keypadController,
				depositCollectorController, advicePrinterController, displayController, envelopDispenserController,
				serverCommunicator);
		mainControllerMBox = new MBox("MainController");
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
				System.out.println(">>>>>>>>>>>>>Main controller is waiting for msg...");
				Msg msg = this.mainControllerMBox.receive();
				System.out.println(">>>>>>>>>>>>>Main controller receives: " + msg);
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
				default:
					break;
				}
			}

			try {
				/*-----------------<Debug----------------------*/
				this.atmssHandler.doDisClearUpper();
				String[] lines = { "", "Out of service!" };
				this.atmssHandler.doDisDisplayUpper(lines);
				/*-----------------Debug>----------------------*/
				waitForRepair();
				sleep(300);
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

	private void handleAPMsg(Msg msg) {
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleCRMsg(Msg msg) {
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleCDMsg(Msg msg) {
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
		if (msg.getType() % 100 != 0)
			handleFatalExceptions(msg);
	}

	private void handleFatalExceptions(Msg msg) {
		_atmssMBox.send(new Msg("MainController", msg.getType(), msg.getDetails()));
		this.isRunning = false;
		this.processor.processorPause();
	}

	private void initAll() // Initiate all for serving next guest
	{
		this.mainControllerMBox.clearBox();
		this.cardReaderController.initCR();
		this.isRunning = true;
		this.atmssHandler.doDisClearAll();
		processor.initProcessor();
	}

	private Session getLastSession() {
		return sessionLog.get(sessionLog.size() - 1);
	}

	private void waitForRepair() {
		Msg msg = null;
		int statusSum = 0;
		try {
			this.advicePrinterController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleAPMsg(msg);
			statusSum += msg.getType();
			this.cardReaderController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleCRMsg(msg);
			statusSum += msg.getType();
			this.cashDispenserController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleCDMsg(msg);
			statusSum += msg.getType();
			this.displayController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleDisMsg(msg);
			statusSum += msg.getType();
			this.keypadController.updateStatus();
			msg = this.mainControllerMBox.receive();
			handleKPMsg(msg);
			statusSum += msg.getType();
			if (statusSum % 100 == 0)
				initAll();
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
	private List<Session> sessionLog = new ArrayList<Session>();
	private ATMSSHandler atmssHandler;
	private MBox mainControllerMBox;
	private volatile boolean isRunning;
	private MBox _atmssMBox;
	// TODO Singleton need to be implemented
	// private static MainController self = new MainController();

}
