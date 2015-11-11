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
	private JTextArea msgTextArea = null;
	public final static int type = 4;
	private int status = 400;
	private boolean slotIsOpen = false;
	private boolean hasEnvelop = false;

	public DepositCollector(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		log = ATMKickstarter.getLogger();

		MyFrame myFrame = new MyFrame("Deposit Collector\n");
	}

	public void openSlot() {
		this.msgTextArea.append("Slop opens, waiting for envelop...\n");
		this.slotIsOpen = true;
	}

	public boolean getHasEnvelop() {
		return this.hasEnvelop;
	}

	protected void setHasEnvelop(boolean has) {
		this.hasEnvelop = has;
	}

	public void closeSlot(boolean isTimeout) {
		if (isTimeout)
			this.msgTextArea.append("Time out! Slop closes, nothing input\n");
		else
			this.msgTextArea.append("Slop closes, envelop received\n");
		setHasEnvelop(false);
		this.slotIsOpen = false;
	}

	public boolean checkSlot() {
		return this.slotIsOpen;
	}

	public int getDCStatus() {
		return status;
	}

	protected void setDCStatus(int Status) {
		this.status = Status;
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
			JButton clearBtn = new JButton("Clear");

			putInBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (slotIsOpen) {
						log.info(id + ": Sending \"Put in envelop\"");
						atmssMBox.send(new Msg("Deposit collector", 4, "Put in envelop"));
						setHasEnvelop(true);
					}
				}
			});

			clearBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					msgTextArea.setText("");
				}
			});

			buttonPanel.add(putInBtn);
			buttonPanel.add(clearBtn);
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
