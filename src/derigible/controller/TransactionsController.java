/**
 * 
 */
package derigible.controller;

import java.sql.Date;

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
	public void exlcudeCategory(String category){
		if(!tlist.hasCategory(category)) return;
		for(Transaction t : tlist.getByCategory(category)){
			t.setExcluded(false);
		}
	}
	
	/**
	 * Exclude all transactions between dates.
	 * @param start - the beginning of the dates to exclude
	 * @param end - the end of the dates to exclude
	 */
	public void excludeBetweenDates(Date start, Date end){
		for(Transaction t : tlist.getBetweenDates(start, end)){
			t.setExcluded(false);
		}
	}
	
	/**
	 * Exclude all transactions of a given category. 
	 * 
	 * @param category - the category of transactions to exclude
	 */
	public void inlcudeCategory(String category){
		if(!tlist.hasCategory(category)) return;
		for(Transaction t : tlist.getByCategory(category)){
			t.setExcluded(true);
		}
	}
	
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
	
	/**
	 * Exclude all transactions between dates.
	 * @param start - the beginning of the dates to exclude
	 * @param end - the end of the dates to exclude
	 */
	public void includeBetweenDates(Date start, Date end){
		for(Transaction t : tlist.getBetweenDates(start, end)){
			t.setExcluded(true);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
