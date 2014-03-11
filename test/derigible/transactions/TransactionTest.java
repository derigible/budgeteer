package derigible.transactions;

import org.junit.Test;
import static org.mockito.Mockito.*;
import derigible.transactions.TList;
import derigible.transactions.TransactionArray;

public class TransactionTest { 	
	

	@Test
	public void test() {
		TList test = mock(TList.class);
		test.getLastTransaction();
		test.getLastTransaction();
		
		verify(test, times(2)).getLastTransaction();
		
		when(test.getLastTransaction()).thenReturn(new TransactionArray());
		
	}

}
