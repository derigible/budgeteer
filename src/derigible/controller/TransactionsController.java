/**
 * 
 */
package derigible.controller;

import java.util.Date;
import java.util.List;

import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.Transformation;

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
		double credited = 0;
		double debited = 0;
		for(Transaction t : tlist.getTransactions()){
			debited += t.getAmount();
		}
		for(Transaction t : tlist.getIncomeTransactions()){
			credited += t.getAmount();
		}
		
		return credited - debited;
	}

}
