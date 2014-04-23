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

import au.com.bytecode.opencsv.CSVWriter;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.utils.FileU;

/**
 * @author marcphillips
 *
 */
public class TransactionsToCSV implements TransformToStorage {
	private String filename = "transactions.csv";
	private boolean exclude = false;

	/* (non-Javadoc)
	 * @see derigible.transformations.TransformToStorage#transactions_to_storage(derigible.transactions.Transactions)
	 */
	@Override
	public void transactions_to_storage(Transactions list) throws IOException {
		CSVWriter csv = new CSVWriter(FileU.getFileWriterToDefaultLocation(filename));
		String[] headers = {"id", "month", "day", "year", "description", "amount",
				"category", "account", "debit_or_credit", "original_description", "note"};
		if(exclude){
			headers[0] = "";
		}
		csv.writeNext(headers);
		String[] row;
		for(Transaction t : list.getTransactions()){	
			row = new String[11];
			if(!exclude){		
				row[0] = t.getGUID();	
			} 
			GregorianCalendar c = t.getDate();
			row[1] = Integer.toString(c.get(Calendar.MONTH) + 1);
			row[2] = Integer.toString(c.get(Calendar.DAY_OF_MONTH));		
			row[3] = Integer.toString(c.get(Calendar.YEAR));
			row[4] = t.getDescription();
			row[5] = Double.toString(t.getAmount());
			row[6] = t.getCategory();
			row[7] = t.getAccount();
			row[8] = t.isCredit() ? "credit" : "debit";
			row[9] = t.getOriginalDescription();
			row[10] = t.getNotes();
			csv.writeNext(row);
		}
		csv.close();
	}
	
	/**
	 * Set the filename of the csv. The csv is optional.
	 * 
	 * @param filename the filename
	 */
	public void setFileName(String filename){
		this.filename = filename;
	}
	
	/**
	 * Set whether or not the id should be included in the csv output.
	 * 
	 * @param exclude set true to exclude
	 */
	public void excludeId(boolean exclude){
		this.exclude = exclude;
	}
}
