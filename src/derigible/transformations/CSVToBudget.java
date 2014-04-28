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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import derigible.transactions.TList;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.utils.FileU;

/**
 * @author marcphillips
 *
 */
public class CSVToBudget implements TransformToTransactions {
	private CSVReader csv;
	private Transactions tlist;
	
	/**
	 * Creates a CSVToBudget object. A budget cannot be created until the transactions
	 * have been read in first as the transactions are retrieved by GUID.
	 * 
	 * @param file the file of the budget
	 * @param tl the transactions list
	 * @throws FileNotFoundException
	 */
	public CSVToBudget(File file, Transactions tl) throws FileNotFoundException{
		csv = new CSVReader(FileU.getFileReader(file));
		tlist = tl;
	}

	/* (non-Javadoc)
	 * @see derigible.transformations.TransformToTransactions#data_to_transactions()
	 */
	@Override
	public Transactions data_to_transactions() throws IOException {
		List<String[]> lines = null;
		
        try {
            lines = csv.readAll();
            if(lines.get(0)[0].length() > 1){
            	throw new IOException("Row too big. Should only be one row. Not a budget.");
            }
        } catch (IOException e) {
            throw new IOException("Problem reading the budget csv. Please make sure"
                    + "that the file is not corrupted in some way.");
        }
        Transaction[] trans = new Transaction[lines.size()];
        for(int i = 0; i < trans.length; i ++){
        	trans[i] = tlist.getTransactionByGUID(lines.get(i)[0]);
        }
		return new TList(trans);
	}

}
