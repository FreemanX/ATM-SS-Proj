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
 * The Class CashDispenser.
 */
public class CashDispenser extends Thread implements EmulatorActions {

	/** The id. */
	private String id;

	/** The log. */
	private Logger log = null;

	/** The atmss. */
	private ATMSS atmss = null;

	/** The atmss m box. */
	private MBox atmssMBox = null;

	/** The view box. */
	private MBox viewBox = null;

	/** The text area. */
	private JTextArea textArea = null;

	/** The Constant type. */
	public final static int type = 3;

	/** The ready2 take. */
	private boolean ready2Take = false;

	/** The status. */
	private int status = 300;

	/** The num of100. */
	private int numOf100 = 100000;

	/** The num of500. */
	private int numOf500 = 100000;

	/** The num of1000. */
	private int numOf1000 = 100000;

	/** The eject num of100. */
	private int ejectNumOf100 = 0;

	/** The eject num of500. */
	private int ejectNumOf500 = 0;

	/** The eject num of1000. */
	private int ejectNumOf1000 = 0;

	/** The eject cash amount. */
	private int ejectCashAmount = 100 * ejectNumOf100 + 500 * ejectNumOf500 + 1000 * ejectNumOf1000;

	/** The my frame. */
	private MyFrame myFrame = null;

	/** The my panel. */
	private MyPanel myPanel = null;

	// ------------------------------------------------------------
	/**
	 * Instantiates a new cash dispenser.
	 *
	 * @param id the id
	 */
	// CashDispenser
	public CashDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		myFrame = new MyFrame("Cash Dispenser");
	} // CashDispenser

	/**
	 * Sets the view box.
	 *
	 * @param box the new view box
	 */
	public void setViewBox(MBox box) {
		this.viewBox = box;
	}

	/**
	 * Sets the num of100.
	 *
	 * @param amount the new num of100
	 */
	protected void setNumOf100(int amount) {
		this.numOf100 = amount;
	}

	/**
	 * Gets the num of100.
	 *
	 * @return the num of100
	 */
	public int getNumOf100() {
		return this.numOf100;
	}

	/**
	 * Sets the num of500.
	 *
	 * @param amount the new num of500
	 */
	protected void setNumOf500(int amount) {
		this.numOf500 = amount;
	}

	/**
	 * Gets the num of500.
	 *
	 * @return the num of500
	 */
	public int getNumOf500() {
		return this.numOf500;
	}

	/**
	 * Sets the num of1000.
	 *
	 * @param amount the new num of1000
	 */
	protected void setNumOf1000(int amount) {
		this.numOf1000 = amount;
	}

	/**
	 * Gets the num of1000.
	 *
	 * @return the num of1000
	 */
	public int getNumOf1000() {
		return this.numOf1000;
	}

	/**
	 * Update cash inventory.
	 *
	 * @param takenThisNumOf100 the taken this num of100
	 * @param takenThisNumOf500 the taken this num of500
	 * @param takenThisNumOf1000 the taken this num of1000
	 */
	private void updateCashInventory(int takenThisNumOf100, int takenThisNumOf500, int takenThisNumOf1000) {
		this.setNumOf100(this.numOf100 - takenThisNumOf100);
		this.setNumOf500(this.numOf500 - takenThisNumOf500);
		this.setNumOf1000(this.numOf1000 - takenThisNumOf1000);
	}

	/**
	 * Sets the cash amount.
	 */
	public void setCashAmount() {
		this.ejectCashAmount = 100 * ejectNumOf100 + 500 * ejectNumOf500 + 1000 * ejectNumOf1000;
	}

	/**
	 * Gets the cash amount.
	 *
	 * @return the cash amount
	 */
	public int getCashAmount() {
		return this.ejectCashAmount;
	}

	/**
	 * Reset eject cash amount.
	 */
	public void resetEjectCashAmount() {
		this.ready2Take = false;
		this.ejectNumOf100 = 0;
		this.ejectNumOf500 = 0;
		this.ejectNumOf1000 = 0;
		this.ejectCashAmount = 0;
	}

	/**
	 * Gets the CD status.
	 *
	 * @return the CD status
	 */
	public int getCDStatus() {
		return status;
	}

	/**
	 * Eject cash.
	 *
	 * @param ejectThisNumof100 the eject this numof100
	 * @param ejectThisNumof500 the eject this numof500
	 * @param ejectThisNumof1000 the eject this numof1000
	 * @return true, if successful
	 */
	public boolean ejectCash(int ejectThisNumof100, int ejectThisNumof500, int ejectThisNumof1000) {
		boolean isSuccessful = false;
		this.ejectNumOf100 = ejectThisNumof100;
		this.ejectNumOf500 = ejectThisNumof500;
		this.ejectNumOf1000 = ejectThisNumof1000;
		boolean isSufficient = this.numOf100 >= ejectThisNumof100 && this.numOf500 >= ejectThisNumof500
				&& this.numOf1000 >= ejectThisNumof1000;
		this.setCashAmount();
		if (isSufficient && ejectCashAmount > 0) {
			this.ready2Take = true;
			this.textArea.append("Please take " + ejectNumOf100 + " of 100HKD\n");
			this.textArea.append("Please take " + ejectNumOf500 + " of 500HKD\n");
			this.textArea.append("Please take " + ejectNumOf1000 + " of 1000HKD\n");
			this.textArea.append("Please take total" + ejectCashAmount + "HKD\n");
			isSuccessful = true;
		} else {
			this.textArea.append("Nothing to be ejected");
		}
		return isSuccessful;
	}

	/**
	 * Retain cash.
	 */
	public void retainCash() {
		this.textArea.append("\n Cash retained! \n");
		this.ready2Take = false;
		resetEjectCashAmount();
	}

	/**
	 * Sets the CD status.
	 *
	 * @param Status the new CD status
	 */
	protected void setCDStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 300) {
				atmssMBox.send(new Msg("300", 3, "normal"));
			}

			if (Status == 301) {
				numOf500 = 0; // For demo only
			}

			if (status == 398) {
				shutdown();
			}

			if (status == 399) {
				atmssMBox.send(new Msg("399", 3, "out of service"));
				fatalHalt();
			}
		}
	}

	// ------------------------------------------------------------
	/**
	 * Sets the atmss.
	 *
	 * @param newAtmss the new atmss
	 */
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		if (status != 398)
			setCDStatus(398);
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
		setCDStatus(300);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 3, "Restarted"));

		textArea.setText("");
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 399)
			setCDStatus(399);
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
	// MyFrame
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
			setLocation(UIManager.x + 900, UIManager.y + 280);
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

		/**
		 * Instantiates a new my panel.
		 */
		public MyPanel() {
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();
			add(msgPanel);
			add(buttonPanel);
		}

		/**
		 * Creates the button panel.
		 *
		 * @return the j panel
		 */
		private JPanel createButtonPanel() {
			JButton collectButton = new JButton("Collect Cash");
			JButton resetButton = new JButton("Clear");

			collectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (status <= 301) {
						if (ready2Take && ejectCashAmount > 0) {
							log.info(id + ": Sending \"Collect Cash\" amount: " + ejectCashAmount);
							atmssMBox.send(new Msg("Cash Dispenser", 3, "Dispense cash: " + ejectCashAmount));
							viewBox.send(new Msg(id, 3, "Cash taken"));
							updateCashInventory(ejectNumOf100, ejectNumOf500, ejectNumOf1000);
							textArea.append(ejectNumOf100 + " of 100HKD taken\n");
							textArea.append(ejectNumOf500 + " of 500HKD taken\n");
							textArea.append(ejectNumOf1000 + " of 1000HKD taken\n");
							textArea.append(ejectCashAmount + " HKD taken in all\n");
							resetEjectCashAmount();
						}
					} else {
						textArea.append("Nothing to take!\n");
					}
				}
			});

			resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					textArea.setText("");
				}
			});
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(collectButton);
			buttonPanel.add(resetButton);
			return buttonPanel;
		}

		/**
		 * Creates the msg panel.
		 *
		 * @return the j panel
		 */
		private JPanel createMsgPanel() {
			textArea = new JTextArea(6, 30);
			textArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(textArea);
			JPanel msgPanel = new JPanel();
			msgPanel.add(msgScrollPane);
			return msgPanel;
		}
	}
}
