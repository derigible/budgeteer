/**
 * 
 */
package derigible.transactions;

import java.util.Date;
import java.util.List;

/**
 * @author marphill
 *
 */
public class TArray implements Transactions, Transaction {
	
	/**
	 * TArray stores data using arrays for each datatype associated in the 
	 * transaction interface. Since TArray is also a transaction, it does
	 * need to rely upon a whole lot of other objects, and thus saves on memory.
	 * Thus, this implementation is much smaller in memory use (probably not a 
	 * big deal practically, but I like to do this as it is a fun little challenge
	 * to see how efficient I can get it; and after all, this is primarily for my
	 * own enlightenment and fun).
	 * 
	 * A quick analysis shows the following:
	 * 
	 * 		Implementations not using this structure will, of necessity, need at 
	 * 		least the following in memory usage (in bytes, where N is the number of 
	 * 		transactions, and the storage structure is an array):
	 * 
	 * 			Transactions obj -> 16
	 * 			Transaction array:
	 * 					Overhead -> 16
	 * 					Transaction obj reference -> 8N
	 * 					Date -> 16N + 8N + ...? (not sure total)
	 * 					String -> 3(40 + 2n)N (n is size of string)
	 * 					boolean -> 1N
	 * 			
	 * 			TOTAL: 32 + 153N + 6Nn
	 * 
	 * 		Compare this to the arrays implementation:
	 * 			
	 * 			Transaction obj -> 16
	 * 			Date Array
	 * 				Overhead -> 16
	 * 				Date -> 16N + 8N + ...?
	 * 			Description, Category, Account  Arrays
	 * 				Overhead -> 16 * 3
	 * 				String -> 3(40 + 2n)N
	 * 			Amount Array
	 * 				Overhead -> 16
	 * 				Integer -> 4N
	 * 			DebitOrCredit Array
	 * 				- None -> Numbers multiplied by -1 if Credit (or Debit for store budget)
	 * 
	 * 			TOTAL: 94 + 148N + 6Nn
	 * 
	 * Thus we see that there isn't an advantage until after 3 transactions
	 * occur, and then with each new transaction memory requirements increase
	 * 4 times as fast.
	 * 
	 * Clearly this is not a very big advantage initially, but there is more.
	 * Whereas this class realy doesn't provide much benefit at lower levels,
	 * the real strength will come when a user wants to handle millions of
	 * transactions at once.
	 * 
	 * How is this better? Arrays will be removed from memory as thresholds
	 * are reached. It is accomplished in this manner:
	 * 
	 * 		N = 10,000 -> Dates converted to int arrays for month, day, year
	 * 						- Has a 3(16 + 4N) = 48 + 12N
	 * 						- NEW Total: 112 + 117N + 6Nn
	 * 		N = 100,000 -> Description Array dropped
	 * 						-NEW Total: 98 + 93N + 4Nn 
	 * 		N = 1,000,000 -> Account Array dropped
	 * 						-NEW Total: 82 + 53N + 2Nn
	 * 		N = 10,000,000 -> Category Array dropped
	 * 						-NEW Total: 66 + 13N + 2Nn
	 * 		N = 100,000,000 -> Will start streaming additional data
	 * 
	 * Honestly, after doing this analysis it is clear that this sort of class
	 * is probably not necessary at all for the scope of what I seek to do.
	 * If there is a huge performance problem with users, revisit this concept,
	 * but not likely to happen.
	 * 
	 * Also, possible consider using this as the basis for some data analysis tools built in in
	 * the future? Possibly market upscaled product to small business with millions of
	 * transactions?
	 * 				
	 * 
	 * @param trans
	 */


	@Override
	public List<Transaction> getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction getTransactionAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction getLastTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsBetweenDates(Date start, Date end)
			throws ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByCategoryAndDate(String cat,
			Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByCategoryAndDates(String cat,
			Date start, Date end) throws ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getIncomeTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getIncomeByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getIncomeBetweenDates(Date start, Date end)
			throws ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTransaction(Transaction tran) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTransaction(Transaction tran) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDebitOrCredit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addTransactions(Transaction[] tran) {
		// TODO Auto-generated method stub
		
	}

}
