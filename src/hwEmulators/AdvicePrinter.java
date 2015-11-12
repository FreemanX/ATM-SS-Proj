package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvicePrinter extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea textArea = null;
	private MyFrame myFrame = null;
	public final static int type = 1;
	private int status = 100;
	private int resource = 10000;

	public AdvicePrinter(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create textArea and frame
		textArea = new JTextArea(20, 30);
		textArea.setEditable(false);
		MyFrame myFrame = new MyFrame("Advice Printer");
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
		this.status = Status;
		if (status == 100) {
			resource = 10000;
		} else if (status == 101 || status == 102) {
			this.resource = 0;
		} else if (status == 103) {
			// TODO simulate Paper jam
		} else {
			// TODO simulate out of service
		}
	}

	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	}

	// ------------------------------------------------------------
	// print
	public void print(String str) {
		if (getResource() > 0) {
			textArea.append(str);
			textArea.setCaretPosition(textArea.getDocument().getLength());
			resource--;
		} else {
			textArea.setText(">>>>>>>>>>>>>Out of service:No resources");
		}
	} // print

	// ------------------------------------------------------------
	// println
	public void println(String str) {
		print(str);
		textArea.append("\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	} // println

	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x, UIManager.y + 400);
			MyPanel myPanel = new MyPanel();
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