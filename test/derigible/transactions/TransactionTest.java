package derigible.transactions;

import org.junit.Before;
import org.junit.After;
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
	Transaction[] trans;
	ArrayList<Transact> talist = new ArrayList<Transact>();
	LinkedList<Transact> tllist = new LinkedList<Transact>();
	
	@Before
	public void setupData(){
		trans = new Transaction[100];
		for(int i = 0; i < trans.length; i++){
			Transact t = new Transact(new Date(), "This is transact " + i,
					30.0 * i, "Category is 1"+ i + " women and children.",
					"Mastercard", false);
			trans[i] = t;
			talist.add(t);
			tllist.add(t);
		}
	}
	
	@After
	public void teardownData(){
		trans = null;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTransactionsCreation() {
		TList t = new TList(trans);
		TList tal = new TList((List) talist);
		TList tll = new TList((List) tllist);
		
		assertEquals("TList by array not equal to TList by ArrayList",
				t.getTransactions().size(), tal.getTransactions().size());
		assertEquals("TList by ArrayList not equal to TList by LinkedList - LinkedList Not ArrayList.",
				tal.getTransactions().getClass(), tll.getTransactions().getClass());
	}
	
	@Test
	public void testCategoryInsertion(){
		
	}
	
}
