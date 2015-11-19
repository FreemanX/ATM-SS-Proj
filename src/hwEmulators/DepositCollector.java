package hwEmulators;

import java.awt.*;
import java.util.Date;
import java.util.Random;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepositCollector extends Thread implements EmulatorActions {

	private MBox viewMbox; // used to notify the DepositCollectorView

	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea msgTextArea = null;
	public final static int type = 4;
	private int status = 400;
	private boolean slotIsOpen = false;
	private boolean hasEnvelop = false;
	private MyFrame myFrame = null;
	private MyPanel myPanel = null;

	public DepositCollector(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		log = ATMKickstarter.getLogger();

		myFrame = new MyFrame("Deposit Collector\n");
	}

	public void openSlot() {
		this.msgTextArea.append("Slot opens, waiting for envelop...\n");
		this.slotIsOpen = true;
	}

	public boolean getHasEnvelop() {
		return this.hasEnvelop;
	}

	protected void setHasEnvelop(boolean has) {
		this.hasEnvelop = has;
	}

	public void setMBox(MBox viewMbox) {
		this.viewMbox = viewMbox;
	}

	public void closeSlot(boolean isTimeout) {
		if (isTimeout)
			this.msgTextArea.append("Time out! Slop closes, nothing input\n");
		else
			this.msgTextArea.append("Slot closes, envelop received\n");
		setHasEnvelop(false);
		this.slotIsOpen = false;
	}

	public boolean isSlotOpen() {
		return this.slotIsOpen;
	}

	public int getDCStatus() {
		return status;
	}

	protected void setDCStatus(int Status) {
		this.status = Status;

		if (Status == 498) {
			shutdown();
		}

		if (status == 499) {
			fatalHalt();
		}
	}

	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	@Override
	public void shutdown() {
		if (status != 498)
			setDCStatus(498);
		setUIEnable(false, true);
	}

	@Override
	public void restart() {
		shutdown();
		long ms = new Random(new Date().getTime()).nextInt(1500) + 200; // 200 - 1700
		try {
			sleep(ms);
		} catch (InterruptedException e) {
		}
		setDCStatus(400);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 4, "Restarted"));
	}

	@Override
	public void fatalHalt() {
		if (status != 499)
			setDCStatus(499);
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

	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y + 200);
			myPanel = new MyPanel();
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
						setHasEnvelop(true);
						atmssMBox.send(new Msg("Deposit collector", 4, "Put in envelop"));
						msgTextArea.setText(msgTextArea.getText() + "Envelop put in, waiting atmss.");
						viewMbox.send(new Msg("DepositCollector", 4, "Put in envelop"));
					} else {
						msgTextArea.setText(msgTextArea.getText() + "Failed! Slot not open.");
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
