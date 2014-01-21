package au.net.asoftware.metamovie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author mick
 * 
 */
public class NfoFile extends Common {

	private final static Logger logger = LoggerFactory.getLogger(NfoFile.class.getName());
	private File nfoFile;

	private String imdbID;
	private String title;
	private int year;
	private double rating;
	private String outline;
	private String mpaa;
	private int runtime;
	private byte[] thumb;
	private String type;
	private String[] genres;
	private List<Actor> actors;
	private List<Director> directors;
	private static String actorName;
	private static String actorRole;
	private static String actorThumb;

	/**
	 * 
	 * @param nfoFile
	 * @throws FileNotFoundException
	 */
	public NfoFile(File nfoFile) throws FileNotFoundException {

		this.nfoFile = nfoFile;

		checkFileExists(this.nfoFile);

		imdbID = "Unknown";
		title = "Unknown";
		year = 0;
		rating = 0.0d;
		outline = "Unknown";
		mpaa = "Unknown";
		runtime = 0;
		thumb = new byte[1];
		type = "movie";
		genres = new String[1];
		actors = new ArrayList<Actor>();
		directors = new ArrayList<Director>();

		analyseNfo();

	}

	/**
	 * Analyse the nfo file and populate data members
	 */
	private void analyseNfo() {

		String nfoFileName = nfoFile.getAbsolutePath();

		logger.debug("Analysing nfo file: [{}]", nfoFileName);

		// Read the nfo to grab the imdb ID

		Path assetFilePath = Paths.get(nfoFile.getAbsolutePath());

		List<String> nfoLines = new ArrayList<String>();

		try {
			nfoLines = Files.readAllLines(assetFilePath, Charset.forName("ISO-8859-1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Actor> nfoActors = new ArrayList<Actor>();
		int i = 0;
		for (String nfoLine : nfoLines) {

			if (nfoLine.trim().toLowerCase().startsWith("<id>")) {
				String nfoId = nfoLine.trim().replace("<id>", "").replace("</id>", "");
				// System.out.println("ID: " + nfoId);
				imdbID = nfoId;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<title>")) {
				String nfoTitle = nfoLine.trim().replace("<title>", "").replace("</title>", "");
				title = nfoTitle;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<year>")) {
				String nfoYear = nfoLine.trim().replace("<year>", "").replace("</year>", "");

				int yearAsInt = 0;
				try {
					yearAsInt = Integer.parseInt(nfoYear);
				} catch (NumberFormatException e) {
					logger.warn("Movie nfo file [{}] has bad year [{}], setting to zero", nfoFileName, nfoYear);
					year = 0;
				}
				year = yearAsInt;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<rating>")) {
				String nfoRating = nfoLine.trim().replace("<rating>", "").replace("</rating>", "");

				double ratingAsInt = 0.0d;
				try {
					ratingAsInt = Double.parseDouble(nfoRating);
				} catch (NumberFormatException e) {
					logger.warn("Movie nfo file [{}] has bad rating [{}], setting to zero", nfoFileName, nfoRating);
					rating = 0.0d;
				}

				rating = ratingAsInt;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<outline>")) {
				String nfoOutline = nfoLine.trim().replace("<outline>", "").replace("</outline>", "");
				outline = nfoOutline;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<mpaa>")) {
				String nfoMpaa = nfoLine.trim().replace("<mpaa>", "").replace("</mpaa>", "");
				mpaa = nfoMpaa;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<runtime>")) {
				String nfoRuntime = nfoLine.trim().replace("<runtime>", "").replace("min</runtime>", "");

				int runtimeAsInt = 0;
				try {
					runtimeAsInt = Integer.parseInt(nfoRuntime.trim());
				} catch (NumberFormatException e) {
					logger.warn("Movie nfo file [{}] has bad runtime [{}], setting to zero", nfoFileName, nfoRuntime);
					runtime = 0;
				}
				runtime = runtimeAsInt;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<thumb>") && i == 6) {
				String nfoThumbName = nfoLine.trim().replace("<thumb>", "").replace("</thumb>", "");
				File nfoThumbFile = new File(assetFilePath.toString().replace("movie.nfo", "") + File.separator + nfoThumbName);
				byte[] nfoThumb = new byte[(int) nfoThumbFile.length()];

				try {
					FileInputStream fileInputStream = new FileInputStream(nfoThumbFile);
					// convert file into array of bytes
					fileInputStream.read(nfoThumb);
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				thumb = nfoThumb;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<genre>")) {
				String[] gen = nfoLine.trim().replace("<genre>", "").replace("</genre>", "").replace(" ", "").split(",");
				genres = gen;
			}

			if (nfoLine.trim().toLowerCase().startsWith("<director>")) {
				String[] nfoDirectors = nfoLine.trim().replace("<director>", "").replace("</director>", "").split(",");

				List<Director> nfoDirectorsList = new ArrayList<Director>();

				for (String nfoDirector : nfoDirectors) {

					Director dir = new Director();

					dir.setName(nfoDirector.trim());
					// dir.setThumb("www.foobar.com");

					nfoDirectorsList.add(dir);
					directors = nfoDirectorsList;
				}
			}

			if (nfoLine.trim().toLowerCase().startsWith("<name>") || nfoLine.trim().toLowerCase().startsWith("<role>")
					|| nfoLine.trim().toLowerCase().startsWith("<thumb>")) {

				if (nfoLine.trim().toLowerCase().startsWith("<name>")) {
					String nfoName = nfoLine.trim().replace("<name>", "").replace("</name>", "");
					setActorName(nfoName);
				}

				if (nfoLine.trim().toLowerCase().startsWith("<role>")) {
					String nfoRole = nfoLine.trim().replace("<role>", "").replace("</role>", "");
					setActorRole(nfoRole);
				}

				if (nfoLine.trim().toLowerCase().startsWith("<thumb>")) {
					String nfoThumb = nfoLine.trim().replace("<thumb>", "").replace("</thumb>", "");
					setActorThumb(nfoThumb);

					if (i != 6) {

						Actor actor = new Actor();

						actor.setName(actorName);
						actor.setRole(actorRole);
						actor.setThumb(actorThumb);

						nfoActors.add(actor);
					}
				}
			}
			i++;
		}
		actors = nfoActors;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return title + " (" + imdbID + ") - " + year;
	}

	/**
	 * @return the nfoFile
	 */
	public File getNfoFile() {
		return nfoFile;
	}

	/**
	 * @return the imdbID
	 */
	public String getImdbID() {
		return imdbID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @return the mpaa
	 */
	public String getMpaa() {
		return mpaa;
	}

	/**
	 * @return the outline
	 */
	public String getOutline() {
		return outline;
	}

	/**
	 * @return the runtime
	 */
	public int getRuntime() {
		return runtime;
	}

	/**
	 * @return the thumb
	 */
	public byte[] getThumb() {
		return thumb;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the genres
	 */
	public String[] getGenres() {
		return genres;
	}

	/**
	 * @return the directors
	 */
	public List<Director> getDirectors() {
		return directors;
	}

	/**
	 * @return the actors
	 */
	public List<Actor> getActors() {
		return actors;
	}

	/**
	 * @param actorName
	 *            the actorName to set
	 */
	public static void setActorName(String actorName) {
		NfoFile.actorName = actorName;
	}

	/**
	 * @param actorRole
	 *            the actorRole to set
	 */
	public static void setActorRole(String actorRole) {
		NfoFile.actorRole = actorRole;
	}

	/**
	 * @param actorThumb
	 *            the actorThumb to set
	 */
	public static void setActorThumb(String actorThumb) {
		NfoFile.actorThumb = actorThumb;
	}

}