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
	 * transactions in the Transactions object to storage. This is the only
	 * defined method of a TransformToStorage object.
	 */
    public abstract File transactions_to_storage(Transactions list) throws IOException;
    
    protected CSVWriter getCSVWriter() throws IOException{
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
		this.filename = filename;
	}
}
