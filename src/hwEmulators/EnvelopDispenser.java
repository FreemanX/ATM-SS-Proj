package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;


//======================================================================
// EnvelopDispenser
public class EnvelopDispenser extends Thread{	
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea msgTextArea = null;
	private JLabel msgLabel = null;
	
	// ------------------------------------------------------------
	// EnvelopDispenser
	public EnvelopDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		MyFrame myFrame = new MyFrame("Envelop Dispenser");
	} // EnvelopDispenser

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// ------------------------------------------------------------
	// MyFrame
	private class MyFrame extends JFrame {
		public static final long serialVersionUID = 1L;
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(960, 640);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(310, 380);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame
	
	private class MyPanel extends JPanel {
		public static final long serialVersionUID = 1L;
		// -----------------------------------------
		// MyPanel
		public MyPanel() {
			// create the panels
			JPanel buttonPanel = createButtonPanel();
			JPanel labelPanel = createLabelPanel();
			JPanel msgPanel = createMsgPanel();
						
			// add the panels
			add(buttonPanel);
			add(labelPanel);
			add(msgPanel);			
		}
			
		// ----------------------------------------
		// createButtonPanel
		private JPanel createButtonPanel() {
			// create the buttons
			JButton clearButton = new JButton("Clear");
			// assign actions to button
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					msgTextArea.setText("");
					msgLabel.setText("Working");
					
					log.info(id + ": Sending \"" +clearButton.getText() + "\"");										
					atmssMBox.send(new Msg("EnvelopDispenser", 7, clearButton.getText()));
									
				}
			});
				
			// create the panel and add the buttons to the panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(clearButton);
			return buttonPanel;
		} // createButtonPanel
			
		// ----------------------------------------
		// createLabelPanel
		private JPanel createLabelPanel() {
			JPanel labelPanel = new JPanel();
			
			// create the msg label
			msgLabel = new JLabel("Working");	
			labelPanel.add(msgLabel);
			return labelPanel;
		} // createLabelPanel
				
		
		// ----------------------------------------
		// createMsgPanel
		private JPanel createMsgPanel() {
			JPanel msgPanel = new JPanel();
			
			// create the msg text area
			msgTextArea = new JTextArea(15, 25);
			msgTextArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(msgTextArea);
			
			// create the msg panel and add the text area into it
			msgPanel.add(msgScrollPane);			
			msgTextArea.append("Preparing for ejecting...\n");										
			msgTextArea.append("Enjecting an envelop...\n");
			msgTextArea.append("Envelop ejected!\n");			
			
			return msgPanel;
		} // createMsgPanel
	} // MyPanel
} // EnvelopDispenser
