/**
 * 
 */
package atmss;

// TODO: Auto-generated Javadoc
/**
 * The Class Operation.
 *
 * @author freeman
 */
public class Operation {

	/** The name of operation. */
	private String name;

	/** The type of operation. */
	private int type;

	/** The description of operation. */
	private String description;

	/**
	 * Instantiates a new operation.
	 *
	 * @param Name
	 *            the name of operation
	 * @param Type
	 *            the type of operation
	 * @param Description
	 *            the description of operation
	 */
	public Operation(String Name, int Type, String Description) {
		this.name = Name;
		this.type = Type;
		this.description = Description;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDes() {
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.name + ":\n" + "    " + this.description;
	}
}
