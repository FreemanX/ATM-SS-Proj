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

	// ------------------------------------------------------------
	// CardReader
	public CardReader(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		MyFrame myFrame = new MyFrame("Card Reader");
	} // CardReader

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
			setLocation(0, 100);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(300, 385);
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
					textField.setText("6-8-9");
				}
			});
			card2Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText("9-8-6-7-D");
				}
			});
			card3Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText("GNUELYC");
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
			JButton resetButton = new JButton("Reset");

			// assign actions to buttons
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					log.info(id + ": Sending " + textField.getText());
					msgTextArea.append("Sending " + textField.getText() + "\n");
					atmssMBox.send(new Msg("CardReader", 1, textField.getText()));
				}
			});
			resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textField.setText("");
				}
			});

			// create sendResetPanel and add the two buttons into it
			JPanel sendResetPanel = new JPanel();
			sendResetPanel.add(sendButton);
			sendResetPanel.add(resetButton);
			return sendResetPanel;
		} // createSendResetPanel

		// ----------------------------------------
		// createMsgPanel
		private JPanel createMsgPanel() {
			// create the msg text area
			msgTextArea = new JTextArea(15, 25);
			msgTextArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(msgTextArea);

			// create the msg panel and add the text area into it
			JPanel msgPanel = new JPanel();
			msgPanel.add(msgScrollPane);
			return msgPanel;
		} // createMsgPanel
	} // MyPanel
} // CardReader
