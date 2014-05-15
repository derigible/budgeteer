/*******************************************************************************
 * Copyright (c) 2014 Derigible Enterprises.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Derigible Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.derigible.com/license
 *
 * Contributors:
 *     Derigible Enterprises - initial API and implementation
 *******************************************************************************/

package derigible.transactions;

import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import derigible.utils.StringU;

/**
 * @author marphill
 *
 */
public class TList implements Transactions {

    /**
     * The transactions list. This list should only hold all transactions.
     */
    private ArrayList<Transaction> tlist;
    /**
     * The income transactions list. Should only hold Credits, not Debits.
     */
    private ArrayList<Transaction> creditslist = new ArrayList<Transaction>();
    private ArrayList<Transaction> debitslist = new ArrayList<Transaction>();
    private HashMap<String, int[]> categories = new HashMap<String, int[]>();
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, int[]>>> years
            = new HashMap<Integer, HashMap<Integer, HashMap<Integer, int[]>>>();
    private HashMap<String, int[]> accounts = new HashMap<String, int[]>();
    private HashMap<String, Transaction> transactions = new HashMap<String, Transaction>();
    private boolean cindexed = false;
    private boolean dindexed = false;
    private boolean aindexed = false;
    private ArrayList<Transaction> excluded = new ArrayList<Transaction>();

    /**
     * Constructor using an array of transaction objects.
     *
     * @param trans the transactions to add
     */
    public TList(Transaction[] trans) {
        init(trans);
    }

    /**
     * Constructor using a list of transaction objects.
     *
     * @param trans the transactions to add
     */
    public TList(List<Transaction> trans) {
        init(trans.toArray(new Transaction[0]));
    }

    /**
     * Internal constructor to allow for two different types of input but
     * needing only one set of code.
     *
     * @param trans
     */
    private void init(Transaction[] trans) {
        tlist = new ArrayList<Transaction>(trans.length);
        for (Transaction tran : trans) {
            tlist.add(tran);
            if (!tran.isCredit()) {
                debitslist.add(tran);
            } else {
                creditslist.add(tran);
            }
            transactions.put(tran.getGUID(), tran);
        }
        Transaction[] newt = tlist.toArray(new Transaction[0]);
        indexCategories(newt);
        indexDates(newt);
        indexAccounts(newt);
    }

    /**
     * Hashmap keeps track of category by key (case is ignored). Since it is not
     * likely the arrays will change much after being cindexed, all arrays will
     * be of a fixed length. It is better to take more time upfront and place
     * the keys in a fixed length array then worry about null values later. Keys
     * stored in lower case to avoid duplicates.
     *
     * @param trans the transactions to be cindexed
     */
    private void indexCategories(Transaction[] trans) {
        int arrayend = 0;
        if (cindexed) {
            arrayend = tlist.size() - 1;
        }
        for (int i = 0; i < trans.length; i++) {
            if (categories.containsKey(lower(trans[i].getCategory()))) {
                int[] tempids = categories.get(lower(trans[i].getCategory()));
                int[] newarray = new int[tempids.length + 1];
                for (int j = 0; j < tempids.length; j++) {
                    newarray[j] = tempids[j];
                }
                newarray[newarray.length - 1] = i + arrayend;
                categories.put(lower(trans[i].getCategory()), newarray);
            } else {
                int[] tempids = new int[1];
                tempids[0] = i + arrayend;
                categories.put(lower(trans[i].getCategory()), tempids);
            }
        }
        cindexed = true;
    }

    /**
     * Same basic idea as index by categories, except for years.
     *
     * @param trans the transactions to be cindexed
     */
    private void indexDates(Transaction[] trans) {
        int year = 0;
        int month = 0;
        int day = 0;
        int arrayend = 0;
        if (dindexed) {
            arrayend = tlist.size() - 1;
        }
        for (int i = 0; i < trans.length; i++) {
            GregorianCalendar gc = trans[i].getDate();
            year = gc.get(Calendar.YEAR);
            month = gc.get(Calendar.MONTH);
            day = gc.get(Calendar.DAY_OF_MONTH);
            if (years.containsKey(year)) {
                HashMap<Integer, HashMap<Integer, int[]>> months = years.get(year);
                if (months.containsKey(month)) {
                    HashMap<Integer, int[]> days = months.get(month);
                    if (days.containsKey(day)) {
                        int[] transacts0 = days.get(day);
                        int[] transacts1 = new int[transacts0.length + 1];
                        for (int j = 0; j < transacts0.length; j++) {
                            transacts1[j] = transacts0[j];
                        }
                        transacts1[transacts1.length - 1] = i + arrayend;
                        days.put(day, transacts1);
                    } else {
                        days.put(day, new int[]{i + arrayend});
                    }
                } else {
                    HashMap<Integer, int[]> days = new HashMap<Integer, int[]>();
                    days.put(day, new int[]{i + arrayend});
                    months.put(month, days);
                }
            } else {
                HashMap<Integer, int[]> days = new HashMap<Integer, int[]>();
                HashMap<Integer, HashMap<Integer, int[]>> months
                        = new HashMap<Integer, HashMap<Integer, int[]>>();
                days.put(day, new int[]{i + arrayend});
                months.put(month, days);
                years.put(year, months);
            }
        }
        dindexed = true;
    }

    private void indexAccounts(Transaction[] trans) {
        int arrayend = 0;
        if (aindexed) {
            arrayend = tlist.size() - 1;
        }
        for (int i = 0; i < trans.length; i++) {
            if (accounts.containsKey(lower(trans[i].getAccount()))) {
                int[] tempids = accounts.get(lower(trans[i].getAccount()));
                int[] newarray = new int[tempids.length + 1];
                for (int j = 0; j < tempids.length; j++) {
                    newarray[j] = tempids[j];
                }
                newarray[newarray.length - 1] = i + arrayend;
                accounts.put(lower(trans[i].getAccount()), newarray);
            } else {
                int[] tempids = new int[1];
                tempids[0] = i + arrayend;
                accounts.put(lower(trans[i].getAccount()), tempids);
            }
        }
        aindexed = true;
    }

    /**
     * Simply a convenience method to make lowering much cleaner.
     */
    private String lower(String input) {
        return StringU.lower(input);
    }

    @Override
    public List<Transaction> getTransactions() {
        return filterExcluded(tlist);
    }
    
    @Override
    public Transaction getTransactionByGUID(String guid){
    	return transactions.get(guid);
    }

    @Override
    public List<Transaction> getDebits() {
        return filterExcluded(debitslist);
    }

    @Override
    public Transaction getTransactionAt(int index) {
        return tlist.get(index);
    }

    /**
     * Used as a means to recursively find the last included index.
     */
    private Transaction getLastTransaction(List<Transaction> trans, int indexreduce) {
        if (trans.size() >= indexreduce && trans.get(trans.size() - indexreduce).isExcluded()) {
            return getLastTransaction(trans, ++indexreduce);
        } else {
            return trans.get(trans.size() - indexreduce);
        }
    }

    @Override
    public Transaction getLastTransaction() {
        return getLastTransaction(tlist, 1);
    }

    @Override
    public int[] getYearsWithTransactions() {
        int[] years0 = new int[years.keySet().size()];
        Integer[] years1 = years.keySet().toArray(new Integer[years.keySet().size()]);
        for (int i = 0; i < years1.length; i++) {
            years0[i] = years1[i];
        }
        return years0;
    }

    @Override
    public int[] getMonthsInYearWithTransactions(int year) {
        int[] months;
        if (years.containsKey(year)) {
            Integer[] months0 = years.get(year).keySet()
                    .toArray(new Integer[years.get(year).keySet().size()]);
            months = new int[months0.length];
            for (int i = 0; i < years.get(year).keySet().size(); i++) {
                months[i] = months0[i];
            }
        } else {
            months = new int[0];
        }
        return months;
    }

    @Override
    public int[] getDaysInMonthInYearWithTransactions(int year, int month) {
        int[] days;
        if (years.containsKey(year)) {
            if (years.get(year).containsKey(month)) {
                HashMap<Integer, int[]> month0 = years.get(year).get(month);
                Integer[] days0 = month0.keySet().toArray(new Integer[month0.keySet().size()]);
                days = new int[days0.length];
                for (int i = 0; i < days0.length; i++) {
                    days[i] = days0[i];
                }
            } else {
                days = new int[0];
            }
        } else {
            days = new int[0];
        }
        return days;
    }

    @Override
    public int[] getAllDaysInYearWithTransactions(int year) {
		//TODO not working correctly. Needs to return the day of the year as an integer, not the integer
        //stored in the array
        ArrayList<Integer> days = new ArrayList<Integer>();
        for (HashMap<Integer, int[]> month : years.get(year).values()) {
            for (Integer i : month.keySet().toArray(new Integer[month.keySet().size()])) {
                days.add(i);
            }
        }
        int[] daysreturn = new int[days.size()];
        for (int k = 0; k < days.size(); k++) {
            daysreturn[k] = days.get(k);
        }
        return daysreturn;
    }

    @Override
    public List<Transaction> getByDate(Date date) {
        return filterExcluded(this.filterByDate(date, tlist));
    }

    @Override
    public List<Transaction> getBetweenDates(Date start, Date end)
            throws ArrayIndexOutOfBoundsException {
        return filterExcluded(this.filterByDates(start, end, tlist));
    }

    @Override
    public List<Transaction> getByCategory(String category) {
        List<Transaction> l = this.filterByCategory(category, tlist);
        return filterExcluded(l);
    }

    @Override
    public List<Transaction> getByCategories(String[] categories) {
        return filterExcluded(this.filterByCategories(categories, tlist));
    }

    @Override
    public List<Transaction> getByCategoryAndDate(String cat, Date date) {
        ArrayList<Transaction> trans = (ArrayList<Transaction>) getByCategory(cat);
        return filterExcluded(this.filterByDate(date, trans));
    }

    @Override
    public List<Transaction> getByCategoriesAndDate(
            String[] categories, Date date) {
        List<Transaction> l = this.filterByCategories(categories, tlist);
        return filterExcluded(this.filterByDate(date, l));
    }

    @Override
    public List<Transaction> getByCategoryAndDates(String cat,
            Date start, Date end) throws ArrayIndexOutOfBoundsException {
        List<Transaction> l = this.getByCategory(cat);
        return filterExcluded(this.filterByDates(start, end, l));
    }

    @Override
    public List<Transaction> getByCategoriesAndDates(String[] cats,
            Date start, Date end) throws ArrayIndexOutOfBoundsException {
        List<Transaction> l = this.filterByDates(start, end, tlist);
        return filterExcluded(this.filterByCategories(cats, l));
    }

    @Override
    public List<Transaction> getByAccount(String account) {
        ArrayList<Transaction> trans0 = (ArrayList<Transaction>) filterByAccount(account, tlist);
        return filterExcluded(trans0);
    }
    
    @Override
	public List<Transaction> getByAccounts(String[] accounts) {
		return filterExcluded(this.filterByAccounts(accounts, tlist));
	}

    @Override
    public List<Transaction> getCredits() {
        return filterExcluded(creditslist);
    }

    @Override
    public List<Transaction> getIncomeByDate(Date date) {
        return filterExcluded(filterByDate(date, creditslist));
    }

    @Override
    public List<Transaction> getIncomeBetweenDates(Date start, Date end)
            throws ArrayIndexOutOfBoundsException {
        if (end.before(start)) {
            String msg = "Start date after end date.";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        if (start.equals(end)) {
            return filterExcluded(this.getIncomeByDate(start));
        }
        return filterExcluded(filterByDates(start, end, creditslist));
    }

    @Override
    public void addTransaction(Transaction tran) {
        tlist.add(tran);
        indexCategories(new Transaction[]{tran});
        indexDates(new Transaction[]{tran});
        indexAccounts(new Transaction[]{tran});
        if (!tran.isCredit()) {
            debitslist.add(tran);
        } else {
            creditslist.add(tran);
        }
    }

    @Override
    public void excludeTransaction(Transaction tran) {
        tran.setExcluded(true);
        excluded.add(tran);
    }

    @Override
    public void includeTransaction(Transaction tran) {
        tran.setExcluded(false);
        excluded.remove(tran);
    }

    @Override
    public void addTransactions(Transaction[] trans) {
        for (Transaction tran : trans) {
            addTransaction(tran);
        }
    }

    @Override
    public String[] getCategories() {
        String[] tempStrings = categories.keySet().toArray(new String[categories.keySet().size()]);
        for (int i = 0; i < tempStrings.length; i++) {
            tempStrings[i] = StringU.formatString(tempStrings[i]);
        }
        return tempStrings;
    }

    @Override
    public String[] getAccounts() {
        String[] tempStrings = accounts.keySet().toArray(new String[accounts.keySet().size()]);
        for (int i = 0; i < tempStrings.length; i++) {
            tempStrings[i] = StringU.formatString(tempStrings[i]);
        }
        return tempStrings;
    }

    @Override
    public boolean hasCategory(String category) {
        return categories.containsKey(lower(category));
    }

    @Override
    public boolean hasAccount(String account) {
        return accounts.containsKey(lower(account));
    }

    @Override
    public List<Transaction> getExcluded() {
        return excluded;
    }

	//////////////////////////////////////////////////////////////////////////////////////////////
    // Filter Methods to use on lists of transactions.
    @Override
    public List<Transaction> filterByAccount(String account, List<Transaction> l) {
        if (accounts.containsKey(lower(account))) {
            ArrayList<Transaction> t = new ArrayList<Transaction>();
            int[] t0 = accounts.get(lower(account));
            if (l.equals(tlist)) {
                for (int i = 0; i < t0.length; i++) {
                    t.add(tlist.get(t0[i]));
                }
            } else {
                for (int i = 0; i < t0.length; i++) {
                    if (l.contains(tlist.get(t0[i])) && !t.contains(tlist.get(t0[i]))) {
                        t.add(tlist.get(t0[i]));
                    }
                }
            }
            return t;
        }
        return new ArrayList<Transaction>();
    }

    @Override
    public List<Transaction> filterByAccounts(String[] accounts,
            List<Transaction> l) {
        ArrayList<Transaction> t = new ArrayList<Transaction>();
        for (String account : accounts) {
            if (this.accounts.containsKey(lower(account))) {
                int[] t0 = this.accounts.get(lower(account));
                if (l.equals(tlist)) {
                    for (int i = 0; i < t0.length; i++) {
                        t.add(l.get(t0[i]));
                    }
                } else {
                    for (int i = 0; i < t0.length; i++) {
                        if (l.contains(tlist.get(t0[i])) && !t.contains(tlist.get(t0[i]))) {
                            t.add(tlist.get(t0[i]));
                        }
                    }
                }
            } else {
                continue;
            }
        }
        return t;
    }

    @Override
    public List<Transaction> filterByCategory(String category,
            List<Transaction> l) {
        if (categories.containsKey(lower(category))) {
            ArrayList<Transaction> t = new ArrayList<Transaction>();
            int[] t0 = categories.get(lower(category));
            for (int i = 0; i < t0.length; i++) {
                t.add(tlist.get(t0[i]));
            }
            return t;
        }
        return new ArrayList<Transaction>();
    }

    @Override
    public List<Transaction> filterByCategories(String[] categories,
            List<Transaction> l) {
        ArrayList<Transaction> t = new ArrayList<Transaction>();
        for (String category : categories) {
            if (this.categories.containsKey(lower(category))) {
                int[] t0 = this.categories.get(lower(category));
                if (l.equals(tlist)) {
                    for (int i = 0; i < t0.length; i++) {
                        t.add(l.get(t0[i]));
                    }
                } else {
                    for (int i = 0; i < t0.length; i++) {
                        if (l.contains(tlist.get(t0[i])) && !t.contains(tlist.get(t0[i]))) {
                            t.add(tlist.get(t0[i]));
                        }
                    }
                }
            } else {
                continue;
            }
        }
        return t;
    }

    @Override
    public List<Transaction> filterByDate(Date date, List<Transaction> l) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (years.containsKey(cal.get(Calendar.YEAR))) {
            HashMap<Integer, HashMap<Integer, int[]>> months
                    = years.get(cal.get(Calendar.YEAR));
            if (months.containsKey(cal.get(Calendar.MONTH))) {
                HashMap<Integer, int[]> days = months.get(cal.get(Calendar.MONTH));
                if (days.containsKey(cal.get(Calendar.DAY_OF_MONTH))) {
                    int[] dayarray = days.get(cal.get(Calendar.DAY_OF_MONTH));
                    ArrayList<Transaction> trans = new ArrayList<Transaction>();
                    for (int i = 0; i < dayarray.length; i++) {
                        if (l.equals(tlist)) {
                            trans.add(tlist.get(dayarray[i]));
                        } else { // Do filtering case of a list not tlist
                            for (int j : dayarray) {
                                if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                    trans.add(tlist.get(j));
                                }
                            }
                        }
                    }
                    return trans;
                }
            }
        }
        return new ArrayList<Transaction>();
    }

    @Override
    public List<Transaction> filterByDates(Date start, Date end,
            List<Transaction> l) {
        if (end.before(start)) {
            String msg = "Start date after end date.";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        if (end.compareTo(start) == 0) { //Same date
            return getByDate(start);
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
        if (yearstart == yearend && monthstart == monthend) {
            for (Map.Entry<Integer, int[]> day : years.get(yearstart).get(monthstart).entrySet()) {
                if (day.getKey() >= daystart && day.getKey() <= dayend) {
                    if (l.equals(tlist)) {
                        for (int j : day.getValue()) {
                            trans.add(l.get(j));
                        }
                    } else { // Do filtering case of a list not tlist
                        for (int j : day.getValue()) {
                            if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                trans.add(tlist.get(j));
                            }
                        }
                    }
                }
            }
        } //Do case where dates are within same year but not the same month
        else if (yearstart == yearend && monthstart != monthend) {
            for (Integer month : years.get(yearstart).keySet()) {
                if (month > monthstart && month < monthend) {//Worry only about non edge months
                    for (Map.Entry<Integer, int[]> day
                            : years.get(yearstart).get(month).entrySet()) {
                        if (l.equals(tlist)) {
                            for (int j : day.getValue()) {
                                trans.add(l.get(j));
                            }
                        } else { // Do filtering case of a list not tlist
                            for (int j : day.getValue()) {
                                if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                    trans.add(tlist.get(j));
                                }
                            }
                        }
                    }
                } else if (month == monthstart) { //Do Start edge case
                    for (Map.Entry<Integer, int[]> day
                            : years.get(yearstart).get(month).entrySet()) {
                        if (day.getKey() >= daystart) {
                            if (l.equals(tlist)) {
                                for (int j : day.getValue()) {
                                    trans.add(l.get(j));
                                }
                            } else { // Do filtering case of a list not tlist
                                for (int j : day.getValue()) {
                                    if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                        trans.add(tlist.get(j));
                                    }
                                }
                            }
                        }
                    }
                } else if (month == monthend) { //Do End edge case
                    for (Map.Entry<Integer, int[]> day
                            : years.get(yearstart).get(month).entrySet()) {
                        if (day.getKey() <= dayend) {
                            if (l.equals(tlist)) {
                                for (int j : day.getValue()) {
                                    trans.add(l.get(j));
                                }
                            } else { // Do filtering case of a list not tlist
                                for (int j : day.getValue()) {
                                    if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                        trans.add(tlist.get(j));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } //Do case where dates are not within the same year
        else {
            for (Integer year : years.keySet()) { //Only interested if not in edge years
                if (year > yearstart && year < yearend) {
                    for (HashMap<Integer, int[]> month : years.get(year).values()) {
                        for (int[] day : month.values()) {
                            if (l.equals(tlist)) {
                                for (int j : day) {
                                    trans.add(l.get(j));
                                }
                            } else { // Do filtering case of a list not tlist
                                for (int j : day) {
                                    if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                        trans.add(tlist.get(j));
                                    }
                                }
                            }
                        }
                    }
                } else if (year == yearstart) { //Only interested in edge case start
                    for (Map.Entry<Integer, HashMap<Integer, int[]>> month
                            : years.get(year).entrySet()) {
                        if (month.getKey() >= monthstart) {
                            for (Map.Entry<Integer, int[]> day : month.getValue().entrySet()) {
                                if (day.getKey() >= daystart) {
                                    if (l.equals(tlist)) {
                                        for (int j : day.getValue()) {
                                            trans.add(l.get(j));
                                        }
                                    } else { // Do filtering case of a list not tlist
                                        for (int j : day.getValue()) {
                                            if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                                trans.add(tlist.get(j));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (year == yearend) { //Only interested in edge case end
                    for (Map.Entry<Integer, HashMap<Integer, int[]>> month
                            : years.get(year).entrySet()) {
                        if (month.getKey() <= monthend) {
                            for (Map.Entry<Integer, int[]> day : month.getValue().entrySet()) {
                                if (day.getKey() <= dayend) {
                                    if (l.equals(tlist)) {
                                        for (int j : day.getValue()) {
                                            trans.add(l.get(j));
                                        }
                                    } else { // Do filtering case of a list not tlist
                                        for (int j : day.getValue()) {
                                            if (l.contains(tlist.get(j)) && !trans.contains(tlist.get(j))) {
                                                trans.add(tlist.get(j));
                                            }
                                        }
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

    private List<Transaction> filterExcluded(List<Transaction> trans) {
        ArrayList<Transaction> trans0 = new ArrayList<Transaction>(trans.size());
        for (Transaction tran : trans) {
            if (!tran.isExcluded()) {
                trans0.add(tran);
            }
        }
        return trans0;
    }

	@Override
	public boolean reindexTransaction(String Guid, Transaction t) {
		Transaction t0 = this.transactions.get(Guid);
		if(!t0.getAccount().equalsIgnoreCase(t.getAccount())){
			return reindexAccount(t0, t);
		}
		if(!t0.getCategory().equalsIgnoreCase(t.getCategory())){
			return reindexCategory(t0, t);
		}
		if(!t0.getDate().equals(t.getDate())){
			return reindexDate(t0, t);
		}
		return false;
	}

	private boolean reindexDate(Transaction t0, Transaction t) {
		GregorianCalendar c = t0.getDate();
		int[] points = years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).get(c.get(Calendar.DAY_OF_MONTH));
		if(points.length == 1){
			years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).remove(c.get(Calendar.DAY_OF_MONTH));
			if(years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).size() == 0){
				years.get(c.get(Calendar.YEAR)).remove(c.get(Calendar.MONTH));
				if(years.get(c.get(Calendar.YEAR)).size() == 0){
					years.remove(c.get(Calendar.YEAR));
				}
			}
		}
		c = t.getDate();
		if(years.containsKey(c.get(Calendar.YEAR))){
			if(years.get(c.get(Calendar.YEAR)).containsKey(c.get(Calendar.MONTH))){
				if(years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).containsKey(c.get(Calendar.DAY_OF_MONTH))){
					int[] transacts0 = years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).get(c.get(Calendar.DAY_OF_MONTH));
                    int[] transacts1 = new int[transacts0.length + 1];
                    for (int j = 0; j < transacts0.length; j++) {
                        transacts1[j] = transacts0[j];
                    }
                    transacts1[transacts1.length - 1] = getTransactionIndex(t0);
                    years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).put(c.get(Calendar.DAY_OF_MONTH), transacts1);
				} else {
					years.get(c.get(Calendar.YEAR)).get(c.get(Calendar.MONTH)).put(c.get(Calendar.DAY_OF_MONTH), new int[] {getTransactionIndex(t0)});
				}
			} else {
				HashMap<Integer, int[]> day = new HashMap<Integer, int[]>();
				day.put(c.get(Calendar.DAY_OF_MONTH), new int[] {getTransactionIndex(t0)});
				years.get(c.get(Calendar.YEAR)).put(c.get(Calendar.MONTH), day);
			}
		} else {
			HashMap<Integer, int[]> day = new HashMap<Integer, int[]>();
			day.put(c.get(Calendar.DAY_OF_MONTH), new int[] {getTransactionIndex(t0)});
			HashMap<Integer, HashMap<Integer, int[]>> month = new HashMap<Integer, HashMap<Integer, int[]>>();
			month.put(c.get(Calendar.MONTH), day);
			years.put(c.get(Calendar.YEAR), month);
		}
		t0.setDate(c);
		return true;
	}

	private boolean reindexCategory(Transaction t0, Transaction t) {
		String cat = lower(t0.getCategory());
		if(categories.get(cat).length == 1){
			categories.remove(cat);
		}
		cat = lower(t.getCategory());
		if(categories.containsKey(cat)){
			int[] transacts0 = categories.get(cat);
            int[] transacts1 = new int[transacts0.length + 1];
            for (int j = 0; j < transacts0.length; j++) {
                transacts1[j] = transacts0[j];
            }
            transacts1[transacts1.length - 1] = getTransactionIndex(t0);
            categories.put(cat, transacts1);
		} else {
			categories.put(cat, new int[] {getTransactionIndex(t0)});
		}
		t0.setCategory(cat);
		return true;
	}

	private boolean reindexAccount(Transaction t0, Transaction t) {
		String act = lower(t0.getAccount());
		if(accounts.get(act).length == 1){
			accounts.remove(act);
		}
		act = lower(t.getAccount());
		if(accounts.containsKey(act)){
			int[] transacts0 = accounts.get(act);
            int[] transacts1 = new int[transacts0.length + 1];
            for (int j = 0; j < transacts0.length; j++) {
                transacts1[j] = transacts0[j];
            }
            transacts1[transacts1.length - 1] = getTransactionIndex(t0);
            accounts.put(act, transacts1);
		} else {
			accounts.put(act, new int[] {getTransactionIndex(t0)});
		}
		t0.setAccount(act);
		return true;
	}

	private int getTransactionIndex(Transaction t){
		if(t.isCredit()){
			return this.creditslist.indexOf(t);
		} else {
			return this.debitslist.indexOf(t);
		}
	}

    /**
     *
     * @param args
     */
//    public static void main(String[] args) {
////		Transaction[] trans = new Transaction[1000000];
////		for(int i = 0; i < trans.length; i++){
////			int m =0;
////			if(i > 11){
////				m = i % 10;
////			} else{
////				m = i;
////			}
////			Transact t = new Transact(new GregorianCalendar(2014,m, 13), "This is transact " + i,
////					30.0 * i, "Category is 1"+ i + " women and children.",
////					"Mastercard", false);
////			trans[i] = t;
////		}
////		TList t = new TList(trans);
////		
////		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
////		GregorianCalendar g2 = new GregorianCalendar(2015,Calendar.NOVEMBER,13);
////		
////		double orig = 0;
////        double prop =0;
////        double orig2 = 0;
////        double prop2 = 0;
////        int runs = 5;
////        int iterations = 100000;
////		
////		System.out.println("Working on orig");
////        for(int i = 0; i < runs; i++){
////        	double results = 0;
////        	for(int j = 0; j < iterations; j++){
////        		long start = System.nanoTime();
////        		t.getTransactionsByDate(g.getTime());
////        		long end = System.nanoTime();
////        		results = (double)(end start);///1000000000L;
////        	}
////        	orig += results/iterations;
////        }
////        orig = orig/runs;
////        
////        System.out.println("Original: " + orig);
////        System.out.println("Proposed: " + prop);
////        System.out.println("Original Between: " + orig2);
////        System.out.println("Proposed Between: " + prop2);
//	}
}
