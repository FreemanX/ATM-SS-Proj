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

	/** The sid session ID. */
	private long sid = -1;
	
	/** The cred credential of current session. */
	private String cred = "";
	
	/** The card number. */
	private String cardNo = "";
	
	/** The ops operations of current session. */
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
	 * Adds the operation.
	 *
	 * @param op the operation
	 */
	public void addOp(Operation op) {
		ops.add(op);
	}

	/**
	 * Gets the last operation.
	 *
	 * @return the last operation
	 */
	public Operation getLastOp() {
		return ops.get(ops.size() - 1);
	}

	/**
	 * Gets the operations.
	 *
	 * @return the operations
	 */
	public List<Operation> getOps() {
		return ops;
	}

	/**
	 * Gets the card number.
	 *
	 * @return the card number
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
