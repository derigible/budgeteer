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
package derigible.transformations;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import derigible.controller.BudgetsController;
import derigible.controller.TransactionsController;
import derigible.controller.BudgetController;
import derigible.utils.FileU;

public class BudgetToCSVTest {

	private static BudgetController bc = null;
	private static TransactionsController tc = null;
	private static BudgetsController bsc = null;
	
	@BeforeClass
    public static void setCSVFiles() {
		 File fgood = null;
		 CSVToTransactions csv = null;
        try {
        	fgood = FileU.getFileInJavaProjectFolder("testDocs/csvModified.csv");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
        	csv = new CSVToTransactions(fgood);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        try {
        	tc = new TransactionsController(csv.data_to_transactions());
        	bsc = new BudgetsController(tc);
        	bc = bsc.createNewBudget("test");
        	bc.includeCategory("test");
        	assertEquals("Wrong number of transactions in budget.", 3, bc.getTransactions().getTransactions().size());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@Test
	public void testBudgetToCSV(){
		File f = null;
		try {
			f = bc.transactionsToCSV("test", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = new File(System.getProperty("user.home") + "/Budgeteer/budgets").getAbsolutePath();
		assertEquals("Wrong path to file.", path, f.getParent());
	}
	
	@Test
	public void testBudgetToCSVBackToBudget(){
		File f = null;
		try {
			f = bc.transactionsToCSV(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = new File(System.getProperty("user.home") + "/Budgeteer/budgets").getAbsolutePath();
		assertEquals("Wrong path to file.", path, f.getParent());
		try {
			BudgetsController bsc0 = new BudgetsController(tc);
			bc = bsc0.readInBudget("test", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("Wrong number of transactions.", 3, bc.getTransactions().getTransactions().size());
	}
	
	@Test
	public void testBudgetsToCSV() throws IOException{
		BudgetController bc = bsc.createNewBudget("discover");
		bc.includeAccount("Discover :");
		bsc.budgetsToCSV();
	}
}
