package hwEmulators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

//======================================================================
// ATMSS
public class ATMSS extends Thread {

	class HWFailureInfo {
		private int code;
		private int type;
		private String message;

		public HWFailureInfo(int type, int code, String message) {
			this.type = type;
			this.code = code;
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public int getCode() {
			return code;
		}

		public int getType() {
			return type;
		}

	}

	private String id;
	private Logger log = null;
	private MBox mbox = null;
	private ATMSSDBugConsole console = null;
	private NewExceptionEmulator newExEmu = null;

	// HW components
	private AdvicePrinter advicePrinter = null; // 1
	private CardReader cardReader = null; // 2
	private CashDispenser cashDispenser = null; // 3
	private DepositCollector depositCollector = null; // 4
	private Display display = null; // 5
	private EnvelopDispenser envelopDispenser = null; // 6
	private Keypad keypad = null; // 7

	private List<HWFailureInfo> failureInfos = new ArrayList<HWFailureInfo>();
	private List<int[]> skipList = Arrays.asList(new int[] { 400, 499 }, new int[] { 500, 599 }, new int[] { 600, 699 },
			new int[] { 301, 301 }); // add more here to skip bluescreen

	// ------------------------------------------------------------
	// ATMSS
	public ATMSS(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();
		mbox = new MBox(id);
		console = new ATMSSDBugConsole("con", this);
	} // ATMSS

	// ------------------------------------------------------------
	// setters
	public void setAdvicePrinter(AdvicePrinter ap) {
		advicePrinter = ap;
	}

	public void setCardReader(CardReader cr) {
		cardReader = cr;
	}

	public void setCashDispenser(CashDispenser cd) {
		cashDispenser = cd;
	}

	public void setDepositCollector(DepositCollector dc) {
		depositCollector = dc;
	}

	public void setDisplay(Display ds) {
		display = ds;
	}

	public void setEnvelopDispenser(EnvelopDispenser ed) {
		envelopDispenser = ed;
	}

	public void setKeypad(Keypad kp) {
		keypad = kp;
	}

	public void setNewExEmu(NewExceptionEmulator newExEmu) {
		this.newExEmu = newExEmu;
	}

	public void setHWStatus(int type, int statusCode) {
		switch (type) {
		case 1:
			advicePrinter.setAPStatus(statusCode);
			System.out.println(">>>>>>>>>AP changes to " + advicePrinter.getAPStatus());
			break;
		case 2:
			cardReader.setCRStatus(statusCode);
			System.out.println(">>>>>>>>>CR changes to " + cardReader.getCRStatus());
			break;
		case 3:
			cashDispenser.setCDStatus(statusCode);
			System.out.println(">>>>>>>>>cashDispenser changes to " + cashDispenser.getCDStatus());
			break;
		case 4:
			depositCollector.setDCStatus(statusCode);
			System.out.println(">>>>>>>>>depositCollector changes to " + depositCollector.getDCStatus());
			break;
		case 5:
			display.setDisStatus(statusCode);
			System.out.println(">>>>>>>>>display changes to " + display.getDisStatus());
			break;
		case 6:
			envelopDispenser.setEDStatus(statusCode);
			System.out.println(">>>>>>>>>envelopDispenser changes to " + envelopDispenser.getEDStatus());
			break;
		case 7:
			keypad.setKPStatus(statusCode);
			System.out.println(">>>>>>>>>keypad changes to " + keypad.getKPStatus());
			break;
		default:
			break;
		}
	}

	// ------------------------------------------------------------
	// getters
	public MBox getMBox() {
		return mbox;
	}

	// ------------------------------------------------------------
	// run
	public void run() {
		while (true) {
			Msg msg = mbox.receiveTemp();
			if (msg.getType() % 100 != 0)
				console.println(id + " received " + msg);
			// System.err.println(id + " received, sender: " + msg.getSender() +
			// ", type: " + msg.getType() + ", details: " + msg.getDetails());

			if (msg.getSender().equalsIgnoreCase("NewExceptionEmulator")) {
				handleExceptionEmu(msg);
			} else if (msg.getDetails().equals("Restarted")) {
				handleComponentRestarted(msg);
			}

			if (msg.getSender().equalsIgnoreCase("MainController")) {
				int type = (int) msg.getType() / 100;
				int code = msg.getType();
				boolean toDo = true;

//				System.out.println("From MainController >> code: " + code + ", details: " + msg.getDetails());
				for (int[] arr : skipList) {
					if (code >= arr[0] && code <= arr[1]) {
						toDo = false;
					}
				}
				if (toDo) {
					if (code % 100 == 0) { // normal
						removeFailure(type);
					} else { // not normal
						putFailure(new HWFailureInfo(type, code, msg.getDetails()));
					}
					if (code == 0) {
						for (int i = 1; i < 8; i++) {
							removeFailure(i);
						}
					}
					if (failureInfos.size() == 0 && code == 0) {
						if (display.getDisStatus() % 100 == 0)
							display.quitBlueScreen();
					}
					if (failureInfos.size() > 0) {
						if (display.getDisStatus() % 100 == 0)
							display.setBlueScreen(failureInfos);
					}
				}
			}
		}
	} // run

	private void putFailure(HWFailureInfo newInfo) { // replace/add failure info
		removeFailure(newInfo.getType());

		// System.out.println("Adding " + newInfo.getType());
		failureInfos.add(newInfo);

		sortList();
		// System.out.println("Put: " + failureInfos.size());
	}

	private void removeFailure(int type) {
		List<HWFailureInfo> removeCandidate = new ArrayList<HWFailureInfo>();

		// find candidates
		for (HWFailureInfo info : failureInfos) {
			if (info.getType() == type) {
				removeCandidate.add(info);
			}
		}
		// remove
		for (HWFailureInfo candidate : removeCandidate) {
			// System.out.println("Removing " + candidate.getType());
			failureInfos.remove(candidate);
		}
	}

	private void sortList() {
		List<HWFailureInfo> result = new ArrayList<HWFailureInfo>();

		while (failureInfos.size() > 0) {
			result.add(0, popMaxType());
		}

		failureInfos = result;
	}

	private HWFailureInfo popMaxType() {
		HWFailureInfo maxTypeInfo = new HWFailureInfo(0, 0, "");

		for (HWFailureInfo info : failureInfos) {
			if (info.getType() > maxTypeInfo.getType())
				maxTypeInfo = info;
		}

		failureInfos.remove(maxTypeInfo);
		return maxTypeInfo;
	}

	private void handleExceptionEmu(Msg m) {
		// 1 - ap, 2 - cr, 3 - cd, 4 - dc, 5 - dis, 6 - ed, 7 - kp
		if (m.getType() == 1) { // AdvicePrinter
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				advicePrinter.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				advicePrinter.restart();
			}
		}

		if (m.getType() == 2) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				cardReader.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				cardReader.restart();
			}
		}

		if (m.getType() == 3) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				cashDispenser.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				cashDispenser.restart();
			}
		}

		if (m.getType() == 4) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				depositCollector.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				depositCollector.restart();
			}
		}

		if (m.getType() == 5) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				display.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				display.restart();
			}
		}

		if (m.getType() == 6) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				envelopDispenser.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				envelopDispenser.restart();
			}
		}

		if (m.getType() == 7) {
			if (m.getDetails().equalsIgnoreCase("shutdown")) {
				keypad.shutdown();
			}
			if (m.getDetails().equalsIgnoreCase("restart")) {
				keypad.restart();
			}
		}

	}

	private void handleComponentRestarted(Msg m) {
		System.out.println("MSG:" + m);
		newExEmu.componentRestarted(m.getType());
	}
} // ATMSS
