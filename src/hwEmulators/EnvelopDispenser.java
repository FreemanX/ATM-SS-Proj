package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class EnvelopDispenser.
 */
// EnvelopDispenser
public class EnvelopDispenser extends Thread implements EmulatorActions {
	
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
	public final static int type = 6;
	
	/** The status. */
	private int status = 600;
	
	/** The num of envelop. */
	private int numOfEnvelop = 10000;
	
	/** The my frame. */
	private MyFrame myFrame = null;
	
	/** The my panel. */
	private MyPanel myPanel = null;

	// ------------------------------------- -----------------------
	/**
	 * Instantiates a new envelop dispenser.
	 *
	 * @param id the id
	 */
	// EnvelopDispenser
	public EnvelopDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		myFrame = new MyFrame("Envelop Dispenser");
	} // EnvelopDispenser

	/**
	 * Gets the ED status.
	 *
	 * @return the ED status
	 */
	public int getEDStatus() {
		return status;
	}

	/**
	 * Sets the ED status.
	 *
	 * @param Status the new ED status
	 */
	protected void setEDStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 600) {
				atmssMBox.send(new Msg("600", 6, "normal"));
			}

			if (status == 601) {
				numOfEnvelop = 0;
			}

			if (status == 600) {
				numOfEnvelop = 10000;
			}

			if (status == 698) {
				shutdown();
			}

			if (status == 699) {
				atmssMBox.send(new Msg("699", 6, "out of service"));
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

	/**
	 * Eject envelop.
	 *
	 * @return true, if successful
	 */
	public boolean ejectEnvelop() {
		if (getEnvelopCount() > 0) {
			msgTextArea.append("Preparing for ejecting...\n");
			msgTextArea.append("Enjecting an envelop...\n");
			msgTextArea.append("Envelop ejected!\n");
			numOfEnvelop--;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the envelop count.
	 *
	 * @return the envelop count
	 */
	public int getEnvelopCount() {
		if (numOfEnvelop < 1) {
			this.status = 601;
			msgTextArea.append("There's no envelop!");
		}
		return numOfEnvelop;
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		if (status != 698)
			setEDStatus(698);
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
		setEDStatus(600);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 6, "Restarted"));

		msgTextArea.setText("");
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 699)
			setEDStatus(699);
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
	 * @param isEnable the is enable
	 * @param isShutdown the is shutdown
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

	// ------------------------------------------------------------
	/**
	 * The Class MyFrame.
	 */
	// MyFrame
	private class MyFrame extends JFrame {
		
		/** The Constant serialVersionUID. */
		public static final long serialVersionUID = 1L;

		// ----------------------------------------
		/**
		 * Instantiates a new my frame.
		 *
		 * @param title the title
		 */
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y);
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
		
		/** The Constant serialVersionUID. */
		public static final long serialVersionUID = 1L;

		// -----------------------------------------
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
		}

		// ----------------------------------------
		/**
		 * Creates the button panel.
		 *
		 * @return the j panel
		 */
		// createButtonPanel
		private JPanel createButtonPanel() {
			// create the buttons
			JButton clearButton = new JButton("Clear");
			// assign actions to button
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					msgTextArea.setText("");
					atmssMBox.send(new Msg("EnvelopDispenser", 6, clearButton.getText()));
				}
			});

			// create the panel and add the buttons to the panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(clearButton);
			return buttonPanel;
		} // createButtonPanel

		// ----------------------------------------
		/**
		 * Creates the msg panel.
		 *
		 * @return the j panel
		 */
		// createMsgPanel
		private JPanel createMsgPanel() {
			JPanel msgPanel = new JPanel();

			// create the msg text area
			msgTextArea = new JTextArea(6, 30);
			msgTextArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(msgTextArea);
			msgPanel.add(msgScrollPane);
			return msgPanel;
		} // createMsgPanel
	} // MyPanel
} // EnvelopDispenser
