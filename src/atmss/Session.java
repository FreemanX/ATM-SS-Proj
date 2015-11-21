/**
 * 
 */
package atmss;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Session.
 *
 * @author freeman
 */
 public class Session {

	/** The sid. */
	private long sid = -1;
	
	/** The cred. */
	private String cred = "";
	
	/** The card no. */
	private String cardNo = "";
	
	/** The ops. */
	private List<Operation> ops;
	
	/**
	 * Instantiates a new session.
	 *
	 * @param sid the sid
	 * @param cred the cred
	 * @param cardNo the card no
	 */
	public Session(long sid, String cred, String cardNo) {
		this.sid = sid;
		this.cred = cred;
		this.cardNo = cardNo;
		ops = new ArrayList<Operation>();
	}

	/**
	 * Adds the op.
	 *
	 * @param op the op
	 */
	public void addOp(Operation op) {
		ops.add(op);
	}

	/**
	 * Gets the last op.
	 *
	 * @return the last op
	 */
	public Operation getLastOp() {
		return ops.get(ops.size() - 1);
	}

	/**
	 * Gets the ops.
	 *
	 * @return the ops
	 */
	public List<Operation> getOps() {
		return ops;
	}

	/**
	 * Gets the card no.
	 *
	 * @return the card no
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * Gets the sid.
	 *
	 * @return the sid
	 */
	public long getSid() {
		return sid;
	}

	/**
	 * Gets the cred.
	 *
	 * @return the cred
	 */
	public String getCred() {
		return cred;
	}
}
