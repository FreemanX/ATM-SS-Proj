package hwEmulators;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class ATMSSDBugConsole.
 */
// ATMSSDBugConsole
public class ATMSSDBugConsole {
	
	/** The id. */
	private String id;
	
	/** The log. */
	private Logger log = null;
	
	/** The atmss. */
	private ATMSS atmss = null;
	
	/** The text area. */
	private JTextArea textArea = null;
	
	/** The my frame. */
	private MyFrame myFrame = null;

	// ------------------------------------------------------------
	/**
	 * Instantiates a new ATMSSD bug console.
	 *
	 * @param id the id
	 * @param atmss the atmss
	 */
	// ATMSSDBugConsole
	public ATMSSDBugConsole(String id, ATMSS atmss) {
		// init
		this.id = id;
		this.atmss = atmss;
		log = ATMKickstarter.getLogger();

		// create textArea and frame
		textArea = new JTextArea(20, 48);
		textArea.setEditable(false);
		myFrame = new MyFrame("ATMSS-Console");
	} // ATMSSDBugConsole

	// ------------------------------------------------------------
	/**
	 * Prints the.
	 *
	 * @param str the str
	 */
	// print
	public void print(String str) {
		textArea.append(str);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	} // println

	// ------------------------------------------------------------
	/**
	 * Println.
	 *
	 * @param str the str
	 */
	// println
	public void println(String str) {
		print(str);
		textArea.append("\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	} // println

	// ------------------------------------------------------------
	/**
	 * The Class MyFrame.
	 */
	// MyFrame
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
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocation(UIManager.x + 350, UIManager.y + 400);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(550, 450);
			setResizable(true);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	// ------------------------------------------------------------
	/**
	 * The Class MyPanel.
	 */
	// MyPanel
	private class MyPanel extends JPanel {
		// ----------------------------------------
		/**
		 * Instantiates a new my panel.
		 */
		// MyPanel
		public MyPanel() {
			// clear button
			JButton clearButton = new JButton("Clear");
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					textArea.setText("");
				}
			});

			// exit button
			JButton exitButton = new JButton("Exit");
			exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					log.info(id + ": Exit button clicked");
					myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
				}
			});

			JScrollPane textScrollPane = new JScrollPane(textArea);
			add(textScrollPane);
			add(clearButton);
			add(exitButton);
		} // MyPanel
	} // MyPanel
} // ATMSSDBugConsole
