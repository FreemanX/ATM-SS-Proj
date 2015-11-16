package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ======================================================================
// Keypad
public class Keypad extends Thread {

	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private MBox _keypadViewMbox = null;
	public final static int type = 7;
	private int status = 700;
	private long _inputId = 0;
	// ------------------------------------------------------------------
	private volatile boolean enabled = false;

	// ------------------------------------------------------------------
	// Listener
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (enabled && status == 700) { // only notify when true
				String cmd = e.getActionCommand();
				System.out.println(cmd);
				log.info("Sending \"" + cmd + "\"");
				atmssMBox.send(new Msg(id, 7, cmd));
				_keypadViewMbox.send(new Msg("Keypad", 7, _inputId + ":" + cmd));
			} else if (!enabled && status == 700) {
				atmssMBox.send(new Msg("Keypad", 7, "Keypad is not enabled!"));
			} else if (status != 700) {
				atmssMBox.send(new Msg("Keypad", 7, "Keypad is out of service!"));
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

	public int getKPStatus() {
		return status;
	}

	protected void setKPStatus(int Status) {
		this.status = Status;
	}

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// toggle keypad listening state
	public void setKeypadEnable(boolean isEnable, long InputId) {
		this.enabled = isEnable;
		this._inputId = InputId;
	}

	public void setViewMBox(MBox KeypadViewMBox) {
		this._keypadViewMbox = KeypadViewMBox;
	}

	// ------------------------------------------------------------
	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x + 900, UIManager.y + 480);
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
			numPoint.addActionListener(listener);

			JPanel functionPannel = new JPanel();
			functionPannel.add(num0);
			functionPannel.add(numPoint);
			return functionPannel;
		}

	} // MyPanel

} // Keypad
