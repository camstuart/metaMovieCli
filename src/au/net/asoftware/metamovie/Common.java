package au.net.asoftware.metamovie;

import java.io.File;
import java.io.FileNotFoundException;

import com.google.gson.Gson;

public abstract class Common {

	/**
	 * 
	 * @return file object as a JSON encoded string
	 */
	public String toJson() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	protected void checkFileExists(File file) throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException("The required file: [" + file.getAbsolutePath() + "] could not be found");
		}
	}

}
