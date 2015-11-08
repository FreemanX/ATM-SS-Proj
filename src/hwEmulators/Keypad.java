package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import atmss.hardware.hw.KeypadView;

//======================================================================
// Keypad
public class Keypad extends Thread {
	private KeypadView view;
	
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	// ------------------------------------------------------------------
	private boolean enabled = true;

	// ------------------------------------------------------------------
	// Listener
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabled) { // only notify when true
				String cmd = e.getActionCommand();
				System.out.println(cmd);
				log.info("Sending \"" + cmd + "\"");
				atmssMBox.send(new Msg("Keypad", 2, cmd));
			}
		}
	} // listener

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

	// toggle keypad listening state
	public void setKeypadEnable(boolean isEnable) {
		enabled = isEnable;
	}

	public void setView(KeypadView view) {
		this.view = view;
	}

	// ------------------------------------------------------------
	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(880, 100);
			MyPanel myPanel = new MyPanel(new MyListener());
			add(myPanel);
			pack();
			setSize(350, 320);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	// ------------------------------------------------------------
	// MyPanel
	private class MyPanel extends JPanel {
		MyListener listener = null;

		// ----------------------------------------
		// MyPanel
		public MyPanel(MyListener listener) {
			this.listener = listener;
			JPanel functionPannel = createFunctionsPanel();
			JPanel numPannel1 = createNumPannel1();
			JPanel numPannel2 = createNumPannel2();
			JPanel numPannel3 = createNumPannel3();
			JPanel numPannel4 = createNumPannel4();
			this.add(functionPannel);
			this.add(numPannel1);
			this.add(numPannel2);
			this.add(numPannel3);
			this.add(numPannel4);
		} // MyPanel

		private JPanel createFunctionsPanel() {
			JButton cancelButton = new JButton("CANCEL");
			JButton clearButton = new JButton("CLEAR");
			JButton enterButton = new JButton("ENTER");

			cancelButton.setPreferredSize(new Dimension(100, 40));
			clearButton.setPreferredSize(new Dimension(100, 40));
			enterButton.setPreferredSize(new Dimension(100, 40));

			cancelButton.addActionListener(listener);

			clearButton.addActionListener(listener);

			enterButton.addActionListener(listener);

			JPanel functionPannel = new JPanel();
			functionPannel.add(cancelButton);
			functionPannel.add(clearButton);
			functionPannel.add(enterButton);
			return functionPannel;
		}

		private JPanel createNumPannel1() {
			JPanel numPannel1 = new JPanel();
			JButton[] num = new JButton[3];
			for (int i = 0; i < 3; i++) {
				num[i] = new JButton(Integer.toString(i + 1));
				num[i].setPreferredSize(new Dimension(100, 40));
			}

			num[0].addActionListener(listener);
			num[1].addActionListener(listener);
			num[2].addActionListener(listener);
			for (int i = 0; i < 3; i++)
				numPannel1.add(num[i]);

			return numPannel1;
		}

		private JPanel createNumPannel2() {
			JPanel numPannel2 = new JPanel();
			JButton[] num = new JButton[3];
			for (int i = 0; i < 3; i++) {
				num[i] = new JButton(Integer.toString(i + 4));
				num[i].setPreferredSize(new Dimension(100, 40));
			}

			num[0].addActionListener(listener);
			num[1].addActionListener(listener);
			num[2].addActionListener(listener);
			for (int i = 0; i < 3; i++)
				numPannel2.add(num[i]);

			return numPannel2;
		}

		private JPanel createNumPannel3() {
			JPanel numPannel3 = new JPanel();
			JButton[] num = new JButton[3];
			for (int i = 0; i < 3; i++) {
				num[i] = new JButton(Integer.toString(i + 7));
				num[i].setPreferredSize(new Dimension(100, 40));
			}

			num[0].addActionListener(listener);
			num[1].addActionListener(listener);
			num[2].addActionListener(listener);
			for (int i = 0; i < 3; i++)
				numPannel3.add(num[i]);

			return numPannel3;
		}

		private JPanel createNumPannel4() {
			JButton num0 = new JButton("0");
			JButton numPoint = new JButton(".");

			num0.setPreferredSize(new Dimension(100, 40));
			numPoint.setPreferredSize(new Dimension(100, 40));

			num0.addActionListener(listener);

			numPoint.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					log.info(id + ": Sending \".\"");
					atmssMBox.send(new Msg("Keypad", 2, "."));
				}
			});

			JPanel functionPannel = new JPanel();
			functionPannel.add(num0);
			functionPannel.add(numPoint);
			return functionPannel;
		}

	} // MyPanel

} // Keypad
