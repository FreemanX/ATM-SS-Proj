package hwEmulators;

import java.util.logging.Logger;
import java.awt.GridLayout;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JPanel;


//======================================================================
// EnvelopDispenser
public class EnvelopDispenser extends Thread{
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	//private JTextArea msgTextArea = null;
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
			myPanel.setLayout(new GridLayout(2,1));
			add(myPanel);
			pack();
			setSize(200, 75);
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
//			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();
			
			// add the panels
//			add(buttonPanel);
			add(msgPanel);
		}
			
		// ----------------------------------------
		// createButtonPanel
//		private JPanel createButtonPanel() {
//			// create the buttons
//			JButton enjectButton = new JButton("Eject Envelop");
//			// assign actions to button
//			enjectButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent event) {
//					log.info(id + ": Sending \"" +enjectButton.getText() + "\"");
//					//msgTextArea.append("Preparing for ejecting...\n");
//					msgLabel.setText("Preparing for ejecting...");
//					atmssMBox.send(new Msg("EnvelopDispenser", 7, enjectButton.getText()));
//					
//					//msgTextArea.append("Enjecting an envelop...\n");
//					msgLabel.setText("Enjecting an envelop...");
//					
//					//msgTextArea.append("Envelop ejected!\n");
//					msgLabel.setText("Envelop ejected!");
//					
//					//msgTextArea.append("Working\n");
//					msgLabel.setText("Working");				
//				}
//			});
//				
//			// create the panel and add the buttons to the panel
//			JPanel buttonPanel = new JPanel();
//			buttonPanel.add(enjectButton);
//
//			// create the panel and add the buttons to the panel
//			return buttonPanel;
//		} // createButtonPanel
			
		
		// ----------------------------------------
		// createMsgPanel
		private JPanel createMsgPanel() {
			// create the msg text area
			//msgTextArea = new JTextArea(15, 25);
			//msgTextArea.setEditable(false);
			//JScrollPane msgScrollPane = new JScrollPane(msgTextArea);
			
			msgLabel = new JLabel();
			
			// create the msg panel and add the text area into it
			JPanel msgPanel = new JPanel();
			//msgPanel.add(msgScrollPane);	
			msgPanel.add(msgLabel);
			msgLabel.setText("Working");
			//msgTextArea.append("Working\n");
			return msgPanel;
		} // createMsgPanel
	} // MyPanel
} // EnvelopDispenser
