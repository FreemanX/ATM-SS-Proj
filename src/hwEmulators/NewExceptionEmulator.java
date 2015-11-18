package hwEmulators;

import javax.swing.*;
import java.awt.*;

/**
 * Created by e4206692 on 11/18/2015.
 */
public class NewExceptionEmulator extends JFrame {

    // advice printer ------------------------------------------------
    private JPanel panelAP = new JPanel();
    private TextField textFieldAP = new TextField(20);

    private JButton btnAPNormal = new JButton("Normal");
    private JButton btnAPOutOfResource = new JButton("Out of resource");
    private JButton btnAPJam = new JButton("Paper jam");
    private JButton btnAPFatal = new JButton("Fatal");
    // end advice printer --------------------------------------------

    // card reader ------------------------------------------------
    private JPanel panelCR = new JPanel();
    private TextField textFieldCR = new TextField(20);

    private JButton btnCRNormal = new JButton("Normal");
    private JButton btnCRNoCash = new JButton("Insufficient cash");
    private JButton btnCRFatal = new JButton("Fatal");
    // end card reader --------------------------------------------

    public NewExceptionEmulator(String title) {
        setTitle(title);
        setLayout(new GridLayout(0, 1)); // many row, one column
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();

        setVisible(true);
    }

    private void buildUI() {
        panelAP.add(textFieldAP);
        panelAP.add(btnAPNormal);
        panelAP.add(btnAPOutOfResource);
        panelAP.add(btnAPJam);
        panelAP.add(btnAPFatal);
        add(panelAP);
    }
}
