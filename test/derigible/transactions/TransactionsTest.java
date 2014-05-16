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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import derigible.controller.GUID;
//import static org.mockito.Mockito.*;
import derigible.transactions.TList;
import derigible.transactions.Transact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
/**
 * Note that filters are tested in the getBy* methods.
 * @author marphill
 *
 */
public class TransactionsTest { 
	static Transaction[] trans;
	static Transaction[] trans2;
	static ArrayList<Transaction> talist = new ArrayList<Transaction>();
	static LinkedList<Transaction> tllist = new LinkedList<Transaction>();
	static Transaction trn = new Transact(new GregorianCalendar(2012,Calendar.NOVEMBER,23), "Ice Cream",
			4.50, "SomethingNew", "Mastercard", false);
	static Transaction trn2 = new Transact(new GregorianCalendar(2012,Calendar.NOVEMBER,24), "Ice Cream",
			4.50, "SomethingNew", "Mastercard", false);
	
	@BeforeClass
	public static void setupData(){
		trans = new Transaction[100];
		for(int i = 0; i < trans.length; i++){
			int m =0;
			if(i > 11){
				m = i % 10;
			} else{
				m = i;
			}
			Transact t = new Transact(new GregorianCalendar(2014,m, 13), "This is transact " + i,
					30.0 * i, "Category is 1"+ i + " women and children.",
					"Mastercard", false);
			t.setGUID(GUID.generate());
			trans[i] = t;
			talist.add(t);
			tllist.add(t);
		}
		trans2 = new Transaction[6];
		trans2[0] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,13), "Ice Cream",
				4.50, "Dessert", "Discover", false);
		trans2[1] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,14), "Home Improvement",
				56.57, "Home Improvement", "Mastercard", false);
		trans2[2] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "New Hammer",
				14.50, "hOme Improvement", "FREE CHECKING", false);
		trans2[3] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,16), "Macey's Grocery Store",
				24.50, "Groceries", "Mastercard", false);
		trans2[4] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert", "Mastercard", false);
		trans2[5] = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		for(Transaction t : trans2){
			t.setGUID(GUID.generate());
		}
	}
	
	@AfterClass
	public static void teardownData(){
		trans = null;
		trans2 = null;
	}

	@Test
	public void testTransactionsCreationAndRetrieval() {
		TList t = new TList(trans);
		TList tal = new TList(talist);
		TList tll = new TList(tllist);
		
		assertEquals("TList by array not equal to TList by ArrayList",
				t.getTransactions().size(), tal.getTransactions().size());
		assertEquals("TList by ArrayList not equal to TList by LinkedList - LinkedList Not ArrayList.",
				tal.getTransactions().getClass(), tll.getTransactions().getClass());
	}
	
	@Test
	public void testCategoryIndexing(){
		TList t = new TList(trans);
		
		assertEquals("Wrong number of categories in index", 100,
				t.getCategories().length);
	}
	
	@Test
	public void testAccurateIndexingForGetTransactionsByCategory(){
		TList t = new TList(trans);
		TList t2 = new TList(trans2);
		
		assertSame("Wrong category transaction fetched.", trans[0],
				t.getByCategory("Category is 10 women and children.").get(0));
		
		assertEquals("Too many/few categories returned.", 2,
				t2.getByCategory("Dessert").size());
	}
	
	@Test
	public void testAccurateIndexingForGetTransactionsByAccount(){
		TList t2 = new TList(trans2);
		
		assertEquals("Too many/few categories returned.", 3,
				t2.getByAccount("Mastercard").size());
	}
	
	@Test
	public void testAccurateDateIndexingForGetTransactionsByDate(){
		TList t = new TList(trans2);
		
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		assertSame("Wrong date transaction fetched.", trans2[0],
				t.getByDate(g.getTime()).get(0));
		
		g = new GregorianCalendar(2014,Calendar.NOVEMBER,15);
		
		assertNotSame("Retrieved date was the same transaction.",trans2[0],
				t.getByDate(g.getTime()).get(0));
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getByDate(g.getTime()).size());
	}
	
	@Test
	public void testAddTransactionEvents(){
		TList t = new TList(trans2);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,23), "Ice Cream",
				4.50, "SomethingNew", "Mastercard", false);
		int before = t.getTransactions().size();
		
		t.addTransaction(transact);
		
		int after = t.getTransactions().size();
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,23);
		
		assertFalse("Transaction not added to list.", before == after);
		assertEquals("Transaction list not changed by correct amount.", 7, after);
		assertSame("Category Indexing not working correctly with add event.", transact,
				t.getByCategory("SomethingNew").get(0));
		assertSame("Date Indexing not working correctly with add event.", transact,
				t.getByDate(g.getTime()).get(0));
	}
	
	@Test
	public void testTransactionsBetweenDates_SameDateCase(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		assertSame("Wrong date transaction fetched.", trans2[0],
				t.getBetweenDates(start.getTime(), end.getTime()).get(0));
	}
	
	@Test
	public void testTransactionsBetweenDates_DifferentDaysSameMonth(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test
	public void testTransactionsBetweenDates_SameYearAndDifferentMonths(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.DECEMBER,14);
		
		Transact transact = new Transact(new GregorianCalendar(2016,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert", "Mastercard", false);
		
		t.addTransaction(transact);

		assertEquals("Wrong number of transactions fetched.", 5,
				t.getBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test
	public void testTransactionsBetweenDates_DifferentYears(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2013,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact);
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testTransactionsBetweenDates_DifferentDatesUnAcceptableCase(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		t.getBetweenDates(start.getTime(), end.getTime()); //Throws exception
	}
	
	@Test
	public void testTransactionsBetweenDates_DifferentDatesAcceptableCaseReturnsNone(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,20);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,22);
		
		assertEquals("Wrong number of transactions fetched.", 0,
				t.getBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test
	public void testGetYearsWithTransactions(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of years passed back.", 2, t.getYearsWithTransactions().length);
		assertTrue("Wrong year passed back.", 2014 == t.getYearsWithTransactions()[0]);
		
		Transact transact = new Transact(new GregorianCalendar(2013,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact);
		
		assertEquals("Wrong number of years pass back after adding transaction.", 3, 
				t.getYearsWithTransactions().length);
		
	}
	
	@Test
	public void testGetMonthsInYearWithTransactions(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of months passed back.", 1, 
				t.getMonthsInYearWithTransactions(2014).length);
		assertTrue("Wrong month passed back.", 10 == t.getMonthsInYearWithTransactions(2014)[0]);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact);
		
		assertEquals("Wrong number of months passed back for 2014.", 2, 
				t.getMonthsInYearWithTransactions(2014).length);
		assertEquals("Wrong number of months passed back for 2015.", 1, 
				t.getMonthsInYearWithTransactions(2015).length);
	}
	
	@Test
	public void testGetDaysInMonthInYearWithTransactions(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of days passed back.", 4, 
				t.getDaysInMonthInYearWithTransactions(2014, 10).length);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact);
		
		assertEquals("Wrong day passed back.", 15 ,
				t.getDaysInMonthInYearWithTransactions(2014, 0)[0]);
	}
	
	@Test
	public void testGetAllDaysInYearWithTransactions(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of days passed back.", 4, 
				t.getAllDaysInYearWithTransactions(2014).length);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact);
		
		assertEquals("Wrong number of days passed back after add.", 5, 
				t.getAllDaysInYearWithTransactions(2014).length);
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of days passed back after second add for 2014.", 5, 
				t.getAllDaysInYearWithTransactions(2014).length);
		assertEquals("Wrong number of days passed back after add for 2015.", 1, 
				t.getAllDaysInYearWithTransactions(2015).length);
		assertEquals("Wrong day passed back.", 15, 
				t.getAllDaysInYearWithTransactions(2015)[0]);
	}
	
	@Test
	public void testGetTransactionsByCategories(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of Transactions returned", 4,
				t.getByCategories(new String[] {"Dessert", "Home Improvement"}).size());
		
		assertEquals("Wrong number of Transactions returned", 0,
				t.getByCategories(new String[] {"Ice Cream", "Grocery"}).size());
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Transactions returned after adding Transaction.", 5,
				t.getByCategories(new String[] {"Dessert", "Home Improvement"}).size());
		assertEquals("Wrong number of Transactions returned after adding Transaction.", 6,
				t.getByCategories(
						new String[] {"Dessert", "Home Improvement", "Groceries", "False"}).size());
	}
	
	@Test
	public void testGetTransactionsByCategoryAndDates(){
		TList t = new TList(trans2);
		
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		assertEquals("Wrong number of Transactions returned", 1,
				t.getByCategoryAndDates("Dessert", 
						start.getTime(), end.getTime()).size());
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Transactions returned after Transaction added.", 1,
				t.getByCategoryAndDates("Dessert", 
						start.getTime(), end.getTime()).size());
		
		end = new GregorianCalendar(2015,Calendar.NOVEMBER,16);
		
		assertEquals("Wrong number of Transactions returned after Transaction added and date changed.", 3,
				t.getByCategoryAndDates("Dessert", 
						start.getTime(), end.getTime()).size());
	}
	
	@Test
	public void testGetTransactionsByCategoriesAndDates(){
		TList t = new TList(trans2);
		
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		assertEquals("Wrong number of Transactions returned", 2,
				t.getByCategoriesAndDates(new String[] {"Dessert", "Home Improvement"}, 
						start.getTime(), end.getTime()).size());
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Transactions returned after Transaction added.", 2,
				t.getByCategoriesAndDates(new String[] {"Dessert", "Home Improvement"}, 
						start.getTime(), end.getTime()).size());
		
		end = new GregorianCalendar(2015,Calendar.NOVEMBER,16);
		
		assertEquals("Wrong number of Transactions returned after Transaction added and date changed.", 5,
				t.getByCategoriesAndDates(new String[] {"Dessert", "Home Improvement"}, 
						start.getTime(), end.getTime()).size());
	}
	
	@Test
	public void testIncomeFiltering(){
		TList t = new TList(trans2);
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		
		assertEquals("Wrong number of Income Transactions returned", 1, t.getCredits().size());
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Transactions returned", 5, t.getDebits().size());
		assertEquals("Wrong number of Income Transactions returned after add.", 2, 
				t.getCredits().size());
	}
	
	@Test
	public void testGetIncomeByDate(){
		TList t = new TList(trans2);
		Transact transact2 = new Transact(new GregorianCalendar(2014,Calendar.OCTOBER,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		GregorianCalendar first = new GregorianCalendar(2014,Calendar.OCTOBER,15);
		GregorianCalendar second = new GregorianCalendar(2015,Calendar.JANUARY,15);
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Income Transactions returned", 1, 
				t.getIncomeByDate(first.getTime()).size());
		
		
		assertEquals("Wrong number of Income Transactions returned after add of second date.", 1, 
				t.getIncomeByDate(second.getTime()).size());
	}
	
	@Test
	public void testGetIncomeBetweenDates(){
		TList t = new TList(trans2);
		Transact transact2 = new Transact(new GregorianCalendar(2014,Calendar.OCTOBER,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar start2 = new GregorianCalendar(2014,Calendar.OCTOBER,15);
		GregorianCalendar end = new GregorianCalendar(2015,Calendar.JANUARY,15);
		GregorianCalendar start3 = new GregorianCalendar(2015,Calendar.JANUARY,12);
		GregorianCalendar end2 = new GregorianCalendar(2015,Calendar.JANUARY,15);
		
		assertEquals("Wrong number of Income Transactions returned.", 1, 
				t.getIncomeBetweenDates(start.getTime(),end.getTime()).size());
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Income Transactions returned after add.", 2, 
				t.getIncomeBetweenDates(start2.getTime(),end.getTime()).size());
		assertEquals("Wrong number of Income Transactions returned after add for end date test.", 1, 
				t.getIncomeBetweenDates(start3.getTime(),end2.getTime()).size());
	}
	
	@Test
	public void testGetIncomeBetweenDates_AcceptedNoReturn(){
		TList t = new TList(trans2);
		Transact transact2 = new Transact(new GregorianCalendar(2014,Calendar.OCTOBER,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		GregorianCalendar start = new GregorianCalendar(2015,Calendar.NOVEMBER,12);
		GregorianCalendar start2 = new GregorianCalendar(2015,Calendar.OCTOBER,15);
		GregorianCalendar end = new GregorianCalendar(2016,Calendar.JANUARY,15);
		
		assertEquals("Wrong number of Income Transactions returned.", 0, 
				t.getIncomeBetweenDates(start.getTime(),end.getTime()).size());
		
		t.addTransaction(transact2);
		
		assertEquals("Wrong number of Income Transactions returned after add.", 0, 
				t.getIncomeBetweenDates(start2.getTime(),end.getTime()).size());
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetIncomeBetweenDates_ThrowsError(){
		TList t = new TList(trans2);
		GregorianCalendar start2 = new GregorianCalendar(2014,Calendar.OCTOBER,15);
		GregorianCalendar end = new GregorianCalendar(2015,Calendar.JANUARY,15);
		t.getIncomeBetweenDates(end.getTime(), start2.getTime());
	}
	
	@Test
	public void testAddTransactions(){
		TList t = new TList(trans2);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,23), "Ice Cream",
				4.50, "SomethingNew", "Mastercard2", false);
		Transact transact2 = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,24), "Ice Cream",
				4.50, "SomethingNew2", "Mastercard", false);
		int before = t.getTransactions().size();
		
		t.addTransactions(new Transaction[] {transact, transact2});
		
		int after = t.getTransactions().size();
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,23);
		
		assertFalse("Transaction not added to list.", before == after);
		assertEquals("Transaction list not changed by correct amount.", 8, after);
		assertSame("Category Indexing not working correctly with add event.", transact,
				t.getByCategory("SomethingNew").get(0));
		assertSame("Date Indexing not working correctly with add event.", transact,
				t.getByDate(g.getTime()).get(0));
		assertSame("Account Indexing not working correctly with add event.", transact,
				t.getByAccount("Mastercard2").get(0));
	}
	
	@Test
	public void testAddTransactionsInAttemptToBreakIndexing(){
		TList t = new TList(trans2);
		t.addTransactions(trans);
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,23), "Ice Cream",
				4.50, "SomethingNew", "Mastercard2", false);
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,23);
		
		t.addTransaction(transact);
		
		assertEquals("Category Indexing not working correctly with add event.", 1,
				t.getByCategory("SomethingNew").size());
		assertEquals("Account Indexing not working correctly with add event.", 1,
				t.getByAccount("Mastercard2").size());
		assertSame("Date Indexing not working correctly with add event.", 1,
				t.getByDate(g.getTime()).size());
		assertTrue("Transactions list too small", t.getTransactions().size() > 100);
	}
	
	private TList setupExclusionTest(){
		TList t = new TList(trans2);
		t.addTransaction(trn);
		t.addTransaction(trn2);
		return t;
	}
	
	private void cleanupExclusionTest(){
		trn.setExcluded(false);
		trn2.setExcluded(false);
	}
	
	@Test
	public void testExclusionStillReturnsObjectInTList(){
		TList t = setupExclusionTest();
		Transaction before1 = t.getLastTransaction();
		t.excludeTransaction(trn2);
		
		Transaction after1 = t.getExcluded().get(0);
		assertSame("Excluded transaction not the same as transaction in TList.", before1, after1);
        cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getLastTransaction(){
		TList t = setupExclusionTest();
		Transaction before1 = t.getLastTransaction();
		t.excludeTransaction(trn2);
		//Should be different transaction
		Transaction after1 = t.getLastTransaction();
		assertNotSame("Same Transaction returned as before, should be excluded for getLastTransaction",
				before1, after1);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getMonthsInYear(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		int[] before2 = t.getMonthsInYearWithTransactions(g.get(Calendar.YEAR));
		t.excludeTransaction(trn);
		//Should return the same
		int[] after2 = t.getMonthsInYearWithTransactions(g.get(Calendar.YEAR));
		assertEquals("Wrong number of transactions returned for getMonthsInYearWithT.",
				before2.length, after2.length);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getAllDaysInYear(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		int[] before3 = t.getAllDaysInYearWithTransactions(g.get(Calendar.YEAR));
		t.excludeTransaction(trn);
		//Should return the same
		int[] after3 = t.getAllDaysInYearWithTransactions(g.get(Calendar.YEAR));
		assertEquals("Wrong number of transactions returned for getAllDaysInYearWithT.",
				before3.length, after3.length);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getDaysInMonthInYear(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		int[] before4 = t.getDaysInMonthInYearWithTransactions(g.get(Calendar.YEAR), g.get(Calendar.MONTH));
		t.excludeTransaction(trn);
		//Should return the same
		int[] after4 = t.getDaysInMonthInYearWithTransactions(g.get(Calendar.YEAR), g.get(Calendar.MONTH));
		assertEquals("Wrong number of transactions returned for getDaysInMonthsInYearWithT.",
				before4.length, after4.length);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionAt(){
		TList t = setupExclusionTest();
		Transaction before5 = t.getTransactionAt(5);
		Transaction before5_1 = t.getTransactionAt(6); //Should stay the same	
		t.excludeTransaction(trn);
		//Should return same
		Transaction after5 = t.getTransactionAt(5);
		//Should stay the same
		Transaction after5_1 = t.getTransactionAt(6); 
		assertSame("Same Transaction not returned as before, should be included for getTransactionAt",
				before5, after5);
		assertSame("Same Transaction not returned as before, shoudld be included for getTransactionAt",
				before5_1, after5_1);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_geTransactions(){
		TList t = setupExclusionTest();
		List<Transaction> before7 = t.getTransactions();
		t.excludeTransaction(trn);
		//Should return 1 less
		List<Transaction> after7 = t.getTransactions();
		assertEquals("Wrong number of transactions returned for getTransactions.",
				before7.size() - 1, after7.size());
		t.excludeTransaction(trn2);
		assertEquals("Wrong number of transactions returned for getTransactions.",
				before7.size() - 2, t.getTransactions().size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getYearsWithTransaction(){
		TList t = setupExclusionTest();
		int[] before8 = t.getYearsWithTransactions();
		t.excludeTransaction(trn);
		//Should return the same
		int[] after8 = t.getYearsWithTransactions();
		
		t.excludeTransaction(trn2);
		//Should return the same
		int[] after16 = t.getYearsWithTransactions();
		assertEquals("Wrong number of transactions returned for getYearsWithTransactions.",
				before8.length, after8.length);
		assertEquals("Wrong number of transactions returned for getYearsWithTransactions after both removed.",
				before8.length, after16.length);
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsBetweenDates(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		GregorianCalendar g2 = new GregorianCalendar(2012,Calendar.DECEMBER,24);
		t.excludeTransaction(trn);
		//Should return 1
		List<Transaction> after10 = t.getBetweenDates(g.getTime(), g2.getTime());
		assertEquals("Wrong number of transactions returned for getTransactionsBetweenDates.",
				1, after10.size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsByDate(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		t.excludeTransaction(trn);
		//Should return 0
		List<Transaction> after9 = t.getByDate(g.getTime());
		assertEquals("Wrong number of transactions returned for getTransactionsByDate.", 
				0, after9.size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsByCatsandDate(){
		//for times sake, this will also be the test for getTransactionsByCatsandDate
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		
		List<Transaction> before = t.getByCategoriesAndDate(
				new String[] {"SomethingNew", "Groceries"} , g.getTime());
		
		assertEquals("Wrong number of transactions returned for getTransactionsByCatAndDates.",
				1, before.size());
		
		t.excludeTransaction(trn);
		
		List<Transaction> after14 = t.getByCategoriesAndDate(
				new String[] {"SomethingNew", "Groceries"} , g.getTime());
		
		assertEquals("Wrong number of transactions returned for getTransactionsByCatAndDates.",
				0, after14.size());
		
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsByCatandDates(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		GregorianCalendar g2 = new GregorianCalendar(2012,Calendar.DECEMBER,24);
		t.excludeTransaction(trn);
		//Should return 1
		List<Transaction> after14 = t.getByCategoryAndDates("SomethingNew", g.getTime(), g2.getTime());
		assertEquals("Wrong number of transactions returned for getTransactionsByCatAndDates.",
				1, after14.size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsByCatsandDates(){
		TList t = setupExclusionTest();
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		GregorianCalendar g2 = new GregorianCalendar(2012,Calendar.DECEMBER,24);
		t.excludeTransaction(trn);
		//Should return 1
		List<Transaction> after15 = t.getByCategoriesAndDates(new String[] {"SomethingNew", "Groceries"}, g.getTime(), g2.getTime());
		assertEquals("Wrong number of transactions returned for getTransactionsByCategoriesAndDates.", 
				1, after15.size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForExclusionFromReturns_getTransactionsByCategory(){
		TList t = setupExclusionTest();
		t.excludeTransaction(trn);
		//Should return 1
		List<Transaction> after11 = t.getByCategory("SomethingNew");
		assertEquals("Wrong number of transactions returned for getTransactionsByCategory.",
				1, after11.size());
		cleanupExclusionTest();
	}
	
	@Test
	public void testRemoveTransactionForIncomeTransactionsExclusion(){
		TList t = new TList(trans2);
		
		Transact transact = new Transact(new GregorianCalendar(2012,Calendar.NOVEMBER,23), "Ice Cream",
				4.50, "SomethingNew", "Mastercard", true);
		
		Transact transact2 = new Transact(new GregorianCalendar(2012,Calendar.NOVEMBER,24), "Ice Cream",
				4.50, "SomethingNew", "Mastercard", true);
		
		t.addTransaction(transact);
		t.addTransaction(transact2);
		
		GregorianCalendar g = new GregorianCalendar(2012,Calendar.NOVEMBER,23);
		GregorianCalendar g2 = new GregorianCalendar(2012,Calendar.DECEMBER,24);
		
		t.excludeTransaction(transact);
		
		//Should be 1
		List<Transaction> before1 = t.getIncomeBetweenDates(g.getTime(), g2.getTime());
		//Should be 0
		List<Transaction> before2 = t.getIncomeByDate(g.getTime());
		//Should be 2
		List<Transaction> before3 = t.getCredits();
		t.excludeTransaction(transact2);
		//Should be 0
		List<Transaction> before4 = t.getCredits();
		
		assertEquals("Wrong number of transactions returned for getIncomeBetweenDates.", 
				1, before1.size());
		assertEquals("Wrong number of transactions returned for getIncomeByDate.", 
				0, before2.size());
		assertEquals("Wrong number of transactions returned for getIncomeTransactions.", 
				2, before3.size());
		assertEquals("Wrong number of transactions returned for getIncomeTransaction after second removal.", 
				1, before4.size());
	}
	
	@Test
	public void testGetCategories(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of categories returned.", 
				4, t.getCategories().length);
	}
	
	@Test
	public void testGetAccounts(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of accounts returned.", 
				4, t.getAccounts().length);
	}
	
	@Test
	public void testGetTransactionsByAccount(){
		TList t = new TList(trans2);
		
		assertEquals("Wrong number of transactions returned for account Mastercard.", 3,
				t.getByAccount("Mastercard").size());
		assertEquals("Wrong number of transactions returned for account Check #11456", 1,
				t.getByAccount("Check #11456").size());
	}
	
	@Test
	public void testGetTransactionByGUID(){
		TList t = new TList(trans2);
		
		assertSame("Wrong transaction found.", t.getTransactionByGUID(trans2[0].getGUID()),
				trans2[0]);
	}
	
	@Test
	public void testReindexDate(){
		TList t = new TList(trans2);
		Transaction t0 = t.getLastTransaction();
		Transact t1 = new Transact();
		t1.setAccount(t0.getAccount());
		t1.setCategory(t0.getCategory());
		t1.setDate(new GregorianCalendar(2018,0,15));
		t.updateTransaction(t0.getGUID(),t1);
		assertEquals("Wrong number of transactions returned", 0, t.getByDate(new GregorianCalendar(2015,Calendar.JANUARY,15).getTime()).size());
		assertEquals("Wrong number of transactions returned", 1, t.getByDate(new GregorianCalendar(2018,0,15).getTime()).size());
	}
	
	@Test
	public void testReindexCategory(){
		TList t = new TList(trans2);
		Transaction t0 = t.getLastTransaction();
		Transact t1 = new Transact();
		t1.setAccount(t0.getAccount());
		t1.setDate(t0.getDate());
		t1.setCategory("New");
		t.updateTransaction(t0.getGUID(),t1);
		assertEquals("Wrong number of transactions returned", 0, t.getByCategory("Paycheck #1123").size());
		assertEquals("Wrong number of transactions returned", 1, t.getByCategory("New").size());
	}
	
	@Test
	public void testReindexAccount(){
		TList t = new TList(trans2);
		Transaction t0 = t.getLastTransaction();
		Transact t1 = new Transact();
		t1.setAccount("New");
		t1.setDate(t0.getDate());
		t1.setCategory(t0.getCategory());
		t.updateTransaction(t0.getGUID(),t1);
		assertEquals("Wrong number of transactions returned", 0, t.getByAccount("Check #11456").size());
		assertEquals("Wrong number of transactions returned", 1, t.getByAccount("New").size());
		t1.setAccount("Check #11456");
		t.updateTransaction(t0.getGUID(),t1);
		assertEquals("Wrong number of transactions returned", 1, t.getByAccount("Check #11456").size());
	}
}