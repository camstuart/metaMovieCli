/**
 * 
 */
package au.net.asoftware.metamovie;

/**
 * @author Cameron Stuart
 * 
 */
abstract class Person extends Common {

	private String imdbID;
	private String name;
	private String info;
	private String thumb;

	/**
	 * Constructor
	 */
	public Person() {

		imdbID = "Unknown";
		name = "Unknown";
		info = "Unknown";
		thumb = "Unknown";

	}

	/**
	 * @return the imdbID
	 */
	public String getImdbID() {
		return imdbID;
	}

	/**
	 * @param imdbID
	 *            the imdbID to set
	 */
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the thumb
	 */
	public String getThumb() {
		return thumb;
	}

	/**
	 * @param thumb
	 *            the thumb to set
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

}
