package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvicePrinter.
 */
public class AdvicePrinter extends Thread implements EmulatorActions {
	
	/** The id. */
	private String id;
	
	/** The log. */
	private Logger log = null;
	
	/** The atmss. */
	private ATMSS atmss = null;
	
	/** The atmss m box. */
	private MBox atmssMBox = null;
	
	/** The text area. */
	private JTextArea textArea = null;
	
	/** The my frame. */
	private MyFrame myFrame = null;
	
	/** The my panel. */
	private MyPanel myPanel = null;
	
	/** The Constant type. */
	public final static int type = 1;
	
	/** The status. */
	private int status = 100;
	
	/** The resource. */
	private int resource = 10000;

	/**
	 * Instantiates a new advice printer.
	 *
	 * @param id the id
	 */
	public AdvicePrinter(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create textArea and frame
		textArea = new JTextArea(20, 30);
		textArea.setEditable(false);
		myFrame = new MyFrame("Advice Printer");
	}

	/**
	 * Gets the AP status.
	 *
	 * @return the AP status
	 */
	public int getAPStatus() {
		return status;
	}

	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public int getResource() {
		if (resource < 1)
			this.status = 101;
		return resource;
	}

	/**
	 * Sets the AP status.
	 *
	 * @param Status the new AP status
	 */
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

	/**
	 * Sets the atmss.
	 *
	 * @param newAtmss the new atmss
	 */
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	// ------------------------------------------------------------
	/**
	 * Prints the.
	 *
	 * @param str the str
	 */
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
	/**
	 * Println.
	 *
	 * @param str the str
	 */
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

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		// set exception status
		if (status != 198)
			setAPStatus(198);
		setUIEnable(false, true);
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#restart()
	 */
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

		textArea.setText("");
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 199)
			setAPStatus(199);
		setUIEnable(false, false);
	}

	/**
	 * Sets the UI enable.
	 *
	 * @param isEnable the new UI enable
	 */
	private void setUIEnable(boolean isEnable) {
		setUIEnable(isEnable, true);
	}

	/**
	 * Sets the ui enable.
	 *
	 * @param isEnable the is enable
	 * @param isShutdown the is shutdown
	 */
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

	/**
	 * The Class MyFrame.
	 */
	private class MyFrame extends JFrame {
		// ----------------------------------------
		/**
		 * Instantiates a new my frame.
		 *
		 * @param title the title
		 */
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y + 400);
			myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 450);
			setResizable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	}

	/**
	 * The Class MyPanel.
	 */
	private class MyPanel extends JPanel {
		// ----------------------------------------
		/**
		 * Instantiates a new my panel.
		 */
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
