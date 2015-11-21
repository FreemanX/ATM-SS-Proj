package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
// ======================================================================
/**
 * The Class Keypad.
 */
// Keypad
public class Keypad extends Thread implements EmulatorActions {

	/** The id. */
	private String id;
	
	/** The log. */
	private Logger log = null;
	
	/** The atmss. */
	private ATMSS atmss = null;
	
	/** The atmss m box. */
	private MBox atmssMBox = null;
	
	/** The _keypad view mbox. */
	private MBox _keypadViewMbox = null;
	
	/** The Constant type. */
	public final static int type = 7;
	
	/** The status. */
	private int status = 700;
	
	/** The _input id. */
	private long _inputId = 0;
	
	/** The my frame. */
	private MyFrame myFrame = null;
	
	/** The my panel. */
	private MyPanel myPanel = null;
	
	/** The enabled. */
	// ------------------------------------------------------------------
	private volatile boolean enabled = false;

	// ------------------------------------------------------------------
	/**
	 * The listener interface for receiving my events.
	 * The class that is interested in processing a my
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyListener<code> method. When
	 * the my event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyEvent
	 */
	// Listener
	class MyListener implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
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
	/**
	 * Instantiates a new keypad.
	 *
	 * @param id the id
	 */
	// Keypad
	public Keypad(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		myFrame = new MyFrame("Keypad");
	} // Keypad

	/**
	 * Gets the KP status.
	 *
	 * @return the KP status
	 */
	public int getKPStatus() {
		return status;
	}

	/**
	 * Sets the KP status.
	 *
	 * @param Status the new KP status
	 */
	protected void setKPStatus(int Status) {
		if (status != Status) {
			this.status = Status;

			if (status == 700) {
				atmssMBox.send(new Msg("700", 7, "normal"));
			}

			if (status == 798) {
				shutdown();
			}

			if (status == 799) {
				atmssMBox.send(new Msg("700", 7, "out of service"));
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
	 * Sets the keypad enable.
	 *
	 * @param isEnable the is enable
	 * @param InputId the input id
	 */
	// toggle keypad listening state
	public void setKeypadEnable(boolean isEnable, long InputId) {
		this.enabled = isEnable;
		this._inputId = InputId;
	}

	/**
	 * Sets the view m box.
	 *
	 * @param KeypadViewMBox the new view m box
	 */
	public void setViewMBox(MBox KeypadViewMBox) {
		this._keypadViewMbox = KeypadViewMBox;
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#shutdown()
	 */
	@Override
	public void shutdown() {
		if (status != 798)
			setKPStatus(798);
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
		setKPStatus(700);
		setUIEnable(true);
		atmssMBox.send(new Msg("Component Restarted", 7, "Restarted"));
	}

	/* (non-Javadoc)
	 * @see hwEmulators.EmulatorActions#fatalHalt()
	 */
	@Override
	public void fatalHalt() {
		if (status != 799)
			setKPStatus(799);
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
			System.out.println("Enabling frame");
			myFrame.getContentPane().removeAll(); // remove existing content

			myFrame.getContentPane().add(myPanel);

			myFrame.getContentPane().revalidate();
			myFrame.getContentPane().repaint();
		}
	}

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
			setLocation(UIManager.x + 900, UIManager.y + 480);
			myPanel = new MyPanel(new MyListener());
			add(myPanel);
			pack();
			setSize(350, 320);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	// ------------------------------------------------------------
	/**
	 * The Class MyPanel.
	 */
	// MyPanel
	private class MyPanel extends JPanel {
		
		/** The listener. */
		MyListener listener = null;

		// ----------------------------------------
		/**
		 * Instantiates a new my panel.
		 *
		 * @param listener the listener
		 */
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

		/**
		 * Creates the functions panel.
		 *
		 * @return the j panel
		 */
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

		/**
		 * Creates the num pannel1.
		 *
		 * @return the j panel
		 */
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

		/**
		 * Creates the num pannel2.
		 *
		 * @return the j panel
		 */
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

		/**
		 * Creates the num pannel3.
		 *
		 * @return the j panel
		 */
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

		/**
		 * Creates the num pannel4.
		 *
		 * @return the j panel
		 */
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
