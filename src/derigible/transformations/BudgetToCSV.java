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
import java.util.Calendar;
import java.util.GregorianCalendar;

import au.com.bytecode.opencsv.CSVWriter;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.utils.FileU;

/**
 * @author marcphillips
 *
 */
public class BudgetToCSV extends TransformToStorage {
	private File file;
	
	public BudgetToCSV(String budgetName, boolean toAppStorage){
		filename = "transactions_" + budgetName + ".csv";
		this.toAppStorage = toAppStorage;
	}

	/* (non-Javadoc)
	 * @see derigible.transformations.TransformToStorage#transactions_to_storage(derigible.transactions.Transactions)
	 */
	@Override
	public File transactions_to_storage(Transactions list) throws IOException {
		CSVWriter csv = this.getCSVWriter();	
		String[] headers = {"id"};
		csv.writeNext(headers);
		String[] row;
		for(Transaction t : list.getTransactions()){	
			row = new String[11];	
			row[0] = t.getGUID();	
			csv.writeNext(row);
		}
		csv.close();
		return file;
	}
}
