/**
 * 
 */
package derigible.controller;

import java.util.Date;
import java.util.List;

import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.Transformation;
import derigible.utils.StringHelper;

/**
 * @author marphill
 *
 */
public class TransactionsController {
	
	private Transactions tlist = null;
	private double balance = 0;
	
	/**
	 * Creates the Transactions list stored in the controller.
	 * 
	 * @param transformer - the transformer object that outputs data to an array
	 */
	public TransactionsController(Transformation transformer){
		tlist = transformer.data_to_transactions();
		balance = getCurrentBalance();
	}
	
	/**
	 * Client should never have access directly to the transactions list. All interactions
	 * should go through the controller or the Transactions object. In this way the transactions
	 * can be kept encapsulated and away from the client.
	 * 
	 * @return the Transactions object
	 */
	public Transactions getTransactions(){
		return tlist;
	}
	
	/**
	 * Exclude all transactions of a given category. 
	 * 
	 * @param category - the category of transactions to exclude
	 */
	public void excludeCategory(String category){
		for(Transaction t : tlist.getByCategory(category)){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions of a given category. 
	 * 
	 * @param category - the category of transactions to exclude
	 */
	public void includeCategory(String category){
		List<Transaction> l = tlist.getExcluded();
		for(Transaction t : tlist.filterByCategory(category, l)){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions of given categories.
	 * 
	 * @param categories - the categories of transactions to exclude
	 */
	public void excludeCategories(String[] categories){
		for(Transaction t : tlist.getByCategories(categories)){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Include all transactions in the given categories.
	 * 
	 * @param categories the categories to include
	 */
	public void includeCategories(String[] categories){
		List<Transaction> l = tlist.getExcluded();
		for(Transaction t : tlist.filterByCategories(categories, l)){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions of a given date.
	 * 
	 * @param date - the date of transactions to exclude
	 */
	public void excludeDate(Date date){
		for(Transaction t : tlist.getByDate(date)){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Include all transactions of the given date.
	 * 
	 * @param date - the date to include
	 */
	public void includeDate(Date date){
		List<Transaction> l = tlist.getExcluded();
		for(Transaction t : tlist.filterByDate(date, l)){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions between dates.
	 * 
	 * @param start - the beginning of the dates to exclude
	 * @param end - the end of the dates to exclude
	 */
	public void excludeBetweenDates(Date start, Date end){
		for(Transaction t : tlist.getBetweenDates(start, end)){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions between dates.
	 * 
	 * @param start - the beginning of the dates to include
	 * @param end - the end of the dates to include
	 */
	public void includeBetweenDates(Date start, Date end){
		List<Transaction> l = tlist.getExcluded();
		for(Transaction t : tlist.filterByDates(start, end, l)){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions of given account.
	 * 
	 * @param categories - the account of transactions to exclude
	 */
	public void excludeAccount(String account){
		for(Transaction t : tlist.filterByAccount(account, tlist.getTransactions())){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Include all transactions of given account.
	 * 
	 * @param categories - the account of transactions to include
	 */
	public void includeAccount(String account){
		for(Transaction t : tlist.filterByAccount(account, tlist.getExcluded())){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Exclude all transactions of given accounts.
	 * 
	 * @param categories - the accounts of transactions to exclude
	 */
	public void excludeAccounts(String[] accounts){
		for(Transaction t : tlist.filterByAccounts(accounts, tlist.getTransactions())){
			tlist.excludeTransaction(t);
		}
	}
	
	/**
	 * Include all transactions of given accounts.
	 * 
	 * @param categories - the accounts of transactions to include
	 */
	public void includeAccounts(String[] accounts){
		for(Transaction t : tlist.filterByAccounts(accounts, tlist.getExcluded())){
			tlist.includeTransaction(t);
		}
	}
	
	/**
	 * Get the balance of credits and debits. In other words, what is the current difference
	 * between cash I received versus cash I have spent.
	 * @return
	 */
	public double getCurrentBalance(){
		//NOTE: All methods will treat credits and debits from the view point of the user,
		// thus, a credit looks like income and a debit looks like payout on a bank statement
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.getDebits()){
			debited += t.getAmount();
		}
		for(Transaction t : tlist.getIncomeTransactions()){
			credited += t.getAmount();
		}
		return credited - debited;
	}
	
	/**
	 * Get the current balance for the given account.
	 * 
	 * @param account - the account to return the balance
	 * @return the balance of the account
	 */
	public double getCurrentBalanceForAccount(String account){
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.getByAccount(account)){
			if(t.isCredit()){
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
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @return the balance between these dates
	 */
	public double getBalanceBetweenDates(Date start, Date end){
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.getBetweenDates(start, end)){
			if(t.isCredit()){
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
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @param account - the account to check
	 * @return the balance
	 */
	public double getBalanceBetweenDatesForAccount(Date start, Date end, String account){
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.filterByAccount(account, tlist.getBetweenDates(start, end))){
			if(t.isCredit()){
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
	 * @param category - the category balance to get
	 * @return the balance
	 */
	public double getBalanceForCategory(String category){
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.getByCategory(category)){
			if(t.isCredit()){
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
	 * @param categories - the categories' balance to get
	 * @return the balance
	 */
	public double getBalanceForCategories(String[] categories){
		double credited = 0;
		double debited = 0;
		for(String category : categories){
			for(Transaction t : tlist.getByCategory(category)){
				if(t.isCredit()){
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
	 * @param categories - the categories' balance to get
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @return the balance
	 */
	public double getBalanceForCategoriesBetweenDates(String[] categories, Date start, Date end){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getBetweenDates(start, end);
		for(String category : categories){
			for(Transaction t : tlist.filterByCategory(category, l)){
				if(t.isCredit()){
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
	 * @param category - the category to check
	 * @param account - the account for the category
	 * @return the balance
	 */
	public double getBalanceForCategoryForAccount(String category, String account){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getByAccount(account);
		for(Transaction t : tlist.filterByCategory(category, l)){
			if(t.isCredit()){
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
	 * @param categories - the categories to search by
	 * @param account - the account to search by
	 * @return the balance
	 */
	public double getBalanceForCategoriesForAccount(String[] categories, String account){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getByAccount(account);
		for(Transaction t : tlist.filterByCategories(categories, l)){
			if(t.isCredit()){
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
	 * @param category - the category to filter by
	 * @param account - the account to search in
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @return the balance
	 */
	public double getBalanceForCategoryForAccountBetweenDates(String category, String account, Date start, Date end){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getByAccount(account);
		for(Transaction t : tlist.filterByDates(start, end, tlist.filterByCategory(category, l))){
			if(t.isCredit()){
				credited += t.getAmount();
			} else {
				debited += t.getAmount();
			}
		}
		return credited - debited;
	}
	
	/**
	 * Get the balance for the categories for the account between the dates provided.
	 * 
	 * @param categories - the categories to filter by
	 * @param account - the account to search in
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @return the balance
	 */
	public double getBalanceForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getByAccount(account);
		for(Transaction t : tlist.filterByDates(start, end, tlist.filterByCategories(categories, l))){
			if(t.isCredit()){
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
	 * @param category - the category to filter by
	 * @param accounts - the accounts to search 
	 * @return the balance
	 */
	public double getBalanceForCategoryForAccounts(String category, String[] accounts){
		double credited = 0;
		double debited = 0;
		List<Transaction> l = tlist.getByCategory(category);
		for(String account : accounts){
			for(Transaction t : tlist.filterByAccount(account, l)){
				if(t.isCredit()){
					credited += t.getAmount();
				} else {
					debited += t.getAmount();
				}
			}
		}
		return credited - debited;
	}
	
	public double getBalanceForCategoriesForAccounts(String[] categories, String[] account){
		
	}
	
	public double getBalanceForCategoryForAccountsBetweenDates(String category, String[] account, Date start, Date end){
		
	}
	
	public double getBalanceForCategoriesForAccountsBetweenDates(String[] categories, String[] account, Date start, Date end){
		
	}
	
	/**
	 * Get the current spending for the given account.
	 * 
	 * @param account - the account to return the balance
	 * @return the spending of the account
	 */
	public double getCurrentSpendingForAccount(String account){
		double debited = 0;
		for(Transaction t : tlist.filterByAccount(account, tlist.getDebits())){
			debited += t.getAmount();
		}
		return debited;
	}
	
	/**
	 * Get the spending of all accounts between dates specified.
	 * 
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @return the spending between these dates
	 */
	public double getSpendingBetweenDates(Date start, Date end){
		double debited = 0;
		for(Transaction t : tlist.filterByDates(start, end, tlist.getDebits())){
			debited += t.getAmount();
		}
		return debited;
	}
	
	/**
	 * Get the spending for the specified account between the given dates.
	 * 
	 * @param start - the start of the period
	 * @param end - the end of the period
	 * @param account - the account to check
	 * @return the spending
	 */
	public double getSpendingBetweenDatesForAccount(Date start, Date end, String account){
		double debited = 0;
		List<Transaction> l = tlist.filterByDates(start, end, tlist.getDebits());
		for(Transaction t : tlist.filterByAccount(account, l)){
			debited += t.getAmount();
		}
		return debited;
	}
	
	public double getSpendingForCategory(String category){
		double debited = 0;
		for(Transaction t : tlist.filterByCategory(category, tlist.getDebits())){
			debited += t.getAmount();
		}
		return debited;
	}
	
	public double getSpendingForCategories(String[] categories){
		double debited = 0;
		for(String category : categories){
			debited += getSpendingForCategory(category);
		}
		return debited;
	}
	
	
	public double getSpendingForCategoriesBetweenDates(String[] categories, Date start, Date end){
		
	}
	
	public double getSpendingForCategoryForAccount(String category, String account){
		
	}
	
	public double getSpendingForCategoriesForAccount(String[] categories, String account){
		
	}
	
	public double getSpendingForCategoryForAccountBetweenDates(String category, String account, Date start, Date end){
		
	}
	
	public double getSpendingForCategoriesForAccountBetweenDates(String[] categories, String account, Date start, Date end){
		
	}
	
	public double getSpendingForCategoryForAccounts(String category, String[] account){
		
	}
	
	public double getSpendingForCategoriesForAccounts(String[] categories, String[] account){
		
	}
	
	public double getSpendingForCategoryForAccountsBetweenDates(String category, String[] account, Date start, Date end){
		
	}
	
	public double getSpendingForCategoriesForAccountsBetweenDates(String[] categories, String[] account, Date start, Date end){
		
	}
	
}
















