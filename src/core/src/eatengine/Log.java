package eatengine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Log {
	private static boolean USE_LOG_FILE = false;
	private static FileHandler LOG_FILE;
	
	static {
		LOG_FILE = null;
		if (USE_LOG_FILE) {
			initLogFile();
		}
	}
	
	private static void initLogFile(){
		try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            String fileName = "myapp-" + dateFormat.format(currentDate) + ".log";
            
            LOG_FILE = new FileHandler(fileName, true);
		} catch (Exception e) {
			// Use java.util logging to except
			java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Log.class.getName());
			LOGGER.log(Level.SEVERE, "Error creating log file", e);
			LOG_FILE = null;
        }
	}
	
	public static void Debug(String message) {
		LogMessage(message, Level.CONFIG);
	}
	
	public static void Info(String message) {
		LogMessage(message, Level.INFO);
	}
	
	public static void Warning(String message) {
		LogMessage(message, Level.WARNING);
	}
	
	public static void Severe(String message) {
		LogMessage(message, Level.SEVERE);
	}
	
	public static void Error(String message) {
		LogMessage(message, Level.FINEST);
	}
	
	public static void Send(String message, Level logLevel) {
		LogMessage(message, logLevel);
	}
	
	private static void LogMessage(String message, Level logLevel) {
		String callerClassName = new Exception().getStackTrace()[2].getClassName();
		Logger logger = Logger.getLogger(callerClassName);
		if (LOG_FILE != null) {
			logger.addHandler(LOG_FILE);
		}
		logger.log(logLevel, message);
		logger.removeHandler(LOG_FILE);
	}
	
	public static void Dispose() {
		if (LOG_FILE != null) {
			LOG_FILE.flush();
			LOG_FILE.close();
		}
	}
}
