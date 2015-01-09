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
import derigible.transactions.SubTransaction;
import derigible.transactions.abstracts.Transaction;
import derigible.transactions.abstracts.Transactions;
import derigible.transformations.abstracts.TransformToStorage;

/**
 * @author marcphillips
 * 
 */
public class TransactionsToCSV extends TransformToStorage {

    public TransactionsToCSV(String filename, boolean toAppStorage) {
	if (filename.charAt(filename.length() - 4) != '.') {
	    filename += ".csv";
	}
	this.filename = filename;
	this.toAppStorage = toAppStorage;
    }

    public TransactionsToCSV() {
	filename = "transactions.csv";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * derigible.transformations.TransformToStorage#transactions_to_storage(
     * derigible.transactions.Transactions)
     */
    @Override
    public File transactions_to_storage(Transactions list) throws IOException {
	CSVWriter csv = this.getCSVWriter();
	int offset = 0;
	String[] headers = { "id", "month", "day", "year", "description",
		"amount", "category", "account", "debit_or_credit",
		"original_description", "note", "parent_transaction" };
	if (!toAppStorage) {
	    headers = new String[] { "month", "day", "year", "description",
		    "amount", "category", "account", "debit_or_credit",
		    "original_description", "note" };
	    offset = 1;
	}
	csv.writeNext(headers);
	String[] row;
	for (Transaction t : list.getTransactions()) {
	    if (toAppStorage) {
		row = new String[12 - offset];
		row[0] = t.getGUID();
	    } else {
		row = new String[10];
	    }
	    GregorianCalendar c = t.getDate();
	    row[1 - offset] = Integer.toString(c.get(Calendar.MONTH) + 1);
	    row[2 - offset] = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
	    row[3 - offset] = Integer.toString(c.get(Calendar.YEAR));
	    row[4 - offset] = t.getDescription();
	    row[5 - offset] = Double.toString(t.getAmount());
	    row[6 - offset] = t.getCategory();
	    row[7 - offset] = t.getAccount();
	    row[8 - offset] = t.isCredit() ? "credit" : "debit";
	    row[9 - offset] = t.getOriginalDescription();
	    row[10 - offset] = t.getNotes();
	    if (t.getClass().equals(SubTransaction.class) && toAppStorage) {
		System.out.println("What up.");
		SubTransaction st = (SubTransaction) t;
		row[11] = st.getParent().getGUID();
	    } else if (toAppStorage) {
		row[11] = null;
	    }
	    csv.writeNext(row);
	}
	csv.close();
	return file;
    }
}
