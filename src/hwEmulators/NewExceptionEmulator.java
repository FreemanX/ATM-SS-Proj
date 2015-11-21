package hwEmulators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by e4206692 on 11/18/2015.
 */
public class NewExceptionEmulator extends JFrame implements ActionListener {

    private String title = "";

    private JPanel centerWrapper = new JPanel(new GridLayout(0, 1));
    private ButtonGroup bgComponent = new ButtonGroup();

    // advice printer ------------------------------------------------
    private JRadioButton btnAPSelect = new JRadioButton();
    private JPanel panelAP = new JPanel(new BorderLayout());
    private JPanel panelAPLeft = new JPanel(new BorderLayout());
    private JPanel headAP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelAP = new JLabel("AdvicePrinter:");
    private JLabel labelStatusAP = new JLabel("Normal");

    private JPanel bottomAP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnAPNormal = new JRadioButton("Normal");
    private JRadioButton btnAPOutOfResource = new JRadioButton("Out of resource");
    private JRadioButton btnAPJam = new JRadioButton("Paper jam");
    private JRadioButton btnAPFatal = new JRadioButton("Fatal");
    private ButtonGroup bgAP = new ButtonGroup();
    // end advice printer --------------------------------------------

    // card reader ------------------------------------------------
    private JRadioButton btnCRSelect = new JRadioButton();
    private JPanel panelCR = new JPanel(new BorderLayout());
    private JPanel panelCRLeft = new JPanel(new BorderLayout());
    private JPanel headCR = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelCR = new JLabel("CardReader:");
    private JLabel labelStatusCR = new JLabel("Normal");

    private JPanel bottomCR = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnCRNormal = new JRadioButton("Normal");
    private JRadioButton btnCRFatal = new JRadioButton("Fatal");
    private ButtonGroup bgCR = new ButtonGroup();
    // end card reader --------------------------------------------

    // deposit collector ------------------------------------------------
    private JRadioButton btnDCSelect = new JRadioButton();
    private JPanel panelDC = new JPanel(new BorderLayout());
    private JPanel panelDCLeft = new JPanel(new BorderLayout());
    private JPanel headDC = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelDC = new JLabel("DepositCollector:");
    private JLabel labelStatusDC = new JLabel("Normal");

    private JPanel bottomDC = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnDCNormal = new JRadioButton("Normal");
    private JRadioButton btnDCFatal = new JRadioButton("Fatal");
    private ButtonGroup bgDC = new ButtonGroup();
    // end deposit collector --------------------------------------------

    // display ------------------------------------------------
    private JRadioButton btnDISSelect = new JRadioButton();
    private JPanel panelDIS = new JPanel(new BorderLayout());
    private JPanel panelDISLeft = new JPanel(new GridLayout(0, 1));
    private JPanel headDIS = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelDIS = new JLabel("Display:");
    private JLabel labelStatusDIS = new JLabel("Normal");

    private JPanel bottomDIS = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnDISNormal = new JRadioButton("Normal");
    private JRadioButton btnDISFatal = new JRadioButton("Fatal");
    private ButtonGroup bgDIS = new ButtonGroup();
    // end dispaly --------------------------------------------

    // EnvelopDispenser ------------------------------------------------
    private JRadioButton btnEDSelect = new JRadioButton();
    private JPanel panelED = new JPanel(new BorderLayout());
    private JPanel panelEDLeft = new JPanel(new BorderLayout());
    private JPanel headED = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelED = new JLabel("EnvelopDispenser:");
    private JLabel labelStatusED = new JLabel("Normal");

    private JPanel bottomED = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnEDNormal = new JRadioButton("Normal");
    private JRadioButton btnEDNoEnv = new JRadioButton("No envelop");
    private JRadioButton btnEDFatal = new JRadioButton("Fatal");
    private ButtonGroup bgED = new ButtonGroup();
    // end EnvelopDispenser --------------------------------------------

    // CashDispenser ------------------------------------------------
    private JRadioButton btnCDSelect = new JRadioButton();
    private JPanel panelCD = new JPanel(new BorderLayout());
    private JPanel panelCDLeft = new JPanel(new BorderLayout());
    private JPanel headCD = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelCD = new JLabel("CashDispenser:");
    private JLabel labelStatusCD = new JLabel("Normal");

    private JPanel bottomCD = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnCDNormal = new JRadioButton("Normal");
    private JRadioButton btnCDNoCash = new JRadioButton("Insufficient cash");
    private JRadioButton btnCDFatal = new JRadioButton("Fatal");
    private ButtonGroup bgCD = new ButtonGroup();
    // end CashDispenser --------------------------------------------

    // Keypad ------------------------------------------------
    private JRadioButton btnKPSelect = new JRadioButton();
    private JPanel panelKP = new JPanel(new BorderLayout());
    private JPanel panelKPLeft = new JPanel(new BorderLayout());
    private JPanel headKP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelKP = new JLabel("Keypad:");
    private JLabel labelStatusKP = new JLabel("Normal");

    private JPanel bottomKP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnKPNormal = new JRadioButton("Normal");
    private JRadioButton btnKPFatal = new JRadioButton("Fatal");
    private ButtonGroup bgKP = new ButtonGroup();
    // end Keypad --------------------------------------------

    // atmss
    private JPanel panelATMSS = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JButton btnShutdown = new JButton("Shutdown");
    private JButton btnRestart = new JButton("Restart");

    private String id = "";
    private Logger log = null;
    private ATMSS atmss = null;
    private MBox atmssMBox = null;

    public NewExceptionEmulator(String id, ATMSS atmss) {
        this.id = id;
        log = ATMKickstarter.getLogger();
        this.atmss = atmss;
        atmssMBox = atmss.getMBox();

        title = "Exception Emulator";
        setTitle(title + " | X:" + getWidth() + " Y:" + getHeight());
        setLayout(new GridLayout(0, 1)); // many row, one column
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();

        setSize(265, 700);
        setResizable(false);
        setVisible(true);
    }

    private void buildUI() {
    	setLayout(new BorderLayout());
        setLocation(UIManager.x + 1250, UIManager.y);

        // AdvicePrinter ---------------------------------------
    	//headAP.setBackground(Color.red);
    	//bottomAP.setBackground(Color.cyan);

        headAP.add(labelAP);
        headAP.add(labelStatusAP);
        labelStatusAP.setForeground(Color.red);
        panelAPLeft.add(headAP, BorderLayout.NORTH);

        bottomAP.add(btnAPNormal);
        bottomAP.add(btnAPOutOfResource);
        bottomAP.add(btnAPJam);
        bottomAP.add(btnAPFatal);
        panelAPLeft.add(bottomAP, BorderLayout.CENTER);

        bgAP.add(btnAPNormal);
        bgAP.add(btnAPOutOfResource);
        bgAP.add(btnAPJam);
        bgAP.add(btnAPFatal);
        bgAP.setSelected(btnAPNormal.getModel(), true);

        btnAPNormal.addActionListener(this);
        btnAPOutOfResource.addActionListener(this);
        btnAPJam.addActionListener(this);
        btnAPFatal.addActionListener(this);

        btnAPSelect.addActionListener(this);
        panelAP.add(btnAPSelect, BorderLayout.WEST);
        panelAPLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelAP.add(panelAPLeft, BorderLayout.CENTER);

        centerWrapper.add(panelAP);
        // end AdvicePrinter ------------------------------------

        // CardReader ---------------------------------------
        headCR.add(labelCR);
        headCR.add(labelStatusCR);
        labelStatusCR.setText("Normal");
        labelStatusCR.setForeground(Color.red);
        panelCRLeft.add(headCR, BorderLayout.NORTH);

        bottomCR.add(btnCRNormal);
        bottomCR.add(btnCRFatal);
        panelCRLeft.add(bottomCR, BorderLayout.CENTER);

        bgCR.add(btnCRNormal);
        bgCR.add(btnCRFatal);
        bgCR.setSelected(btnCRNormal.getModel(), true);

        btnCRNormal.addActionListener(this);
        btnCRFatal.addActionListener(this);

        btnCRSelect.addActionListener(this);
        panelCR.add(btnCRSelect, BorderLayout.WEST);
        panelCRLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelCR.add(panelCRLeft, BorderLayout.CENTER);

        centerWrapper.add(panelCR);
        // end CardReader ------------------------------------

        // CashDispenser ---------------------------------------
        headCD.add(labelCD);
        headCD.add(labelStatusCD);
        labelStatusCD.setText("Normal");
        labelStatusCD.setForeground(Color.red);
        panelCDLeft.add(headCD, BorderLayout.NORTH);

        bottomCD.add(btnCDNormal);
        bottomCD.add(btnCDNoCash);
        bottomCD.add(btnCDFatal);
        panelCDLeft.add(bottomCD, BorderLayout.CENTER);

        bgCD.add(btnCDNormal);
        bgCD.add(btnCDNoCash);
        bgCD.add(btnCDFatal);
        bgCD.setSelected(btnCDNormal.getModel(), true);

        btnCDNormal.addActionListener(this);
        btnCDNoCash.addActionListener(this);
        btnCDFatal.addActionListener(this);

        btnCDSelect.addActionListener(this);
        panelCD.add(btnCDSelect, BorderLayout.WEST);
        panelCDLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelCD.add(panelCDLeft, BorderLayout.CENTER);

        centerWrapper.add(panelCD);
        // end CashDispenser ------------------------------------

        // DepositCollector ---------------------------------------
        headDC.add(labelDC);
        headDC.add(labelStatusDC);
        labelStatusDC.setText("Normal");
        labelStatusDC.setForeground(Color.red);
        panelDCLeft.add(headDC, BorderLayout.NORTH);

        bottomDC.add(btnDCNormal);
        bottomDC.add(btnDCFatal);
        panelDCLeft.add(bottomDC, BorderLayout.CENTER);

        bgDC.add(btnDCNormal);
        bgDC.add(btnDCFatal);
        bgDC.setSelected(btnDCNormal.getModel(), true);

        btnDCNormal.addActionListener(this);
        btnDCFatal.addActionListener(this);

        btnDCSelect.addActionListener(this);
        panelDC.add(btnDCSelect, BorderLayout.WEST);
        panelDCLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelDC.add(panelDCLeft, BorderLayout.CENTER);

        centerWrapper.add(panelDC);
        // end DepositCollector ------------------------------------

        // Display ---------------------------------------
        headDIS.add(labelDIS);
        headDIS.add(labelStatusDIS);
        labelStatusDIS.setText("Normal");
        labelStatusDIS.setForeground(Color.red);
        panelDISLeft.add(headDIS, BorderLayout.NORTH);

        bottomDIS.add(btnDISNormal);
        bottomDIS.add(btnDISFatal);
        panelDISLeft.add(bottomDIS, BorderLayout.CENTER);

        bgDIS.add(btnDISNormal);
        bgDIS.add(btnDISFatal);
        bgDIS.setSelected(btnDISNormal.getModel(), true);

        btnDISNormal.addActionListener(this);
        btnDISFatal.addActionListener(this);

        btnDISSelect.addActionListener(this);
        panelDIS.add(btnDISSelect, BorderLayout.WEST);
        panelDISLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelDIS.add(panelDISLeft, BorderLayout.CENTER);

        centerWrapper.add(panelDIS);
        // end Display ------------------------------------

        // EnvelopDispenser ---------------------------------------
        headED.add(labelED);
        headED.add(labelStatusED);
        labelStatusED.setText("Normal");
        labelStatusED.setForeground(Color.red);
        panelEDLeft.add(headED, BorderLayout.NORTH);

        bottomED.add(btnEDNormal);
        bottomED.add(btnEDNoEnv);
        bottomED.add(btnEDFatal);
        panelEDLeft.add(bottomED, BorderLayout.CENTER);

        bgED.add(btnEDNormal);
        bgED.add(btnEDNoEnv);
        bgED.add(btnEDFatal);
        bgED.setSelected(btnEDNormal.getModel(), true);

        btnEDNormal.addActionListener(this);
        btnEDNoEnv.addActionListener(this);
        btnEDFatal.addActionListener(this);

        btnEDSelect.addActionListener(this);
        panelED.add(btnEDSelect, BorderLayout.WEST);
        panelEDLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelED.add(panelEDLeft, BorderLayout.CENTER);

        centerWrapper.add(panelED);
        // end EnvelopDispenser ------------------------------------

        // Keypad ---------------------------------------
        headKP.add(labelKP);
        headKP.add(labelStatusKP);
        labelStatusKP.setText("Normal");
        labelStatusKP.setForeground(Color.red);
        panelKPLeft.add(headKP, BorderLayout.NORTH);

        bottomKP.add(btnKPNormal);
        bottomKP.add(btnKPFatal);
        panelKPLeft.add(bottomKP, BorderLayout.CENTER);

        bgKP.add(btnKPNormal);
        bgKP.add(btnKPFatal);
        bgKP.setSelected(btnKPNormal.getModel(), true);

        btnKPNormal.addActionListener(this);
        btnKPFatal.addActionListener(this);

        btnKPSelect.addActionListener(this);
        panelKP.add(btnKPSelect, BorderLayout.WEST);
        panelKPLeft.setBorder(BorderFactory.createLineBorder(Color.gray));
        panelKP.add(panelKPLeft, BorderLayout.CENTER);

        centerWrapper.add(panelKP);
        // end Keypad ------------------------------------

        btnAPSelect.setActionCommand("APSelect");
        btnCDSelect.setActionCommand("CDSelect");
        btnCRSelect.setActionCommand("CRSelect");
        btnDCSelect.setActionCommand("DCSelect");
        btnDISSelect.setActionCommand("DISSelect");
        btnEDSelect.setActionCommand("EDSelect");
        btnKPSelect.setActionCommand("KPSelect");

        bgComponent.add(btnAPSelect);
        bgComponent.add(btnCDSelect);
        bgComponent.add(btnCRSelect);
        bgComponent.add(btnDCSelect);
        bgComponent.add(btnDISSelect);
        bgComponent.add(btnEDSelect);
        bgComponent.add(btnKPSelect);

        add(centerWrapper, BorderLayout.CENTER);

        panelATMSS.add(btnShutdown);
        panelATMSS.add(btnRestart);
        btnShutdown.addActionListener(this);
        btnShutdown.setEnabled(false);
        btnRestart.addActionListener(this);
        btnRestart.setEnabled(false);
        add(panelATMSS, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // AdvicePrinter --------------------------------------
        if (src == btnAPNormal) {
            labelStatusAP.setText("Normal");
            log.info(id + ": Setting " + labelStatusAP.getText());
            atmssMBox.send(new Msg("100", 1, labelStatusAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 100);
        }
        if (src == btnAPOutOfResource) {
            labelStatusAP.setText("No paper or ink");
            log.info(id + ": Setting " + labelStatusAP.getText());
            atmssMBox.send(new Msg("101", 1, labelStatusAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 101);
        }
        if (src == btnAPJam) {
            labelStatusAP.setText("Paper jammed");
            log.info(id + ": Setting " + labelStatusAP.getText());
            atmssMBox.send(new Msg("103", 1, labelStatusAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 103);
        }
        if (src == btnAPFatal) {
            labelStatusAP.setText("Out of service");
            log.info(id + ": Setting " + labelStatusAP.getText());
            atmssMBox.send(new Msg("199", 1, labelStatusAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 199);
            setButtonGroupEnable(bgAP, false);
        }
        // end AdvicePrinter --------------------------------------

        // CardReader ---------------------------------------------
        if (src == btnCRNormal) {
            labelStatusCR.setText("Normal");
            log.info(id + ": Setting " + labelStatusCR.getText());
            atmssMBox.send(new Msg("200", 2, labelStatusCR.getText()));
            atmss.setHWStatus(CardReader.type, 200);
        }
        if (src == btnCRFatal) {
            labelStatusCR.setText("Out of service");
            log.info(id + ": Setting " + labelStatusCR.getText());
            atmssMBox.send(new Msg("299", 2, labelStatusCR.getText()));
            atmss.setHWStatus(CardReader.type, 299);
            setButtonGroupEnable(bgCR, false);
        }
        // end CardReader ---------------------------------------------

        // CashDispenser -----------------------------------------
        if (src == btnCDNormal) {
            labelStatusCD.setText("Normal");
            log.info(id + ": Setting " + labelStatusCD.getText());
            atmssMBox.send(new Msg("300", 3, labelStatusCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 300);
        }
        if (src == btnCDNoCash) {
            labelStatusCD.setText("Insufficient Cash");
            log.info(id + ": Setting " + labelStatusCD.getText());
            atmssMBox.send(new Msg("301", 3, labelStatusCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 301);
        }
        if (src == btnCDFatal) {
            labelStatusCD.setText("Out of service");
            log.info(id + ": Setting " + labelStatusCD.getText());
            atmssMBox.send(new Msg("399", 3, labelStatusCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 399);
            setButtonGroupEnable(bgCD, false);
        }
        // end CashDispenser ------------------------------------------

        // DepositCollector -----------------------------------------
        if (src == btnDCNormal) {
            labelStatusDC.setText("Normal");
            log.info(id + ": Setting " + labelStatusDC.getText());
            atmssMBox.send(new Msg("400", 4, labelStatusDC.getText()));
            atmss.setHWStatus(DepositCollector.type, 400);
        }
        if (src == btnDCFatal) {
            labelStatusDC.setText("Out of service");
            log.info(id + ": Setting " + labelStatusDC.getText());
            atmssMBox.send(new Msg("499", 4, labelStatusDC.getText()));
            atmss.setHWStatus(DepositCollector.type, 499);
            setButtonGroupEnable(bgDC, false);
        }
        // end DepositCollector ----------------------------------------

        // Display --------------------------------------------------
        if (src == btnDISNormal) {
            labelStatusDIS.setText("Normal");
            log.info(id + ": Setting " + labelStatusDIS.getText());
            atmssMBox.send(new Msg("500", 5, labelStatusDIS.getText()));
            atmss.setHWStatus(Display.type, 500);
        }
        if (src == btnDISFatal) {
            labelStatusDIS.setText("Out of service");
            log.info(id + ": Setting " + labelStatusDIS.getText());
            atmssMBox.send(new Msg("599", 5, labelStatusDIS.getText()));
            atmss.setHWStatus(Display.type, 599);
            setButtonGroupEnable(bgDIS, false);
        }
        // end Display --------------------------------------------------

        // EnvelopDispenser --------------------------------------------------
        if (src == btnEDNormal) {
            labelStatusED.setText("Normal");
            log.info(id + ": Setting " + labelStatusED.getText());
            atmssMBox.send(new Msg("600", 6, labelStatusED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 600);
        }
        if (src == btnEDNoEnv) {
            labelStatusED.setText("No Envelop");
            log.info(id + ": Setting " + labelStatusED.getText());
            atmssMBox.send(new Msg("601", 6, labelStatusED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 601);
        }
        if (src == btnEDFatal) {
            labelStatusED.setText("Out of service");
            log.info(id + ": Setting " + labelStatusED.getText());
            atmssMBox.send(new Msg("699", 6, labelStatusED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 699);
            setButtonGroupEnable(bgED, false);
        }
        // end EnvelopDispenser --------------------------------------------------

        // Keypad -------------------------------------------------------------
        if (src == btnKPNormal) {
            labelStatusKP.setText("Normal");
            log.info(id + ": Setting " + labelStatusKP.getText());
            atmssMBox.send(new Msg("700", 7, labelStatusKP.getText()));
            atmss.setHWStatus(Keypad.type, 700);
        }
        if (src == btnKPFatal) {
            labelStatusKP.setText("Out of service");
            log.info(id + ": Setting " + labelStatusKP.getText());
            atmssMBox.send(new Msg("799", 7, labelStatusKP.getText()));
            atmss.setHWStatus(Keypad.type, 799);
            setButtonGroupEnable(bgKP, false);
        }
        // end Keypad -------------------------------------------------------------

        // atmss components ---------------------------------------------------------

        if (src == btnAPSelect || src == btnCDSelect
        		|| src == btnCRSelect || src == btnDCSelect
        		|| src == btnDISSelect || src == btnEDSelect
        		|| src == btnKPSelect) {
        	// enable shutdown & restart
        	btnShutdown.setEnabled(true);
        	btnRestart.setEnabled(true);
        }

        if (src == btnShutdown) {
            log.info(id + ": Setting Shutdown");

            handleComponentRequest(src, bgComponent.getSelection().getActionCommand());

            // clear selection
            bgComponent.clearSelection();
            btnShutdown.setEnabled(false);
            btnRestart.setEnabled(false);
        }
        if (src == btnRestart) {
            log.info(id + ": Setting Restart");

            handleComponentRequest(src, bgComponent.getSelection().getActionCommand());

            // clear selection
            bgComponent.clearSelection();
            btnShutdown.setEnabled(false);
            btnRestart.setEnabled(false);
        }
        // end atmss components--------------------------------------------------------
    }

    public void handleComponentRequest(Object src, String command) {
    	boolean isShutdown = true;

    	if (src == btnRestart) isShutdown = false;

    	if (command.equals(btnAPSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusAP.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 1, "Shutdown"));
                setButtonGroupEnable(bgAP, false);
    		} else {
    			labelStatusAP.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 1, "Restart"));
                setButtonGroupEnable(bgAP, false);
    		}
    	}
    	if (command.equals(btnCDSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusCD.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 3, "Shutdown"));
                setButtonGroupEnable(bgCD, false);
    		} else {
    			labelStatusCD.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 3, "Restart"));
                setButtonGroupEnable(bgCD, false);
    		}
    	}
    	if (command.equals(btnCRSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusCR.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 2, "Shutdown"));
                setButtonGroupEnable(bgCR, false);
    		} else {
    			labelStatusCR.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 2, "Restart"));
                setButtonGroupEnable(bgCR, false);
    		}
    	}
    	if (command.equals(btnDCSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusDC.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 4, "Shutdown"));
                setButtonGroupEnable(bgDC, false);
    		} else {
    			labelStatusDC.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 4, "Restart"));
                setButtonGroupEnable(bgDC, false);
    		}
    	}
    	if (command.equals(btnDISSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusDIS.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 5, "Shutdown"));
                setButtonGroupEnable(bgDIS, false);
    		} else {
    			labelStatusDIS.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 5, "Restart"));
                setButtonGroupEnable(bgDIS, false);
    		}
    	}
    	if (command.equals(btnEDSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusED.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 6, "Shutdown"));
                setButtonGroupEnable(bgED, false);
    		} else {
    			labelStatusED.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 6, "Restart"));
                setButtonGroupEnable(bgED, false);
    		}
    	}
    	if (command.equals(btnKPSelect.getActionCommand())) {
    		if (isShutdown) {
    			labelStatusKP.setText("Shutdown");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 7, "Shutdown"));
                setButtonGroupEnable(bgKP, false);
    		} else {
    			labelStatusKP.setText("Restarting");
    			// do some other things...
                atmssMBox.send(new Msg(this.getClass().getSimpleName(), 7, "Restart"));
                setButtonGroupEnable(bgKP, false);
    		}
    	}
    	System.out.println("handleComponentRequest: " + command);
    }

    public void componentRestarted(int type) { // 1 - ap, 2 - cr, 3 - cd, 4 - dc, 5 - dis, 6 - ed, 7 - kp
        System.out.println("Component Restarted >> type: " + type);
        switch (type) {
            case 1:
                labelStatusAP.setText("Normal");
                bgAP.setSelected(btnAPNormal.getModel(), true);
                setButtonGroupEnable(bgAP, true);
                break;
            case 2:
                labelStatusCR.setText("Normal");
                bgCR.setSelected(btnCRNormal.getModel(), true);
                setButtonGroupEnable(bgCR, true);
                break;
            case 3:
                labelStatusCD.setText("Normal");
                bgCD.setSelected(btnCDNormal.getModel(), true);
                setButtonGroupEnable(bgCD, true);
                break;
            case 4:
                labelStatusDC.setText("Normal");
                bgDC.setSelected(btnDCNormal.getModel(), true);
                setButtonGroupEnable(bgDC, true);
                break;
            case 5:
                labelStatusDIS.setText("Normal");
                bgDIS.setSelected(btnDISNormal.getModel(), true);
                setButtonGroupEnable(bgDIS, true);
                break;
            case 6:
                labelStatusED.setText("Normal");
                bgED.setSelected(btnEDNormal.getModel(), true);
                setButtonGroupEnable(bgED, true);
                break;
            case 7:
                labelStatusKP.setText("Normal");
                bgKP.setSelected(btnKPNormal.getModel(), true);
                setButtonGroupEnable(bgKP, true);
                break;
        }
    }

    private void setButtonGroupEnable(ButtonGroup bg, boolean isEnable) {
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton btn = buttons.nextElement();
            btn.setEnabled(isEnable);
        }
    }
}
