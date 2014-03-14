/**
 * 
 */
package derigible.transactions;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marphill
 *
 */
public class TList implements Transactions{
	
	private ArrayList<Transaction> tlist = new ArrayList<Transaction>();
	
	/**
	 * Constructor using an array of transaction objects. 
	 * 
	 * @param trans
	 */
	public TList(Transaction[] trans){
		for(int i = 0; i < trans.length; i++){
			tlist.add(trans[i]);
		}
	}
	
	/**
	 * Constructor using a list of transaction objects.
	 * @param trans
	 */
	public TList(List<Transaction> trans){
		if(trans.getClass() == ArrayList.class){
			this.tlist = (ArrayList) trans;
		} else{
			Transaction[] t = trans.toArray(new Transaction[0]);
			for(int i = 0; i < t.length; i++){
				tlist.add(t[i]);
			}
		}
	}
	
	
	@Override
	/**
	 * Returns the underlying java.util.ArrayList.
	 * 
	 * @return the Transactions ArrayList
	 */
	public List<Transaction> getTransactions() {
			return tlist;
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

}
