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
package derigible.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import derigible.transactions.Transaction;
import derigible.transactions.Transactions;

/**
 * @author marcphillips
 *
 *	Defines the controller objects shared methods.
 */
abstract class AbstractController {
	
	/**
	 * Returns a Transactions object of the transactions within this controller.
	 * 
	 * @return the transactions object
	 */
	public abstract Transactions getTransactions();
	
    /**
     * The export method of all transactions in the controller.
     * 
     * @param filename the file to put the output
     * @param toAppStorage determines if this should go to app storage
     * @return the csv file object
     * @throws IOException 
     */
    public abstract File transactionsToCSV(String filename, boolean toAppStorage) throws IOException;
    
    /**
     * Calculate the balance of the list provided.
     * 
     * @param l the list of transactions
     * @return the balance
     */
    protected double calculate(List<Transaction> l){
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
     * @param l the list of credit transactions
     * @return the credited amount
     */
    protected double calculateCredits(List<Transaction> l){
    	double credit = 0;
    	for(Transaction t : l){
    		credit += t.getAmount();
    	}
    	return credit;
    }
    
    /**
     * Calculates the amount for debits. Encapsulated in case changes to debits
     * handling in the future is deemed necessary.
     * 
     * @param l the list of debit transactions
     * @return the debited amount
     */
    protected double calculateDebits(List<Transaction> l){
    	double debit = 0;
    	for(Transaction t : l){
    		debit += t.getAmount();
    	}
    	return debit;
    }
}
