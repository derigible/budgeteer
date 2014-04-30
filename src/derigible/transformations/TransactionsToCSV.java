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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import derigible.transactions.SubTransaction;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;

/**
 * @author marcphillips
 *
 */
public class TransactionsToCSV extends TransformToStorage {
	
	public TransactionsToCSV(String filename, boolean toAppStorage){
		if(filename.charAt(filename.length() - 4) != '.'){
			filename += ".csv";
		}
		this.filename = filename;
		this.toAppStorage = toAppStorage;
	}
	
	public TransactionsToCSV(){
		filename = "transactions.csv";
	}

	/* (non-Javadoc)
	 * @see derigible.transformations.TransformToStorage#transactions_to_storage(derigible.transactions.Transactions)
	 */
	@Override
	public File transactions_to_storage(Transactions list) throws IOException {
		CSVWriter csv = this.getCSVWriter();
		String[] headers = {"id", "month", "day", "year", "description", "amount",
				"category", "account", "debit_or_credit", "original_description", "note", "parent_transaction"};
		if(!toAppStorage){
			headers[0] = "";
			headers[11] = "";
		}
		csv.writeNext(headers);
		String[] row;
		for(Transaction t : list.getTransactions()){
			row = new String[12];
			if(toAppStorage){	
				row = new String[12];
				row[0] = t.getGUID();	
			} else {
				row = new String[11];
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
			if(t.getClass().equals("SubTransaction") && toAppStorage){
				SubTransaction st = (SubTransaction) t;
				row[11] = st.getParent().getGUID();
			} else if(toAppStorage){
				row[11] = null;
			}
			csv.writeNext(row);
		}
		csv.close();
		return file;
	}
}
