/*******************************************************************************
 * Copyright (c) 2014 Derigible Enterprises.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Derigible Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.derigible.com/license
 *
 * Contributors:
 *     Derigible Enterprises - initial API and implementation
 *******************************************************************************/
package derigible.transformations;

import java.io.File;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;
import derigible.transactions.Transactions;
import derigible.utils.FileU;

/**
 * @author marcphillips
 *
 * This acts as a controller factory to the client. The controller will be the storage
 * of all transactions back into whatever medium is using this interface.
 *
 * Because Transactions can also act as the controller to the model, it may not
 * be necessary to implement this class. Say, for example, that the data rests
 * in a database - use a Transactions class that makes use of this fact. This
 * Class is used primarily for the intent to transform flat files into usable data.
 */
abstract class TransformToStorage {
	protected String filename;
	protected File file;
	protected boolean toAppStorage = false;
	protected String dir;
	
	/**
	 * Set the location where the file is to be written.
	 * 
	 * @param location the file location
	 * @return the system file location path
	 */
	public void setFileWriteLocation(File location){
		file = location;
	}

	/**
	 * All classes that implement this interface use this method to push all
	 * transactions in the Transactions object to storage. 
	 */
    public abstract File transactions_to_storage(Transactions list) throws IOException;
    
    //TODO This really should be put in an intermediary class
    protected CSVWriter getCSVWriter() throws IOException{
    	if(filename.charAt(filename.length() - 4) != '.'){
    		filename += ".csv";
    	}
    	if(dir != null){
    		if(filename.charAt(0) != '/'){
    			filename = "/" + filename;
    		}
    		filename = dir + filename;
    	}
    	CSVWriter csv;
    	if(toAppStorage){
			csv = new CSVWriter(FileU.getFileWriterToDefaultLocation(filename));
			file = new File(System.getProperty("user.home") + "/Budgeteer/"+filename);
		} else {
			if(file == null){
				file = new File(System.getProperty("user.home") + "/Budgeteer/"+filename);
			}
			csv = new CSVWriter(FileU.getFileWriter(file));
		}
    	return csv;
    }
    
    /**
	 * Set the filename of the csv. The csv is optional.
	 * 
	 * @param filename the filename
	 */
	public void setFileName(String filename){
		if(filename.charAt(0) != '/'){
			filename = "/" + filename;
		}
		this.filename = filename;
	}
	
	/**
	 * Make the directories as needed in the directory to store the Transformation.
	 * 
	 * @param dirs the directories
	 */
	public void mkDirs(String dirs){
		if(dirs.charAt(0) != '/'){
			dirs = "/" + dirs;
		}
		File create = new File(System.getProperty("user.home") + "/Budgeteer"+dirs);
		if(!create.isDirectory()){
			create.mkdirs();
		}
	}
	
	/**
	 * Set the directory to store the file within the Budgeteer folder.
	 * 
	 * @param dir the directory to store in
	 */
	public void setDir(String dir){
		if(dir.charAt(0) != '/'){
			dir = "/" + dir;
		}
		this.dir = dir;
	}
}
