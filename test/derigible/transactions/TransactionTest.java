package derigible.transactions;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import derigible.transactions.TList;
import derigible.transactions.Transact;

import java.util.ArrayList;
import java.util.Date;
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
			Transact t = new Transact(new Date(), "This is transact " + i,
					30.0 * i, "Category is 1"+ i + " women and children.",
					"Mastercard", false);
			trans[i] = t;
			talist.add(t);
			tllist.add(t);
		}
		trans2 = new Transaction[5];
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
				t.getTransactionsByCategory("Category is 10 women and children.").get(0));
		
		assertEquals("Too many/few categories returned.", 2,
				t2.getTransactionsByCategory("Dessert").size());
	}
	
}
