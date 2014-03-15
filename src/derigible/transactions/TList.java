/**
 * 
 */
package derigible.transactions;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import derigible.utils.StringHelper;

/**
 * @author marphill
 *
 */
public class TList implements Transactions{
	
	private ArrayList<Transaction> tlist = new ArrayList<Transaction>();
	private HashMap<String, int[]> categories = new HashMap<String, int[]>();
	
	/**
	 * Constructor using an array of transaction objects. 
	 * 
	 * @param trans
	 */
	public TList(Transaction[] trans){
		for(int i = 0; i < trans.length; i++){
			tlist.add(trans[i]);
		}
		indexCategories(trans);
	}
	
	/**
	 * Constructor using a list of transaction objects.
	 * @param trans
	 */
	public TList(List<Transaction> trans){
		if(trans.getClass() == ArrayList.class){
			this.tlist = (ArrayList) trans;
			indexCategories(trans.toArray(new Transaction[0]));
		} else{
			Transaction[] t = trans.toArray(new Transaction[0]);
			indexCategories(t);
			for(int i = 0; i < t.length; i++){
				tlist.add(t[i]);
			}
		}
	}
	
	/**
	 * Hashmap keeps track of category by key (case is ignored). Since it is not likely
	 * the arrays will change much after being indexed, all arrays will be of a fixed length.
	 * It is better to take more time upfront and place the keys in a fixed length array
	 * then worry about null values later. Keys stored in lower case to avoid duplicates.
	 * 
	 * @param trans - the transactions to be indexed
	 */
	private void indexCategories(Transaction[] trans){
		for(int i = 0; i < trans.length; i++){
			if(categories.containsKey(lower(trans[i].getCategory()))){
				int[] tempids = categories.get(lower(trans[i].getCategory()));
				int[] newarray = new int[tempids.length +1];
				for(int j = 0; j < tempids.length; j++){
					newarray[j] = tempids[j];
				}
				newarray[newarray.length - 1] = i;
				categories.put(lower(trans[i].getCategory()), newarray);
			} else{
				int[] tempids = new int[1];
				tempids[0] = i;
				categories.put(lower(trans[i].getCategory()), tempids);
			}
		}
	}
	
	/**
	 * Simply a convenience method to make lowering much cleaner.
	 */
	private String lower(String input){
		return StringHelper.formatStringToLowercase(input);
	}
	
	@Override
	public List<Transaction> getTransactions() {
			return tlist;
	}

	@Override
	public Transaction getTransactionAt(int index) {
		return tlist.get(index);
	}

	@Override
	public Transaction getLastTransaction() {
		return tlist.get(tlist.size() - 1);
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
		if(categories.containsKey(lower(category))){
			ArrayList<Transaction> t = new ArrayList<Transaction>();
			int [] t0 = categories.get(lower(category));
			for(int i = 0; i < t0.length; i++){
				t.add(tlist.get(t0[i]));
			}
			return t;
		}
		return new ArrayList<Transaction>();
	}
	
	@Override
	public List<Transaction> getTransactionsByCategories(String[] categories) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByCategoryAndDate(String cat,
			Date date) {
		ArrayList<Transaction> trans = (ArrayList<Transaction>) getTransactionsByCategory(cat);
		for(int i =0; i < trans.size(); i++){
			if(!trans.get(i).getDate().equals(date)){
				trans.remove(i);
			}
		}
		
		return trans;
	}

	@Override
	public List<Transaction> getTransactionsByCategoryAndDates(String cat,
			Date start, Date end) throws ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Transaction> getTransactionsByCategoriesAndDates(String[] cats,
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
	public void addTransactions(Transaction[] tran) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getCategories() {
		return categories.keySet().toArray(new String[categories.keySet().size()]);
	}

	 public static void main(String[] args) {
		 Transaction[] trans2 = new Transaction[5];
		trans2[0] = new Transact(new Date(1000000L), "Ice Cream",
				4.50, "Dessert",
				"Mastercard", false);
		trans2[1] = new Transact(new Date(1546700L), "Home Improvement",
				56.57, "Home Improvement",
				"Mastercard", false);
		trans2[2] = new Transact(new Date(1899900L), "New Hammer",
				14.50, "hOme Improvement",
				"Mastercard", false);
		trans2[3] = new Transact(new Date(1999900L), "Macey's Grocery Store",
				24.50, "Groceries",
				"Mastercard", false);
		trans2[4] = new Transact(new Date(1234500L), "Ice Cream",
				4.50, "Dessert",
				"Mastercard", false);
		
			TList t = new TList(trans2);
	 }

}
