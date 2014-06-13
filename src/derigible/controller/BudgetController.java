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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import derigible.transactions.TList;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.BudgetToCSV;

/**
 * @author marcphillips
 * 
 *         A controller that will store all of the budget transactions for a
 *         given budget and perform basic calculations and operations on that
 *         list. A budget has a name, and that name should be kept unique as it
 *         will be saved in app storage by that name. It is recommended that
 *         BudgetControllers be kept in a SET to ensure that only one is ever
 *         made. This will require checking to make sure that a BudgetController
 *         that already exists is not overwritten unless explicitly meant to.
 */
public class BudgetController extends AbstractController {
    private Transactions tlist;
    private final String NAME;
    private HashMap<String, Transaction> budget = new HashMap<String, Transaction>();

    /**
     * This class is made only by the TransactionsController. Takes in the
     * translist of the controller and creates a new budget object. An empty
     * budget list will be created with this option.
     * 
     * @param t
     *            the transactions of the system
     * @param name
     *            the name of the budget
     * @param inBudget
     *            the transactions in the budget
     */
    protected BudgetController(Transactions t, String name,
	    List<Transaction> inBudget) {
	NAME = name;
	init(t, inBudget);
    }

    /**
     * This class is made only by the TransactionsController. Takes in the
     * translist of the controller and creates a new budget object. An empty
     * budget list will be created with this option.
     * 
     * @param t
     *            the transactions of the system
     * @param name
     *            the name of the budget
     */
    protected BudgetController(Transactions t, String name) {
	NAME = name;
	init(t, new ArrayList<Transaction>());
    }

    private void init(Transactions t, List<Transaction> inBudget) {
	// Only constructable by the TransactionsController.
	tlist = t;
	for (Transaction t0 : inBudget) {
	    budget.put(t0.getGUID(), t0);
	}
    }

    @Override
    public Transactions getTransactions() {
	return new TList(budget.values().toArray(new Transaction[0]));
    }

    private ArrayList<Transaction> getValues() {
	return new ArrayList<Transaction>(budget.values());
    }

    @Override
    public void addTransaction(Transaction t) {
	budget.put(t.getGUID(), t);
    }

    @Override
    public void removeTransaction(String GUID) {
	budget.remove(GUID);
    }

    /**
     * Exclude all transactions of a given category for this budget. This action
     * removes the transaction from the budget list.
     * 
     * @param category
     *            the category of transactions to exclude
     */
    public void excludeCategory(String category) {
	for (Transaction t : tlist.filterByCategory(category, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions of a given category. This adds all transactions
     * of a category to the budget
     * 
     * @param category
     *            the category of transactions to exclude
     */
    public void includeCategory(String category) {
	for (Transaction t : tlist.getByCategory(category)) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Exclude all transactions of given categories to this budget. This action
     * removes the transaction from the budget list.
     * 
     * @param categories
     *            the categories of transactions to exclude
     */
    public void excludeCategories(String[] categories) {
	for (Transaction t : tlist.filterByCategories(categories, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions in the given categories. This adds all
     * transactions of the categories to the budget.
     * 
     * @param categories
     *            the categories to include
     */
    public void includeCategories(String[] categories) {
	for (Transaction t : tlist.getByCategories(categories)) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Exclude all transactions of a given date. These transactions are removed
     * from the budget.
     * 
     * @param date
     *            the date of transactions to exclude
     */
    public void excludeDate(Date date) {
	for (Transaction t : tlist.filterByDate(date, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions of the given date.
     * 
     * @param date
     *            the date to include
     */
    public void includeDate(Date date) {
	for (Transaction t : tlist.getByDate(date)) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Exclude all transactions between dates. Will remove from the budget.
     * 
     * @param start
     *            the beginning of the dates to exclude
     * @param end
     *            the end of the dates to exclude
     */
    public void excludeBetweenDates(Date start, Date end) {
	for (Transaction t : tlist.filterByDates(start, end, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions between dates.
     * 
     * @param start
     *            the beginning of the dates to include
     * @param end
     *            the end of the dates to include
     */
    public void includeBetweenDates(Date start, Date end) {
	for (Transaction t : tlist.getBetweenDates(start, end)) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Exclude all transactions of given account. Removes them from the budget.
     * 
     * @param account
     *            the account of transactions to exclude
     */
    public void excludeAccount(String account) {
	for (Transaction t : tlist.filterByAccount(account, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions of given account.
     * 
     * @param account
     *            the account of transactions to include
     */
    public void includeAccount(String account) {
	for (Transaction t : tlist.getByAccount(account)) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Exclude all transactions of given accounts.
     * 
     * @param accounts
     *            the accounts to exclude transactions
     */
    public void excludeAccounts(String[] accounts) {
	for (Transaction t : tlist.filterByAccounts(accounts, getValues())) {
	    budget.remove(t.getGUID());
	}
    }

    /**
     * Include all transactions of given accounts.
     * 
     * @param accounts
     *            the accounts to include transactions
     */
    public void includeAccounts(String[] accounts) {
	for (Transaction t : tlist.filterByAccounts(accounts,
		tlist.getExcluded())) {
	    budget.put(t.getGUID(), t);
	}
    }

    /**
     * Get the balance of credits and debits. In other words, what is the
     * current difference between cash I received versus cash I have spent.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param debits
     *            the list of transactions to use as debits
     * @param credits
     *            the list of transactions to use as credits
     * @return the current balance
     */
    public double getCurrentBalance(List<Transaction> debits) {
	return calculate(getValues());
    }

    /**
     * Get the current balance for the given account in the budget.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param account
     *            the account to return the balance
     * @param l
     *            the list of transactions to use
     * @return the balance of the account
     */
    public double getCurrentBalanceForAccount(String account) {
	return calculate(tlist.filterByAccount(account, getValues()));
    }

    /**
     * Get the balance of all accounts between dates specified.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance between these dates
     */
    public double getBalanceBetweenDates(Date start, Date end) {
	return calculate(tlist.filterByDates(start, end, getValues()));
    }

    /**
     * Get the balance for the specified account between the given dates.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @param account
     *            the account to check
     * @return the balance
     */
    public double getBalanceBetweenDatesForAccount(Date start, Date end,
	    String account) {
	return calculate(tlist.filterByAccount(account,
		tlist.filterByDates(start, end, getValues())));
    }

    /**
     * Get the balance for the given category.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param category
     *            the category balance to get
     * @return the balance
     */
    public double getBalanceForCategory(String category) {
	return calculate(tlist.filterByCategory(category, getValues()));
    }

    /**
     * Get the balance for the given categories.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories' balance to get
     * @return the balance
     */
    public double getBalanceForCategories(String[] categories) {
	double balance = 0;
	for (String category : categories) {
	    balance += calculate(tlist.filterByCategory(category, getValues()));
	}
	return balance;
    }

    /**
     * Get the balance for the given categories between the given dates.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories' balance to get
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance
     */
    public double getBalanceForCategoriesBetweenDates(String[] categories,
	    Date start, Date end) {
	double balance = 0;
	List<Transaction> l = tlist.filterByDates(start, end, getValues());
	for (String category : categories) {
	    balance += calculate(tlist.filterByCategory(category, l));
	}
	return balance;
    }

    /**
     * Get the balance for the category of the account.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param category
     *            the category to check
     * @param account
     *            the account for the category
     * @return the balance
     */
    public double getBalanceForCategoryForAccount(String category,
	    String account) {
	List<Transaction> l0 = tlist.filterByAccount(account, getValues());
	return calculate(tlist.filterByCategory(category, l0));
    }

    /**
     * Get the balance for the categories for the account provided.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories to search by
     * @param account
     *            the account to search by
     * @return the balance
     */
    public double getBalanceForCategoriesForAccount(String[] categories,
	    String account) {
	List<Transaction> l0 = tlist.filterByAccount(account, getValues());
	return calculate(tlist.filterByCategories(categories, l0));
    }

    /**
     * Get the balance for the category of the account between the given dates.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param category
     *            the category to filter by
     * @param account
     *            the account to search in
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance
     */
    public double getBalanceForCategoryForAccountBetweenDates(String category,
	    String account, Date start, Date end) {
	List<Transaction> l0 = tlist.filterByAccount(account, getValues());
	return calculate(tlist.filterByDates(start, end,
		tlist.filterByCategory(category, l0)));
    }

    /**
     * Get the balance for the categories for the account between the dates
     * provided.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories to filter by
     * @param account
     *            the account to search in
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance
     */
    public double getBalanceForCategoriesForAccountBetweenDates(
	    String[] categories, String account, Date start, Date end) {
	List<Transaction> l0 = tlist.filterByAccount(account, getValues());
	return calculate(tlist.filterByDates(start, end,
		tlist.filterByCategories(categories, l0)));
    }

    /**
     * Get the balance for the category for the accounts provided.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param category
     *            the category to filter by
     * @param accounts
     *            the accounts to search
     * @return the balance
     */
    public double getBalanceForCategoryForAccounts(String category,
	    String[] accounts) {
	double balance = 0;
	List<Transaction> l0 = tlist.filterByCategory(category, getValues());
	for (String account : accounts) {
	    balance += calculate(tlist.filterByAccount(account, l0));
	}
	return balance;
    }

    /**
     * Get the balance for the categories in the selected accounts.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories to filter by
     * @param accounts
     *            the accounts to search in
     * @return the balance
     */
    public double getBalanceForCategoriesForAccounts(String[] categories,
	    String[] accounts) {
	double balance = 0;
	List<Transaction> l0 = tlist
		.filterByCategories(categories, getValues());
	for (String account : accounts) {
	    balance += calculate(tlist.filterByAccount(account, l0));
	}
	return balance;
    }

    /**
     * Get the balance for the category in the selected accounts between the
     * provided dates.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param category
     *            the category to filter by
     * @param accounts
     *            the accounts to search in
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance
     */
    public double getBalanceForCategoryForAccountsBetweenDates(String category,
	    String[] accounts, Date start, Date end) {
	double balance = 0;
	List<Transaction> l0 = tlist.filterByDates(start, end,
		tlist.filterByCategory(category, getValues()));
	for (String account : accounts) {
	    balance += calculate(tlist.filterByAccount(account, l0));
	}
	return balance;
    }

    /**
     * Get the balance for the categories in the accounts between the dates
     * given.
     * 
     * Returns a negative number if debits &gt; credits.
     * 
     * @param categories
     *            the categories to filter by
     * @param accounts
     *            the accounts to search in
     * @param start
     *            the start of the period
     * @param end
     *            the end of the period
     * @return the balance
     */
    public double getBalanceForCategoriesForAccountsBetweenDates(
	    String[] categories, String[] accounts, Date start, Date end) {
	double balance = 0;
	List<Transaction> l0 = tlist.filterByDates(start, end,
		tlist.filterByCategories(categories, getValues()));
	for (String account : accounts) {
	    balance += calculate(tlist.filterByAccount(account, l0));
	}
	return balance;
    }

    /**
     * Get the name of the Budget.
     * 
     * @return the budget name
     */
    public String getName() {
	return NAME;
    }

    @Override
    public File transactionsToCSV(String filename, boolean toAppStorage)
	    throws IOException {
	BudgetToCSV csv = new BudgetToCSV(NAME + "_" + filename, toAppStorage);
	return csv.transactions_to_storage(new TList(getValues()));
    }

    /**
     * Create the budget CSV with the budgetname in the filename only.
     * 
     * @param toAppStorage
     *            store in app storage or not
     * @return the file that was created
     * @throws IOException
     */
    public File transactionsToCSV(boolean toAppStorage) throws IOException {
	BudgetToCSV csv = new BudgetToCSV(NAME, toAppStorage);
	return csv.transactions_to_storage(new TList(getValues()));
    }

    /**
     * Create the budget CSV with the budgetname as the filename in the
     * specified directory.
     * 
     * @param dir
     *            the directory to add the budget
     * @return the file of the budget
     * @throws IOException
     */
    public File transactionsToCSV(String dir) throws IOException {
	BudgetToCSV csv = new BudgetToCSV(NAME, true);
	csv.mkDirs(dir);
	csv.setDir(dir);
	return csv.transactions_to_storage(new TList(getValues()));
    }
}
