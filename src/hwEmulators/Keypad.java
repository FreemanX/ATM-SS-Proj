package hwEmulators;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.text.DefaultCaret;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//======================================================================
// Keypad
public class Keypad extends Thread{
    private String id;
    private Logger log = null;
    private ATMSS atmss = null;
    private MBox atmssMBox = null;


    //------------------------------------------------------------
    // Keypad
    public Keypad(String id) {
	this.id = id;
	log = ATMKickstarter.getLogger();

	// create frame
	MyFrame myFrame = new MyFrame("Keypad");
    } // Keypad


    //------------------------------------------------------------
    // setATMSS
    public void setATMSS(ATMSS newAtmss) {
	atmss = newAtmss;
	atmssMBox = atmss.getMBox();
    } // setATMSS


    //------------------------------------------------------------
    // MyFrame
    private class MyFrame extends JFrame {
	//----------------------------------------
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


    //------------------------------------------------------------
    // MyPanel
    private class MyPanel extends JPanel {
	//----------------------------------------
	// MyPanel
	public MyPanel() {
	    JButton ejectButton=new JButton("Eject Card");

            ejectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    log.info(id + ": Sending \"Eject\"");
		    atmssMBox.send(new Msg("Keypad", 2, "Eject"));
		}
	    });
	    add(ejectButton);
	} // MyPanel
    } // MyPanel
} // Keypad
