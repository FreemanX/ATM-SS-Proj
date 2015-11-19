package hwEmulators;

import java.awt.*;
import java.util.Date;
import java.util.Random;
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
public class EnvelopDispenser extends Thread implements EmulatorActions {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea msgTextArea = null;
	public final static int type = 6;
	private int status = 600;
	private int numOfEnvelop = 10000;
	private MyFrame myFrame = null;
	private MyPanel myPanel = null;

	// ------------------------------------- -----------------------
	// EnvelopDispenser
	public EnvelopDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create the text field and our frame
		myFrame = new MyFrame("Envelop Dispenser");
	} // EnvelopDispenser

	public int getEDStatus() {
		return status;
	}

	protected void setEDStatus(int Status) {
		this.status = Status;

		if (status == 601) {
			numOfEnvelop = 0;
		}

		if (status == 600) {
			numOfEnvelop = 10000;
		}

		if (status == 698) {
			shutdown();
		}

		if (status == 699) {
			fatalHalt();
		}
	}

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	public boolean ejectEnvelop() {
		if (getEnvelopCount() > 0) {
			msgTextArea.append("Preparing for ejecting...\n");
			msgTextArea.append("Enjecting an envelop...\n");
			msgTextArea.append("Envelop ejected!\n");
			numOfEnvelop--;
			return true;
		} else {
			return false;
		}
	}

	public int getEnvelopCount() {
		if (numOfEnvelop < 1) {
			this.status = 601;
			msgTextArea.append("There's no envelop!");
		}
		return numOfEnvelop;
	}

	@Override
	public void shutdown() {
		if (status != 698)
			setEDStatus(698);
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
		setEDStatus(600);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 6, "Restarted"));
	}

	@Override
	public void fatalHalt() {
		if (status != 699)
			setEDStatus(699);
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
		public static final long serialVersionUID = 1L;

		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y);
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
		public static final long serialVersionUID = 1L;

		// -----------------------------------------
		// MyPanel
		public MyPanel() {
			// create the panels
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();

			// add the panels
			add(msgPanel);
			add(buttonPanel);
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
					atmssMBox.send(new Msg("EnvelopDispenser", 6, clearButton.getText()));
				}
			});

			// create the panel and add the buttons to the panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(clearButton);
			return buttonPanel;
		} // createButtonPanel

		// ----------------------------------------
		// createMsgPanel
		private JPanel createMsgPanel() {
			JPanel msgPanel = new JPanel();

			// create the msg text area
			msgTextArea = new JTextArea(6, 30);
			msgTextArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(msgTextArea);
			msgPanel.add(msgScrollPane);
			return msgPanel;
		} // createMsgPanel
	} // MyPanel
} // EnvelopDispenser
