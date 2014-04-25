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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.TransformToTransactions;
import derigible.transformations.TransactionsToCSV;

/**
 * @author marphill
 *
 */
public class TransactionsController extends AbstractController {

    /**
     * Creates the Transactions list stored in the controller.
     *
     * @param transformer the transformer object that outputs data to an array
     * @throws java.io.IOException problem reading data from transformer
     */
    public TransactionsController(TransformToTransactions transformer) throws IOException {
        super(transformer);
    }

    /**
     * This constructor is here merely to allow for updates to the primary
     * constructor (such as add throws Exception clauses) and not have to catch
     * the creation error in my tests.
     *
     * @param transformer the transformer to use
     * @param DONOTUSE DO NOT USE THIS CONSTRUCTOR
     */
    public TransactionsController(TransformToTransactions transformer, int DONOTUSE) {
        super(transformer, DONOTUSE);
    }

    /**
     * Client should never have access directly to the transactions list. All
     * interactions should go through the controller or the Transactions object.
     * In this way the transactions can be kept encapsulated and away from the
     * client.
     *
     * @return the Transactions object
     */
    public Transactions getTransactions() {
        return tlist;
    }

    /**
     * Exclude all transactions of a given category.
     *
     * @param category the category of transactions to exclude
     */
    public void excludeCategory(String category) {
        super.excludeCategory(category, tlist.getTransactions());
    }

    /**
     * Exclude all transactions of a given category.
     *
     * @param category the category of transactions to exclude
     */
    public void includeCategory(String category) {
        List<Transaction> l = tlist.getExcluded();
        super.includeCategory(category, l);
    }

    /**
     * Exclude all transactions of given categories.
     *
     * @param categories the categories of transactions to exclude
     */
    public void excludeCategories(String[] categories) {
        super.excludeCategories(categories, tlist.getByCategories(categories));
    }

    /**
     * Include all transactions in the given categories.
     *
     * @param categories the categories to include
     */
    public void includeCategories(String[] categories) {
        super.includeCategories(categories, tlist.getExcluded());
    }

    /**
     * Exclude all transactions of a given date.
     *
     * @param date the date of transactions to exclude
     */
    public void excludeDate(Date date) {
        super.excludeDate(date, tlist.getTransactions());
    }

    /**
     * Include all transactions of the given date.
     *
     * @param date the date to include
     */
    public void includeDate(Date date) {
        super.includeDate(date, tlist.getExcluded());
    }

    /**
     * Exclude all transactions between dates.
     *
     * @param start the beginning of the dates to exclude
     * @param end the end of the dates to exclude
     */
    public void excludeBetweenDates(Date start, Date end) {
        super.excludeBetweenDates(start, end, tlist.getTransactions());
    }

    /**
     * Exclude all transactions between dates.
     *
     * @param start the beginning of the dates to include
     * @param end the end of the dates to include
     */
    public void includeBetweenDates(Date start, Date end) {
        super.includeBetweenDates(start, end, tlist.getExcluded());
    }

    /**
     * Exclude all transactions of given account.
     *
     * @param account the account of transactions to exclude
     */
    public void excludeAccount(String account) {
        super.excludeAccount(account, tlist.getTransactions());
    }

    /**
     * Include all transactions of given account.
     *
     * @param account the account of transactions to include
     */
    public void includeAccount(String account) {
        super.includeAccount(account, tlist.getTransactions());
    }

    /**
     * Exclude all transactions of given accounts.
     *
     * @param accounts the accounts to exclude transactions
     */
    public void excludeAccounts(String[] accounts) {
        super.excludeAccounts(accounts, tlist.getTransactions());
    }

    /**
     * Include all transactions of given accounts.
     *
     * @param accounts the accounts to include transactions
     */
    public void includeAccounts(String[] accounts) {
        super.includeAccounts(accounts, tlist.getTransactions());
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
      return super.getCurrentBalance(tlist.getDebits(), tlist.getCredits());
    }

    /**
     * Get the current balance for the given account.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param account the account to return the balance
     * @return the balance of the account
     */
    public double getCurrentBalanceForAccount(String account) {
        return super.getCurrentBalanceForAccount(account, tlist.getTransactions());
    }

    /**
     * Get the balance of all accounts between dates specified.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @return the balance between these dates
     */
    public double getBalanceBetweenDates(Date start, Date end) {
        return super.getBalanceBetweenDates(start, end, tlist.getTransactions());
    }

    /**
     * Get the balance for the specified account between the given dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @param account the account to check
     * @return the balance
     */
    public double getBalanceBetweenDatesForAccount(Date start, Date end, String account) {
        return super.getBalanceBetweenDatesForAccount(start, end, account, tlist.getTransactions());
    }

    /**
     * Get the balance for the given category.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category balance to get
     * @return the balance
     */
    public double getBalanceForCategory(String category) {
        return super.getBalanceForCategory(category, tlist.getTransactions());
    }

    /**
     * Get the balance for the given categories.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories' balance to get
     * @return the balance
     */
    public double getBalanceForCategories(String[] categories) {
        return super.getBalanceForCategories(categories, tlist.getTransactions());
    }

    /**
     * Get the balance for the given categories between the given dates.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories' balance to get
     * @param start the start of the period
     * @param end the end of the period
     * @return the balance
     */
    public double getBalanceForCategoriesBetweenDates(String[] categories, Date start, Date end) {
        return super.getBalanceForCategoriesBetweenDates(categories, start, end, tlist.getTransactions());
    }

    /**
     * Get the balance for the category of the account.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to check
     * @param account the account for the category
     * @return the balance
     */
    public double getBalanceForCategoryForAccount(String category, String account) {
        return super.getBalanceForCategoryForAccount(category, account, tlist.getTransactions());
    }

    /**
     * Get the balance for the categories for the account provided.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to search by
     * @param account the account to search by
     * @return the balance
     */
    public double getBalanceForCategoriesForAccount(String[] categories, String account) {
        return super.getBalanceForCategoriesForAccount(categories, account, tlist.getTransactions());
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
     * @return the balance
     */
    public double getBalanceForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
        return super.getBalanceForCategoryForAccountBetweenDates(category, account, start, end, tlist.getTransactions());
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
     * @return the balance
     */
    public double getBalanceForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end) {
        return super.getBalanceForCategoriesBetweenDates(categories, start, end, tlist.getTransactions());
    }

    /**
     * Get the balance for the category for the accounts provided.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search
     * @return the balance
     */
    public double getBalanceForCategoryForAccounts(String category, String[] accounts) {
        return super.getBalanceForCategoryForAccounts(category, accounts, tlist.getTransactions());
    }

    /**
     * Get the balance for the categories in the selected accounts.
     *
     * Returns a negative number if debits &gt; credits.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @return the balance
     */
    public double getBalanceForCategoriesForAccounts(String[] categories, String[] accounts) {
        return super.getBalanceForCategoriesForAccounts(categories, accounts, tlist.getTransactions());
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
     * @return the balance
     */
    public double getBalanceForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
        return super.getBalanceForCategoryForAccountsBetweenDates(category, accounts, start, end, tlist.getTransactions());
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
     * @return the balance
     */
    public double getBalanceForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start, Date end) {
        return super.getBalanceForCategoriesForAccountsBetweenDates(categories, accounts, start, end, tlist.getTransactions());
    }

    /**
     * Get the current income for the given account.
     *
     * @param account the account to return the balance
     * @return the income of the account
     */
    public double getCurrentIncomeForAccount(String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account, tlist.getCredits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the income of all accounts between dates specified.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @return the income between these dates
     */
    public double getIncomeBetweenDates(Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByDates(start, end, tlist.getCredits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the income for the specified account between the given dates.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @param account the account to check
     * @return the income
     */
    public double getIncomeBetweenDatesForAccount(Date start, Date end, String account) {
        double debited = 0;
        List<Transaction> l = tlist.filterByDates(start, end, tlist.getCredits());
        for (Transaction t : tlist.filterByAccount(account, l)) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the credits(income) for the provided category.
     *
     * @param category the category to filter by
     * @return the income
     */
    public double getIncomeForCategory(String category) {
        double debited = 0;
        for (Transaction t : tlist.filterByCategory(category, tlist.getCredits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the credits(income) for the provided categories.
     *
     * @param categories the categories to filter by
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
     * @param categories the categories to filter by
     * @param start the start of the period
     * @param end the end of the period
     * @return the income
     */
    public double getIncomeForCategoriesBetweenDates(String[] categories, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByDates(start, end,
                tlist.filterByCategories(categories, tlist.getCredits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the credits(income) for the provided categories between the specified
     * dates.
     *
     * @param category the category to filter by
     * @param account the account to search in
     * @return the income
     */
    public double getIncomeForCategoryForAccount(String category, String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByCategory(category, tlist.getCredits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the credits(income) for the provided categories between the specified
     * dates.
     *
     * @param categories the categories to filter by
     * @param account the account to search in
     * @return the income
     */
    public double getIncomeForCategoriesForAccount(String[] categories, String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByCategories(categories, tlist.getCredits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the income for the category of the account between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the income
     */
    public double getIncomeForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getCredits())))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the income for the categories for the account between the dates
     * specified.
     *
     * @param categories the categories to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the income
     */
    public double getIncomeForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getCredits())))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the income for the category in the accounts between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search in
     * @return the income
     */
    public double getIncomeForCategoryForAccounts(String category, String[] accounts) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByCategory(category, tlist.getCredits()))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the income for the categories for the accounts.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @return the income
     */
    public double getIncomeForCategoriesForAccounts(String[] categories, String[] accounts) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByCategories(categories, tlist.getCredits()))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the income for the category for the accounts between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the income
     */
    public double getIncomeForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getCredits())))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the income for the categories in the accounts between the specified
     * dates.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the income
     */
    public double getIncomeForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start, Date end) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getCredits())))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the current spending for the given account.
     *
     * @param account the account to return the balance
     * @return the spending of the account
     */
    public double getCurrentSpendingForAccount(String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account, tlist.getDebits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the spending of all accounts between dates specified.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending between these dates
     */
    public double getSpendingBetweenDates(Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByDates(start, end, tlist.getDebits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the spending for the specified account between the given dates.
     *
     * @param start the start of the period
     * @param end the end of the period
     * @param account the account to check
     * @return the spending
     */
    public double getSpendingBetweenDatesForAccount(Date start, Date end, String account) {
        double debited = 0;
        List<Transaction> l = tlist.filterByDates(start, end, tlist.getDebits());
        for (Transaction t : tlist.filterByAccount(account, l)) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the debits(spending) for the provided category.
     *
     * @param category the category to filter by
     * @return the spending
     */
    public double getSpendingForCategory(String category) {
        double debited = 0;
        for (Transaction t : tlist.filterByCategory(category, tlist.getDebits())) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the debits(spending) for the provided categories.
     *
     * @param categories the categories to filter by
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
     * @param categories the categories to filter by
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending
     */
    public double getSpendingForCategoriesBetweenDates(String[] categories, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByDates(start, end,
                tlist.filterByCategories(categories, tlist.getDebits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the debits(spending) for the provided categories between the
     * specified dates.
     *
     * @param category the category to filter by
     * @param account the account to search in
     * @return the spending
     */
    public double getSpendingForCategoryForAccount(String category, String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByCategory(category, tlist.getDebits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the debits(spending) for the provided categories between the
     * specified dates.
     *
     * @param categories the categories to filter by
     * @param account the account to search in
     * @return the spending
     */
    public double getSpendingForCategoriesForAccount(String[] categories, String account) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByCategories(categories, tlist.getDebits()))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the spending for the category of the account between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending
     */
    public double getSpendingForCategoryForAccountBetweenDates(String category, String account, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getDebits())))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the spending for the categories for the account between the dates
     * specified.
     *
     * @param categories the categories to filter by
     * @param account the account to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending
     */
    public double getSpendingForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end) {
        double debited = 0;
        for (Transaction t : tlist.filterByAccount(account,
                tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getDebits())))) {
            debited += t.getAmount();
        }
        return debited;
    }

    /**
     * Get the spending for the category in the accounts between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search in
     * @return the spending
     */
    public double getSpendingForCategoryForAccounts(String category, String[] accounts) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByCategory(category, tlist.getDebits()))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the spending for the categories for the accounts.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @return the spending
     */
    public double getSpendingForCategoriesForAccounts(String[] categories, String[] accounts) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByCategories(categories, tlist.getDebits()))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the spending for the category for the accounts between the dates
     * specified.
     *
     * @param category the category to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending
     */
    public double getSpendingForCategoryForAccountsBetweenDates(String category, String[] accounts, Date start, Date end) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByDates(start, end, tlist.filterByCategory(category, tlist.getDebits())))) {
                debited += t.getAmount();
            }
        }
        return debited;
    }

    /**
     * Get the spending for the categories in the accounts between the specified
     * dates.
     *
     * @param categories the categories to filter by
     * @param accounts the accounts to search in
     * @param start the start of the period
     * @param end the end of the period
     * @return the spending
     */
    public double getSpendingForCategoriesForAccountsBetweenDates(String[] categories, String[] accounts, Date start, Date end) {
        double debited = 0;
        for (String account : accounts) {
            for (Transaction t : tlist.filterByAccount(account,
                    tlist.filterByDates(start, end, tlist.filterByCategories(categories, tlist.getDebits())))) {
                debited += t.getAmount();
            }
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

    public void transactionsToCSV(String filename, boolean toAppStorage){
    	TransactionsToCSV csv = new TransactionsToCSV();
    	csv.setFileName(filename);
    	csv.excludeId(toAppStorage);
    }
}
