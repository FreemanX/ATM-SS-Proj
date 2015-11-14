package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

//======================================================================
// CardReader
public class CardReader extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextField textField = null;
	private JTextArea msgTextArea = null;
	public final static int type = 2;
	private int status = 200;
	private final String Card1 = "981358459216";
	private final String Card2 = "981370846450";
	private final String Card3 = "Joker from poker";
	private String cardToSend = "";
	private boolean waitForTaken = false;

	// ------------------------------------------------------------
	// CardReader
	public CardReader(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		MyFrame myFrame = new MyFrame("Card Reader");
	} // CardReader

	public void ejectCard() {
		waitForTaken = true;
		msgTextArea.append("Card" + this.cardToSend + " Ejected, please take the card/n");
		log.info(id + ": Ejecting " + this.cardToSend);
		atmssMBox.send(new Msg("CardReader", 2, textField.getText()));
	}

	public void eatCard() {
		waitForTaken = false;
		msgTextArea.append("Card retained/n");
		log.info(id + ": Retain " + this.cardToSend);
		atmssMBox.send(new Msg("CardReader", 2, ": Retain " + this.cardToSend));
	}

	public String getCard() {
		return cardToSend;
	}

	public int getCRStatus() {
		return status;
	}

	protected void setCRStatus(int Status) {
		this.status = Status;
	}

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// ------------------------------------------------------------
	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x + 900, UIManager.y);
			MyPanel myPanel = new MyPanel();
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
						msgTextArea.append("Card Ejected\n");
						atmssMBox.send(new Msg("CardReader", CardReader.type, "Eject card: " + cardToSend));
						log.info(id + ": Ejecting " + cardToSend);
						cardToSend = "";
					} else {
						msgTextArea.append("Nothing to take!/n");
						atmssMBox.send(new Msg("CardReader", 2, "Nothing to take!/n"));
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
