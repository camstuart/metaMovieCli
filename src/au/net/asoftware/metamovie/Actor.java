package au.net.asoftware.metamovie;

/**
 * @author Cameron Stuart
 * 
 */
public class Actor extends Person {

	private String role;

	/**
	 * Constructor
	 */
	public Actor() {
		this.role = "";
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
