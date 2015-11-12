package hwEmulators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CashDispenser extends Thread {
	private String id;
	private Logger log = null;
	private ATMSS atmss = null;
	private MBox atmssMBox = null;
	private JTextArea textArea = null;
	public final static int type = 3;
	private int status = 300;
	private int numOf100 = 100000;
	private int numOf500 = 100000;
	private int numOf1000 = 100000;
	private int ejectNumOf100 = 0;
	private int ejectNumOf500 = 0;
	private int ejectNumOf1000 = 0;
	private int ejectCashAmount = 100 * ejectNumOf100 + 500 * ejectNumOf500 + 1000 * ejectNumOf1000;

	// ------------------------------------------------------------
	// CashDispenser
	public CashDispenser(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();

		// create frame
		MyFrame myFrame = new MyFrame("Cash Dispenser");
	} // CashDispenser

	protected void setNumOf100(int amount) {
		this.numOf100 = amount;
	}

	public int getNumOf100() {
		return this.numOf100;
	}

	protected void setNumOf500(int amount) {
		this.numOf500 = amount;
	}

	public int getNumOf500() {
		return this.numOf500;
	}

	protected void setNumOf1000(int amount) {
		this.numOf1000 = amount;
	}

	public int getNumOf1000() {
		return this.numOf1000;
	}

	private void updateCashInventory(int takenThisNumOf100, int takenThisNumOf500, int takenThisNumOf1000) {
		this.setNumOf100(this.numOf100 - takenThisNumOf100);
		this.setNumOf500(this.numOf500 - takenThisNumOf500);
		this.setNumOf1000(this.numOf1000 - takenThisNumOf1000);
	}

	public void setCashAmount() {
		this.ejectCashAmount = 100 * ejectNumOf100 + 500 * ejectNumOf500 + 1000 * ejectNumOf1000;
	}

	public int getCashAmount() {
		return this.ejectCashAmount;
	}

	public void resetEjectCashAmount() {
		this.ejectNumOf100 = 0;
		this.ejectNumOf500 = 0;
		this.ejectNumOf1000 = 0;
		this.ejectCashAmount = 0;
	}

	public int getCDStatus() {
		return status;
	}

	public boolean ejectCash(int ejectThisNumof100, int ejectThisNumof500, int ejectThisNumof1000) {
		boolean isSuccessful = false;
		this.ejectNumOf100 = ejectThisNumof100;
		this.ejectNumOf500 = ejectThisNumof500;
		this.ejectNumOf1000 = ejectThisNumof1000;
		boolean isSufficient = this.numOf100 >= ejectThisNumof100 && this.numOf500 >= ejectThisNumof500
				&& this.numOf1000 >= ejectThisNumof1000;
		this.setCashAmount();
		if (isSufficient && ejectCashAmount > 0) {
			this.textArea.append("Please take " + ejectNumOf100 + " of 100HKD\n");
			this.textArea.append("Please take " + ejectNumOf500 + " of 500HKD\n");
			this.textArea.append("Please take " + ejectNumOf1000 + " of 1000HKD\n");
			this.textArea.append("Please take total" + ejectCashAmount + "HKD\n");
			isSuccessful = true;
		} else {
			this.textArea.append("Nothing to be ejected");
		}
		return isSuccessful;
	}

	protected void setCDStatus(int Status) {
		if (Status == 301) {
			numOf500 = 0; // For demo only
		}
		this.status = Status;
	}

	// ------------------------------------------------------------
	// setATMSS
	public void setATMSS(ATMSS newAtmss) {
		atmss = newAtmss;
		atmssMBox = atmss.getMBox();
	} // setATMSS

	// MyFrame
	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			setLocation(UIManager.x + 900, UIManager.y + 280);
			MyPanel myPanel = new MyPanel();
			add(myPanel);
			pack();
			setSize(350, 200);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame
	} // MyFrame

	private class MyPanel extends JPanel {
		public MyPanel() {
			JPanel buttonPanel = createButtonPanel();
			JPanel msgPanel = createMsgPanel();
			add(msgPanel);
			add(buttonPanel);
		}

		private JPanel createButtonPanel() {
			JButton collectButton = new JButton("Collect Cash");
			JButton resetButton = new JButton("Clear");

			collectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (status <= 301) {
						if (ejectCashAmount > 0) {
							log.info(id + ": Sending \"Collect Cash\" amount: " + ejectCashAmount);
							atmssMBox.send(new Msg("Cash Dispenser", 3, "Dispense cash: " + ejectCashAmount));
							updateCashInventory(ejectNumOf100, ejectNumOf500, ejectNumOf1000);
							textArea.append(ejectNumOf100 + " of 100HKD taken\n");
							textArea.append(ejectNumOf500 + " of 500HKD taken\n");
							textArea.append(ejectNumOf1000 + " of 1000HKD taken\n");
							textArea.append(ejectCashAmount + " HKD taken in all\n");
							resetEjectCashAmount();
						}
					} else {
						textArea.setText("This hardware is not working, status code: " + status);
					}
				}
			});

			resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					textArea.setText("");
				}
			});
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(collectButton);
			buttonPanel.add(resetButton);
			return buttonPanel;
		}

		private JPanel createMsgPanel() {
			textArea = new JTextArea(6, 30);
			textArea.setEditable(false);
			JScrollPane msgScrollPane = new JScrollPane(textArea);
			JPanel msgPanel = new JPanel();
			msgPanel.add(msgScrollPane);
			return msgPanel;
		}
	}
}