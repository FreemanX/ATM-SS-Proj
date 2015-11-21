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
	
	/** The name. */
	private String name;
	
	/** The type. */
	private int type;
	
	/** The description. */
	private String description;

	/**
	 * Instantiates a new operation.
	 *
	 * @param Name the name
	 * @param Type the type
	 * @param Description the description
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
	 * Gets the des.
	 *
	 * @return the des
	 */
	public String getDes() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.name + ":\n" + "    " + this.description;
	}
}
