package com.igonics.transformers.simple.helpers;

import java.io.File;

/**
 * @author gggordon <https://github.com/gggordon>
 * @version 1.0.0
 * @description Abstract Directory Walker
 * @created 1.11.2015
 *  
 * */

//TODO: Implement Runnable Interface
public abstract class ADirWalker{

	/**
	 * Recursive Depth
	 * */
	protected int depth;
	/**
	 * Base Path of Directory
	 * */
	protected String basePath;
	
	private float maxExecutionTime;
	/**
	 * Whether the walker is still running
	 * */
	protected boolean IsRunning;
	/**
	 * Start Time
	 * */
	protected long startTime;
	private long endTime;
	
	/**
	 * @param basePath Directory Base Path
	 * @param depth Recursive Depth
	 * @param maxExecutionTime Maximum Execution Time
	 * */
	public ADirWalker(String basePath,int depth, float maxExecutionTime) {
		super();
		this.depth = depth;
		this.basePath = basePath;
		this.maxExecutionTime = maxExecutionTime;		
		this.IsRunning=false;
	}

	/**
     * @see com.igonics.transformers.simple.helpers.ADirWalker#ADirWalker(java.lang.String, java.lang.Integer, java.lang.Float)
     * */
	public ADirWalker() {
		//Start with current directory by default
		this(System.getProperty("user.dir"));
	}
    /**
     * @see com.igonics.transformers.simple.helpers.ADirWalker#ADirWalker(java.lang.String, java.lang.Integer, java.lang.Float)
     * */
	public ADirWalker(String basePath) {
		this(basePath,3);
	}

	/**
     * @see com.igonics.transformers.simple.helpers.ADirWalker#ADirWalker(java.lang.String, java.lang.Integer, java.lang.Float)
     * */
	public ADirWalker( String basePath,int depth) {
		this(basePath,depth,-1);
	}

	/**
	 * Start Execution
	 * */
	public void start(){
		IsRunning=true;
		startTime = System.currentTimeMillis();
		walk(basePath,depth);
		IsRunning =false;
		endTime = System.currentTimeMillis();
	}
	
	/*public void stop(){
		//TODO: Implement
	}*/
	
	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public float getMaxExecutionTime() {
		return maxExecutionTime;
	}

	public void setMaxExecutionTime(float maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
	}

	public Boolean IsRunning() {
		return IsRunning;
	}

	/**
     * @param currentFilePath Absolute File Path of File or Directory
     * @param isDir Is Directory
     * */
	protected abstract void task(String currentFilePath, Boolean isDir);

	private boolean hasExceededMaxExecutionTime(){
		if(maxExecutionTime < 0)
			return false;
		else return System.currentTimeMillis() - startTime > maxExecutionTime;
	}
	
	/**
	 * @param path  Base directory path
	 * @param depth Recursive Depth, Set to -1 to recurse indefinitely
	 * 
	 * Recursively walk and process directories
	 */
	protected void walk(String path, int depth) {
		long dirStartTime = System.currentTimeMillis();
		if (depth == 0 || hasExceededMaxExecutionTime())
			return;
		try {
			File root = new File(path);
			File[] list = root.listFiles();

			if (list == null)
				return;

			for (File f : list) {
				if(hasExceededMaxExecutionTime())
					return;
				task(f.getAbsolutePath(), f.isDirectory());
				walk(f.getAbsolutePath(), depth - 1);
			}
			CSVLogger.info(String.format("Processed %s : {%s} in %d ms",
					 (root.isDirectory()?"Directory":"File" ),
					 path,
					 System.currentTimeMillis() - dirStartTime));
		} catch (NullPointerException ne) {
            System.err.println(ne.getStackTrace());
		}
		
		
	}
	
}