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
 * The Class CardReader.
 */
// CardReader
public class CardReader extends Thread implements EmulatorActions {

	/** The id. */
	private String id;

	/** The log. */
	private Logger log = null;

	/** The atmss. */
	private ATMSS atmss = null;

	/** The atmss m box. */
	private MBox atmssMBox = null;

	/** The _cr view m box. */
	private MBox _crViewMBox = null;

	/** The text field. */
	private JTextField textField = null;

	/** The msg text area. */
	private JTextArea msgTextArea = null;

	/** The Constant type. */
	public final static int type = 2;

	/** The status. */
	private int status = 200;

	/** The Card1. */
	private final String Card1 = "981358459216";

	/** The Card2. */
	private final String Card2 = "981370846450";

	/** The Card3. */
	private final String Card3 = "Joker from poker";

	/** The card to send. */
	private String cardToSend = "";

	/** The wait for taken. */
	private boolean waitForTaken = false;

	/** The my frame. */
	private JFrame myFrame = null;

	/** The my panel. */
	private MyPanel myPanel = null;

	// ------------------------------------------------------------
	/**
	 * Instantiates a new card reader.
	 *
	 * @param id the id
	 */
	// CardReader
	public CardReader(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		myFrame = new MyFrame("Card Reader");
	} // CardReader

	/**
	 * Eject card.
	 */
	public void ejectCard() {
		waitForTaken = true;
		msgTextArea.append("Card" + this.cardToSend + " Ejected, please take the card\n");
		log.info(id + ": Ejecting " + this.cardToSend);
		atmssMBox.send(new Msg("CardReader", 2, textField.getText()));
	}

	/**
	 * Sets the CR view box.
	 *
	 * @param CRViewMBox the new CR view box
	 */
	public void setCRViewBox(MBox CRViewMBox) {
		this._crViewMBox = CRViewMBox;
	}

	/**
	 * Eat card.
	 */
	public void eatCard() {
		waitForTaken = false;
		msgTextArea.append("Card retained\n");
		log.info(id + ": Retain " + this.cardToSend);
		cardToSend = "";
		atmssMBox.send(new Msg("CardReader", 2, ": Retain " + this.cardToSend));
	}

	/**
	 * Gets the card.
	 *
	 * @return the card
	 */
	public String getCard() {
		return cardToSend;
	}

	/**
	 * Gets the CR status.
	 *
	 * @return the CR status
	 */
	public int getCRStatus() {
		return status;
	}

	/**
	 * Sets the CR status.
	 *
	 * @param Status the new CR status
	 */
	protected void setCRStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 200) {
				atmssMBox.send(new Msg("200", 2, "normal"));
			}

			if (status == 298) {
				shutdown();
			}

			if (status == 299) {
				atmssMBox.send(new Msg("299", 2, "out of service"));
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
		if (status != 298)
			setCRStatus(298);
		setUIEnable(false, true);
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#restart()
	 */
	@Override
	public void restart() {
		shutdown();
		// reset all stuffs
		long ms = new Random(new Date().getTime()).nextInt(1500) + 200; // 200 - 1700
		try {
			sleep(ms);
		} catch (InterruptedException e) {
		}
		setCRStatus(200);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 2, "Restarted"));

		msgTextArea.setText("");
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 299)
			setCRStatus(299);
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

	// ------------------------------------------------------------
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
			setLocation(UIManager.x + 900, UIManager.y);
			myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 280);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	// ------------------------------------------------------------
	/**
	 * The Class MyPanel.
	 */
	// MyPanel
	private class MyPanel extends JPanel {
		// ----------------------------------------
		/**
		 * Instantiates a new my panel.
		 */
		// MyPanel
		public MyPanel() {
			// create the panels
			JPanel buttonPanel = createButtonPanel();
			JPanel cardNumPanel = createCardNumPanel();
			JPanel sendResetPanel = createSendResetPanel();
			JPanel msgPanel = createMsgPanel();

			// add the panels
			add(buttonPanel);
			add(cardNumPanel);
			add(sendResetPanel);
			add(msgPanel);
		} // MyPanel

		// ----------------------------------------
		/**
		 * Creates the button panel.
		 *
		 * @return the j panel
		 */
		// createButtonPanel
		private JPanel createButtonPanel() {
			// create the buttons
			JButton card1Button = new JButton("Card 1");
			JButton card2Button = new JButton("Card 2");
			JButton card3Button = new JButton("Card 3");

			// assign actions to buttons
			card1Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText(Card1);
				}
			});
			card2Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText(Card2);
				}
			});
			card3Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText(Card3);
				}
			});

			// create the panel and add the buttons to the panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(card1Button);
			buttonPanel.add(card2Button);
			buttonPanel.add(card3Button);
			return buttonPanel;
		} // createButtonPanel

		// ----------------------------------------
		/**
		 * Creates the card num panel.
		 *
		 * @return the j panel
		 */
		// createCardNumPanel
		private JPanel createCardNumPanel() {
			// create the label and the card num text field
			JLabel label = new JLabel("Card Number:");
			textField = new JTextField(15);

			// create cardNumPanel and add the label and the text field into it
			JPanel cardNumPanel = new JPanel();
			cardNumPanel.add(label);
			cardNumPanel.add(textField);
			return cardNumPanel;
		} // createCardNumPanel

		// ----------------------------------------
		/**
		 * Creates the send reset panel.
		 *
		 * @return the j panel
		 */
		// createSendResetPanel
		private JPanel createSendResetPanel() {
			// create the two buttons
			JButton sendButton = new JButton("Send to ATMSS");
			JButton takeButton = new JButton("Take Card");
			JButton resetButton = new JButton("Clear");

			// assign actions to buttons
			sendButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					cardToSend = textField.getText();
					if (cardToSend.length() > 0) {
						log.info(id + ": Sending " + textField.getText());
						atmssMBox.send(new Msg("CardReader", 2, textField.getText()));
						_crViewMBox.send(new Msg("CardReader", 2, textField.getText()));
						msgTextArea.append("Sending " + textField.getText() + "\n");
					} else {
						msgTextArea.append("Please insert a card!\n");
					}
				}
			});

			takeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (waitForTaken && cardToSend.length() > 0) {
						textField.setText("");
						msgTextArea.append("Card taken\n");
						atmssMBox.send(new Msg("CardReader", CardReader.type, "Eject card: " + cardToSend));
						_crViewMBox.send(new Msg("CardReader", CardReader.type, "Card taken " + cardToSend));
						log.info(id + ": Ejecting " + cardToSend);
						cardToSend = "";
						waitForTaken = false;
					} else {
						msgTextArea.append("Nothing to take!\n");
						atmssMBox.send(new Msg("CardReader", 2, "Nothing to take!\n"));
					}

				}
			});
			resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					msgTextArea.setText("");
					textField.setText("");
				}
			});

			// create sendResetPanel and add the two buttons into it
			JPanel sendResetPanel = new JPanel();
			sendResetPanel.add(sendButton);
			sendResetPanel.add(takeButton);
			sendResetPanel.add(resetButton);
			return sendResetPanel;
		} // createSendResetPanel

		// ----------------------------------------
		/**
		 * Creates the msg panel.
		 *
		 * @return the j panel
		 */
		// createMsgPanel
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

	} // MyPanel

} // CardReader
