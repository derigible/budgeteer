/**
 * 
 */
package derigible.transactions;

import java.util.List;
import java.util.Date;

/**
 * @author marphill
 *	An interface to define transactions objects to be used in the main program.
 *	Since it is unclear at this point how a Transaction may need to be
 *	stored, it is best to just define an interface and let the implementation
 *	determine the structure.  It may also be useful to experiment. Thus, my
 *	client can use multiple implementations of this without ever needing
 *	to know how it is done. Makes for great iterative programming.
 */
public interface Transactions {
	
	/**
	 * Returns all transactions as a List type object.
	 * 
	 * This should not return income transactions.
	 * 
	 * @return List of Transaction objects
	 */
	public abstract List<Transaction> getTransactions();
	
	/**
	 * Get the transaction at a specified index. If transactions data structure
	 * does not implement an indexing scheme, a new scheme must be created
	 * to get a transaction at an index (value should not ever return null
	 * unless list is empty). Should return an empty list if no transactions
	 * recorded.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param index of the Transactions List
	 * @return Transaction at the provided index
	 */
	public abstract Transaction getTransactionAt(int index);
	
	/**
	 * Returns the last Transaction in the transactions list. The transactions
	 * list refers to all transactions ordered first by date and then by 
	 * alphabetical order of category, and then finally by alphabetical
	 * order of Description.
	 * 
	 * Since last transaction can be a rather arbitrary term, implementations
	 * of this class can return a different "last transaction". You must
	 * specify what last means in this case if it is different from the 
	 * default.
	 * 
	 * Should return an empty list if no transactions recorded.
	 * 
	 * This should not return income transactions.
	 * 
	 * @return last transaction of list
	 */
	public abstract Transaction getLastTransaction();
	
	/**
	 * Get all the transactions at a certain date.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param date - date of Transactions
	 * @return list of transactions on given date
	 */
	public abstract List<Transaction> getTransactionsByDate(Date date);
	
	/**
	 * Get all the transactions between two specified dates. Should return an
	 * empty list if none present.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param start - start date of the transactions list
	 * @param end - end date of the transactions list
	 * @return list of transactions between given dates.
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getTransactionsBetweenDates(Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Get all transactions for a given category. Should return an empty list
	 * if none present.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param category - category of transactions to return
	 * @return list of transactions of a given category
	 */
	public abstract List<Transaction> getTransactionsByCategory(String category);
	
	/**
	 * Get all transactions for a given category on a specified date. Should
	 * return an empty list if none present.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param cat - category of transactions
	 * @param date - date of transactions
	 * @return list of transactions of a category and date
	 */
	public abstract List<Transaction> getTransactionsByCategoryAndDate(String cat, Date date);
	
	/**
	 * Get all transactions for a given category in a specified time interval.
	 * Should return an empty list if none found.
	 * 
	 * This should not return income transactions.
	 * 
	 * @param cat - category of transactions
	 * @param start - start date of the transactions list
	 * @param end - end date of the transactions list
	 * @return list of transaction of category within dates
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getTransactionsByCategoryAndDates(String cat, Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Get all of the income transactions.
	 * Should return an empty list if none found.
	 * 
	 * @return a list of transactions that are income
	 */
	public abstract List<Transaction> getIncomeTransactions();
	
	/**
	 * Get all income transactions on a given date.
	 * Should return an empty list if none found.
	 * 
	 * @param date - the date of all income transactions
	 * @return a list of transactions that are income on a particular date
	 */
	public abstract List<Transaction> getIncomeByDate(Date date);
	
	/**
	 * Get all income transactions between given dates.
	 * Should return an empty list if none found.
	 * 
	 * @param start - start date of the income transactions list
	 * @param end - end date of the income transactions list
	 * @return list of income transactions between given dates.
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getIncomeBetweenDates(Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Add a transaction to the transactions list.
	 */
	public abstract void addTransaction(Transaction tran);
	
	/**
	 * Remove a transaction from the transactions list.
	 * 
	 * @param tran - the transaction to remove
	 */
	public abstract void removeTransaction(Transaction tran);
}
