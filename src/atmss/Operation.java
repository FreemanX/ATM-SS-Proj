/**
 * 
 */
package atmss;

/**
 * @author freeman
 *
 */
public class Operation {
	private String name;
	private int type;
	private String description;

	public Operation(String Name, int Type, String Description) {
		this.name = Name;
		this.type = Type;
		this.description = Description;
	}

	public String getName() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}

	public String getDes() {
		return this.description;
	}

	public String toString() {
		return this.name + ":\n" + "    " + this.description;
	}
}
