package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DepositCollector.
 */
public class DepositCollector extends Thread implements EmulatorActions {

	/** The view mbox. */
	private MBox viewMbox; // used to notify the DepositCollectorView

	/** The id. */
	private String id;

	/** The log. */
	private Logger log = null;

	/** The atmss. */
	private ATMSS atmss = null;

	/** The atmss m box. */
	private MBox atmssMBox = null;

	/** The msg text area. */
	private JTextArea msgTextArea = null;

	/** The Constant type. */
	public final static int type = 4;

	/** The status. */
	private int status = 400;

	/** The slot is open. */
	private boolean slotIsOpen = false;

	/** The has envelop. */
	private boolean hasEnvelop = false;

	/** The my frame. */
	private MyFrame myFrame = null;

	/** The my panel. */
	private MyPanel myPanel = null;

	/**
	 * Instantiates a new deposit collector.
	 *
	 * @param id the id
	 */
	public DepositCollector(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		log = ATMKickstarter.getLogger();

		myFrame = new MyFrame("Deposit Collector\n");
	}

	/**
	 * Open slot.
	 */
	public void openSlot() {
		this.msgTextArea.append("Slot opens, waiting for envelop...\n");
		this.slotIsOpen = true;
	}

	/**
	 * Gets the checks for envelop.
	 *
	 * @return the checks for envelop
	 */
	public boolean getHasEnvelop() {
		return this.hasEnvelop;
	}

	/**
	 * Sets the checks for envelop.
	 *
	 * @param has the new checks for envelop
	 */
	protected void setHasEnvelop(boolean has) {
		this.hasEnvelop = has;
	}

	/**
	 * Sets the m box.
	 *
	 * @param viewMbox the new m box
	 */
	public void setMBox(MBox viewMbox) {
		this.viewMbox = viewMbox;
	}

	/**
	 * Close slot.
	 *
	 * @param isTimeout the is timeout
	 */
	public void closeSlot(boolean isTimeout) {
		if (isTimeout)
			this.msgTextArea.append("Time out! Slop closes, nothing input\n");
		else
			this.msgTextArea.append("Slot closes, envelop received\n");
		setHasEnvelop(false);
		this.slotIsOpen = false;
	}

	/**
	 * Checks if is slot open.
	 *
	 * @return true, if is slot open
	 */
	public boolean isSlotOpen() {
		return this.slotIsOpen;
	}

	/**
	 * Gets the DC status.
	 *
	 * @return the DC status
	 */
	public int getDCStatus() {
		return status;
	}

	/**
	 * Sets the DC status.
	 *
	 * @param Status the new DC status
	 */
	protected void setDCStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 400) {
				atmssMBox.send(new Msg("400", 4, "normal"));
			}

			if (Status == 498) {
				shutdown();
			}

			if (status == 499) {
				atmssMBox.send(new Msg("499", 4, "out of service"));
				fatalHalt();
			}
		}
	}

	/**
	 * Sets the atmss.
	 *
	 * @param newAtmss the new atmss
	 */
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		if (status != 498)
			setDCStatus(498);
		setUIEnable(false, true);
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#restart()
	 */
	@Override
	public void restart() {
		shutdown();
		long ms = new Random(new Date().getTime()).nextInt(1500) + 200; // 200 - 1700
		try {
			sleep(ms);
		} catch (InterruptedException e) {
		}
		setDCStatus(400);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 4, "Restarted"));

		msgTextArea.setText("");
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 499)
			setDCStatus(499);
		setUIEnable(false, false);
	}

	/**
	 * Sets the UI enable.
	 *
	 * @param isEnable the new UI enable
	 */
	private void setUIEnable(boolean isEnable) {
		setUIEnable(isEnable, true);
	}

	/**
	 * Sets the ui enable.
	 *
	 * @param isEnable should the UI enable?
	 * @param isShutdown should the UI shutdown?
	 */
	private void setUIEnable(boolean isEnable, boolean isShutdown) {
		String msg = "";
		Color screenColor = Color.RED;

		if (!isEnable) { // disable the UI
			if (isShutdown) {
				msg = "Shutdown";
				screenColor = Color.GRAY;
			} else {
				msg = "Fatal halt";
			}

			myFrame.getContentPane().removeAll(); // remove existing content

			// add new panel
			JPanel panel = new JPanel(new GridBagLayout());
			JLabel label = new JLabel(msg);
			label.setForeground(Color.WHITE);
			panel.setBackground(screenColor);
			panel.add(label, new GridBagConstraints());
			myFrame.getContentPane().add(panel);

			myFrame.getContentPane().revalidate();
			myFrame.getContentPane().repaint();
		} else {
			System.out.println("Enabling frame");
			myFrame.getContentPane().removeAll(); // remove existing content

			myFrame.getContentPane().add(myPanel);

			myFrame.getContentPane().revalidate();
			myFrame.getContentPane().repaint();
		}
	}

	/**
	 * The Class MyFrame.
	 */
	private class MyFrame extends JFrame {
		// ----------------------------------------
		/**
		 * Instantiates a new my frame.
		 *
		 * @param title the title
		 */
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y + 200);
			myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 200);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	/**
	 * The Class MyPanel.
	 */
	private class MyPanel extends JPanel {
		// ----------------------------------------
		/**
		 * Instantiates a new my panel.
		 */
		// MyPanel
		public MyPanel() {
			// create the panels
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();

			// add the panels
			add(msgPanel);
			add(buttonPanel);
		} // MyPanel

		/**
		 * Creates the button panel.
		 *
		 * @return the j panel
		 */
		private JPanel createButtonPanel() {
			JPanel buttonPanel = new JPanel();
			JButton putInBtn = new JButton("Put in envelop");
			JButton clearBtn = new JButton("Clear");

			putInBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (slotIsOpen) {
						log.info(id + ": Sending \"Put in envelop\"");
						setHasEnvelop(true);
						atmssMBox.send(new Msg("Deposit collector", 4, "Put in envelop"));
						msgTextArea.setText(msgTextArea.getText() + "Envelop put in, waiting atmss.\n");
						viewMbox.send(new Msg("DepositCollector", 4, "Put in envelop"));
					} else {
						msgTextArea.setText(msgTextArea.getText() + "Failed! Slot not open.\n");
					}
				}
			});

			clearBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					msgTextArea.setText("");
				}
			});

			buttonPanel.add(putInBtn);
			buttonPanel.add(clearBtn);
			return buttonPanel;
		}

		/**
		 * Creates the msg panel.
		 *
		 * @return the j panel
		 */
		private JPanel createMsgPanel() {
			// create the msg text area
			msgTextArea = new JTextArea(6, 30);
			msgTextArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(msgTextArea);

			// create the msg panel and add the text area into it
			JPanel msgPanel = new JPanel();
			msgPanel.add(msgScrollPane);
			return msgPanel;
		} // createMsgPanel

	}

}
