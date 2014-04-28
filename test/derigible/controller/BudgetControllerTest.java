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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transformations.MockTransform;
import derigible.transformations.TransformToTransactions;

/**
 * @author marcphillips
 *
 */
public class BudgetControllerTest {
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
	static TransactionsController tc;
	
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
			tc = new TransactionsController(x2t.data_to_transactions());
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	public void testBudgetInitialization(){
		BudgetController b = tc.createBudget("test");
		b.includeAccount("Mastercard");
		assertEquals("Wrong number of trans in budget.", 3, b.getTransactions().getTransactions().size());
		assertEquals("Wrong name of budget.", "test", b.getName());
		
		
	}
	
	@Test
	public void testExcludeDate(){
		BudgetController b = tc.createBudget("test");
		b.includeAccount("Mastercard");
		b.excludeDate(new GregorianCalendar(2014,Calendar.NOVEMBER,15).getTime());
		assertEquals("Wrong number of trans in budget after removal of dates.", 2, 
				b.getTransactions().getTransactions().size());
	}
}
