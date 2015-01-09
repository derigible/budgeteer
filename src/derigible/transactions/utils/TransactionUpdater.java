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

package derigible.transactions.utils;

import java.util.GregorianCalendar;

import derigible.controller.TransactionsController;
import derigible.transactions.TransUpdater;
import derigible.transactions.abstracts.Transaction;

/**
 * @author marcphillips
 *
 *         Utility class for the visual aspects of this program.
 */
public final class TransactionUpdater {
	private TransactionUpdater() {
		// Do nothing, not instantiable
	}

	public static boolean updateDate(Transaction t, GregorianCalendar gc, TransactionsController tc) {
		TransUpdater tu = new TransUpdater();
		tu.setCategory(t.getCategory());
		tu.setAccount(t.getAccount());
		tu.setDebitOrCredit(t.isCredit());
		tu.setDate(gc);
		return tc.getTransactions().updateTransaction(t.getGUID(), tu);
	}

	public static boolean updateCategory(Transaction t, String category, TransactionsController tc) {
		TransUpdater tu = new TransUpdater();
		tu.setCategory(category);
		tu.setAccount(t.getAccount());
		tu.setDebitOrCredit(t.isCredit());
		tu.setDate(t.getDate());
		return tc.getTransactions().updateTransaction(t.getGUID(), tu);
	}

	public static boolean updateAccount(Transaction t, String account, TransactionsController tc) {
		TransUpdater tu = new TransUpdater();
		tu.setCategory(t.getCategory());
		tu.setAccount(account);
		tu.setDebitOrCredit(t.isCredit());
		tu.setDate(t.getDate());
		return tc.getTransactions().updateTransaction(t.getGUID(), tu);
	}

	public static boolean updateCreditOrDebit(Transaction t, boolean credit, TransactionsController tc) {
		TransUpdater tu = new TransUpdater();
		tu.setCategory(t.getAccount());
		tu.setAccount(t.getAccount());
		tu.setDebitOrCredit(credit);
		tu.setDate(t.getDate());
		return tc.getTransactions().updateTransaction(t.getGUID(), tu);
	}

}
