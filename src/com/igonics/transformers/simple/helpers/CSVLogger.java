package com.igonics.transformers.simple.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gggordon <https://github.com/gggordon>
 * @version 1.0.0
 * @created 1.11.2015
 *  
 * @description
 * Static Logger Implementation
 * */
public class CSVLogger {
    private static Logger logger;
    private static boolean loggingOn;
    //TODO: Implement Log Level Constraints or import log4j
    //private static Level logLevel;
    
    static {
    	logger = Logger.getAnonymousLogger();
    	loggingOn = logger != null;
    }
    
    public static void logginOn(boolean turnOn){
    	loggingOn=turnOn;
    }
    
    public static void info(Object message){
    	if(loggingOn)logger.log(Level.INFO, (String)message);
    }
    
    public static void debug(Object message){
    	if(loggingOn)logger.log(Level.INFO, (String)message);
    }
    
    public static void warn(Object message){
    	if(loggingOn)logger.log(Level.WARNING, (String)message);
    }
    
    public static void fatal(Object message){
    	if(loggingOn)logger.log(Level.SEVERE, (String)message);
    }
    
    public static void fatal(Object message, Exception e){
    	if(loggingOn)logger.log(Level.SEVERE, (String)message,e);
    }
    
    public static void error(Object message){
    	if(loggingOn)logger.log(Level.SEVERE, (String)message);
    }
    
    public static void error(Object message, Exception e){
    	if(loggingOn)logger.log(Level.SEVERE, (String)message,e);
    }
    

    
}
