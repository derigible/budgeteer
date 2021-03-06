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
package derigible.controller.abstracts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import derigible.saves.Saved;
import derigible.transactions.abstracts.Transaction;
import derigible.transactions.abstracts.Transactions;

/**
 * @author marcphillips
 * 
 *         Defines the controller objects shared methods.
 */
public abstract class AbstractController implements Saved {

    private boolean saved = true;

    /**
     * Returns a Transactions object of the transactions within this controller.
     * 
     * @return the transactions object
     */
    public abstract Transactions getTransactions();

    /**
     * Add a single transaction to the budget.
     * 
     * @param t
     *            transaction to add
     */
    public abstract void addTransaction(Transaction t);

    /**
     * Add an array of transactions to the budget.
     * 
     * @param trans
     *            the transaction array
     */
    public void addTransactions(Transaction[] trans) {
	for (Transaction t0 : trans) {
	    addTransaction(t0);
	}
    }

    /**
     * Remove the transaction from the budget. Does nothing if not found.
     * 
     * @param t
     *            the transaction to remove
     */
    public void removeTransaction(Transaction t) {
	removeTransaction(t.getGUID());
    }

    /**
     * Remove the transaction from the budget by the GUID. Does nothing if not
     * found.
     * 
     * @param GUID
     *            the GUID of the transaction to remove.
     */
    public abstract void removeTransaction(String GUID);

    /**
     * Remove a list of transactions from the controller.
     * 
     * @param tlist
     *            the array of transactions to remove
     */
    public void removeTransactions(Transaction[] tlist) {
	for (Transaction t : tlist) {
	    removeTransaction(t);
	}
    }

    /**
     * The export method of all transactions in the controller.
     * 
     * @param filename
     *            the file to put the output
     * @param toAppStorage
     *            determines if this should go to app storage
     * @return the csv file object
     * @throws IOException
     */
    public abstract File transactionsToCSV(String filename, boolean toAppStorage)
	    throws IOException;

    /**
     * Calculate the balance of the list provided.
     * 
     * @param l
     *            the list of transactions
     * @return the balance
     */
    protected double calculate(List<Transaction> l) {
	double credited = 0;
	double debited = 0;
	for (Transaction t : l) {
	    if (t.isCredit()) {
		credited += t.getAmount();
	    } else {
		debited += t.getAmount();
	    }
	}
	return credited - debited;
    }

    /**
     * Calculates the amount for credits. Encapsulated in case changes to credit
     * handling in the future is deemed necessary.
     * 
     * @param l
     *            the list of credit transactions
     * @return the credited amount
     */
    protected double calculateCredits(List<Transaction> l) {
	double credit = 0;
	for (Transaction t : l) {
	    credit += t.getAmount();
	}
	return credit;
    }

    /**
     * Calculates the amount for debits. Encapsulated in case changes to debits
     * handling in the future is deemed necessary.
     * 
     * @param l
     *            the list of debit transactions
     * @return the debited amount
     */
    protected double calculateDebits(List<Transaction> l) {
	double debit = 0;
	for (Transaction t : l) {
	    debit += t.getAmount();
	}
	return debit;
    }

    @Override
    public boolean saved() {
	return saved;
    }

    @Override
    public void toggleSaved() {
	this.saved = !this.saved;
    }

    @Override
    public File save(String filename) {
	try {
	    return this.transactionsToCSV(filename, true);
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Get the name of the Controller.
     * 
     * @return the budget name
     */
    @Override
    public abstract String getName();
}
