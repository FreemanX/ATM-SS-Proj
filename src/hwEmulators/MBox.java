package hwEmulators;

import java.util.ArrayList;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class MBox.
 */
// MBox
public class MBox {
	
	/** The id. */
	private String id;
	
	/** The log. */
	private Logger log = null;
	
	/** The mqueue. */
	private ArrayList<Msg> mqueue = new ArrayList<Msg>();
	
	/** The msg cnt. */
	private int msgCnt = 0;

	// ------------------------------------------------------------
	/**
	 * Instantiates a new m box.
	 *
	 * @param id the id
	 */
	// MBox
	public MBox(String id) {
		this.id = id;
		log = ATMKickstarter.getLogger();
	} // MBox
	
	/**
	 * Clear box.
	 */
	@Deprecated
	public final synchronized void clearBox() {
		msgCnt = 0;
		mqueue.clear();
	}

	// ------------------------------------------------------------
	/**
	 * Send.
	 *
	 * @param msg the msg
	 */
	// send
	public final synchronized void send(Msg msg) {
		msgCnt++;
		mqueue.add(msg);
		log.fine(id + ": send \"" + msg + "\"");
		notify();
	} // send

	// ------------------------------------------------------------
	/**
	 * Receive.
	 *
	 * @return the msg
	 */
	// receive
	public final synchronized Msg receive() {
		// wait if message queue is empty
		if (--msgCnt <= 0) {
			while (true) {
				try {
					log.fine(id + ": waiting");
//					System.out.println(id + ": waiting");
					wait();
					log.fine(id + ": finish waiting");
//					System.out.println(id + ": finish waiting");
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
		if (msg.getType() % 100 != 0)
			log.info(id + ": receiving \"" + msg + "\"");
		return msg;
	} // receive

	/**
	 * Receive temp.
	 *
	 * @return the msg
	 */
	/*
	 * DO NOT USE THIS,
	 * this is only used in NewExceptionEmu. for temporally solution
	 * - Tony
	 */
	public final synchronized Msg receiveTemp() {
		try {
			while (mqueue.size() < 1) {
				// wait
				wait();
			}
		} catch (InterruptedException e) {
		}

		return mqueue.remove(0);
	}

} // MBox
