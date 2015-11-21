package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

//======================================================================
// CardReader
public class CardReader extends Thread implements EmulatorActions {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private MBox _crViewMBox = null;
	private JTextField textField = null;
	private JTextArea msgTextArea = null;
	public final static int type = 2;
	private int status = 200;
	private final String Card1 = "981358459216";
	private final String Card2 = "981370846450";
	private final String Card3 = "Joker from poker";
	private String cardToSend = "";
	private boolean waitForTaken = false;
	private JFrame myFrame = null;
	private MyPanel myPanel = null;

	// ------------------------------------------------------------
	// CardReader
	public CardReader(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		myFrame = new MyFrame("Card Reader");
	} // CardReader

	public void ejectCard() {
		waitForTaken = true;
		msgTextArea.append("Card" + this.cardToSend + " Ejected, please take the card\n");
		log.info(id + ": Ejecting " + this.cardToSend);
		atmssMBox.send(new Msg("CardReader", 2, textField.getText()));
	}

	public void setCRViewBox(MBox CRViewMBox) {
		this._crViewMBox = CRViewMBox;
	}

	public void eatCard() {
		waitForTaken = false;
		msgTextArea.append("Card retained\n");
		log.info(id + ": Retain " + this.cardToSend);
		cardToSend = "";
		atmssMBox.send(new Msg("CardReader", 2, ": Retain " + this.cardToSend));
	}

	public String getCard() {
		return cardToSend;
	}

	public int getCRStatus() {
		return status;
	}

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
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	@Override
	public void shutdown() {
		if (status != 298)
			setCRStatus(298);
		setUIEnable(false, true);
	}

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

	@Override
	public void fatalHalt() {
		if (status != 299)
			setCRStatus(299);
		setUIEnable(false, false);
	}

	private void setUIEnable(boolean isEnable) {
		setUIEnable(isEnable, true);
	}

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
	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
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
	// MyPanel
	private class MyPanel extends JPanel {
		// ----------------------------------------
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
