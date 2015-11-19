package hwEmulators;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.*;

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
		this.status = Status;
		if (status == 599) {
			shutdown();
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
		if (status != 599)
			setDisStatus(599);
		setUIEnable(false);
	}

	@Override
	public void restart() {
		shutdown();
		long ms = new Random(new Date().getTime()).nextInt(4000) + 500; // 500 - 4500
		try {
			sleep(ms);
		} catch (InterruptedException e) {
		}
		setDisStatus(500);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 5, "Restarted"));
	}
	// --------------------------------------------------------

	public void setBlueScreen() {
		myFrame.getContentPane().removeAll(); // remove existing content

		// add new panel
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel label = new JLabel("BlueScreen message");
		label.setForeground(Color.WHITE);
		panel.setBackground(Color.BLUE);
		panel.add(label, new GridBagConstraints());
		myFrame.getContentPane().add(panel);

		myFrame.getContentPane().revalidate();
		myFrame.getContentPane().repaint();
	}

	private void setUIEnable(boolean isEnable) {
		if (!isEnable) { // disable the UI
			myFrame.getContentPane().removeAll(); // remove existing content

			// add new panel
			JPanel panel = new JPanel(new GridBagLayout());
			JLabel label = new JLabel("SHUTDOWN");
			//label.setForeground(Color.WHITE);
			panel.setBackground(Color.black);
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
