/**
 * 
 */
package atmss;

import java.util.ArrayList;
import java.util.List;

/**
 * @author freeman
 *
 */
 public class Session {

	private long sid = -1;
	private String cred = "";
	private String cardNo = "";
	private List<Operation> ops;
	/**
	 * 
	 */
	public Session(long sid, String cred, String cardNo) {
		this.sid = sid;
		this.cred = cred;
		this.cardNo = cardNo;
		ops = new ArrayList<Operation>();
	}

	public void addOp(Operation op) {
		ops.add(op);
	}

	public Operation getLastOp() {
		return ops.get(ops.size() - 1);
	}

	public List<Operation> getOps() {
		return ops;
	}

	public String getCardNo() {
		return cardNo;
	}

	public long getSid() {
		return sid;
	}

	public String getCred() {
		return cred;
	}
}
