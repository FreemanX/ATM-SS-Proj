package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.text.DefaultCaret;

import com.sun.org.apache.xpath.internal.functions.Function;
import com.sun.org.apache.xpath.internal.patterns.FunctionPattern;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//======================================================================
// Keypad
public class Keypad extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;

	// ------------------------------------------------------------
	// Keypad
	public Keypad(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		MyFrame myFrame = new MyFrame("Keypad");
	} // Keypad

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// ------------------------------------------------------------
	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(880, 100);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	// ------------------------------------------------------------
	// MyPanel
	private class MyPanel extends JPanel {
		// ----------------------------------------
		// MyPanel
		public MyPanel() {
			JPanel functionPannel = createFunctionsPanel();
			JPanel numPannel1 = createNumPannel1();
			
			
			this.add(functionPannel);
			this.add(numPannel1);
			
		} // MyPanel
		
		private JPanel createFunctionsPanel()
		{
			JButton cancelButton = new JButton("CANCEL");
			JButton clearButton = new JButton("CLEAR");
			JButton enterButton = new JButton("ENTER");
			
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					log.info(id + ": Sending \"CANCEL\"");
					atmssMBox.send(new Msg("Keypad", 2, "CANCEL"));
				}
			});
			
			clearButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \"CLEAR\"");
					atmssMBox.send(new Msg("Keypad", 2, "CLEAR"));
				}
			});
			
			enterButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \"ENTER\"");
					atmssMBox.send(new Msg("Keypad", 2, "ENTER"));
				}
			});
			
			JPanel functionPannel = new JPanel();
			functionPannel.add(cancelButton);
			functionPannel.add(clearButton);
			functionPannel.add(enterButton);
			return functionPannel;
		}
		
		private JPanel createNumPannel1()
		{
			JButton num1 = new JButton("1");
			JButton num2 = new JButton("2");
			JButton num3 = new JButton("3");
			
			num1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					log.info(id + ": Sending \"1\"");
					atmssMBox.send(new Msg("Keypad", 2, "1"));
				}
			});
			
			num2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \"2\"");
					atmssMBox.send(new Msg("Keypad", 2, "2"));
				}
			});
			
			num3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \"3\"");
					atmssMBox.send(new Msg("Keypad", 2, "3"));
				}
			});
			
			JPanel numPannel1 = new JPanel();
			numPannel1.add(num1);
			numPannel1.add(num2);
			numPannel1.add(num3);
			return numPannel1;
		}
		
		
	} // MyPanel
} // Keypad
