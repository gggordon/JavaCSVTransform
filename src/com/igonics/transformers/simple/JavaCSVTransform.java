package com.igonics.transformers.simple;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.relique.jdbc.csv.CsvDriver;

import com.igonics.transformers.simple.helpers.CSVDirWalker;
import com.igonics.transformers.simple.helpers.CSVLogger;


/**
 * @author gggordon <https://github.com/gggordon>
 * @version 1.0.0
 * @description Transforms sub-directories of similar CSV files into one database file
 * @created 1.11.2015
 *  
 * */
public class JavaCSVTransform {
	
	/**
	 * @param baseDir Base Directory to Check for CSV Files
	 * @param dbFile Database File Name or Path
	 * @param subDirectoryDepth Recursive Depth to check for CSV Files. -1 will recurse indefinitely
	 * @param keepTemporaryFile Keep Temporary Buffer File or Delete
	 * */
	public void createCSVDatabase(String baseDir, String dbFile,int subDirectoryDepth,boolean keepTemporaryFile){
		final String BASE_DIR =baseDir==null? System.getProperty("user.dir") + "\\dataFiles" : baseDir;
		final String DB_FILE = dbFile==null?"DB-" + System.currentTimeMillis() + ".csv":dbFile;
		long startTime = System.currentTimeMillis();
        
		CSVLogger.info("Base Dir : " + BASE_DIR);

		try {
            
			CSVDirWalker dirWalker = new CSVDirWalker(BASE_DIR, subDirectoryDepth);
			//Process Directories
			dirWalker.start();
			
			CSVLogger.debug("Column Names : " + dirWalker.getHeader());
			CSVLogger.info("Temporary Buffer File Complete. Starting Database Queries");
			// Writing to database
			// Load the driver.
			Class.forName("org.relique.jdbc.csv.CsvDriver");

			// Create a connection. The first command line parameter is the directory containing the .csv files.
			Connection conn = DriverManager.getConnection("jdbc:relique:csv:"
					+ System.getProperty("user.dir"));

			// Create a Statement object to execute the query with.
			Statement stmt = conn.createStatement();

			
			ResultSet results = stmt.executeQuery("SELECT * FROM "
					+ dirWalker.getTempBufferPath().replaceAll(".csv", ""));
			CSVLogger.info("Retrieved Records From Temporary File");

			// Dump out the results to a CSV file with the same format
			// using CsvJdbc helper function
			CSVLogger.info("Writing Records to database file");
			
			long databaseSaveStartTime = System.currentTimeMillis();
			//Create redirect stream to database file
			PrintStream printStream = new PrintStream(DB_FILE);
			//print column headings
			printStream.print(dirWalker.getHeader()+System.lineSeparator());
			CsvDriver.writeToCsv(results, printStream, false);
			CSVLogger.info("Time taken to save records to database (ms): "+(System.currentTimeMillis() - databaseSaveStartTime));
			
			//delete temporary file
			if(!keepTemporaryFile){
			    CSVLogger.info("Removing Temporary File");
			    dirWalker.removeTemporaryFile();
			}
			
			//Output Program Execution Completed
			CSVLogger.info("Total execution time (ms) : "
					+ (System.currentTimeMillis() - startTime)
					+ " | Approx Size (bytes) : "
					+ dirWalker.getTotalBytesRead());
		} catch (Exception ioe) {
			CSVLogger.error(ioe.getMessage(), ioe);
		}
	}
	
	// TODO: Modularize Concepts
	public static void main(String args[]) {
		//Parse Command Line Options
		Options opts = new Options();
		HelpFormatter formatter = new HelpFormatter();
		opts.addOption("d", "dir", false, "Base Directory of CSV files. Default : Current Directory");
		opts.addOption("db", "database", false, "Database File Name. Default DB-{timestamp}.csv");
		opts.addOption("depth", "depth", false, "Recursive Depth. Set -1 to recurse indefintely. Default : -1");
		opts.addOption("keepTemp",false,"Keeps Temporary file. Default : false");
		opts.addOption("h", "help", false, "Display Help");
		try {
			CommandLine cmd = new DefaultParser().parse(opts,args);
			if(cmd.hasOption("h") || cmd.hasOption("help")){
				formatter.printHelp( "javacsvtransform", opts );
				return;
			}
			//Create CSV Database With Command Line Options or Defaults
			new JavaCSVTransform().createCSVDatabase(cmd.getOptionValue("d"), cmd.getOptionValue("db"),Integer.parseInt(cmd.getOptionValue("depth", "-1")), cmd.hasOption("keepTemp"));
		} catch (ParseException e) {
			formatter.printHelp( "javacsvtransform", opts );
		}
	}
}
