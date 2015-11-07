package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Display extends Thread{
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea textArea = null;
	private MyFrame myFrame = null;
	
	public Display(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		textArea = new JTextArea(21,48);
		textArea.setEditable(false);
		MyFrame myFrame = new MyFrame("Display");
	} // Display

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS
	
	// ------------------------------------------------------------
	// append
	public void append(String str) {
		textArea.append(str);
		textArea.append("\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	} // append
	
	// ------------------------------------------------------------
	// clear
	public void clear() {
		textArea.setText("");
	} // clear
	
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocation(UIManager.x + 350, UIManager.y);
			MyPanel myPanel = new MyPanel();
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
			JScrollPane textScrollPane = new JScrollPane(textArea);
			add(textScrollPane);
		} // MyPanel
	} // MyPanel
} // Display
