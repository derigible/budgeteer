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
	private String[] catIndex;
	private boolean catIndexed;
	
	/**
	 * Constructor using an array of transaction objects. 
	 * 
	 * @param trans
	 */
	public TList(Transaction[] trans){
		for(int i = 0; i < trans.length; i++){
			tlist.add(trans[i]);
			indexCategories(trans);
		}
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
	 * Since categories aren't likely to change during runtime, this isn't likely to be called.
	 * Hence the inefficiency. It is better to have a fixed array every time to avoid messy nulls.
	 * 
	 * @param trans
	 */
	private void indexCategories(Transaction[] trans){
		if(catIndexed){
			String[] catIndextemp = new String[trans.length];
			int newer = 0;
			for(int i=0; i < catIndex.length; i++){
				for(int j = 0; j < trans.length; j++){
					if(!trans[j].getCategory().equalsIgnoreCase(catIndex[i])){
						catIndextemp[newer] = trans[j].getCategory();
						newer++;
					}
				}
			}
			if(newer > 0){
				String[] newcatIndex = new String[catIndex.length + newer];
				for(int k = 0; k < catIndex.length; k++){
					newcatIndex[k] = catIndex[k];
				}
				for(int k = catIndex.length -1,  l = 0; l < newer; k++, l++){
					
					newcatIndex[k] = catIndextemp[l];
				}
			}
		}
		else{
			String[] tempcats = new String[trans.length];
			int newer = 0;
			for(int i = 0; i < trans.length; i++){
				for(int j = 0; j < newer; j++){
					if(!trans[i].getCategory().equalsIgnoreCase(tempcats[j])){
						tempcats[++newer] = trans[i].getCategory();
					}
				}
			}
			if(newer >0){
				catIndex = new String[newer];
				for(int k =0; k < newer; k++){
					catIndex[k] = tempcats[k];
				}
			}
		}
	}
	
	private void indexCategories(String cat){
		
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

	@Override
	public void addTransactions(Transaction[] tran) {
		// TODO Auto-generated method stub
		
	}

}
