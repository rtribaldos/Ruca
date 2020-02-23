package com.ruca.config;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogsManager {
	
	private static final Logger logger = Logger.getLogger(LogsManager.class.getName()); 
	
	LogsManager() {
		super();
	}
	
	public static void showError(String message) {
		logger.log(Level.SEVERE, message);
	}
	
	public static void showError(String message, Throwable throwable) {
		logger.log(Level.SEVERE, message, throwable);
	} 
	
	public static void showWarn(String message) {
		logger.log(Level.WARNING, message);
	}
	
	public static void showWarn(String message, Throwable throwable) {
		logger.log(Level.WARNING, message, throwable);
	} 
	
	public static void showInfo(String message) {
		logger.info(message);
	}
	
	public static void showInfo(String message, Throwable throwable) {
		logger.log(Level.INFO, message, throwable);
	}
}
