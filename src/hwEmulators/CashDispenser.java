package hwEmulators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CashDispenser extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea textArea = null;
	public final static int type = 3;
	private int status = 300;

	// ------------------------------------------------------------
	// CashDispenser
	public CashDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		MyFrame myFrame = new MyFrame("Cash Dispenser");
	} // CashDispenser

	public int getCDStatus() {
		return status;
	}

	protected void setCDStatus(int Status) {
		this.status = Status;
	}

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x + 900, UIManager.y + 280);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 200);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	private class MyPanel extends JPanel {
		public MyPanel() {
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();
			add(msgPanel);
			add(buttonPanel);
		}

		private JPanel createButtonPanel() {
			JButton collectButton = new JButton("Collect Cash");
			collectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					log.info(id + ": Sending \"Collect Cash\"");
					atmssMBox.send(new Msg("Cash Dispenser", 3, "Dispense cash"));
					textArea.setText("Yeah! Cash collected!");
				}
			});
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(collectButton);
			return buttonPanel;
		}

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
