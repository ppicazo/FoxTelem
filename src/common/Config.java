package common;

import gui.MainWindow;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

import decoder.HighSpeedBitStream;
import decoder.SourceIQ;
import telemetry.PayloadStore;
import decoder.SlowSpeedBitStream;
import telemetry.RawFrameQueue;

/**
 * FOX 1 Telemetry Decoder
 * @author chris.e.thompson g0kla/ac2cz
 *
 * Copyright (C) 2015 amsat.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * This class stores the configuration variables that are shared across the program.  They are saved
 * into a properties file so that they are available next time the program is run.
 * 
 * New properties should be added at the end of the list.  If the program is run and a property does not exist 
 * in the file, then loading halts and default values are set.  If the new values are at the end, then 
 * the user will preserve all of their values up to that point.
 * 
 */
public class Config {
	public static Properties properties; // Java properties file for user defined values
	public static String currentDir = "";  // this is the directory that the Jar file is in.  We read the spacecraft files from here

	public static String VERSION_NUM = "1.03";
	public static String VERSION = VERSION_NUM + " - 21 November 2015";
	public static final String propertiesFileName = "FoxTelem.properties";
	
	public static final String WINDOWS = "win";
	public static final String MACOS = "mac";
	public static final String LINUX = "lin";
	
	public static final Color AMSAT_BLUE = new Color(0,0,116);
	public static final Color AMSAT_RED = new Color(224,0,0);
	public static final Color PURPLE = new Color(123,6,130);
	public static final Color AMSAT_GREEN = new Color(0,102,0);

	public static SatelliteManager satManager;
	public static PassManager passManager;
	static Thread passManagerThread;
	
	public static PayloadStore payloadStore;
	static Thread payloadStoreThread;
	public static RawFrameQueue rawFrameQueue;
	// We have one queue for the whole application
	static Thread rawFrameQueueThread;
	//public static Filter filter = null; // This is set when the GUI initializes.  This decoder gets the filter from here
	//public static int currentSampleRate = 48000; // this is the actual sample rate we are using in the decoder and is not saved.  
	public static double filterFrequency = 200;
	
	public static Sequence sequence;
	
	/**
	 * These flags can be set to change the output types and operation
	 */
	public static int wavSampleRate = 48000; //44100; //192000;
	public static int scSampleRate = 48000; //44100; //192000;
	public static final String NO_SOUND_CARD_SELECTED = "NONE";
	public static final String DEFAULT_CALLSIGN = "NONE";
	public static final String DEFAULT_STATION = "NONE";
	public static final String DEFAULT_ALTITUDE = "NONE";
	public static final String DEFAULT_LATITUDE = "0.0";
	public static final String DEFAULT_LONGITUDE = "0.0";
	public static final String DEFAULT_LOCATOR = "XX00xx";
	public static String soundCard = NO_SOUND_CARD_SELECTED;
	public static String audioSink = NO_SOUND_CARD_SELECTED;
	public static int playbackSampleRate = 48000; //44100; //192000;
	static public boolean flipReceivedBits1 = false;  // FIXME - Quick Hack to see if this is an issue.  If this stays MUST go at end of config
	static public boolean flipReceivedBits2 = false;
	static public boolean recoverClock = true;
	static public boolean writeDebugWavFile = false;
	static public boolean debugValues = false;
	static public boolean debugPerformance = false;
	static public boolean debugClock = false;
	static public boolean debugBits = false;
	static public boolean debugFrames = false;
	static public boolean debugFieldValues = false;
	static public boolean debugCameraFrames = false;
	static public boolean debugBytes = false; // This prints the RAW bytes
	static public boolean debugAudioGlitches = false; 
	static public boolean debugSignalFinder = false;
	static public int DEBUG_COUNT = -1;
	static public boolean filterData = true; // Low Pass filter the data
	public static int filterIterations = 1; // The number of times to run the low pass filter.  Gain is applied only after the first run
	public static int filterLength = 2048;
	static public boolean useRSfec = true;
	static public boolean squelchAudio = true;
	static public boolean useAGC = true;
	static public boolean useRSerasures = true;
	static public boolean realTimePlaybackOfFile = false;
	public static int useFilterNumber = 0;
	public static boolean useLeftStereoChannel = true; // ***** true
    public static boolean highSpeed = false; // true if we are running the decoder at 9600 bps
    public static boolean iq = false; // true if we are running the decoder in IQ mode
    public static boolean eliminateDC = true;
    public static boolean viewFilteredAudio = true;
    public static boolean monitorFilteredAudio = false;
    public static boolean monitorAudio = true;
    public static boolean filterOutputAudio = true;
    public static boolean logging = false;
    public static boolean FIRST_RUN = true;  // only true of the very first run
    public static boolean useDDEforAzEl = false;
    public static boolean useDDEforFreq = false;
    
    public static String osName = "Unknown OS";
    public static String OS = "win";
    
    //ftp and sockets comms
    public static int ftpPeriod = 60; // time in mins
    public static String callsign = DEFAULT_CALLSIGN;
    public static String latitude = DEFAULT_LATITUDE;
    public static String longitude = DEFAULT_LONGITUDE;
    public static String maidenhead = DEFAULT_LOCATOR;
    public static String stationDetails = DEFAULT_STATION;
    public static String altitude = DEFAULT_ALTITUDE;
    static public boolean ftpFiles = false;
    
    // Server
    public static int serverTxPeriod = 5; // time in secs (no point being more frequent than time to download a frame)
    public static int serverRetryWaitPeriod = 10; // time in multiples of TxPeriod
    static public boolean uploadToServer = false;
    public static String primaryServer = "tlm.amsat.org";
    public static String secondaryServer = "tlm.amsat.us";
    public static String webSiteUrl = "http://www.amsat.org/tlm";
    public static boolean sendToBothServers = false;
    
    // These are not saved to the file
    public static int udpPort = 41041;
    public static int tcpPort = 41042;
    public static int serverPort = tcpPort;
    public static int serverProtocol = TlmServer.TCP; 
  //  public static int serverProtocol = TlmServer.UDP;
    
	// GUI properties
	public static int windowHeight = 750;
	public static int windowWidth = 850;
	public static int windowX = 100;
	public static int windowY = 100;
	public static int windowFcHeight = 600;
	public static int windowFcWidth = 600;
	public static int fcdFrequency = 145930;  // the default frequency we set the FCD to if this is a fresh install
	public static int selectedBin = 192/4; // the bin in the fcd display that was last selected
	public static final int DEFAULT_FROM_BIN = 0;
	public static final int DEFAULT_TO_BIN = SourceIQ.FFT_SAMPLES;
	public static int fromBin = DEFAULT_FROM_BIN; 
	public static int toBin = DEFAULT_TO_BIN; 
	public static String windowCurrentDirectory = "";
	public static String csvCurrentDirectory = "";
	public static String logFileDirectory = ""; // This is the directory that we write the decoded data into and any other log files
	public static String homeDirectory = ""; // This is the directory we write the properties in.  This allows us to find the other directories.
	public static String serverFileDirectory = ""; 
	static public boolean displayRawValues = false;
	static public boolean showLatestImage = false;
	static public boolean displayRawRadData = false;
	static public boolean displayUTCtime = true;
	static public boolean applyBlackmanWindow = false;
	public static boolean useLimiter = false;
	static public boolean showIF = false;
	static public boolean trackSignal = true;
	static public boolean findSignal = false;
	static public boolean storePayloads = true; // write the payloads to the flat file database
	
	static public int displayModuleFontSize = 12;
	static public int graphAxisFontSize = 12;
	static public boolean useNativeFileChooser = true;
	
	static public boolean showSNR = true;
	static public double SCAN_SIGNAL_THRESHOLD = 15d; // This is peak signal to average noise.  Strongest signal needs to be above this
	static public double ANALYZE_SNR_THRESHOLD = 6d; // This is average signal in the pass band to average noise outside the passband
	static public double BIT_SNR_THRESHOLD = 1.8d; 
	
	static public String newVersionUrl = "http://amsat.us/FoxTelem/version.txt";
	static public String serverParamsUrl = "http://amsat.us/FoxTelem/server.txt";
	static public String t0UrlPath = "http://amsat.us/FoxTelem/";
	static public String t0UrlFile = "T0.txt";
	static public boolean downloadT0FromServer = true;
	
	// Post V1.00
	static public boolean debugHerciFrames = false;
	
	// V1.03
	static public boolean autoDecodeSpeed = true;
	
	public static boolean missing() { 
		Config.homeDirectory = System.getProperty("user.home") + File.separator + ".FoxTelem";
		File aFile = new File(Config.homeDirectory + File.separator + propertiesFileName );
		if(!aFile.exists()){
			return true;
		}
		return false;
	}
	
	public static void setHome() {
		Config.homeDirectory = System.getProperty("user.home") + File.separator + ".FoxTelem";
		File aFile = new File(Config.homeDirectory);
		if(!aFile.isDirectory()){
			
			aFile.mkdir();
			//Log.errorDialog("GOOD", "Making directory: " + Config.homeDirectory);
			
		}
		if(!aFile.isDirectory()){
			Log.errorDialog("ERROR", "ERROR can't create the directory: " + aFile.getAbsolutePath() +  
					"\nFoxTelem needs to save the program settings in your home directroy.  It is either not accessible or not writable\n");
		}
		
		System.out.println("Set Home to: " + homeDirectory);
	}
	
	public static void init() {
		properties = new Properties();
		load();
		initSequence();
		
		// Work out the OS but dont save in the properties.  It miight be a different OS next time!
		osName = System.getProperty("os.name").toLowerCase();
		setOs();
		
		satManager = new SatelliteManager();
		initPayloadStore();
		initPassManager();
		// Start this last or we get a null pointer exception if it tries to access the data before it is loaded
		initServerQueue();
	}

	public static int getVersionMajor() {
		return parseVersionMajor(VERSION_NUM);
	}
	
	public static int parseVersionMajor(String ver) {
		String version[] = ver.split("\\.");
		return Integer.parseInt(version[0]);
	}
	
	public static int getVersionMinor() {
		return parseVersionMinor(VERSION_NUM);
	}

	public static int parseVersionMinor(String ver) {
		String version[] = ver.split("\\.");
		String min = version[1].replaceAll("\\D", "");
		return Integer.parseInt(min);
	}

	public static String getVersionPoint() {
		return parseVersionPoint(VERSION_NUM);
	}

	public static String parseVersionPoint(String ver) {
		String version = ver.substring(ver.length()-1, ver.length());
		if (version.matches("[0-9]+"))
			return null; // we have no point release
		return version;		
	}

	
	public static void initPassManager() {	
		passManager = new PassManager(satManager);
		passManagerThread = new Thread(passManager);
		passManagerThread.start();
	}

	
	public static void initPayloadStore() {	
		payloadStore = new PayloadStore();
		payloadStoreThread = new Thread(payloadStore);
		payloadStoreThread.start();
	}
	
	public static void initSequence() {
		try {
			sequence = new Sequence();
		} catch (IOException e) {
			Log.errorDialog("ERROR", "Could not create the Server Sequence, uploading to server not possible.\n"
					+ e.getMessage());
			e.printStackTrace(Log.getWriter());
			
		}
	}
	
	public static void initServerQueue() {
		
		if (rawFrameQueueThread != null) { rawFrameQueue.stopProcessing(); }
		rawFrameQueue = new RawFrameQueue();
		rawFrameQueueThread = new Thread(rawFrameQueue);
		rawFrameQueueThread.setUncaughtExceptionHandler(Log.uncaughtExHandler);
		rawFrameQueueThread.start();

	}
	
	private static void setOs() {
		if (osName.indexOf("win") >= 0) {
			OS = WINDOWS;
		} else if (osName.indexOf("mac") >= 0) {
			OS = MACOS;
		} else {
			OS = LINUX;
		}
	}
	
	public static boolean isWindowsOs() {
		if (OS == WINDOWS) {
			return true;
		}
		return false;
	}

	public static boolean isLinuxOs() {
		if (OS == LINUX) {
			return true;
		}
		return false;
	}

	public static boolean isMacOs() {
		if (OS == MACOS) {
			return true;
		}
		return false;
	}
	
	public static void saveGraphParam(String sat, String fieldName, String key, String value) {
		properties.setProperty("Graph" + sat + fieldName + key, value);
		//store();
	}
	
	public static String loadGraphValue(String sat, String fieldName, String key) {
		return properties.getProperty("Graph" + sat + fieldName + key);
	}
	
	public static void saveGraphIntParam(String sat, String fieldName, String key, int value) {
		properties.setProperty("Graph" + sat +  fieldName + key, Integer.toString(value));
		//store();
	}

	public static void saveGraphLongParam(String sat, String fieldName, String key, long value) {
		properties.setProperty("Graph" + sat +  fieldName + key, Long.toString(value));
		//store();
	}

	public static void saveGraphBooleanParam(String sat, String fieldName, String key, boolean value) {
		properties.setProperty("Graph" + sat +  fieldName + key, Boolean.toString(value));
		//store();
	}
	
	public static int loadGraphIntValue(String sat, String fieldName, String key) {
		try {
			return Integer.parseInt(properties.getProperty("Graph" + sat +  fieldName + key));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static long loadGraphLongValue(String sat, String fieldName, String key) {
		try {
			return Long.parseLong(properties.getProperty("Graph" + sat +  fieldName + key));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static boolean loadGraphBooleanValue(String sat, String fieldName, String key) {
		try {
			return Boolean.parseBoolean(properties.getProperty("Graph" + sat +  fieldName + key));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void save() {
		properties.setProperty("slowSpeedSyncWordSperation", Integer.toString(SlowSpeedBitStream.SLOW_SPEED_SYNC_WORD_DISTANCE));
		properties.setProperty("highSpeedSyncWordSperation", Integer.toString(HighSpeedBitStream.HIGH_SPEED_SYNC_WORD_DISTANCE));
		properties.setProperty("recoverClock", Boolean.toString(recoverClock));
		properties.setProperty("flipReceivedBits1", Boolean.toString(flipReceivedBits1));
		properties.setProperty("flipReceivedBits2", Boolean.toString(flipReceivedBits2));
		properties.setProperty("filterData", Boolean.toString(filterData));
		properties.setProperty("filterIterations", Integer.toString(filterIterations));
		properties.setProperty("filterLength", Integer.toString(filterLength));
		properties.setProperty("filterFrequency", Double.toString(filterFrequency));
		properties.setProperty("debugBits", Boolean.toString(debugBits));
		properties.setProperty("debugFrames", Boolean.toString(debugFrames));
		properties.setProperty("debugFieldValues", Boolean.toString(debugFieldValues));
		properties.setProperty("debugCameraFrames", Boolean.toString(debugCameraFrames));
		properties.setProperty("debugValues", Boolean.toString(debugValues));
		properties.setProperty("debugPerformance", Boolean.toString(debugPerformance));
		properties.setProperty("debugClock", Boolean.toString(debugClock));
		properties.setProperty("debugBytes", Boolean.toString(debugBytes));
		properties.setProperty("debugAudioGlitches", Boolean.toString(debugAudioGlitches));
		properties.setProperty("DEBUG_COUNT", Integer.toString(DEBUG_COUNT));
		properties.setProperty("writeDebugWavFile", Boolean.toString(writeDebugWavFile));
		properties.setProperty("useRSfec", Boolean.toString(useRSfec));
		properties.setProperty("squelchAudio", Boolean.toString(squelchAudio));
		properties.setProperty("useAGC", Boolean.toString(useAGC));
		properties.setProperty("useRSerasures", Boolean.toString(useRSerasures));
		properties.setProperty("realTimePlaybackOfFile", Boolean.toString(realTimePlaybackOfFile));
		properties.setProperty("useFilterNumber", Integer.toString(useFilterNumber));
		properties.setProperty("useLeftStereoChannel", Boolean.toString(useLeftStereoChannel));
		properties.setProperty("highSpeed", Boolean.toString(highSpeed));
		properties.setProperty("iq", Boolean.toString(iq));
		properties.setProperty("eliminateDC", Boolean.toString(eliminateDC));
		properties.setProperty("viewFilteredAudio", Boolean.toString(viewFilteredAudio));
		properties.setProperty("monitorFilteredAudio", Boolean.toString(monitorFilteredAudio));
		properties.setProperty("monitorAudio", Boolean.toString(monitorAudio));
		properties.setProperty("filterOutputAudio", Boolean.toString(filterOutputAudio));
		properties.setProperty("logging", Boolean.toString(logging));
		properties.setProperty("FIRST_RUN", Boolean.toString(FIRST_RUN));
		properties.setProperty("useDDEforAzEl", Boolean.toString(useDDEforAzEl));
		properties.setProperty("useDDEforFreq", Boolean.toString(useDDEforFreq));
		
		properties.setProperty("wavSampleRate", Integer.toString(wavSampleRate));
		properties.setProperty("scSampleRate", Integer.toString(scSampleRate));
//		properties.setProperty("wavSampleRate", Integer.toString(wavSampleRate));
		properties.setProperty("playbackSampleRate", Integer.toString(playbackSampleRate));
		properties.setProperty("soundCard", soundCard);
		properties.setProperty("audioSink", audioSink);
		
//		properties.setProperty("SPACECRAFT_ID", Integer.toString(SPACECRAFT_ID));
		
		//ftp
		properties.setProperty("ftpPeriod", Integer.toString(ftpPeriod));
		properties.setProperty("callsign", callsign);
		properties.setProperty("stationDetails", stationDetails);
		properties.setProperty("altitude", altitude);
		properties.setProperty("latitude", latitude);
		properties.setProperty("longitude", longitude);
		properties.setProperty("maidenhead", maidenhead);
		properties.setProperty("ftpFiles", Boolean.toString(ftpFiles));

		// Server
		properties.setProperty("serverTxPeriod", Integer.toString(serverTxPeriod));
		properties.setProperty("serverRetryWaitPeriod", Integer.toString(serverRetryWaitPeriod));
		properties.setProperty("uploadToServer", Boolean.toString(uploadToServer));
		properties.setProperty("primaryServer", primaryServer);
		properties.setProperty("secondaryServer", secondaryServer);


		// GUI
		properties.setProperty("windowHeight", Integer.toString(windowHeight));
		properties.setProperty("windowWidth", Integer.toString(windowWidth));
		properties.setProperty("windowX", Integer.toString(windowX));
		properties.setProperty("windowY", Integer.toString(windowY));
		properties.setProperty("fcdFrequency", Integer.toString(fcdFrequency));
		properties.setProperty("selectedBin", Integer.toString(selectedBin));
		properties.setProperty("windowCurrentDirectory", windowCurrentDirectory);
		properties.setProperty("csvCurrentDirectory", csvCurrentDirectory);
		properties.setProperty("logFileDirectory", logFileDirectory);
		properties.setProperty("homeDirectory", homeDirectory);
		properties.setProperty("windowFcHeight", Integer.toString(windowFcHeight));
		properties.setProperty("windowFcWidth", Integer.toString(windowFcWidth));
		properties.setProperty("displayRawValues", Boolean.toString(displayRawValues));
		properties.setProperty("showLatestImage", Boolean.toString(showLatestImage));
		properties.setProperty("displayRawRadData", Boolean.toString(displayRawRadData));
		
		properties.setProperty("displayUTCtime", Boolean.toString(displayUTCtime));
		properties.setProperty("storePayloads", Boolean.toString(storePayloads));
		properties.setProperty("trackSignal", Boolean.toString(trackSignal));
		properties.setProperty("findSignal", Boolean.toString(findSignal));
		
		properties.setProperty("displayModuleFontSize", Integer.toString(displayModuleFontSize));
		properties.setProperty("graphAxisFontSize", Integer.toString(graphAxisFontSize));

		properties.setProperty("useNativeFileChooser", Boolean.toString(useNativeFileChooser));

		// Version 1.01 settings
		properties.setProperty("debugSignalFinder", Boolean.toString(debugSignalFinder));
		properties.setProperty("serverProtocol", Integer.toString(serverProtocol));
		properties.setProperty("serverPort", Integer.toString(serverPort));
		properties.setProperty("showSNR", Boolean.toString(showSNR));
		properties.setProperty("SCAN_SIGNAL_THRESHOLD", Double.toString(SCAN_SIGNAL_THRESHOLD));
		properties.setProperty("ANALYZE_SNR_THRESHOLD", Double.toString(ANALYZE_SNR_THRESHOLD));
		properties.setProperty("BIT_SNR_THRESHOLD", Double.toString(BIT_SNR_THRESHOLD));
		properties.setProperty("serverParamsUrl", serverParamsUrl);
		properties.setProperty("sendToBothServers", Boolean.toString(sendToBothServers));
		properties.setProperty("downloadT0FromServer", Boolean.toString(downloadT0FromServer));
		
		// Version 1.02 settings
		properties.setProperty("serverFileDirectory", serverFileDirectory);
		properties.setProperty("webSiteUrl", webSiteUrl);
		
		// Version 1.03 settings
		properties.setProperty("debugHerciFrames", Boolean.toString(debugHerciFrames));
		properties.setProperty("autoDecodeSpeed", Boolean.toString(autoDecodeSpeed));
		store();
	}
	
	private static void store() {
		try {
			FileOutputStream fos = new FileOutputStream(Config.homeDirectory + File.separator + propertiesFileName);
			properties.store(fos, "Fox 1 Telemetry Decoder Properties");
			fos.close();
		} catch (FileNotFoundException e1) {
			Log.errorDialog("ERROR", "Could not write properties file. Check permissions on directory or on the file\n" +
					Config.homeDirectory + File.separator + propertiesFileName);
			e1.printStackTrace(Log.getWriter());
		} catch (IOException e1) {
			Log.errorDialog("ERROR", "Error writing properties file");
			e1.printStackTrace(Log.getWriter());
		}

	}
	
	public static void load() {
		// try to load the properties from a file
		try {
			FileInputStream fis = new FileInputStream(Config.homeDirectory + File.separator + propertiesFileName);
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			//System.out.println("Writing Default properties file");
			save();
		}
		try {
		recoverClock = Boolean.parseBoolean(getProperty("recoverClock"));
		SlowSpeedBitStream.SLOW_SPEED_SYNC_WORD_DISTANCE = Integer.parseInt(getProperty("slowSpeedSyncWordSperation"));
		HighSpeedBitStream.HIGH_SPEED_SYNC_WORD_DISTANCE = Integer.parseInt(getProperty("highSpeedSyncWordSperation"));
		flipReceivedBits1 = Boolean.parseBoolean(getProperty("flipReceivedBits1"));
		flipReceivedBits2 = Boolean.parseBoolean(getProperty("flipReceivedBits2"));
		filterData = Boolean.parseBoolean(getProperty("filterData"));
		filterIterations = Integer.parseInt(getProperty("filterIterations"));
		filterLength = Integer.parseInt(getProperty("filterLength"));
		filterFrequency = Double.parseDouble(getProperty("filterFrequency"));
		debugBits = Boolean.parseBoolean(getProperty("debugBits"));
		debugFrames = Boolean.parseBoolean(getProperty("debugFrames"));
		debugFieldValues = Boolean.parseBoolean(getProperty("debugFieldValues"));
		debugCameraFrames = Boolean.parseBoolean(getProperty("debugCameraFrames"));
		debugValues = Boolean.parseBoolean(getProperty("debugValues"));
		debugPerformance = Boolean.parseBoolean(getProperty("debugPerformance"));
		debugClock = Boolean.parseBoolean(getProperty("debugClock"));
		debugBytes = Boolean.parseBoolean(getProperty("debugBytes"));
		debugAudioGlitches = Boolean.parseBoolean(getProperty("debugAudioGlitches"));
		DEBUG_COUNT = Integer.parseInt(getProperty("DEBUG_COUNT"));
		writeDebugWavFile = Boolean.parseBoolean(getProperty("writeDebugWavFile"));
		useRSfec = Boolean.parseBoolean(getProperty("useRSfec"));
		squelchAudio = Boolean.parseBoolean(getProperty("squelchAudio"));
		useAGC = Boolean.parseBoolean(getProperty("useAGC"));
		useRSerasures = Boolean.parseBoolean(getProperty("useRSerasures"));
		realTimePlaybackOfFile = Boolean.parseBoolean(getProperty("realTimePlaybackOfFile"));
		useLeftStereoChannel = Boolean.parseBoolean(getProperty("useLeftStereoChannel"));
		highSpeed = Boolean.parseBoolean(getProperty("highSpeed"));
		iq = Boolean.parseBoolean(getProperty("iq"));
		eliminateDC = Boolean.parseBoolean(getProperty("eliminateDC"));
		viewFilteredAudio = Boolean.parseBoolean(getProperty("viewFilteredAudio"));
		monitorFilteredAudio = Boolean.parseBoolean(getProperty("monitorFilteredAudio"));
		monitorAudio = Boolean.parseBoolean(getProperty("monitorAudio"));
		filterOutputAudio = Boolean.parseBoolean(getProperty("filterOutputAudio"));
		logging = Boolean.parseBoolean(getProperty("logging"));
		FIRST_RUN = Boolean.parseBoolean(getProperty("FIRST_RUN"));
		useDDEforAzEl = Boolean.parseBoolean(getProperty("useDDEforAzEl"));
		useDDEforFreq = Boolean.parseBoolean(getProperty("useDDEforFreq"));
		
		useFilterNumber = Integer.parseInt(getProperty("useFilterNumber"));
		wavSampleRate = Integer.parseInt(getProperty("wavSampleRate"));
		scSampleRate = Integer.parseInt(getProperty("scSampleRate"));
//		wavSampleRate = Integer.parseInt(getProperty("wavSampleRate"));
		playbackSampleRate = Integer.parseInt(getProperty("playbackSampleRate"));
		soundCard = getProperty("soundCard");
		if (soundCard == null) soundCard = NO_SOUND_CARD_SELECTED;
		audioSink = getProperty("audioSink");
		if (audioSink == null) audioSink = NO_SOUND_CARD_SELECTED;
//		SPACECRAFT_ID = Integer.parseInt(getProperty("SPACECRAFT_ID"));
		
		//spacecraft = new Spacecraft(SPACECRAFT_ID);
		
		//ftp
		ftpPeriod = Integer.parseInt(getProperty("ftpPeriod"));
		callsign = getProperty("callsign");
		stationDetails = getProperty("stationDetails");
		altitude = getProperty("altitude");
		latitude = getProperty("latitude");
		longitude = getProperty("longitude");
		maidenhead = getProperty("maidenhead");
		ftpFiles = Boolean.parseBoolean(getProperty("ftpFiles"));

		// Server
		serverTxPeriod = Integer.parseInt(getProperty("serverTxPeriod"));
		serverRetryWaitPeriod = Integer.parseInt(getProperty("serverRetryWaitPeriod"));
		uploadToServer = Boolean.parseBoolean(getProperty("uploadToServer"));
		primaryServer = getProperty("primaryServer");
		secondaryServer = getProperty("secondaryServer");


		// GUI
		windowHeight = Integer.parseInt(getProperty("windowHeight"));
		windowWidth = Integer.parseInt(getProperty("windowWidth"));
		windowX = Integer.parseInt(getProperty("windowX"));
		windowY = Integer.parseInt(getProperty("windowY"));
		fcdFrequency = Integer.parseInt(getProperty("fcdFrequency"));
		selectedBin = Integer.parseInt(getProperty("selectedBin"));
		windowCurrentDirectory = getProperty("windowCurrentDirectory");
		if (windowCurrentDirectory == null) windowCurrentDirectory = "";
		csvCurrentDirectory = getProperty("csvCurrentDirectory");
		if (csvCurrentDirectory == null) csvCurrentDirectory = "";
		logFileDirectory = getProperty("logFileDirectory");
		if (logFileDirectory == null) logFileDirectory = "";
		homeDirectory = getProperty("homeDirectory");
		if (homeDirectory == null) homeDirectory = "";
		windowFcHeight = Integer.parseInt(getProperty("windowFcHeight"));
		windowFcWidth = Integer.parseInt(getProperty("windowFcWidth"));
		displayRawValues = Boolean.parseBoolean(getProperty("displayRawValues"));
		showLatestImage = Boolean.parseBoolean(getProperty("showLatestImage"));
		displayRawRadData = Boolean.parseBoolean(getProperty("displayRawRadData"));
		
		displayUTCtime = Boolean.parseBoolean(getProperty("displayUTCtime"));
		storePayloads = Boolean.parseBoolean(getProperty("storePayloads"));
		trackSignal = Boolean.parseBoolean(getProperty("trackSignal"));
		findSignal = Boolean.parseBoolean(getProperty("findSignal"));
		
		displayModuleFontSize = Integer.parseInt(getProperty("displayModuleFontSize"));
		graphAxisFontSize = Integer.parseInt(getProperty("graphAxisFontSize"));
		
		useNativeFileChooser = Boolean.parseBoolean(getProperty("useNativeFileChooser"));
		
		// Version 1.01 settings
		debugSignalFinder = Boolean.parseBoolean(getProperty("debugSignalFinder"));
		serverProtocol = Integer.parseInt(getProperty("serverProtocol"));
		serverPort = Integer.parseInt(getProperty("serverPort"));
		showSNR = Boolean.parseBoolean(getProperty("showSNR"));
		SCAN_SIGNAL_THRESHOLD = Double.parseDouble(getProperty("SCAN_SIGNAL_THRESHOLD"));
		ANALYZE_SNR_THRESHOLD = Double.parseDouble(getProperty("ANALYZE_SNR_THRESHOLD"));
		BIT_SNR_THRESHOLD = Double.parseDouble(getProperty("BIT_SNR_THRESHOLD"));
		
		
		serverParamsUrl = getProperty("serverParamsUrl");
		sendToBothServers = Boolean.parseBoolean(getProperty("sendToBothServers"));
		downloadT0FromServer = Boolean.parseBoolean(getProperty("downloadT0FromServer"));
	
		// Version 1.02 settings
		serverFileDirectory = getProperty("serverFileDirectory");
		if (serverFileDirectory == null) serverFileDirectory = "";
		webSiteUrl = getProperty("webSiteUrl");
		if (webSiteUrl == null) webSiteUrl = "";
		
		//Version 1.03
		debugHerciFrames = Boolean.parseBoolean(getProperty("debugHerciFrames"));
		autoDecodeSpeed = Boolean.parseBoolean(getProperty("autoDecodeSpeed"));
		
		} catch (NumberFormatException nf) {
			catchException();
		} catch (NullPointerException nf) {
			catchException();
		}
	}
	
	private static String getProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) throw new NullPointerException();
		return value;
	}
	
	private static void catchException() {
		// Cant write to the log here as it is not initilized
		//Log.println("Could not read properties file. Likely Corrupt.");
		Object[] options = {"Yes",
        "Exit"};
		int n = JOptionPane.showOptionDialog(
				MainWindow.frame,
				"Could not read properties file. If this is a new release then the format has probablly been extended.\n"
				+ "Should I create a new properties file after reading as much as possible from the existing one?",
				"Error Loading " + Config.homeDirectory + File.separator + propertiesFileName,
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.ERROR_MESSAGE,
			    null,
			    options,
			    options[0]);
					
		if (n == JOptionPane.YES_OPTION) {
			save();
			Log.println("Created new properties file.");
		} else
			System.exit(1);

	}
}
