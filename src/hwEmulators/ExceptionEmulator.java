package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ExceptionEmulator extends Thread {
	private String id;
	// private Logger log = null;
	// private ATMSS atmss = null;
	// private MBox atmssMBox = null;

	public ExceptionEmulator(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		// log = ATMKickstarter.getLogger();

		MyFrame myFrame = new MyFrame("Exception Emulator");
	}

	// ------------------------------------------------------------
	// setATMSS
	// public void setATMSS(ATMSS newAtmss) {
	// atmss = newAtmss;
	// atmssMBox = atmss.getMBox();
	// } // setATMSS

	private class MyFrame extends JFrame {
		// ----------------------------------------
		// MyFrame
		public MyFrame(String title) {
			setTitle(title);
			initComponents();
			pack();
			setLocation(1200, 100);
			setResizable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		} // MyFrame

		private void APNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void APFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CDStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CDInsufficientCashActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CDNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CRStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CRFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void CRNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DCNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DCFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DCStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DisNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DisFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void DisStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void EDNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void EDFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void EDStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void EDNoEvelopActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void KPNormalActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void KPFatalErrorActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void KPStatusActionPerformed(java.awt.event.ActionEvent evt) {
			// TODO add your handling code here:
		}

		private void initComponents() {

			jPanel1 = new javax.swing.JPanel();
			APLable = new javax.swing.JLabel();
			APNormal = new javax.swing.JButton();
			APOutOfResource = new javax.swing.JButton();
			APPaperJam = new javax.swing.JButton();
			APFatalError = new javax.swing.JButton();
			APStatus = new javax.swing.JTextField();
			jPanel3 = new javax.swing.JPanel();
			CRLable = new javax.swing.JLabel();
			CRNormal = new javax.swing.JButton();
			CRFatalError = new javax.swing.JButton();
			CRStatus = new javax.swing.JTextField();
			jPanel4 = new javax.swing.JPanel();
			CDLable = new javax.swing.JLabel();
			CDNormal = new javax.swing.JButton();
			CDInsufficientCash = new javax.swing.JButton();
			CDStatus = new javax.swing.JTextField();
			CDFaital = new javax.swing.JButton();
			jPanel5 = new javax.swing.JPanel();
			DCLable = new javax.swing.JLabel();
			DCNormal = new javax.swing.JButton();
			DCFatalError = new javax.swing.JButton();
			DCStatus = new javax.swing.JTextField();
			jPanel6 = new javax.swing.JPanel();
			DisLable = new javax.swing.JLabel();
			DisNormal = new javax.swing.JButton();
			DisFatalError = new javax.swing.JButton();
			DisStatus = new javax.swing.JTextField();
			jPanel7 = new javax.swing.JPanel();
			EDLable = new javax.swing.JLabel();
			EDNormal = new javax.swing.JButton();
			EDFatalError = new javax.swing.JButton();
			EDStatus = new javax.swing.JTextField();
			EDNoEvelop = new javax.swing.JButton();
			jPanel8 = new javax.swing.JPanel();
			KPLable = new javax.swing.JLabel();
			KPNormal = new javax.swing.JButton();
			KPFatalError = new javax.swing.JButton();
			KPStatus = new javax.swing.JTextField();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			APLable.setText("Advice Printer");

			APNormal.setText("Normal status");
			APNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					APNormalActionPerformed(evt);
				}
			});

			APOutOfResource.setText("Out of Resource");

			APPaperJam.setText("Paper Jam");

			APFatalError.setText("Fatal Error");
			APFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					APFatalErrorActionPerformed(evt);
				}
			});

			APStatus.setForeground(new java.awt.Color(255, 0, 0));
			APStatus.setText("Status");

			CRLable.setText("Card Reader");

			CRNormal.setText("Normal status");
			CRNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CRNormalActionPerformed(evt);
				}
			});

			CRFatalError.setText("Fatal Error");
			CRFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CRFatalErrorActionPerformed(evt);
				}
			});

			CRStatus.setForeground(new java.awt.Color(255, 51, 51));
			CRStatus.setText("Status");
			CRStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CRStatusActionPerformed(evt);
				}
			});

			CDLable.setText("Cash Dispenser");

			CDNormal.setText("Normal status");
			CDNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CDNormalActionPerformed(evt);
				}
			});

			CDInsufficientCash.setText("Insufficient Cash");
			CDInsufficientCash.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CDInsufficientCashActionPerformed(evt);
				}
			});

			CDStatus.setForeground(new java.awt.Color(255, 51, 51));
			CDStatus.setText("Status");
			CDStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					CDStatusActionPerformed(evt);
				}
			});

			CDFaital.setText("Fatal Error");

			DCLable.setText("Deposit Collector");

			DCNormal.setText("Normal status");
			DCNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DCNormalActionPerformed(evt);
				}
			});

			DCFatalError.setText("Fatal Error");
			DCFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DCFatalErrorActionPerformed(evt);
				}
			});

			DCStatus.setForeground(new java.awt.Color(255, 51, 51));
			DCStatus.setText("Status");
			DCStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DCStatusActionPerformed(evt);
				}
			});

			DisLable.setText("Display");

			DisNormal.setText("Normal status");
			DisNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DisNormalActionPerformed(evt);
				}
			});

			DisFatalError.setText("Fatal Error");
			DisFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DisFatalErrorActionPerformed(evt);
				}
			});

			DisStatus.setForeground(new java.awt.Color(255, 51, 51));
			DisStatus.setText("Status");
			DisStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					DisStatusActionPerformed(evt);
				}
			});

			EDLable.setText("Envelop Disoenser");

			EDNormal.setText("Normal status");
			EDNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					EDNormalActionPerformed(evt);
				}
			});

			EDFatalError.setText("Fatal Error");
			EDFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					EDFatalErrorActionPerformed(evt);
				}
			});

			EDStatus.setForeground(new java.awt.Color(255, 51, 51));
			EDStatus.setText("Status");
			EDStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					EDStatusActionPerformed(evt);
				}
			});

			EDNoEvelop.setText("No Envelop");
			EDNoEvelop.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					EDNoEvelopActionPerformed(evt);
				}
			});

			KPLable.setText("Keypad");

			KPNormal.setText("Normal status");
			KPNormal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					KPNormalActionPerformed(evt);
				}
			});

			KPFatalError.setText("Fatal Error");
			KPFatalError.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					KPFatalErrorActionPerformed(evt);
				}
			});

			KPStatus.setForeground(new java.awt.Color(255, 51, 51));
			KPStatus.setText("Status");
			KPStatus.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					KPStatusActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
			jPanel8.setLayout(jPanel8Layout);
			jPanel8Layout.setHorizontalGroup(jPanel8Layout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addGroup(jPanel8Layout
							.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
							.addGroup(jPanel8Layout.createSequentialGroup().addComponent(KPLable).addGap(87, 87, 87))
							.addGroup(jPanel8Layout.createSequentialGroup()
									.addComponent(KPNormal, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
							.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(KPStatus).addComponent(KPFatalError,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE))
							.addContainerGap()));
			jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel8Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(KPLable)
									.addComponent(KPStatus, javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(KPNormal).addComponent(KPFatalError))
							.addGap(43, 43, 43)));

			javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
			jPanel7.setLayout(jPanel7Layout);
			jPanel7Layout
					.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap()
									.addGroup(jPanel7Layout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
											.addComponent(EDFatalError, javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(EDNormal, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
									.addComponent(EDLable, javax.swing.GroupLayout.Alignment.LEADING))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(EDStatus).addComponent(EDNoEvelop,
											javax.swing.GroupLayout.Alignment.TRAILING,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE))
									.addContainerGap())
							.addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
			jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel7Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(EDLable).addComponent(EDStatus,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(EDNormal).addComponent(EDNoEvelop))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(EDFatalError)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(59, 59, 59)));

			javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
			jPanel6.setLayout(jPanel6Layout);
			jPanel6Layout.setHorizontalGroup(jPanel6Layout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(DisNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 117,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(DisLable))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(DisStatus).addComponent(DisFatalError,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE))
							.addContainerGap())
					.addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE));
			jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel6Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(DisLable).addComponent(DisStatus,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(DisNormal).addComponent(DisFatalError))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(57, 57, 57)));

			javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
			jPanel5.setLayout(jPanel5Layout);
			jPanel5Layout
					.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(DCNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 117,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(DCLable))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(DCStatus).addComponent(DCFatalError,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE))
							.addContainerGap()));
			jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel5Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(DCLable).addComponent(DCStatus,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(DCNormal).addComponent(DCFatalError))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(67, 67, 67)));

			javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
			jPanel4.setLayout(jPanel4Layout);
			jPanel4Layout.setHorizontalGroup(jPanel4Layout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(jPanel4Layout.createSequentialGroup()
											.addGroup(jPanel4Layout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
													.addComponent(CDFaital, javax.swing.GroupLayout.Alignment.LEADING,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(CDNormal, javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
									.addGroup(jPanel4Layout.createSequentialGroup().addComponent(CDLable).addGap(51, 51,
											51)))
							.addGroup(
									jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(CDStatus).addComponent(CDInsufficientCash,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addContainerGap())
					.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE));
			jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel4Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(CDLable)
									.addComponent(CDStatus, javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(CDNormal).addComponent(CDInsufficientCash))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CDFaital)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel5,
									javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));

			javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
			jPanel3.setLayout(jPanel3Layout);
			jPanel3Layout.setHorizontalGroup(jPanel3Layout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(CRNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 119,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(CRLable))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(CRStatus).addComponent(CRFatalError,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE))
							.addContainerGap())
					.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE));
			jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(CRLable).addComponent(CRStatus,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(CRNormal).addComponent(CRFatalError))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(jPanel1Layout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(jPanel1Layout.createSequentialGroup()
											.addGroup(jPanel1Layout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(APPaperJam, javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(APNormal, javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
									.addGroup(jPanel1Layout.createSequentialGroup().addComponent(APLable).addGap(60, 60,
											60)))
							.addGroup(jPanel1Layout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
									.addComponent(APStatus)
									.addGroup(jPanel1Layout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(APOutOfResource, javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(APFatalError, javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addContainerGap())
					.addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING,
							javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE));
			jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(APLable).addComponent(APStatus,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(APNormal).addComponent(APOutOfResource))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(APPaperJam).addComponent(APFatalError))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3,
									javax.swing.GroupLayout.PREFERRED_SIZE, 493,
									javax.swing.GroupLayout.PREFERRED_SIZE)));

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1,
							javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE)));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addContainerGap()
							.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addContainerGap(88, Short.MAX_VALUE)));

			pack();
		}

		private javax.swing.JButton APFatalError;
		private javax.swing.JLabel APLable;
		private javax.swing.JButton APNormal;
		private javax.swing.JButton APOutOfResource;
		private javax.swing.JButton APPaperJam;
		private javax.swing.JTextField APStatus;
		private javax.swing.JButton CDFaital;
		private javax.swing.JButton CDInsufficientCash;
		private javax.swing.JLabel CDLable;
		private javax.swing.JButton CDNormal;
		private javax.swing.JTextField CDStatus;
		private javax.swing.JButton CRFatalError;
		private javax.swing.JLabel CRLable;
		private javax.swing.JButton CRNormal;
		private javax.swing.JTextField CRStatus;
		private javax.swing.JButton DCFatalError;
		private javax.swing.JLabel DCLable;
		private javax.swing.JButton DCNormal;
		private javax.swing.JTextField DCStatus;
		private javax.swing.JButton DisFatalError;
		private javax.swing.JLabel DisLable;
		private javax.swing.JButton DisNormal;
		private javax.swing.JTextField DisStatus;
		private javax.swing.JButton EDFatalError;
		private javax.swing.JLabel EDLable;
		private javax.swing.JButton EDNoEvelop;
		private javax.swing.JButton EDNormal;
		private javax.swing.JTextField EDStatus;
		private javax.swing.JButton KPFatalError;
		private javax.swing.JLabel KPLable;
		private javax.swing.JButton KPNormal;
		private javax.swing.JTextField KPStatus;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JPanel jPanel3;
		private javax.swing.JPanel jPanel4;
		private javax.swing.JPanel jPanel5;
		private javax.swing.JPanel jPanel6;
		private javax.swing.JPanel jPanel7;
		private javax.swing.JPanel jPanel8;

	} // MyFrame

	public static void main(String[] args) {
		new ExceptionEmulator("Exception Emulator").run();
	}

}
