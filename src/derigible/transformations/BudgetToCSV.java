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
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;

/**
 * @author marcphillips
 *
 */
public class BudgetToCSV extends TransformToStorage {
	private File file;
	
	public BudgetToCSV(String budgetName, boolean toAppStorage){
		filename = "budgets/transactions_" + budgetName + ".csv"; //Save to budgets folder
		this.toAppStorage = toAppStorage;
		File test = new File(System.getProperty("user.home") + "/Budgeteer/budgets");
		if(!test.isDirectory()){ //Ensure budgets folder is there
			test.mkdir();
		}
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
