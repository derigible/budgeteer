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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import derigible.transactions.Splittable;
import derigible.transactions.SubTransaction;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.CSVToBudget;
import derigible.transformations.TransactionsToCSV;

/**
 * @author marphill
 *
 */
public class TransactionsController extends AbstractController {

	private Transactions tlist = null;
	private String guid = null;

	/**
	 * Creates the Transactions list stored in the controller. Will be given a
	 * GUID, in essence creating a brand new TransactionsController.
	 *
	 * @param trans
	 *            the transactions list
	 */
	public TransactionsController(Transactions trans) {
		tlist = trans;
		guid = GUID.generate();
	}

	/**
	 * Creates a TransactionsController with the specified GUID. This is used
	 * when data for the controller is stored in files and not in a database.
	 * These files should all be stored in a directory named after this GUID.
	 *
	 * @param trans
	 *            the transactions list
	 * @param GUID
	 *            the GUID of the TransactionsController
	 */
	public TransactionsController(Transactions trans, String GUID) {
		tlist = trans;
		guid = GUID;
	}

	@Override
	public Transactions getTransactions() {
		return tlist;
	}

	@Override
	public void addTransaction(Transaction t) {
		this.tlist.addTransaction(t);
	}

	@Override
	public void removeTransaction(String GUID) {
		this.tlist.excludeTransaction(tlist.getTransactionByGUID(GUID));
	}

	/**
	 * Add a subtransaction to a given transaction and place it in the
	 * Transactions object.
	 *
	 * @param t
	 *            the transaction to add a SubTransaction to
	 * @param amount
	 *            the amount of the SubTransaction
	 * @return the SubTransaction
	 */
	public SubTransaction addSubTransaction(Splittable t, double amount) {
		return this.addSubTransaction(t, amount, "");
	}

	/**
	 * Add a subtransaction to a given transaction and place it in the
	 * Transactions object.
	 *
	 * @param t
	 *            the transaction to add a SubTransaction to
	 * @param amount
	 *            the amount of the SubTransaction
	 * @param description
	 *            the description of the SubTransaction
	 * @return the SubTransaction
	 */
	public SubTransaction addSubTransaction(Splittable t, double amount, String description) {
		SubTransaction st = new SubTransaction(t, amount, description);
		this.addTransaction(st);
		return st;
	}

	/**
	 * Add a subtransaction to a given transaction and place it in the
	 * Transactions object. Find the Transaction object by it's guid.
	 *
	 * @param Guid
	 *            guid of the transaction to add a SubTransaction to
	 * @param amount
	 *            the amount of the SubTransaction
	 * @return the SubTransaction
	 * @throws IOException
	 *             The Guid is to a transaction that is not splittable
	 */
	public SubTransaction addSubTransaction(String Guid, double amount) throws IOException {
		return this.addSubTransaction(Guid, amount, "");
	}

	/**
	 * Add a subtransaction to a given transaction and place it in the
	 * Transactions object. Find the Transaction object by it's guid.
	 *
	 * @param Guid
	 *            guid of the transaction to add a SubTransaction to
	 * @param amount
	 *            the amount of the SubTransaction
	 * @param description
	 *            the description of the SubTransaction
	 * @return the SubTransaction
	 * @throws IOException
	 *             The Guid is to a transaction that is not splittable
	 */
	public SubTransaction addSubTransaction(String Guid, double amount, String description) throws IOException {
		try {
			return this.addSubTransaction((Splittable) this.getTransactions().getTransactionByGUID(Guid), amount);
		} catch (ClassCastException e) {
			throw new IOException("Transaction with GUID: " + Guid + " is not of the Splittable type.");
		}
	}

	/**
	 * Exclude all transactions of a given category.
	 *
	 * @param category
	 *            the category of transactions to exclude
	 */
	public void excludeCategory(String category) {
		for (Transaction t : tlist.getByCategory(category)) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions of a given category.
	 *
	 * @param category
	 *            the category of transactions to exclude
	 */
	public void includeCategory(String category) {
		List<Transaction> l = tlist.getExcluded();
		for (Transaction t : tlist.filterByCategory(category, l)) {
			tlist.includeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions of given categories.
	 *
	 * @param categories
	 *            the categories of transactions to exclude
	 */
	public void excludeCategories(String[] categories) {
		for (Transaction t : tlist.getByCategories(categories)) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Include all transactions in the given categories.
	 *
	 * @param categories
	 *            the categories to include
	 */
	public void includeCategories(String[] categories) {
		List<Transaction> l = tlist.getExcluded();
		for (Transaction t : tlist.filterByCategories(categories, l)) {
			tlist.includeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions of a given date.
	 *
	 * @param date
	 *            the date of transactions to exclude
	 */
	public void excludeDate(Date date) {
		for (Transaction t : tlist.getByDate(date)) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Include all transactions of the given date.
	 *
	 * @param date
	 *            the date to include
	 */
	public void includeDate(Date date) {
		List<Transaction> l = tlist.getExcluded();
		for (Transaction t : tlist.filterByDate(date, l)) {
			tlist.includeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions between dates.
	 *
	 * @param start
	 *            the beginning of the dates to exclude
	 * @param end
	 *            the end of the dates to exclude
	 */
	public void excludeBetweenDates(Date start, Date end) {
		for (Transaction t : tlist.getBetweenDates(start, end)) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions between dates.
	 *
	 * @param start
	 *            the beginning of the dates to include
	 * @param end
	 *            the end of the dates to include
	 */
	public void includeBetweenDates(Date start, Date end) {
		List<Transaction> l = tlist.getExcluded();
		for (Transaction t : tlist.filterByDates(start, end, l)) {
			tlist.includeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions of given account.
	 *
	 * @param account
	 *            the account of transactions to exclude
	 */
	public void excludeAccount(String account) {
		for (Transaction t : tlist.filterByAccount(account, tlist.getTransactions())) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Include all transactions of given account.
	 *
	 * @param account
	 *            the account of transactions to include
	 */
	public void includeAccount(String account) {
		for (Transaction t : tlist.filterByAccount(account, tlist.getExcluded())) {
			tlist.includeTransaction(t);
		}
	}

	/**
	 * Exclude all transactions of given accounts.
	 *
	 * @param accounts
	 *            the accounts to exclude transactions
	 */
	public void excludeAccounts(String[] accounts) {
		for (Transaction t : tlist.filterByAccounts(accounts, tlist.getTransactions())) {
			tlist.excludeTransaction(t);
		}
	}

	/**
	 * Include all transactions of given accounts.
	 *
	 * @param accounts
	 *            the accounts to include transactions
	 */
	public void includeAccounts(String[] accounts) {
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
	 * @return the current balance
	 */
	public double getCurrentBalance() {
		// NOTE: All methods will treat credits and debits from the view point
		// of the user,
		// thus, a credit looks like income and a debit looks like payout on a
		// bank statement
		double credited = 0;
		double debited = 0;
		for (Transaction t : tlist.getDebits()) {
			debited += t.getAmount();
		}
		for (Transaction t : tlist.getCredits()) {
			credited += t.getAmount();
		}
		return credited - debited;
	}

	/**
	 * Get the current balance for the given account.
	 *
	 * Returns a negative number if debits &gt; credits.
	 *
	 * @param account
	 *            the account to return the balance
	 * @return the balance of the account
	 */
	public double getCurrentBalanceForAccount(String account) {
		return calculate(tlist.getByAccount(account));
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
		return calculate(tlist.getBetweenDates(start, end));
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
	public double getBalanceBetweenDatesForAccount(Date start, Date end, String account) {
		return calculate(tlist.filterByAccount(account, tlist.getBetweenDates(start, end)));
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
		return calculate(tlist.getByCategory(category));
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
			balance += calculate(tlist.getByCategory(category));
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
	public double getBalanceForCategoriesBetweenDates(String[] categories, Date start, Date end) {
		double balance = 0;
		List<Transaction> l = tlist.getBetweenDates(start, end);
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
	public double getBalanceForCategoryForAccount(String category, String account) {
		List<Transaction> l = tlist.getByAccount(account);
		return calculate(tlist.filterByCategory(category, l));
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
	public double getBalanceForCategoriesForAccount(String[] categories, String account) {
		List<Transaction> l = tlist.getByAccount(account);
		return calculate(tlist.filterByCategories(categories, l));
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
	public double getBalanceForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
		List<Transaction> l = tlist.getByAccount(account);
		return calculate(tlist.filterByDates(start, end, tlist.filterByCategory(category, l)));
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
	public double getBalanceForCategoriesForAccountBetweenDates(String[] categories, String account, Date start,
			Date end) {
		List<Transaction> l = tlist.getByAccount(account);
		return calculate(tlist.filterByDates(start, end, tlist.filterByCategories(categories, l)));
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
	public double getBalanceForCategoryForAccounts(String category, String[] accounts) {
		double balance = 0;
		List<Transaction> l = tlist.getByCategory(category);
		for (String account : accounts) {
			balance += calculate(tlist.filterByAccount(account, l));
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
	public double getBalanceForCategoriesForAccounts(String[] categories, String[] accounts) {
		double balance = 0;
		List<Transaction> l = tlist.getByCategories(categories);
		for (String account : accounts) {
			balance += calculate(tlist.filterByAccount(account, l));
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
	public double getBalanceForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
		double balance = 0;
		List<Transaction> l = tlist.filterByDates(start, end, tlist.getByCategory(category));
		for (String account : accounts) {
			balance += calculate(tlist.filterByAccount(account, l));
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
	public double getBalanceForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start,
			Date end) {
		double balance = 0;
		List<Transaction> l = tlist.filterByDates(start, end, tlist.getByCategories(categories));
		for (String account : accounts) {
			balance += calculate(tlist.filterByAccount(account, l));
		}
		return balance;
	}

	/**
	 * Get the current income for the given account.
	 *
	 * @param account
	 *            the account to return the balance
	 * @return the income of the account
	 */
	public double getCurrentIncomeForAccount(String account) {
		return calculateCredits(tlist.filterByAccount(account, tlist.getCredits()));
	}

	/**
	 * Get the income of all accounts between dates specified.
	 *
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income between these dates
	 */
	public double getIncomeBetweenDates(Date start, Date end) {
		return calculateCredits(tlist.filterByDates(start, end, tlist.getCredits()));
	}

	/**
	 * Get the income for the specified account between the given dates.
	 *
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @param account
	 *            the account to check
	 * @return the income
	 */
	public double getIncomeBetweenDatesForAccount(Date start, Date end, String account) {
		return calculateCredits(tlist.filterByAccount(account, tlist.filterByDates(start, end, tlist.getCredits())));
	}

	/**
	 * Get the credits(income) for the provided category.
	 *
	 * @param category
	 *            the category to filter by
	 * @return the income
	 */
	public double getIncomeForCategory(String category) {
		return calculateCredits(tlist.filterByCategory(category, tlist.getCredits()));
	}

	/**
	 * Get the credits(income) for the provided categories.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @return the income
	 */
	public double getIncomegForCategories(String[] categories) {
		double debited = 0;
		for (String category : categories) {
			debited += getIncomeForCategory(category);
		}
		return debited;
	}

	/**
	 * Get the credits(income) for the provided categories between the specified
	 * dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income
	 */
	public double getIncomeForCategoriesBetweenDates(String[] categories, Date start, Date end) {
		return calculateCredits(tlist.filterByDates(start, end,
				tlist.filterByCategories(categories, tlist.getCredits())));
	}

	/**
	 * Get the credits(income) for the provided categories between the specified
	 * dates.
	 *
	 * @param category
	 *            the category to filter by
	 * @param account
	 *            the account to search in
	 * @return the income
	 */
	public double getIncomeForCategoryForAccount(String category, String account) {
		return calculateCredits(tlist.filterByAccount(account, tlist.filterByCategory(category, tlist.getCredits())));
	}

	/**
	 * Get the credits(income) for the provided categories between the specified
	 * dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param account
	 *            the account to search in
	 * @return the income
	 */
	public double getIncomeForCategoriesForAccount(String[] categories, String account) {
		return calculateCredits(tlist
				.filterByAccount(account, tlist.filterByCategories(categories, tlist.getCredits())));
	}

	/**
	 * Get the income for the category of the account between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param account
	 *            the account to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income
	 */
	public double getIncomeForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
		return calculateCredits(tlist.filterByAccount(account,
				tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getCredits()))));
	}

	/**
	 * Get the income for the categories for the account between the dates
	 * specified.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param account
	 *            the account to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income
	 */
	public double getIncomeForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end) {
		return calculateCredits(tlist.filterByAccount(account,
				tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getCredits()))));
	}

	/**
	 * Get the income for the category in the accounts between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @return the income
	 */
	public double getIncomeForCategoryForAccounts(String category, String[] accounts) {
		double balance = 0;
		for (String account : accounts) {
			balance += calculateCredits(tlist.filterByAccount(account,
					tlist.filterByCategory(category, tlist.getCredits())));
		}
		return balance;
	}

	/**
	 * Get the income for the categories for the accounts.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @return the income
	 */
	public double getIncomeForCategoriesForAccounts(String[] categories, String[] accounts) {
		double balance = 0;
		for (String account : accounts) {
			balance += calculateCredits(tlist.filterByAccount(account,
					tlist.filterByCategories(categories, tlist.getCredits())));
		}
		return balance;
	}

	/**
	 * Get the income for the category for the accounts between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income
	 */
	public double getIncomeForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
		double balance = 0;
		for (String account : accounts) {
			balance += calculateCredits(tlist.filterByAccount(account,
					tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getCredits()))));
		}
		return balance;
	}

	/**
	 * Get the income for the categories in the accounts between the specified
	 * dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the income
	 */
	public double getIncomeForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start,
			Date end) {
		double balance = 0;
		for (String account : accounts) {
			balance += calculateCredits(tlist.filterByAccount(account,
					tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getCredits()))));
		}
		return balance;
	}

	/**
	 * Get the current spending for the given account.
	 *
	 * @param account
	 *            the account to return the balance
	 * @return the spending of the account
	 */
	public double getCurrentSpendingForAccount(String account) {
		return calculateDebits(tlist.filterByAccount(account, tlist.getDebits()));
	}

	/**
	 * Get the spending of all accounts between dates specified.
	 *
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending between these dates
	 */
	public double getSpendingBetweenDates(Date start, Date end) {
		return calculateDebits(tlist.filterByDates(start, end, tlist.getDebits()));
	}

	/**
	 * Get the spending for the specified account between the given dates.
	 *
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @param account
	 *            the account to check
	 * @return the spending
	 */
	public double getSpendingBetweenDatesForAccount(Date start, Date end, String account) {
		return calculateDebits(tlist.filterByAccount(account, tlist.filterByDates(start, end, tlist.getDebits())));
	}

	/**
	 * Get the debits(spending) for the provided category.
	 *
	 * @param category
	 *            the category to filter by
	 * @return the spending
	 */
	public double getSpendingForCategory(String category) {
		return calculateDebits(tlist.filterByCategory(category, tlist.getDebits()));
	}

	/**
	 * Get the debits(spending) for the provided categories.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @return the spending
	 */
	public double getSpendingForCategories(String[] categories) {
		double debited = 0;
		for (String category : categories) {
			debited += getSpendingForCategory(category);
		}
		return debited;
	}

	/**
	 * Get the debits(spending) for the provided categories between the
	 * specified dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending
	 */
	public double getSpendingForCategoriesBetweenDates(String[] categories, Date start, Date end) {
		return calculateDebits(tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getDebits())));
	}

	/**
	 * Get the debits(spending) for the provided categories between the
	 * specified dates.
	 *
	 * @param category
	 *            the category to filter by
	 * @param account
	 *            the account to search in
	 * @return the spending
	 */
	public double getSpendingForCategoryForAccount(String category, String account) {
		return calculateDebits(tlist.filterByAccount(account, tlist.filterByCategory(category, tlist.getDebits())));
	}

	/**
	 * Get the debits(spending) for the provided categories between the
	 * specified dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param account
	 *            the account to search in
	 * @return the spending
	 */
	public double getSpendingForCategoriesForAccount(String[] categories, String account) {
		return calculateDebits(tlist.filterByAccount(account, tlist.filterByCategories(categories, tlist.getDebits())));
	}

	/**
	 * Get the spending for the category of the account between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param account
	 *            the account to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending
	 */
	public double getSpendingForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
		return calculateDebits(tlist.filterByAccount(account,
				tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getDebits()))));
	}

	/**
	 * Get the spending for the categories for the account between the dates
	 * specified.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param account
	 *            the account to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending
	 */
	public double getSpendingForCategoriesForAccountBetweenDates(String[] categories, String account, Date start,
			Date end) {
		return calculateDebits(tlist.filterByAccount(account,
				tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getDebits()))));
	}

	/**
	 * Get the spending for the category in the accounts between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @return the spending
	 */
	public double getSpendingForCategoryForAccounts(String category, String[] accounts) {
		double debited = 0;
		for (String account : accounts) {
			debited += calculateDebits(tlist.filterByAccount(account,
					tlist.filterByCategory(category, tlist.getDebits())));
		}
		return debited;
	}

	/**
	 * Get the spending for the categories for the accounts.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @return the spending
	 */
	public double getSpendingForCategoriesForAccounts(String[] categories, String[] accounts) {
		double debited = 0;
		for (String account : accounts) {
			debited += calculateDebits(tlist.filterByAccount(account,
					tlist.filterByCategories(categories, tlist.getDebits())));
		}
		return debited;
	}

	/**
	 * Get the spending for the category for the accounts between the dates
	 * specified.
	 *
	 * @param category
	 *            the category to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending
	 */
	public double getSpendingForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
		double debited = 0;
		for (String account : accounts) {
			debited += calculateDebits(tlist.filterByAccount(account,
					tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getDebits()))));
		}
		return debited;
	}

	/**
	 * Get the spending for the categories in the accounts between the specified
	 * dates.
	 *
	 * @param categories
	 *            the categories to filter by
	 * @param accounts
	 *            the accounts to search in
	 * @param start
	 *            the start of the period
	 * @param end
	 *            the end of the period
	 * @return the spending
	 */
	public double getSpendingForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start,
			Date end) {
		double debited = 0;
		for (String account : accounts) {
			debited += calculateDebits(tlist.filterByAccount(account,
					tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getDebits()))));
		}
		return debited;
	}

	/**
	 * Searches through all transactions in the following manner: 1) Separate
	 * transactions into accounts and search each separately 2) Search separated
	 * accounts into discrete category/date combos 3) Search for similar amounts
	 * 4) If similar amounts are found, search for similar descriptions of
	 * amounts 5) If a similar description is found, we have a possible
	 * duplicate 6) Each possible duplicate placed in same array
	 *
	 * Returns an empty list if none are found.
	 *
	 * @return list of arrays of possible duplicates
	 */
	public List<Transaction[]> getPossibleDuplicates() {
		LinkedList<Transaction[]> dupes = new LinkedList<Transaction[]>();
		for (String account : tlist.getAccounts()) {
			for (String category : tlist.getCategories()) {
				for (int year : tlist.getYearsWithTransactions()) {
					for (int month : tlist.getMonthsInYearWithTransactions(year)) {
						for (int day : tlist.getDaysInMonthInYearWithTransactions(year, month)) {
							GregorianCalendar g = new GregorianCalendar(year, month, day);
							List<Transaction> l0 = tlist.getByCategoryAndDate(category, g.getTime());
							List<Transaction> l = tlist.filterByAccount(account, l0);
							if (l.size() > 1) {
								HashMap<Double, LinkedList<Transaction>> amounts = new HashMap<Double, LinkedList<Transaction>>();
								for (Transaction t : l) {
									if (amounts.containsKey(t.getAmount())) {
										amounts.get(t.getAmount()).add(t);
									} else {
										LinkedList<Transaction> templ = new LinkedList<Transaction>();
										templ.add(t);
										amounts.put(t.getAmount(), templ);
									}
								}
								for (Map.Entry<Double, LinkedList<Transaction>> entry : amounts.entrySet()) {
									if (entry.getValue().size() > 1) {
										HashMap<String, List<Transaction>> desc = new HashMap<String, List<Transaction>>();
										for (Transaction t : entry.getValue()) {
											if (desc.containsKey(t.getDescription().toLowerCase())) {
												desc.get(t.getDescription().toLowerCase()).add(t);
											} else {
												LinkedList<Transaction> templ = new LinkedList<Transaction>();
												templ.add(t);
												desc.put(t.getDescription().toLowerCase(), templ);
											}
										}
										for (Map.Entry<String, List<Transaction>> descEntry : desc.entrySet()) {
											if (descEntry.getValue().size() > 1) {
												Transaction[] temparray = new Transaction[descEntry.getValue().size()];
												for (int i = 0; i < descEntry.getValue().size(); i++) {
													temparray[i] = descEntry.getValue().get(i);
												}
												dupes.add(temparray);
											}
										}
									}
								}
							}
						}
					}
				}
			}

		}
		return dupes;
	}

	@Override
	public File transactionsToCSV(String filename, boolean toAppStorage) throws IOException {
		TransactionsToCSV csv = new TransactionsToCSV(filename, toAppStorage);
		csv.mkDirs(this.guid);
		csv.setDir(this.guid);
		return csv.transactions_to_storage(this.tlist);
	}

	/**
	 * Creates an empty BudgetController object with the given name.
	 *
	 * @param budgetName
	 *            the name of the budget
	 * @return the BudgetController
	 */
	protected BudgetController createBudget(String budgetName) {
		return new BudgetController(tlist, budgetName);
	}

	/**
	 * Creates a budget from a file.
	 *
	 * @param budgetName
	 *            the name for the new budget
	 * @param file
	 *            the file of the budget
	 * @return the BudgetController for the budget
	 * @throws IOException
	 */
	protected BudgetController createBudgetFromFile(String budgetName, File file) throws IOException {
		return new BudgetController(tlist, budgetName, new CSVToBudget(file, tlist).data_to_transactions()
				.getTransactions());
	}

	/**
	 * Get the GUID of this transactionscontroller.
	 *
	 * @return the guid
	 */
	@Override
	public String getName() {
		return guid;
	}

}
