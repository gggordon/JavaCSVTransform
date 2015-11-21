package com.igonics.transformers.simple.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author gggordon <https://github.com/gggordon>
 * @version 1.0.0
 * @created 1.11.2015
 *  
 * @description
 * Concatenates all CSV files of sub-directories into a temporary file
 * */

public class CSVDirWalker extends ADirWalker {

	private String headerLine="";
	private long fileCount=0;
	private long totalBytesRead=0;
	private String tempBufferPath = "tempBuffer-" + System.currentTimeMillis()+ ".csv";
	private FileWriter tempBuffer=null;
	/**
	 * @param basePath
	 * @param depth
	 * @param maxExecutionTime
	 */
	public CSVDirWalker(String basePath, int depth, float maxExecutionTime) {
		super(basePath, depth, maxExecutionTime);
		CSVLogger.info("Temp Buffer File : "+tempBufferPath);
	}

	public CSVDirWalker() {
		super();
	}

	public CSVDirWalker(String basePath) {
		super(basePath);
	}

	public CSVDirWalker(String basePath, int depth) {
		super(basePath, depth);
	}

	public long getTotalBytesRead(){
		//TODO: Verify
		return totalBytesRead;
	}
	
	public String getHeader(){
		return headerLine;
	}
	
	public String[] getHeaderArray(){
		return headerLine == null ? 
				new String[0] : 
				headerLine.trim().replaceAll("\r\n", "").split(",");
	}
	
	public long totalFilesProcessed(){
		return fileCount;
	}
	public String getTempBufferPath() {
		return tempBufferPath;
	}

	public void setTempBufferPath(String tempBufferPath) {
		this.tempBufferPath = tempBufferPath;
	}

	@Override
	public void start(){
		IsRunning=true;
		startTime = System.currentTimeMillis();
		fileCount = totalBytesRead=0;
		headerLine="";
		try {

			tempBuffer = new FileWriter(tempBufferPath, false);
		    walk(basePath,depth);
		    tempBuffer.close();
		}catch(IOException e){
			CSVLogger.error("", e);
		}
		IsRunning=false;
	}

	/* (non-Javadoc)
	 * @see com.igonics.transformers.simple.helpers.ADirWalker#task(java.lang.String, java.lang.Boolean)
	 */
	@Override
	protected void task(String currentFilePath, Boolean isDir) {
		//CSVLogger.info("Current Path : "+currentFilePath+" | isDir : "+isDir);
		if (!isDir && currentFilePath != null && currentFilePath.endsWith(".csv") ) {
			
			
			try {
				String fileContents = "", 
					   line = null;
				int lineCount=0;
				
				FileReader fr = new FileReader(currentFilePath);
				BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null ){
                	if(lineCount ==0){
                		if(headerLine==""){
                		    //prevent duplicate column names
                			//may cause issues. 
                			//TODO: Remove Duplicate Columns entirely for security
                			String[] tempL = line.replaceAll("\r\n","").split(",");
                			for(String columnName : tempL){
                				if(headerLine.indexOf(columnName) < 0)
                					headerLine+=columnName+ (tempL[tempL.length-1]==columnName?"":",");
                			}
                			
                		}
                		lineCount++;	
                		
                	}else
                	    fileContents+=line+System.lineSeparator();
                	
                }
                br.close();
                
                totalBytesRead+=fileContents.length()+headerLine.length();
                fileCount++;
                if(fileCount==1)
                	tempBuffer.write(headerLine+System.lineSeparator());
				tempBuffer.write(fileContents);
				
			} catch (IOException e) {
                CSVLogger.error("Error occured while processing "+currentFilePath,e);
			}
		}

	}

	
	public void removeTemporaryFile() {
		try{
			File f = new File(tempBufferPath);
			f.delete();
		}catch(Exception e){
			CSVLogger.error("Unable to remove temporary file : "+tempBufferPath,e);
		}
		
	}

}
