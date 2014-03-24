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
	 * Excluded Transactions should not be returned.
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
	 * Excluded Transactions SHOULD be returned.
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
	 * Excluded Transactions should not be returned.
	 * 
	 * @return last transaction of list
	 */
	public abstract Transaction getLastTransaction();
	
	/**
	 * Gets all the years in which a Transaction is recorded. Returned as an
	 * int array since representing year as an int plays well with the
	 * Calendar API, and just about any other date API you will find in Java
	 * (or most any other language for that matter).
	 * 
	 * Excluded Transactions SHOULD be returned and handled by client (may want to show that
	 * it contains excluded transactions.
	 * 
	 * @return the int array of years with a Transaction recorded
	 */
	public abstract int[] getYearsWithTransactions();
	
	/**
	 * Gets all the months in the given year that have a Transaction recorded.
	 * In theory this should never be called without first querying the 
	 * getYearsWithTransactions method and then taking a value from that.
	 * However, in the even that a year is passed in that has no Transactions
	 * recorded, an empty int[] should  be returned. Same reasoning for
	 * using ints as getYearsWithTransactions.
	 * 
	 * Excluded Transactions SHOULD be returned and handled by client (may want to show that
	 * it contains excluded transactions.
	 * 
	 * @param year - the year of the months in question
	 * @return the int array of months within the year with a Transaction recorded
	 */
	public abstract int[] getMonthsInYearWithTransactions(int year);
	
	/**
	 * Gets all the days in the given month of the given year with a Transaction
	 * recorded. Works in much the same way as getYearsWithTransactions and
	 * getMonthsInYearWithTransactions does. Returns an empty int[] id none found.
	 * 
	 * Excluded Transactions SHOULD be returned and handled by client (may want to show that
	 * it contains excluded transactions.
	 * 
	 * @param year - the year of the days in question
	 * @param month - the month of the days in question
	 * @return the int array of days within the month within the year with a Transaction recorded
	 */
	public abstract int[] getDaysInMonthInYearWithTransactions(int year, int month);
	
	/**
	 * Get all the days in the year with Transactions. Works just like the 
	 * getDayInMonthInYearWithTransactions method, except it doesn't take into
	 * account any month. Returns empty if none are found.
	 * 
	 * Excluded Transactions SHOULD be returned and handled by client (may want to show that
	 * it contains excluded transactions.
	 * 
	 * @param year - the year of the days in question
	 * @return the int array of days within the specified year with a Transaction Recorded
	 */
	public abstract int[] getAllDaysInYearWithTransactions(int year);
	
	/**
	 * Get all the transactions at a certain date. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param date - date of Transactions
	 * @return list of transactions on given date
	 */
	public abstract List<Transaction> getTransactionsByDate(Date date);
	
	/**
	 * Get all the transactions between two specified dates. Should return an
	 * empty list if none present. Dates provided ARE inclusive. This is a convenience method to call 
	 * directly and get a filtered list. Use the corresponding filter for all other Transaction 
	 * lists that you want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param start - start date of the transactions list
	 * @param end - end date of the transactions list
	 * @return list of transactions between given dates.
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getTransactionsBetweenDates(Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Get all transactions for a given category. Should return an empty list
	 * if none present. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param category - category of transactions to return
	 * @return list of transactions of a given category
	 */
	public abstract List<Transaction> getTransactionsByCategory(String category);
	
	/**
	 * Get all transactions for the given categories. Should return an empty list if
	 * none present. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param categories - categories of transactions to return
	 * @return list of transactions of given categories
	 */
	public abstract List<Transaction> getTransactionsByCategories(String[] categories);
	
	/**
	 * Get all transactions for the given categories and date. Should return an empty list if
	 * none present. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param categories - categories of transactions to return
	 * @param date - date of the transactions to return
	 * @return list of transactions of given categories and date
	 */
	public abstract List<Transaction> getTransactionsByCategoriesAndDate(String[] categories, Date date);
	
	/**
	 * Get all transactions for a given category on a specified date. Should
	 * return an empty list if none present. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param cat - category of transactions
	 * @param date - date of transactions
	 * @return list of transactions of a category and date
	 */
	public abstract List<Transaction> getTransactionsByCategoryAndDate(String cat, Date date);
	
	/**
	 * Get all transactions for a given category in a specified time interval.
	 * Should return an empty list if none found. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param cat - category of transactions
	 * @param start - start date of the transactions list
	 * @param end - end date of the transactions list
	 * @return list of transaction of category within dates
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getTransactionsByCategoryAndDates(String cat, Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Get all the transactions in the specified account. Should return an empty list if none found.
	 * 
	 * This SHOULD return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param account - the account to check for
	 * @return the list of transactions in account
	 */
	public abstract List<Transaction> getTransactionsByAccount(String account);
	
	/**
	 * 
	 * Get all transactions for the given categories in a specified time interval.
	 * Should return an empty list if none found. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * This should not return income transactions.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param cats - the categories to return
	 * @param start - start date of the transactions list
	 * @param end - end date of the transactions list
	 * @return list of transactions of categories and dates
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getTransactionsByCategoriesAndDates(String[] cats, Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Get all of the income transactions. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * Should return an empty list if none found.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @return a list of transactions that are income
	 */
	public abstract List<Transaction> getIncomeTransactions();
	
	/**
	 * Get all income transactions on a given date. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * Should return an empty list if none found.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param date - the date of all income transactions
	 * @return a list of transactions that are income on a particular date
	 */
	public abstract List<Transaction> getIncomeByDate(Date date);
	
	/**
	 * Get all income transactions between given dates. This is a convenience method to call directly and
	 * get a filtered list. Use the corresponding filter for all other Transaction lists that you
	 * want to filter by this method.
	 * 
	 * Should return an empty list if none found.
	 * 
	 * Excluded Transactions should not be returned.
	 * 
	 * @param start - start date of the income transactions list
	 * @param end - end date of the income transactions list
	 * @return list of income transactions between given dates.
	 * @throws ArrayIndexOutOfBoundsException if end < start
	 */
	public abstract List<Transaction> getIncomeBetweenDates(Date start, Date end) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Add a transaction to the transactions list.
	 * 
	 * @param tran - the transaction to add
	 */
	public abstract void addTransaction(Transaction tran);
	
	/**
	 * Remove a set of transactions from the transactions list.
	 * 
	 * @param trans - the transactions to add
	 */
	public abstract void addTransactions(Transaction[] trans);
	
	/**
	 * Remove a transaction from the transactions list.
	 * 
	 * @param tran - the transaction to remove
	 */
	public abstract void removeTransaction(Transaction tran);
	
	/**
	 * Get the categories stored in the transactions list.
	 * 
	 * Categories with only excluded Transactions SHOULD be returned. Handle these lists
	 * within the client.
	 * 
	 * @return the stored transaction categories
	 */
	public abstract String[] getCategories();
	
	/**
	 * Get the accounts in the transactions list.
	 * 
	 * @return the stored transaction accounts
	 */
	public abstract String[] getAccounts();
	
	/**
	 * Get all of the transactions that have have excluded. This list will return both income and
	 * regular transactions.
	 * 
	 * Returns an empty list if none are found.
	 * 
	 * @return the list of excluded transactions
	 */
	public abstract List<Transaction> getExcludedTransactions();
	
}
