package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
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

public class DepositCollector extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextField textField = null;
	private JTextArea msgTextArea = null;
	private boolean enable = true;
	public final static int type = 4;
	private int status = 400;

	public DepositCollector(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		log = ATMKickstarter.getLogger();

		MyFrame myFrame = new MyFrame("Deposit Collector");
	}

	public int getDCStatus() {
		return status;
	}

	protected void setDCStatus(int Status) {
		this.status = Status;
	}

	public void setDepositCollectorEnable(boolean isEnable) {
		this.enable = isEnable;
	}

	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y + 200);
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
		// ----------------------------------------
		// MyPanel
		public MyPanel() {
			// create the panels
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();

			// add the panels
			add(msgPanel);
			add(buttonPanel);
		} // MyPanel

		private JPanel createButtonPanel() {
			JPanel buttonPanel = new JPanel();
			JButton putInBtn = new JButton("Put in envelop");
			putInBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \"Put in envelop\"");
					atmssMBox.send(new Msg("Deposit collector", 4, "Put in envelop"));
				}
			});

			buttonPanel.add(putInBtn);
			return buttonPanel;
		}

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
