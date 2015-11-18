package hwEmulators;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

/**
 * Created by e4206692 on 11/18/2015.
 */
public class NewExceptionEmulator extends JFrame implements ActionListener {

    private String title = "";

    // advice printer ------------------------------------------------
    private JPanel panelAP = new JPanel(new GridLayout(0, 1));
    private JPanel headAP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelAP = new JLabel("AdvicePrinter");
    private TextField textFieldAP = new TextField(15);

    private JPanel bottomAP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnAPNormal = new JRadioButton("Normal");
    private JRadioButton btnAPOutOfResource = new JRadioButton("Out of resource");
    private JRadioButton btnAPJam = new JRadioButton("Paper jam");
    private JRadioButton btnAPFatal = new JRadioButton("Fatal");
    private ButtonGroup bgAP = new ButtonGroup();
    // end advice printer --------------------------------------------

    // card reader ------------------------------------------------
    private JPanel panelCR = new JPanel(new GridLayout(0, 1));
    private JPanel headCR = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelCR = new JLabel("CardReader");
    private TextField textFieldCR = new TextField(15);

    private JPanel bottomCR = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnCRNormal = new JRadioButton("Normal");
    private JRadioButton btnCRFatal = new JRadioButton("Fatal");
    private ButtonGroup bgCR = new ButtonGroup();
    // end card reader --------------------------------------------

    // deposit collector ------------------------------------------------
    private JPanel panelDC = new JPanel(new GridLayout(0, 1));
    private JPanel headDC = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelDC = new JLabel("DepositCollector");
    private TextField textFieldDC = new TextField(15);

    private JPanel bottomDC = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnDCNormal = new JRadioButton("Normal");
    private JRadioButton btnDCFatal = new JRadioButton("Fatal");
    private ButtonGroup bgDC = new ButtonGroup();
    // end deposit collector --------------------------------------------

    // display ------------------------------------------------
    private JPanel panelDIS = new JPanel(new GridLayout(0, 1));
    private JPanel headDIS = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelDIS = new JLabel("Display");
    private TextField textFieldDIS = new TextField(15);

    private JPanel bottomDIS = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnDISNormal = new JRadioButton("Normal");
    private JRadioButton btnDISFatal = new JRadioButton("Fatal");
    private ButtonGroup bgDIS = new ButtonGroup();
    // end dispaly --------------------------------------------

    // EnvelopDispenser ------------------------------------------------
    private JPanel panelED = new JPanel(new GridLayout(0, 1));
    private JPanel headED = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelED = new JLabel("EnvelopDispenser");
    private TextField textFieldED = new TextField(15);

    private JRadioButton btnEDNormal = new JRadioButton("Normal");
    private JRadioButton btnEDNoEnv = new JRadioButton("No envelop");
    private JRadioButton btnEDFatal = new JRadioButton("Fatal");
    private ButtonGroup bgED = new ButtonGroup();
    // end EnvelopDispenser --------------------------------------------

    // CashDispenser ------------------------------------------------
    private JPanel panelCD = new JPanel(new GridLayout(0, 1));
    private JPanel headCD = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelCD = new JLabel("CashDispenser");
    private TextField textFieldCD = new TextField(15);

    private JPanel bottomCD = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton btnCDNormal = new JRadioButton("Normal");
    private JRadioButton btnCDNoCash = new JRadioButton("No cash");
    private JRadioButton btnCDFatal = new JRadioButton("Fatal");
    private ButtonGroup bgCD = new ButtonGroup();
    // end CashDispenser --------------------------------------------

    // Keypad ------------------------------------------------
    private JPanel panelKP = new JPanel(new GridLayout(0, 1));
    private JPanel headKP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel labelKP = new JLabel("Keypad");
    private TextField textFieldKP = new TextField(15);

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

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                setTitle(title + " | X:" + getWidth() + " Y:" + getHeight());
                System.out.println(getTitle());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        buildUI();

        setSize(265, 950);
        setResizable(false);
        setVisible(true);
    }

    private void buildUI() {
        // AdvicePrinter ---------------------------------------
        headAP.add(labelAP);
        headAP.add(textFieldAP);
        textFieldAP.setText("Normal");
        textFieldAP.setEditable(false);
        panelAP.add(headAP);

        bottomAP.add(btnAPNormal);
        bottomAP.add(btnAPOutOfResource);
        bottomAP.add(btnAPJam);
        bottomAP.add(btnAPFatal);
        panelAP.add(bottomAP);

        bgAP.add(btnAPNormal);
        bgAP.add(btnAPOutOfResource);
        bgAP.add(btnAPJam);
        bgAP.add(btnAPFatal);
        bgAP.setSelected(btnAPNormal.getModel(), true);

        btnAPNormal.addActionListener(this);
        btnAPOutOfResource.addActionListener(this);
        btnAPJam.addActionListener(this);
        btnAPFatal.addActionListener(this);

        add(panelAP);
        // end AdvicePrinter ------------------------------------

        // CardReader ---------------------------------------
        headCR.add(labelCR);
        headCR.add(textFieldCR);
        textFieldCR.setText("Normal");
        textFieldCR.setEditable(false);
        panelCR.add(headCR);

        panelCR.add(btnCRNormal);
        panelCR.add(btnCRFatal);

        bgCR.add(btnCRNormal);
        bgCR.add(btnCRFatal);
        bgCR.setSelected(btnCRNormal.getModel(), true);

        btnCRNormal.addActionListener(this);
        btnCRFatal.addActionListener(this);

        add(panelCR);
        // end CardReader ------------------------------------

        // CashDispenser ---------------------------------------
        headCD.add(labelCD);
        headCD.add(textFieldCD);
        textFieldCD.setText("Normal");
        textFieldCD.setEditable(false);
        panelCD.add(headCD);

        panelCD.add(btnCDNormal);
        panelCD.add(btnCDNoCash);
        panelCD.add(btnCDFatal);

        bgCD.add(btnCDNormal);
        bgCD.add(btnCDNoCash);
        bgCD.add(btnCDFatal);
        bgCD.setSelected(btnCDNormal.getModel(), true);

        btnCDNormal.addActionListener(this);
        btnCDNoCash.addActionListener(this);
        btnCDFatal.addActionListener(this);

        add(panelCD);
        // end CashDispenser ------------------------------------

        // DepositCollector ---------------------------------------
        headDC.add(labelDC);
        headDC.add(textFieldDC);
        textFieldDC.setText("Normal");
        textFieldDC.setEditable(false);
        panelDC.add(headDC);

        panelDC.add(btnDCNormal);
        panelDC.add(btnDCFatal);

        bgDC.add(btnDCNormal);
        bgDC.add(btnDCFatal);
        bgDC.setSelected(btnDCNormal.getModel(), true);

        btnDCNormal.addActionListener(this);
        btnDCFatal.addActionListener(this);

        add(panelDC);
        // end DepositCollector ------------------------------------

        // Display ---------------------------------------
        headDIS.add(labelDIS);
        headDIS.add(textFieldDIS);
        textFieldDIS.setText("Normal");
        textFieldDIS.setEditable(false);
        panelDIS.add(headDIS);

        panelDIS.add(btnDISNormal);
        panelDIS.add(btnDISFatal);

        bgDIS.add(btnDISNormal);
        bgDIS.add(btnDISFatal);
        bgDIS.setSelected(btnDISNormal.getModel(), true);

        btnDISNormal.addActionListener(this);
        btnDISFatal.addActionListener(this);

        add(panelDIS);
        // end Display ------------------------------------

        // EnvelopDispenser ---------------------------------------
        headED.add(labelED);
        headED.add(textFieldED);
        textFieldED.setText("Normal");
        textFieldED.setEditable(false);
        panelED.add(headED);

        panelED.add(btnEDNormal);
        panelED.add(btnEDNoEnv);
        panelED.add(btnEDFatal);

        bgED.add(btnEDNormal);
        bgED.add(btnEDNoEnv);
        bgED.add(btnEDFatal);
        bgED.setSelected(btnEDNormal.getModel(), true);

        btnEDNormal.addActionListener(this);
        btnEDNoEnv.addActionListener(this);
        btnEDFatal.addActionListener(this);

        add(panelED);
        // end EnvelopDispenser ------------------------------------

        // Keypad ---------------------------------------
        headKP.add(labelKP);
        headKP.add(textFieldKP);
        textFieldKP.setText("Normal");
        textFieldKP.setEditable(false);
        panelKP.add(headKP);

        panelKP.add(btnKPNormal);
        panelKP.add(btnKPFatal);

        bgKP.add(btnKPNormal);
        bgKP.add(btnKPFatal);
        bgKP.setSelected(btnKPNormal.getModel(), true);

        btnKPNormal.addActionListener(this);
        btnKPFatal.addActionListener(this);

        add(panelKP);
        // end Keypad ------------------------------------

        panelATMSS.add(btnShutdown);
        panelATMSS.add(btnRestart);
        btnShutdown.addActionListener(this);
        btnRestart.addActionListener(this);
        add(panelATMSS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // AdvicePrinter --------------------------------------
        if (src == btnAPNormal) {
            textFieldAP.setText("Normal");
            log.info(id + ": Setting " + textFieldAP.getText());
            atmssMBox.send(new Msg("Advice Printer is fine(100)", 1, textFieldAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 100);
        }
        if (src == btnAPOutOfResource) {
            textFieldAP.setText("No paper or ink");
            log.info(id + ": Setting " + textFieldAP.getText());
            atmssMBox.send(new Msg("Advice Printer Exception(101)", 1, textFieldAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 101);
        }
        if (src == btnAPJam) {
            textFieldAP.setText("Paper jamed");
            log.info(id + ": Setting " + textFieldAP.getText());
            atmssMBox.send(new Msg("Advice Printer Exception(103)", 1, textFieldAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 103);
        }
        if (src == btnAPFatal) {
            textFieldAP.setText("Out of service");
            log.info(id + ": Setting " + textFieldAP.getText());
            atmssMBox.send(new Msg("Advice Printer Exception(199)", 1, textFieldAP.getText()));
            atmss.setHWStatus(AdvicePrinter.type, 199);
        }
        // end AdvicePrinter --------------------------------------

        // CardReader ---------------------------------------------
        if (src == btnCRNormal) {
            textFieldCR.setText("Normal");
            log.info(id + ": Setting " + textFieldCR.getText());
            atmssMBox.send(new Msg("Card Reader Normal(200)", 2, textFieldCR.getText()));
            atmss.setHWStatus(CardReader.type, 200);
        }
        if (src == btnCRFatal) {
            textFieldCR.setText("Out of service");
            log.info(id + ": Setting " + textFieldCR.getText());
            atmssMBox.send(new Msg("Card Reader Exception(299)", 2, textFieldCR.getText()));
            atmss.setHWStatus(CardReader.type, 299);
        }
        // end CardReader ---------------------------------------------

        // CashDispenser -----------------------------------------
        if (src == btnCDNormal) {
            textFieldCD.setText("Normal");
            log.info(id + ": Setting " + textFieldCD.getText());
            atmssMBox.send(new Msg("Cash Dispenser Normal(300)", 3, textFieldCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 300);
        }
        if (src == btnCDNoCash) {
            textFieldCD.setText("Insufficient Cash");
            log.info(id + ": Setting " + textFieldCD.getText());
            atmssMBox.send(new Msg("Cash Dispenser Exception(301)", 3, textFieldCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 301);
        }
        if (src == btnCDFatal) {
            textFieldCD.setText("Out of service");
            log.info(id + ": Setting " + textFieldCD.getText());
            atmssMBox.send(new Msg("Cash Dispenser Exception(399 or 302)", 3, textFieldCD.getText()));
            atmss.setHWStatus(CashDispenser.type, 399);
        }
        // end CashDispenser ------------------------------------------

        // DepositCollector -----------------------------------------
        if (src == btnDCNormal) {
            textFieldDC.setText("Normal");
            log.info(id + ": Setting " + textFieldDC.getText());
            atmssMBox.send(new Msg("Deposit Collector Normal(400)", 4, textFieldDC.getText()));
            atmss.setHWStatus(DepositCollector.type, 400);
        }
        if (src == btnDCFatal) {
            textFieldDC.setText("Out of service");
            log.info(id + ": Setting " + textFieldDC.getText());
            atmssMBox.send(new Msg("Deposit Collector Exception(499)", 4, textFieldDC.getText()));
            atmss.setHWStatus(DepositCollector.type, 499);
        }
        // end DepositCollector ----------------------------------------

        // Display --------------------------------------------------
        if (src == btnDISNormal) {
            textFieldDIS.setText("Normal");
            log.info(id + ": Setting " + textFieldDIS.getText());
            atmssMBox.send(new Msg("Display Normal(500)", 5, textFieldDIS.getText()));
            atmss.setHWStatus(Display.type, 500);
        }
        if (src == btnDISFatal) {
            textFieldDIS.setText("Out of service");
            log.info(id + ": Setting " + textFieldDIS.getText());
            atmssMBox.send(new Msg("Display Exception(599)", 5, textFieldDIS.getText()));
            atmss.setHWStatus(Display.type, 599);
        }
        // end Display --------------------------------------------------

        // EnvelopDispenser --------------------------------------------------
        if (src == btnEDNormal) {
            textFieldED.setText("Normal");
            log.info(id + ": Setting " + textFieldED.getText());
            atmssMBox.send(new Msg("Envelop Dispenser Normal(600)", 6, textFieldED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 600);
        }
        if (src == btnEDNoEnv) {
            textFieldED.setText("No Envelop");
            log.info(id + ": Setting " + textFieldED.getText());
            atmssMBox.send(new Msg("Envelop Dispenser Exception(601)", 6, textFieldED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 601);
        }
        if (src == btnEDFatal) {
            textFieldED.setText("Out of service");
            log.info(id + ": Setting " + textFieldED.getText());
            atmssMBox.send(new Msg("Envelop Dispenser Exception(699)", 6, textFieldED.getText()));
            atmss.setHWStatus(EnvelopDispenser.type, 699);
        }
        // end EnvelopDispenser --------------------------------------------------

        // Keypad -------------------------------------------------------------
        if (src == btnKPNormal) {
            textFieldKP.setText("Normal");
            log.info(id + ": Setting " + textFieldKP.getText());
            atmssMBox.send(new Msg("Keypad Normal(700)", 7, textFieldKP.getText()));
            atmss.setHWStatus(Keypad.type, 700);
        }
        if (src == btnKPFatal) {
            textFieldKP.setText("Out of service");
            log.info(id + ": Setting " + textFieldKP.getText());
            atmssMBox.send(new Msg("Keypad Exception(799)", 7, textFieldKP.getText()));
            atmss.setHWStatus(Keypad.type, 799);
        }
        // end Keypad -------------------------------------------------------------

        // atmss ---------------------------------------------------------
        if (src == btnShutdown) {
            log.info(id + ": Setting Shutdown");
            atmssMBox.send(new Msg("Shutdown", -1, "Trying to shutdown"));
        }
        if (src == btnRestart) {
            log.info(id + ": Setting Restart");
            atmssMBox.send(new Msg("Restart", -1, "Trying to Restart"));
        }
        // end atmss --------------------------------------------------------
    }
}
