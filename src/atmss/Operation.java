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
	private String description;

	public Operation(String Name, String Description) {
		this.name = Name;
		this.description = Description;
	}

	public String getName() {
		return this.name;
	}

	public String getDes() {
		return this.description;
	}
}
