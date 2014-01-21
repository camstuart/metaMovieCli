package au.net.asoftware.metamovie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Properties;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.net.asoftware.metamovie.utility.Directory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * @author Cameron Stuart
 * 
 */
public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class.getName());

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Base paths:
		File dataBasePath = null;
		File applicationBasePath = null;
		File logbackXmlFile = null;
		File propertiesFile = null;

		if (getHostname().equals("zion")) {

			dataBasePath = new File("D:/sde/data/movies");
			applicationBasePath = new File("D:/sde/workspaces/ggts/metaMovieCli");

		} else if (getHostname().equals("romulas")) {

			dataBasePath = new File("C:/sde/data/movies");
			applicationBasePath = new File("C:/sde/workspaces/ggts/metaMovieCli");

		} else if (getHostname().startsWith("RAVX")) {

			dataBasePath = new File("C:/sde/data/movies");
			applicationBasePath = new File("C:/sde/dev/ggts/metaMovieCli");

		} else {
			logger.error("What box are you working on? not one I know about");
			System.exit(1);
		}

		logbackXmlFile = new File(applicationBasePath.getAbsolutePath() + File.separator + "conf" + File.separator + getHostname() + "_logback.xml");
		propertiesFile = new File(applicationBasePath.getAbsolutePath() + File.separator + "conf" + File.separator + getHostname()
				+ "_metamovie.properties");

		/*
		 * Set up logging:
		 */
		Path logbackFile = FileSystems.getDefault().getPath(logbackXmlFile.getAbsolutePath());

		if (!Files.exists(logbackFile)) {
			throw new RuntimeException("Required logback file not found in: [" + logbackFile.toFile().getAbsolutePath() + "]");
		}

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure(logbackFile.toFile());
		} catch (JoranException je) {
			je.printStackTrace();
			System.exit(1);
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);

		logger.info("metaMovieCli starting up, whirr click...\n");

		// Load up application properties (config)
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));

		logger.debug("foo.bar from config file: [{}]", properties.getProperty("foo.bar"));

		Directory parentDirectory = new Directory(dataBasePath);

		// Array list of movie extensions supported
		SortedSet<String> movieExtensions = parentDirectory.getSupportedVideoTypes();

		Mongo mongo = new Mongo("localhost");

		mongo.dropDatabase("meta_movie");
		Morphia morphia = new Morphia();
		morphia.map(Movie.class);

		MovieDAO movieDAO = new MovieDAO(mongo, morphia, "meta_movie");

		// Get a list of directories under the basePath:
		// String[] movieDirectories = dataBasePath.list();

		// Loop over movie directories
		for (String movieDirectory : parentDirectory.getBasePath().list()) {

			if (movieDirectory.startsWith("_")) {
				logger.debug("Skipping: [{}]", movieDirectory);
				continue;
			}

			Movie movie = new Movie();

			File currentMovie = new File(dataBasePath + File.separator + movieDirectory);

			logger.info("-----------------------------------------------------");

			logger.info("Reading Movie Directory: [{}]", currentMovie.getAbsolutePath());

			// Get a list of files (assets) under the movie directory:
			String[] assetsInMovieDirectory = currentMovie.list();

			// Loop over movie assets, looking for NFO and supported movie type
			for (String assetInMovieDirectory : assetsInMovieDirectory) {

				File assetFile = new File(parentDirectory.getBasePath() + File.separator + movieDirectory + File.separator + assetInMovieDirectory);

				if (assetFile.getName().toLowerCase().trim().endsWith(".nfo")) {

					logger.info("Found the NFO file: [{}]", assetFile.getName());

					NfoFile nfoFile = new NfoFile(assetFile);
					movie.setNfoFile(nfoFile.getNfoFile());
					movie.setImdbID(nfoFile.getImdbID());
					movie.setTitle(nfoFile.getTitle());
					movie.setYear(nfoFile.getYear());
					movie.setRating(nfoFile.getRating());
					movie.setType(properties.getProperty("movie.type"));
					movie.setOutline(nfoFile.getOutline());
					movie.setMpaa(nfoFile.getMpaa());
					movie.setRuntime(nfoFile.getRuntime());
					movie.setThumb(nfoFile.getThumb());
					movie.setGenres(nfoFile.getGenres());
					movie.setDirectors(nfoFile.getDirectors());
					movie.setActors(nfoFile.getActors());

					continue;
				}

				// See if this file is a supported movie extension:
				for (String movieExtension : movieExtensions) {

					if (assetFile.getName().toLowerCase().trim().endsWith(movieExtension)) {

						// Found a supported movie:
						logger.info("Found supported video file: [{}]", assetFile.getName());

						VideoAttributes videoAttributes = new VideoAttributes(assetFile);

						// Get all video attribute data:
						movie.setVideoAttributes(videoAttributes);
						break;
					}

				}

			}

			logger.debug("Movie Object as JSON:\n\n{}\n", movie.toJson());

			Date now = new Date();

			logger.info("Current date (Java): [{}]", now.toString());

			// Store in mongodb:
			movie.setCreated(now);
			movie.setUpdated(now);
			movieDAO.save(movie);

			logger.info("Date as is [{}]", movie.getCreated().toString());
			if (movie.imdbID.isEmpty()) {
				logger.info("No video codec");
			} else {
				logger.info("Video codec [{}]", movie.getVideoAttributes().getVideoCodec());
			}

			/*
			 * List<Movie> movieList =
			 * movieDAO.getDatastore().createQuery(Movie.class).asList();
			 * 
			 * for (Movie m : movieList) { logger.info("Date as is [{}]",
			 * m.getCreated().toString()); if (m.imdbID.isEmpty()) {
			 * logger.info("No video codec"); } else {
			 * logger.info("Video codec [{}]",
			 * m.getVideoAttributes().getVideoCodec()); } //
			 * logger.info("Movie Object as JSON:\n\n{}\n", m.toJson()); }
			 */

		}

		// parentDirectory.fullReport(dataBasePath);

		parentDirectory.summaryReport(dataBasePath);

		logger.info("metaMovieCli shutting down, grind clunk...");
	}

	/**
	 * 
	 * @return String hostName
	 */
	private static String getHostname() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			String msg = "Unable to determine the systems hostname";
			System.err.println(msg);
			System.exit(1);
		}
		// byte[] ipAddr = addr.getAddress();
		String hostName = addr.getHostName();
		return hostName;
	}

}
