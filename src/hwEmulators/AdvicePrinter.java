package hwEmulators;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvicePrinter extends Thread implements EmulatorActions {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea textArea = null;
	private MyFrame myFrame = null;
	private MyPanel myPanel = null;
	public final static int type = 1;
	private int status = 100;
	private int resource = 10000;

	public AdvicePrinter(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create textArea and frame
		textArea = new JTextArea(20, 30);
		textArea.setEditable(false);
		myFrame = new MyFrame("Advice Printer");
	}

	public int getAPStatus() {
		return status;
	}

	public int getResource() {
		if (resource < 1)
			this.status = 101;
		return resource;
	}

	protected void setAPStatus(int Status) {
		System.out.println(status + " vs. " + Status);
		if (status != Status) {
			this.status = Status;
			if (status == 100) {
				resource = 10000;
				System.out.println("Sending");
				atmssMBox.send(new Msg("100", 1, "normal"));
			}
			if (status == 101 || status == 102) {
				this.resource = 0;
			}
			if (status == 103) {
				// TODO simulate Paper jam
			}
			if (status == 198) {
				shutdown();
			}
			if (status == 199) {
				atmssMBox.send(new Msg("199", 1, "out of service"));
				fatalHalt();
			}
		}
	}

	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	// ------------------------------------------------------------
	// print
	public void print(String str) {
		if (getResource() > 1) {
			textArea.append(str);
			textArea.setCaretPosition(textArea.getDocument().getLength());
			resource--;
		} else {
			this.status = 101;
			textArea.append(">>>>>>>>>>>>>Out of service:No resources");
		}
	} // print

	// ------------------------------------------------------------
	// println
	public void println(String str) {
		try {
			sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print(str);
		textArea.append("\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	} // println

	@Override
	public void shutdown() {
		// set exception status
		if (status != 198)
			setAPStatus(198);
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
		setAPStatus(100);
		atmssMBox.send(new Msg(this.getClass().getSimpleName(), 1, "Restarted"));
		setUIEnable(true);
	}

	@Override
	public void fatalHalt() {
		if (status != 199)
			setAPStatus(199);
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
			setLocation(UIManager.x, UIManager.y + 400);
			myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 400);
			setResizable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	}

	private class MyPanel extends JPanel {
		// ----------------------------------------
		// MyPanel
		public MyPanel() {
			JButton clearButton = new JButton("Clear");
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textArea.setText("");
					;
				}
			});
			JScrollPane textScrollPane = new JScrollPane(textArea);
			add(textScrollPane);
			add(clearButton);
		} // MyPanel

	}
}
