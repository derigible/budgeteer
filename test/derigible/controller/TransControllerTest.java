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

package derigible.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transactions.abstracts.Transaction;
import derigible.transformations.MockTransform;
import derigible.transformations.abstracts.TransformToTransactions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author marphill
 *
 */
public class TransControllerTest {
	TransformToTransactions x2t;
	TList t;
	static Transaction[] trans;
	static Transaction[] trans2;
	static ArrayList<Transact> talist = new ArrayList<Transact>();
	static LinkedList<Transact> tllist = new LinkedList<Transact>();
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
	
	@Before
	public void setup(){
		x2t = Mockito.mock(MockTransform.class);
		t = new TList(trans2);
		try {
			when(x2t.data_to_transactions()).thenReturn(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void teardown(){
		for(Transaction t0 : t.getExcluded()){
			t0.setExcluded(false);
		}
		for(Transaction t0 : t.getExcluded()){
			t0.setExcluded(false);
		}
		x2t = null;
		t = null;
	}


	@Test
	public void testControllerInitialization() throws IOException{
		
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		assertSame("Wrong TList object returned - not initialized correcty.", t, tc.getTransactions());
	}
	
	@Test
	public void testGetCurrentBalance() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		assertEquals("Wrong amount of money returned.", 9.93, tc.getCurrentBalance(), .001);
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", false);
		tc.getTransactions().addTransaction(transact2);
		
		assertEquals("Wrong amount of money returned after 4.50 credit added.", 5.43, 
				tc.getCurrentBalance(), .001);
	}
	
	@Test
	public void testExcludeBetweenDates() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		tc.excludeBetweenDates(start.getTime(), end.getTime());
		
		assertEquals("Wrong number of transaction returned.", 4, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeBetweenDates() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		tc.excludeBetweenDates(start.getTime(), end.getTime());
		
		assertEquals("Wrong number of transaction returned for exclude.", 4, 
				tc.getTransactions().getTransactions().size());
		
		tc.includeBetweenDates(start.getTime(), end.getTime());
		
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testExcludeByDate() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		tc.excludeDate(start.getTime());
		
		assertEquals("Wrong number of transaction returned for exclude.", 5, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeByDate() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		tc.excludeDate(start.getTime());
		
		assertEquals("Wrong number of transaction returned for exclude.", 5, 
				tc.getTransactions().getTransactions().size());
		
		tc.includeDate(start.getTime());
		
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testExcludeCategory() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeCategory("Dessert");
		assertEquals("Wrong number of transaction returned for exclude.", 4, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeCategory() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeCategory("Dessert");
		assertEquals("Wrong number of transaction returned for exclude.", 4, 
				tc.getTransactions().getTransactions().size());
		tc.includeCategory("Dessert");
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testExcludeCategories() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeCategories(new String[] {"Dessert", "Groceries"});
		assertEquals("Wrong number of transaction returned for exclude.", 3, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeCategories() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeCategories(new String[] {"Dessert", "Groceries"});
		assertEquals("Wrong number of transaction returned for exclude.", 3, 
				tc.getTransactions().getTransactions().size());
		tc.includeCategories(new String[] {"Dessert", "Groceries"});
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testExcludeAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeAccount("Mastercard");
		assertEquals("Wrong number of transaction returned for exclude.", 3, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeAccount("Mastercard");
		assertEquals("Wrong number of transaction returned for exclude.", 3, 
				tc.getTransactions().getTransactions().size());
		tc.includeAccount("Mastercard");
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testExcludeAccounts() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeAccounts(new String[] {"Mastercard", "Discover"});
		assertEquals("Wrong number of transaction returned for exclude.", 2, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testIncludeAccounts() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		tc.excludeAccounts(new String[] {"Mastercard", "Discover"});
		assertEquals("Wrong number of transaction returned for exclude.", 2, 
				tc.getTransactions().getTransactions().size());
		tc.includeAccounts(new String[] {"Mastercard", "Discover"});
		assertEquals("Wrong number of transaction returned for include.", 6, 
				tc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testCurrentBalanceForAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		assertEquals("Wrong balance for Mastercard account.", -85.57, 
				tc.getCurrentBalanceForAccount("Mastercard"), .001);
	}

	@Test
	public void testCurrentBalanceForAccount_withDebitsAndCredits() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", true);
		tc.getTransactions().addTransaction(transact2);
		assertEquals("Wrong balance for Mastercard account.", -81.07, 
				tc.getCurrentBalanceForAccount("Mastercard"), .001);
	}
	
	@Test
	public void testCurrentBalanceBetweenDates() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		GregorianCalendar end2 = new GregorianCalendar(2015,Calendar.NOVEMBER,15);
		assertEquals("Wrong balance for all accounts between dates.", -61.07, 
				tc.getBalanceBetweenDates(start.getTime(), end.getTime()), .001);
		assertEquals("Wrong balance for all transactions.", 9.93, 
				tc.getBalanceBetweenDates(start.getTime(), end2.getTime()), .001);
	}
	
	@Test
	public void testCurrentBalBetweenDatesForAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		GregorianCalendar end2 = new GregorianCalendar(2015,Calendar.NOVEMBER,15);
		assertEquals("Wrong balance for Mastercard account between dates.", -56.57, 
				tc.getBalanceBetweenDatesForAccount(start.getTime(), end.getTime(), "Mastercard"), .001);
		assertEquals("Wrong balance for all Mastercard transactions.", -85.57, 
				tc.getBalanceBetweenDatesForAccount(start.getTime(), end2.getTime(), "Mastercard"), .001);
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", true);
		tc.getTransactions().addTransaction(transact2);
		assertEquals("Wrong balance for all Mastercard transactions with a credit.", -81.07, 
				tc.getBalanceBetweenDatesForAccount(start.getTime(), end2.getTime(), "Mastercard"), .001);
	}
	
	@Test
	public void testCurrentSpendingBetweenDatesForAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		GregorianCalendar end2 = new GregorianCalendar(2015,Calendar.NOVEMBER,15);
		assertEquals("Wrong balance for Mastercard account between dates.", 56.57, 
				tc.getSpendingBetweenDatesForAccount(start.getTime(), end.getTime(), "Mastercard"), .001);
		assertEquals("Wrong balance for all Mastercard transactions.", 0, 
				tc.getSpendingBetweenDatesForAccount(start.getTime(), end2.getTime(), "Check #11456"), .001);
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", true);
		tc.getTransactions().addTransaction(transact2);
		assertEquals("Wrong balance for all Mastercard transactions with a credit.", 0, 
				tc.getSpendingBetweenDatesForAccount(start.getTime(), end2.getTime(), "Check #11456"), .001);
	}
	
	@Test
	public void testCurrentIncomeBetweenDatesForAccount() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		GregorianCalendar end2 = new GregorianCalendar(2015,Calendar.NOVEMBER,15);
		assertEquals("Wrong balance for Mastercard account between dates.", 0, 
				tc.getIncomeBetweenDatesForAccount(start.getTime(), end.getTime(), "Check #11456"), .001);
		assertEquals("Wrong balance for all Mastercard transactions.", 0, 
				tc.getIncomeBetweenDatesForAccount(start.getTime(), end2.getTime(), "Mastercard"), .001);
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Ice Cream",
				4.50, "Dessert","Mastercard", true);
		tc.getTransactions().addTransaction(transact2);
		assertEquals("Wrong balance for all Mastercard transactions with a credit.", 114.50, 
				tc.getIncomeBetweenDatesForAccount(start.getTime(), end2.getTime(), "Check #11456"), .001);
	}
	
	@Test
	public void testGetPossibleDuplicates() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		
		assertEquals("Wrong number of duplicates returned.", 0, 
				tc.getPossibleDuplicates().size());
		
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		tc.getTransactions().addTransaction(transact2);
		
		assertEquals("Wrong number of duplicates returned.", 1, 
				tc.getPossibleDuplicates().size());
		
		//Wrong amount
		Transact transact3 = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,16), "Macey's Grocery Store",
				24.00, "Groceries", "Mastercard", false);
		tc.getTransactions().addTransaction(transact3);
		
		assertEquals("Wrong number of duplicates returned.", 1, 
				tc.getPossibleDuplicates().size());
		
		//Missing the apostrophe in description
		Transact transact4 = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,16), "Maceys Grocery Store",
				24.00, "Groceries", "Mastercard", false);
		tc.getTransactions().addTransaction(transact4);
		
		assertEquals("Wrong number of duplicates returned.", 1, 
				tc.getPossibleDuplicates().size());
		
		//Should duplicate at transaction4
		Transact transact5 = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,16), "Maceys Grocery Store",
				24.00, "Groceries", "Mastercard", false);
		tc.getTransactions().addTransaction(transact5);
		
		assertEquals("Wrong number of duplicates returned.", 2, 
				tc.getPossibleDuplicates().size());
	}
	
	@Test
	public void testDuplicatesFoundAreCorrect() throws IOException{
		TransactionsController tc = new TransactionsController(x2t.data_to_transactions());
		Transact transact2 = new Transact(new GregorianCalendar(2015,Calendar.JANUARY,15), "Paycheck #1123",
				114.50, "Payroll", "Check #11456", true);
		tc.getTransactions().addTransaction(transact2);
		
		assertEquals("Wrong amount returned.", 2, 
				tc.getPossibleDuplicates().get(0).length);
		Transaction[] l = tc.getPossibleDuplicates().get(0);
		assertTrue("Wrong transactions in return.", l[0] == trans2[5] ? true : l[1] == trans2[5]);
	}
	
	//TODO Create a transcontroller from a folder of data
}
