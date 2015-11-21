package hwEmulators;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.*;

public class Display extends Thread implements EmulatorActions {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea upperArea = null;
	private JTextArea lowerArea = null;
	private MyFrame myFrame = null;
	private MyPanel myPanel = null;
	public final static int type = 5;
	private int status = 500;

	private boolean isBlueScreen = false;

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

	public int getDisStatus() {
		return status;
	}

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
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// upper --------------------------------------------------
	public void displayUpper(String[] lines) {
		upperArea.setText("");
		for (int i = 0; i < lines.length; i++) {
			upperArea.append(lines[i]);
			upperArea.append("\n");
			upperArea.setCaretPosition(upperArea.getDocument().getLength());
		}
	}

	public List<String> getUpperContentList() { // get current displayed text
		return Arrays.asList(upperArea.getText().split("\n"));
	}

	public String getUpperContent() {
		return upperArea.getText();
	}
	// --------------------------------------------------------


	// lower --------------------------------------------------
	public void displayLower(String line) { lowerArea.setText(line); }

	public String getLowerContent() {
		return lowerArea.getText();
	}

	@Override
	public void shutdown() {
		if (status != 598)
			setDisStatus(598);
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
		setDisStatus(500);
		setUIEnable(true);
		isBlueScreen = false;
		atmssMBox.send(new Msg("Component Restarted", 5, "Restarted"));
	}
	// --------------------------------------------------------

	public void quitBlueScreen() {
		myFrame.getContentPane().removeAll();
		myFrame.getContentPane().add(myPanel);
		myFrame.getContentPane().revalidate();
		myFrame.getContentPane().repaint();
		isBlueScreen = false;
	}

	public void setBlueScreen(List<ATMSS.HWFailureInfo> infos) {
		String msg = "A problem has been detected and ATM has been shut down to prevent damage.\n\n"
				+ "If this is the first time you've seen this Stop error screen,\n"
				+ "contact a technician, +852 5174-0740\n\n"
				+ "If you are a technician, follow these steps:\n"
				+ "Check log details failed component(s). Reset the component and restart the ATM.\n\n"
				+ "Technical information:\n";

		isBlueScreen = true;

		for (ATMSS.HWFailureInfo info : infos) {
			msg += "*** Failure component type:  " + String.format("0x%08x", info.getType()) + ", code: " + String.format("0x%08x", info.getCode()) + " (" + info.getCode() + ")\n"
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

	@Override
	public void fatalHalt() {
		if (status != 599)
			setDisStatus(599);
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

	public boolean isBlueScreen() { return isBlueScreen; }

	private class MyFrame extends JFrame {
		// ----------------------------------------
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

	private class MyPanel extends JPanel {
		// ----------------------------------------
		// MyPanel
		public MyPanel() {
			//JScrollPane upperPane = new JScrollPane(upperArea);
			add(upperArea);
			//JScrollPane lowerPane = new JScrollPane(upperArea);
			add(lowerArea);
		} // MyPanel
	} // MyPanel
} // Display
