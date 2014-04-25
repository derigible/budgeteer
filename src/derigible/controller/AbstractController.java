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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.TransformToTransactions;

/**
 * @author marcphillips
 *
 *	Created as a means to define package specific functions to be used across all 
 *	controllers that deal with transactions. This is more of a means to retrofit
 *	BudgetController to extend the functionallity in TransactionController.
 */
abstract class AbstractController {
	
	protected Transactions tlist = null;
	
	protected AbstractController(){
		//Empty constructor
	}

    /**
     * Creates the Transactions list stored in the controller.
     *
     * @param transformer the transformer object that outputs data to an array
     * @throws java.io.IOException problem reading data from transformer
     */
    protected AbstractController(TransformToTransactions transformer) throws IOException {
        tlist = transformer.data_to_transactions();
    }

    /**
     * This constructor is here merely to allow for updates to the primary
     * constructor (such as add throws Exception clauses) and not have to catch
     * the creation error in my tests.
     *
     * @param transformer the transformer to use
     * @param DONOTUSE DO NOT USE THIS CONSTRUCTOR
     */
    protected AbstractController(TransformToTransactions transformer, int DONOTUSE) {
        try {
            tlist = transformer.data_to_transactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Client should never have access directly to the transactions list. All
     * interactions should go through the controller or the Transactions object.
     * In this way the transactions can be kept encapsulated and away from the
     * client.
     *
     * @return the Transactions object
     */
    protected Transactions getTransactions() {
        return tlist;
    }
	

	 /**
     * Exclude all transactions of a given category.
     *
     * @param category the category of transactions to exclude
     * @param l the list of transactions to use
     */
    protected void excludeCategory(String category, List<Transaction> l) {
        for (Transaction t : tlist.filterByCategory(category, l)) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Exclude all transactions of a given category.
     *
     * @param category the category of transactions to exclude
     * @param l the list of transactions to use
     */
    protected void includeCategory(String category, List<Transaction> l) {
        for (Transaction t : tlist.filterByCategory(category, l)) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Exclude all transactions of given categories.
     *
     * @param categories the categories of transactions to exclude
     * @param l the list of transactions to use
     */
    protected void excludeCategories(String[] categories, List<Transaction> l) {
        for (Transaction t : tlist.filterByCategories(categories, l)) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Include all transactions in the given categories.
     *
     * @param categories the categories to include
     * @param l the list of transactions to use
     */
    protected void includeCategories(String[] categories, List<Transaction> l) {
        for (Transaction t : tlist.filterByCategories(categories, l)) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Exclude all transactions of a given date.
     *
     * @param date the date of transactions to exclude
     * @param l the list of transactions to use
     */
    protected void excludeDate(Date date, List<Transaction> l) {
        for (Transaction t : tlist.filterByDate(date, l)) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Include all transactions of the given date.
     *
     * @param date the date to include
     * @param l the list of transactions to use
     */
    protected void includeDate(Date date, List<Transaction> l) {
        for (Transaction t : tlist.filterByDate(date, l)) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Exclude all transactions between dates.
     *
     * @param start the beginning of the dates to exclude
     * @param end the end of the dates to exclude
     * @param l the list of transactions to use
     */
    protected void excludeBetweenDates(Date start, Date end, List<Transaction> l) {
        for (Transaction t : tlist.filterByDates(start, end, l)) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Exclude all transactions between dates.
     *
     * @param start the beginning of the dates to include
     * @param end the end of the dates to include
     * @param l the list of transactions to use
     */
    protected void includeBetweenDates(Date start, Date end, List<Transaction> l) {
        for (Transaction t : tlist.filterByDates(start, end, l)) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Exclude all transactions of given account.
     *
     * @param account the account of transactions to exclude
     * @param l the list of transactions to use
     */
    protected void excludeAccount(String account, List<Transaction> l) {
        for (Transaction t : tlist.filterByAccount(account, tlist.getTransactions())) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Include all transactions of given account.
     *
     * @param account the account of transactions to include
     * @param l the list of transactions to use
     */
    protected void includeAccount(String account, List<Transaction> l) {
        for (Transaction t : tlist.filterByAccount(account, tlist.getExcluded())) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Exclude all transactions of given accounts.
     *
     * @param accounts the accounts to exclude transactions
     * @param l the list of transactions to use
     */
    protected void excludeAccounts(String[] accounts, List<Transaction> l) {
        for (Transaction t : tlist.filterByAccounts(accounts, tlist.getTransactions())) {
            tlist.excludeTransaction(t);
        }
    }

    /**
     * Include all transactions of given accounts.
     *
     * @param accounts the accounts to include transactions
     * @param l the list of transactions to use
     */
    protected void includeAccounts(String[] accounts, List<Transaction> l) {
        for (Transaction t : tlist.filterByAccounts(accounts, tlist.getExcluded())) {
            tlist.includeTransaction(t);
        }
    }

    /**
     * Get the balance of credits and debits. In other words, what is the
     * current difference between cash I received versus cash I have spent.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param debits the list of transactions to use as debits
     * @param credits the list of transactions to use as credits
     * @return the current balance
     */
    protected double getCurrentBalance(List<Transaction> debits, List<Transaction> credits) {
		//NOTE: All methods will treat credits and debits from the view point of the user,
        // thus, a credit looks like income and a debit looks like payout on a bank statement
        double credited = 0;
        double debited = 0;
        for (Transaction t : debits) {
            debited += t.getAmount();
        }
        for (Transaction t : credits) {
            credited += t.getAmount();
        }
        return credited - debited;
    }

    /**
     * Get the current balance for the given account.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param account the account to return the balance
     * @param l the list of transactions to use
     * @return the balance of the account
     */
    protected double getCurrentBalanceForAccount(String account, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account, l)) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance of all accounts between dates specified.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @param l the list of transactions to use
     * @return the balance between these dates
     */
    protected double getBalanceBetweenDates(Date start, Date end, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        for (Transaction t : tlist.filterByDates(start, end, l)) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the specified account between the given dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @param account the account to check
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceBetweenDatesForAccount(Date start, Date end, String account, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account, tlist.filterByDates(start, end, l))) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the given category.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category balance to get
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategory(String category, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        for (Transaction t : tlist.filterByCategory(category, l)) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the given categories.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories' balance to get
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategories(String[] categories, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        for (String category : categories) {
            for (Transaction t : tlist.filterByCategory(category, l)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the given categories between the given dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories' balance to get
     * @param start the start of the period
     * @param end the end of the period
     * @param l0 the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoriesBetweenDates(String[] categories, Date start, Date end, List<Transaction> l0) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l = tlist.filterByDates(start, end, l0);
        for (String category : categories) {
            for (Transaction t : tlist.filterByCategory(category, l)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the category of the account.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to check
     * @param account the account for the category
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoryForAccount(String category, String account, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByAccount(account, l);
        for (Transaction t : tlist.filterByCategory(category, l0)) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the categories for the account provided.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to search by
     * @param account the account to search by
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoriesForAccount(String[] categories, String account, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByAccount(account, l);
        for (Transaction t : tlist.filterByCategories(categories, l0)) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the category of the account between the given dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoryForAccountBetweenDates(String category, String account, Date start, Date end, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByAccount(account, l);
        for (Transaction t : tlist.filterByDates(start, end, tlist.filterByCategory(category, l0))) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the categories for the account between the dates
     * provided.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByAccount(account, l);
        for (Transaction t : tlist.filterByDates(start, end, tlist.filterByCategories(categories, l0))) {
            if (t.isCredit()) {
                credited += t.getAmount();
            } else {
                debited += t.getAmount();
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the category for the accounts provided.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoryForAccounts(String category, String[] accounts, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByCategory(category,l);
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account, l0)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the categories in the selected accounts.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoriesForAccounts(String[] categories, String[] accounts, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByCategories(categories, l);
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account, l0)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the category in the selected accounts between the
     * provided dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByDates(start, end, tlist.filterByCategory(category, l));
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account, l0)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }

    /**
     * Get the balance for the categories in the accounts between the dates
     * given.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @param l the list of transactions to use
     * @return the balance
     */
    protected double getBalanceForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start, Date end, List<Transaction> l) {
        double credited = 0;
        double debited = 0;
        List<Transaction> l0 = tlist.filterByDates(start, end, tlist.filterByCategories(categories, l));
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account, l0)) {
                if (t.isCredit()) {
                    credited += t.getAmount();
                } else {
                    debited += t.getAmount();
                }
            }
        }
        return credited - debited;
    }
	
}
