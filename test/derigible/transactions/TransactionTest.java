package derigible.transactions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
//import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
import derigible.transactions.TList;
import derigible.transactions.Transact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class TransactionTest { 
	static Transaction[] trans;
	static Transaction[] trans2;
	static ArrayList<Transact> talist = new ArrayList<Transact>();
	static LinkedList<Transact> tllist = new LinkedList<Transact>();
	
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
			trans[i] = t;
			talist.add(t);
			tllist.add(t);
		}
		trans2 = new Transaction[5];
		trans2[0] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,13), "Ice Cream",
				4.50, "Dessert",
				"Mastercard", false);
		trans2[1] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,14), "Home Improvement",
				56.57, "Home Improvement",
				"Mastercard", false);
		trans2[2] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "New Hammer",
				14.50, "hOme Improvement",
				"Mastercard", false);
		trans2[3] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,16), "Macey's Grocery Store",
				24.50, "Groceries",
				"Mastercard", false);
		trans2[4] = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert",
				"Mastercard", false);
	}
	
	@AfterClass
	public static void teardownData(){
		trans = null;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTransactionsCreationAndRetrieval() {
		TList t = new TList(trans);
		TList tal = new TList((List) talist);
		TList tll = new TList((List) tllist);
		
		assertEquals("TList by array not equal to TList by ArrayList",
				t.getTransactions().size(), tal.getTransactions().size());
		assertEquals("TList by ArrayList not equal to TList by LinkedList - LinkedList Not ArrayList.",
				tal.getTransactions().getClass(), tll.getTransactions().getClass());
	}
	
	@Test
	@Ignore
	public void testCategoryIndexing(){
		TList t = new TList(trans);
		
		assertEquals("Wrong number of categories in index", 100,
				t.getCategories().length);
	}
	
	@Test
	@Ignore
	public void testAccurateIndexingForGetTransactionsByCategory(){
		TList t = new TList(trans);
		TList t2 = new TList(trans2);
		
		assertSame("Wrong category transaction fetched.", trans[0],
				t.getTransactionsByCategory("Category is 10 women and children.").get(0));
		
		assertEquals("Too many/few categories returned.", 2,
				t2.getTransactionsByCategory("Dessert").size());
	}
	
	@Test
	@Ignore
	public void testAccurateDateIndexingForGetTransactionsByDate(){
		TList t = new TList(trans2);
		
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		assertSame("Wrong date transaction fetched.", trans2[0],
				t.getTransactionsByDate(g.getTime()).get(0));
		
		g = new GregorianCalendar(2014,Calendar.NOVEMBER,15);
		
		assertNotSame("Retrieved date was the same transaction.",trans2[0],
				t.getTransactionsByDate(g.getTime()).get(0));
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getTransactionsByDate(g.getTime()).size());
	}
	
	@Test
	public void testAddTransactionEvents(){
		TList t = new TList(trans2);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,23), "Ice Cream",
				4.50, "SomethingNew",
				"Mastercard", false);
		int before = t.getTransactions().size();
		
		t.addTransaction(transact);
		
		int after = t.getTransactions().size();
		GregorianCalendar g = new GregorianCalendar(2014,Calendar.NOVEMBER,23);
		
		System.out.println("Result: " +t.getTransactionsByCategory("SomethingNew").get(0).getCategory());
		
		assertFalse("Transaction not added to list.", before == after);
		assertEquals("Transaction list not changed by correct amount.", 6, after);
		assertSame("Category Indexing not working correctly with add event.", transact,
				t.getTransactionsByCategory("SomethingNew").get(0));
		assertSame("Date Indexing not working correctly with add event.", transact,
				t.getTransactionsByDate(g.getTime()).get(0));
	}
	
	@Test
	@Ignore
	public void testTransactionsBetweenDates_SameDateCase(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		
		assertSame("Wrong date transaction fetched.", trans2[0],
				t.getTransactionsBetweenDates(start.getTime(), end.getTime()).get(0));
	}
	
	@Test
	@Ignore
	public void testTransactionsBetweenDates_DifferentDatesSameMonth(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getTransactionsBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test
	@Ignore
	public void testTransactionsBetweenDates_DifferentDatesSameYearAndDifferentMonths(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.DECEMBER,14);

		assertEquals("Wrong number of transactions fetched.", 5,
				t.getTransactionsBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test
	@Ignore
	public void testTransactionsBetweenDates_DifferentDatesDifferentYears(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2013,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,14);
		
		Transact transact = new Transact(new GregorianCalendar(2014,Calendar.NOVEMBER,15), "Ice Cream",
				4.50, "Dessert",
				"Mastercard", false);
		
//		t.addTransaction(transact);
		
		assertEquals("Wrong number of transactions fetched.", 2,
				t.getTransactionsBetweenDates(start.getTime(), end.getTime()).size());
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	@Ignore
	public void testTransactionsBetweenDates_DifferentDatesUnAcceptableCase(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,13);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,12);
		t.getTransactionsBetweenDates(start.getTime(), end.getTime()); //Throws exception
	}
	
	@Test
	@Ignore
	public void testTransactionsBetweenDates_DifferentDatesAcceptableCaseReturnsNone(){
		TList t = new TList(trans2);
		GregorianCalendar start = new GregorianCalendar(2014,Calendar.NOVEMBER,20);
		GregorianCalendar end = new GregorianCalendar(2014,Calendar.NOVEMBER,22);
		
		assertEquals("Wrong number of transactions fetched.", 0,
				t.getTransactionsBetweenDates(start.getTime(), end.getTime()).size());
	}
}
