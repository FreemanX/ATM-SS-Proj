package hwEmulators;

// TODO: Auto-generated Javadoc
//======================================================================
/**
 * The Class Msg.
 */
// Msg
public class Msg {
	
	/** The sender. */
	private String sender;
	
	/** The type. */
	private int type;
	
	/** The details. */
	private String details;

	// ------------------------------------------------------------
	/**
	 * Instantiates a new msg.
	 *
	 * @param sender the sender
	 * @param type the type
	 * @param details the details
	 */
	// Msg
	public Msg(String sender, int type, String details) {
		this.sender = sender;
		this.type = type;
		this.details = details;
	} // Msg

	// ------------------------------------------------------------
	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	// getters
	public String getSender() {
		return sender;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the details.
	 *
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	// ------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	// toString
	public String toString() {
		return sender + "(" + type + ") -- " + details;
	} // toString
} // Msg
