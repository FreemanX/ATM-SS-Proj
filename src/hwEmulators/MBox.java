package hwEmulators;

import java.util.logging.Logger;
import java.util.ArrayList;

//======================================================================
// MBox
public class MBox {
	private String id;
	private Logger log = null;
	private ArrayList<Msg> mqueue = new ArrayList<Msg>();
	private int msgCnt = 0;

	// ------------------------------------------------------------
	// MBox
	public MBox(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();
	} // MBox

	// ------------------------------------------------------------
	// send
	public final synchronized void send(Msg msg) {
		msgCnt++;
		mqueue.add(msg);
		log.fine(id + ": send \"" + msg + "\"");
		notify();
	} // send

	// ------------------------------------------------------------
	// receive
	public final synchronized Msg receive() {
		// wait if message queue is empty
		if (--msgCnt <= 0) {
			while (true) {
				try {
					log.fine(id + ": waiting");
					System.out.println(id + ": waiting");
					wait();
					log.fine(id + ": finish waiting");
					System.out.println(id + ": finish waiting");
					break;
				} catch (InterruptedException e) {
					log.warning(id + ".receive: InterruptedException");

					if (msgCnt >= 0)
						break; // msg arrived already
					else
						continue; // no msg yet, continue waiting
				}
			}
		}

		Msg msg = mqueue.remove(0);
		log.info(id + ": receiveing \"" + msg + "\"");
		return msg;
	} // receive
} // MBox
