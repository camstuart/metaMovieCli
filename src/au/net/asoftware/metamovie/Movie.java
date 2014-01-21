package au.net.asoftware.metamovie;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Transient;

/**
 * @author mick
 * 
 */
@Entity("movie")
public class Movie extends Common {

	@Id
	protected String id;

	@Transient
	protected File nfoFile;

	@Indexed
	protected String imdbID;

	@Indexed
	protected String title;

	@Indexed
	protected int year;
	protected double rating;
	protected String type;
	protected String outline;
	protected String mpaa;
	protected int runtime;
	protected byte[] thumb;
	protected String[] genres;

	@Embedded
	protected List<Actor> actors;

	@Embedded
	protected List<Director> directors;

	@Embedded
	protected VideoAttributes videoAttributes;

	protected Date created;
	protected Date updated;
	protected boolean active;

	/**
	 * constructor
	 */
	public Movie() {

		this.imdbID = "";
		this.title = "";
		this.year = 0;
		this.rating = 0.0d;
		this.type = "";
		this.outline = "";
		this.mpaa = "";
		this.runtime = 0;
		this.thumb = new byte[1];
		this.genres = new String[1];
		this.actors = new ArrayList<Actor>();
		this.directors = new ArrayList<Director>();
		this.nfoFile = new File("/");
		this.videoAttributes = null;
		this.active = true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return title + " (" + imdbID + ") - " + year;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nfoFile
	 */
	public File getNfoFile() {
		return nfoFile;
	}

	/**
	 * @param nfoFile
	 *            the nfoFile to set
	 */
	public void setNfoFile(File nfoFile) {
		this.nfoFile = nfoFile;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the outline
	 */
	public String getOutline() {
		return outline;
	}

	/**
	 * @param outline
	 *            the outline to set
	 */
	public void setOutline(String outline) {
		this.outline = outline;
	}

	/**
	 * @return the mpaa
	 */
	public String getMpaa() {
		return mpaa;
	}

	/**
	 * @param mpaa
	 *            the mpaa to set
	 */
	public void setMpaa(String mpaa) {
		this.mpaa = mpaa;
	}

	/**
	 * @return the runtime
	 */
	public int getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime
	 *            the runtime to set
	 */
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	/**
	 * @return the thumb
	 */
	public byte[] getThumb() {
		return thumb;
	}

	/**
	 * @param thumb
	 *            the thumb to set
	 */
	public void setThumb(byte[] thumb) {
		this.thumb = thumb;
	}

	/**
	 * @return the genres
	 */
	public String[] getGenres() {
		return genres;
	}

	/**
	 * @param genres
	 *            the genres to set
	 */
	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	/**
	 * @return the actors
	 */
	public List<Actor> getActors() {
		return actors;
	}

	/**
	 * @param actors
	 *            the actors to set
	 */
	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	/**
	 * @return the directors
	 */
	public List<Director> getDirectors() {
		return directors;
	}

	/**
	 * @param directors
	 *            the directors to set
	 */
	public void setDirectors(List<Director> directors) {
		this.directors = directors;
	}

	/**
	 * @return the videoAttributes
	 */
	public VideoAttributes getVideoAttributes() {
		return videoAttributes;
	}

	/**
	 * @param videoAttributes
	 *            the videoAttributes to set
	 */
	public void setVideoAttributes(VideoAttributes videoAttributes) {
		this.videoAttributes = videoAttributes;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
