package au.net.asoftware.metamovie;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.annotations.Transient;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoAttributes extends Common {

	private final static Logger logger = LoggerFactory.getLogger(VideoAttributes.class.getName());

	private final static String UNKNOWN = "Unknown";

	@Transient
	private File videoFile;

	private long duration; // Minutes
	private String audioCodec;
	private int audioSampleRate;
	private int audioChannels;
	private String audioFormat;
	private String videoCodec;
	private String videoFormat;
	private double videoFrameRate;
	private long fileSize; // Megabytes
	private int videoWidth;
	private int heightWidth;

	public VideoAttributes() {
		super();
	}

	/**
	 * @param videoFile
	 * @throws FileNotFoundException
	 */
	public VideoAttributes(final File videoFile) throws FileNotFoundException {
		super();
		this.videoFile = videoFile;

		checkFileExists(this.videoFile);

		duration = 0;
		audioCodec = UNKNOWN;
		audioSampleRate = 0;
		audioChannels = 0;
		audioFormat = UNKNOWN;
		videoCodec = UNKNOWN;
		videoFormat = UNKNOWN;
		videoFrameRate = 0;
		fileSize = 0;
		videoWidth = 0;
		heightWidth = 0;

		fileSize = videoFile.length() / 1024 / 1024;
		logger.debug("Video size: [{}] (Megabytes)", fileSize);
		analyseVideo();

	}

	/**
	 * Analyse the video file and populate data members
	 */
	private void analyseVideo() {

		final String videoFileName = videoFile.getAbsolutePath();

		logger.debug("Analysing video file: [{}]", videoFileName);

		// Create a Xuggler container object
		final IContainer container = IContainer.make();

		// Open up the container
		if (container.open(videoFileName, IContainer.Type.READ, null) < 0) {
			throw new IllegalArgumentException("could not open file: " + videoFileName);
		}

		duration = container.getDuration() / 60000000;

		// query how many streams the call to open found
		int numStreams = container.getNumStreams();

		// and iterate through the streams to print their meta data
		for (int i = 0; i < numStreams; i++) {
			// Find the stream object
			IStream stream = container.getStream(i);
			// Get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = stream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {

				audioCodec = coder.getCodecID().toString();
				audioSampleRate = coder.getSampleRate();
				audioChannels = coder.getChannels();
				audioFormat = coder.getSampleFormat().toString();

			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

				videoCodec = coder.getCodecID().toString();
				videoWidth = coder.getWidth();
				heightWidth = coder.getHeight();
				videoFormat = coder.getPixelType().toString();
				videoFrameRate = coder.getFrameRate().getDouble();

			}
		}

	}

	/**
	 * @return the videoFile
	 */
	public File getVideoFile() {
		return videoFile;
	}

	/**
	 * @param videoFile
	 *            the videoFile to set
	 */
	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the audioCodec
	 */
	public String getAudioCodec() {
		return audioCodec;
	}

	/**
	 * @param audioCodec
	 *            the audioCodec to set
	 */
	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	/**
	 * @return the audioSampleRate
	 */
	public int getAudioSampleRate() {
		return audioSampleRate;
	}

	/**
	 * @param audioSampleRate
	 *            the audioSampleRate to set
	 */
	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}

	/**
	 * @return the audioChannels
	 */
	public int getAudioChannels() {
		return audioChannels;
	}

	/**
	 * @param audioChannels
	 *            the audioChannels to set
	 */
	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}

	/**
	 * @return the audioFormat
	 */
	public String getAudioFormat() {
		return audioFormat;
	}

	/**
	 * @param audioFormat
	 *            the audioFormat to set
	 */
	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}

	/**
	 * @return the videoCodec
	 */
	public String getVideoCodec() {
		return videoCodec;
	}

	/**
	 * @param videoCodec
	 *            the videoCodec to set
	 */
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	/**
	 * @return the videoFormat
	 */
	public String getVideoFormat() {
		return videoFormat;
	}

	/**
	 * @param videoFormat
	 *            the videoFormat to set
	 */
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}

	/**
	 * @return the videoFrameRate
	 */
	public double getVideoFrameRate() {
		return videoFrameRate;
	}

	/**
	 * @param videoFrameRate
	 *            the videoFrameRate to set
	 */
	public void setVideoFrameRate(double videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the videoWidth
	 */
	public int getVideoWidth() {
		return videoWidth;
	}

	/**
	 * @param videoWidth
	 *            the videoWidth to set
	 */
	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	/**
	 * @return the heightWidth
	 */
	public int getHeightWidth() {
		return heightWidth;
	}

	/**
	 * @param heightWidth
	 *            the heightWidth to set
	 */
	public void setHeightWidth(int heightWidth) {
		this.heightWidth = heightWidth;
	}

}