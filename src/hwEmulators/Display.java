package hwEmulators;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Display.
 */
public class Display extends Thread implements EmulatorActions {

	/** The id. */
	private String id;

	/** The log. */
	private Logger log = null;

	/** The atmss. */
	private ATMSS atmss = null;

	/** The atmss m box. */
	private MBox atmssMBox = null;

	/** The upper area. */
	private JTextArea upperArea = null;

	/** The lower area. */
	private JTextArea lowerArea = null;

	/** The my frame. */
	private MyFrame myFrame = null;

	/** The my panel. */
	private MyPanel myPanel = null;

	/** The Constant type. */
	public final static int type = 5;

	/** The status. */
	private int status = 500;

	/** The is blue screen. */
	private boolean isBlueScreen = false;

	/**
	 * Instantiates a new display.
	 *
	 * @param id the id
	 */
	public Display(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		upperArea = new JTextArea(19, 48);
		upperArea.setEditable(false);
		lowerArea = new JTextArea(2, 48);
		lowerArea.setEditable(false);
		myFrame = new MyFrame("Display");
	} // Display

	/**
	 * Gets the dis status.
	 *
	 * @return the dis status
	 */
	public int getDisStatus() {
		return status;
	}

	/**
	 * Sets the dis status.
	 *
	 * @param Status the new dis status
	 */
	protected void setDisStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 500) {
				atmssMBox.send(new Msg("500", 5, "normal"));
			}

			if (status == 598) {
				shutdown();
			}

			if (status == 599) {
				atmssMBox.send(new Msg("599", 5, "out of service"));
				fatalHalt();
			}
		}
	}

	// ------------------------------------------------------------
	/**
	 * Sets the atmss.
	 *
	 * @param newAtmss the new atmss
	 */
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	/**
	 * Display upper.
	 *
	 * @param lines the lines
	 */
	// upper --------------------------------------------------
	public void displayUpper(String[] lines) {
		upperArea.setText("");
		for (int i = 0; i < lines.length; i++) {
			upperArea.append(lines[i]);
			upperArea.append("\n");
			upperArea.setCaretPosition(upperArea.getDocument().getLength());
		}
	}

	/**
	 * Gets the upper content list.
	 *
	 * @return the upper content list
	 */
	public List<String> getUpperContentList() { // get current displayed text
		return Arrays.asList(upperArea.getText().split("\n"));
	}

	/**
	 * Gets the upper content.
	 *
	 * @return the upper content
	 */
	public String getUpperContent() {
		return upperArea.getText();
	}
	// --------------------------------------------------------


	/**
	 * Display lower.
	 *
	 * @param line the line
	 */
	// lower --------------------------------------------------
	public void displayLower(String line) { lowerArea.setText(line); }

	/**
	 * Gets the lower content.
	 *
	 * @return the lower content
	 */
	public String getLowerContent() {
		return lowerArea.getText();
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		if (status != 598)
			setDisStatus(598);
		setUIEnable(false, true);
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#restart()
	 */
	@Override
	public void restart() {
		shutdown();
		long ms = new Random(new Date().getTime()).nextInt(1500) + 200; // 200 - 1700
		try {
			sleep(ms);
		} catch (InterruptedException e) {
		}
		setDisStatus(500);
		setUIEnable(true);
		isBlueScreen = false;
		atmssMBox.send(new Msg("Component Restarted", 5, "Restarted"));

		upperArea.setText("");
		lowerArea.setText("");
	}
	// --------------------------------------------------------

	/**
	 * Quit blue screen.
	 */
	public void quitBlueScreen() {
		myFrame.getContentPane().removeAll();
		myFrame.getContentPane().add(myPanel);
		myFrame.getContentPane().revalidate();
		myFrame.getContentPane().repaint();
		isBlueScreen = false;
	}

	/**
	 * Sets the blue screen.
	 *
	 * @param infos the new blue screen
	 */
	public void setBlueScreen(List<ATMSS.HWFailureInfo> infos) {
		String[] componentName = new String[]{"AdvicePrinter", "CardReader", "CashDispenser", "DepositCollector", "Display", "EnvelopDispenser", "Keypad", "Network"};
		String msg = "A problem has been detected and ATM has been shut down to prevent damage.\n\n"
				+ "If this is the first time you've seen this Stop error screen,\n"
				+ "contact a technician, +852 5174-0740\n"
				+ "If you are a technician, follow these steps:\n"
				+ "Check log details for failed component(s). Reset the component and restart the ATM.\n\n"
				+ "Technical information [" + infos.size() + "]:\n";

		isBlueScreen = true;

		for (ATMSS.HWFailureInfo info : infos) {
			msg += "*** Component type:  " + String.format("0x%08x", info.getType()) + " (" + componentName[info.getType() - 1] + ")" + ", code: " + String.format("0x%08x", info.getCode()) + " (" + info.getCode() + ")\n"
					+ "    message: " + info.getMessage() + "\n\n";
		}

		myFrame.getContentPane().removeAll(); // remove existing content

		// add new panel
		JPanel panel = new JPanel(new GridLayout(1, 1));

		JTextPane textPane = new JTextPane();
		textPane.setBackground(Color.BLUE);
		textPane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		StyledDocument doc = textPane.getStyledDocument();

		Style style = textPane.addStyle("whiteText", null);
		StyleConstants.setForeground(style, Color.white);
		try {
			doc.insertString(doc.getLength(), msg, style);
		} catch (BadLocationException e) {}

		panel.add(textPane);
		myFrame.getContentPane().add(panel);

		myFrame.getContentPane().revalidate();
		myFrame.getContentPane().repaint();
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 599)
			setDisStatus(599);
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
	 * @param isEnable should the UI enable?
	 * @param isShutdown should the UI shutdown?
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
			System.out.println("Enabling frame");
			myFrame.getContentPane().removeAll(); // remove existing content

			myFrame.getContentPane().add(myPanel);

			myFrame.getContentPane().revalidate();
			myFrame.getContentPane().repaint();
		}
	}

	/**
	 * Checks if is blue screen.
	 *
	 * @return true, if is blue screen
	 */
	public boolean isBlueScreen() { return isBlueScreen; }

	/**
	 * The Class MyFrame.
	 */
	private class MyFrame extends JFrame {
		// ----------------------------------------
		/**
		 * Instantiates a new frame.
		 *
		 * @param title the title
		 */
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocation(UIManager.x + 350, UIManager.y);
			myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(550, 400);
			setResizable(false);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	/**
	 * The Class MyPanel.
	 */
	private class MyPanel extends JPanel {
		// ----------------------------------------
		/**
		 * Instantiates a new panel.
		 */
		// MyPanel
		public MyPanel() {
			//JScrollPane upperPane = new JScrollPane(upperArea);
			add(upperArea);
			//JScrollPane lowerPane = new JScrollPane(upperArea);
			add(lowerArea);
		} // MyPanel
	} // MyPanel
} // Display
