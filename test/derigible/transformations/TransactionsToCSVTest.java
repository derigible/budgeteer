/**
 * 
 */
package derigible.transformations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import derigible.controller.TransactionsController;
import derigible.transformations.TransactionsToCSV;
import derigible.transactions.TList;
import derigible.utils.FileU;
//import org.junit.Ignore;

/**
 * @author marcphillips
 *
 */
public class TransactionsToCSVTest {

	private static TList list = null;
	
	@BeforeClass
    public static void setCSVFiles() {
		 File fgood = null;
		 CSVToTransactions csv = null;
        try {
        	fgood = FileU.getFileInJavaProjectFolder("testDocs/csvModified.csv");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
        	csv = new CSVToTransactions(fgood);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			list = (TList) new TransactionsController(csv).getTransactions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	 
	@Test
	public void testTransactionsToCSV(){
		TransactionsToCSV ttc = new TransactionsToCSV();
		try {
			ttc.transactions_to_storage(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChangeNameAndExcludeID(){
		TransactionsToCSV ttc = new TransactionsToCSV();
		try {
			ttc.setFileName("test.csv");
			ttc.excludeId(true);
			ttc.transactions_to_storage(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}