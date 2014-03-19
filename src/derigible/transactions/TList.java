/**
 * 
 */
package derigible.transactions;

import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import derigible.utils.StringHelper;
import java.util.Collections;

/**
 * @author marphill
 *
 */
public class TList implements Transactions{
	
	private ArrayList<Transaction> tlist = new ArrayList<Transaction>();
	private HashMap<String, int[]> categories = new HashMap<String, int[]>();
	private HashMap<Integer, HashMap<Integer,HashMap<Integer, int[]>>> years = 
			new HashMap<Integer, HashMap<Integer,HashMap<Integer, int[]>>>();
	private boolean indexed = false;
	private boolean dindexed = false;
	
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
		indexDates(trans);
	}
	
	/**
	 * Constructor using a list of transaction objects.
	 * @param trans
	 */
	public TList(List<Transaction> trans){
		if(trans.getClass() == ArrayList.class){
			this.tlist = (ArrayList<Transaction>) trans;
			indexCategories(trans.toArray(new Transaction[0]));
			indexDates(trans.toArray(new Transaction[0]));
		} else{
			Transaction[] t = trans.toArray(new Transaction[0]);
			indexCategories(t);
			indexDates(t);
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
			if(!indexed){
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
				
			} else{
				if(categories.containsKey(lower(trans[i].getCategory()))){
					int[] tempids = categories.get(lower(trans[i].getCategory()));
					int[] newarray = new int[tempids.length +1];
					for(int j = 0; j < tempids.length; j++){
						newarray[j] = tempids[j];
					}
					newarray[newarray.length - 1] = tlist.size() + i -1;
					categories.put(lower(trans[i].getCategory()), newarray);
				} else{
					int[] tempids = new int[1];
					tempids[0] = tlist.size() + i - 1;
					categories.put(lower(trans[i].getCategory()), tempids);
				}
			}
		}
		indexed = true;
		
	}
	
	/**
	 * Same basic idea as index by categories, except for years.
	 * 
	 * @param trans - the transactions to be indexed
	 */
	private void indexDates(Transaction[] trans){
		int year = 0;
		int month = 0;
		int day = 0;
		for(int i=0; i < trans.length; i++){
			GregorianCalendar gc = trans[i].getDate();
			year = gc.get(Calendar.YEAR);
			month = gc.get(Calendar.MONTH);
			day = gc.get(Calendar.DAY_OF_MONTH);
			if(years.containsKey(year)){
				HashMap<Integer, HashMap<Integer, int[]>> months = years.get(year);
				if(months.containsKey(month)){
					HashMap<Integer, int[]> days = months.get(month);
					if(days.containsKey(day)){
						int[] transacts0 = days.get(day);
						int[] transacts1 = new int[transacts0.length + 1];
						for(int j = 0; j < transacts0.length; j++){
							transacts1[j] = transacts0[j];
						}
						if(!dindexed){
							transacts1[transacts1.length - 1] = i;
						} else{
							transacts1[transacts1.length - 1] = i + tlist.size() -1;
						}
						days.put(day, transacts1);
					} else {
						if(!dindexed){
							days.put(day, new int[] {i});
						} else{
							days.put(day, new int[] {i + tlist.size() -1});
						}
					}
				} else {
					HashMap<Integer, int[]> days = new HashMap<Integer, int[]>();
					days.put(day, new int[] {i});
					months.put(month, days);
				}
			} else {
				HashMap<Integer, int[]> days = new HashMap<Integer, int[]>();
				HashMap<Integer, HashMap<Integer, int[]>> months = 
						new HashMap<Integer, HashMap<Integer, int[]>>();
				if(!dindexed){
					days.put(day, new int[] {i});
				}
				else{
					days.put(day, new int[] {i + tlist.size() -1});
				}
				months.put(month, days);
				years.put(year, months);
			}
		}
		dindexed = true;
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
	public int[] getYearsWithTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getMonthsInYearWithTransactions(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getDaysInMonthInYearWithTransactions(int year, int month) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getAllDaysInYearWithTransactions(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(years.containsKey(cal.get(Calendar.YEAR))){
			HashMap<Integer, HashMap<Integer, int[]>> months = 
					years.get(cal.get(Calendar.YEAR));
			if(months.containsKey(cal.get(Calendar.MONTH))){
				HashMap<Integer, int[]> days = months.get(cal.get(Calendar.MONTH));
				if(days.containsKey(cal.get(Calendar.DAY_OF_MONTH))){
					int[] dayarray = days.get(cal.get(Calendar.DAY_OF_MONTH));
					ArrayList<Transaction> trans = new ArrayList<Transaction>();
					for(int i =0; i < dayarray.length; i++){
						trans.add(tlist.get(dayarray[i]));
					}
					return trans;
				}
			}
		}
		return new ArrayList<Transaction>();
	}

	@Override
	public List<Transaction> getTransactionsBetweenDates(Date start, Date end)
			throws ArrayIndexOutOfBoundsException {
		if(end.before(start)) {
			String msg = "Start date after end date."; 
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		if(end.compareTo(start) == 0){ //Same date
			return getTransactionsByDate(start);
		}
		ArrayList<Transaction> trans = new ArrayList<Transaction>();
		Calendar start0 = Calendar.getInstance();
		start0.setTime(start);
		Calendar end0 = Calendar.getInstance();
		end0.setTime(end);
		int yearstart = start0.get(Calendar.YEAR);
		int yearend = end0.get(Calendar.YEAR);
		int monthstart = start0.get(Calendar.MONTH);
		int monthend = end0.get(Calendar.MONTH);
		int daystart = start0.get(Calendar.DAY_OF_MONTH);
		int dayend = end0.get(Calendar.DAY_OF_MONTH);
		
		//Do case where dates are within the same year and month but not the same day
		if(yearstart == yearend	&& monthstart == monthend){
			for(Map.Entry<Integer, int[]> day : years.get(yearstart).get(monthstart).entrySet()){
				if(day.getKey() >= daystart && day.getKey() <= dayend){
					for(int j : day.getValue()){		
						trans.add(tlist.get(j));
					}
				}
			}
		}
		//Do case where dates are within same year but not the same month
		else if(yearstart == yearend && monthstart != monthend){
			for(Integer month : years.get(yearstart).keySet()){
				if(month >= monthstart && month <= monthend){
					for(Map.Entry<Integer, int[]> day : 
						years.get(yearstart).get(month).entrySet()){
						if(day.getKey() >= daystart && day.getKey() <= dayend){
							for(int j : day.getValue()){
								trans.add(tlist.get(j));
							}
						}
					}
				}
			}
		}
		//Do case where dates are not within the same year
		else {
			for(Integer year : years.keySet()){ //Only interested if not in edge years
				if(year > yearstart && year < yearend){
					for(HashMap<Integer, int[]> month : years.get(year).values()){
						for(int[] day : month.values()){
							for(int j : day){
								trans.add(tlist.get(j));
							}
						}
					}
				} else if(year == yearstart){ //Only interested in edge case start
					for(Map.Entry<Integer, HashMap<Integer, int[]>> month : 
						years.get(year).entrySet()){
						if(month.getKey() >= monthstart){
							for(Map.Entry<Integer, int[]> day : month.getValue().entrySet()){
								if(day.getKey() >= daystart){
									for(int j : day.getValue()){
										trans.add(tlist.get(j));
									}
								}
							}
						}
					}
				} else if(year == yearend){ //Only interested in edge case end
					for(Map.Entry<Integer, HashMap<Integer, int[]>> month : 
						years.get(year).entrySet()){
						if(month.getKey() <= monthend){
							for(Map.Entry<Integer, int[]> day : month.getValue().entrySet()){
								if(day.getKey() <= dayend){
									for(int j : day.getValue()){
										trans.add(tlist.get(j));
									}
								}
							}
						}
					}
				}
			}
		}
		return trans;
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
		tlist.add(tran);
		indexCategories(new Transaction[] {tran});
		indexDates(new Transaction[] {tran});
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
		 trans2 = new Transaction[5];
			trans2[0] = new Transact(new GregorianCalendar(2014,12,13), "Ice Cream",
					4.50, "Dessert",
					"Mastercard", false);
			trans2[1] = new Transact(new GregorianCalendar(2014,12,14), "Home Improvement",
					56.57, "Home Improvement",
					"Mastercard", false);
			trans2[2] = new Transact(new GregorianCalendar(2014,12,15), "New Hammer",
					14.50, "hOme Improvement",
					"Mastercard", false);
			trans2[3] = new Transact(new GregorianCalendar(2014,12,16), "Macey's Grocery Store",
					24.50, "Groceries",
					"Mastercard", false);
			trans2[4] = new Transact(new GregorianCalendar(2014,12,15), "Ice Cream",
					4.50, "Dessert",
					"Mastercard", false);
		
			TList t = new TList(trans2);
	 }


}
