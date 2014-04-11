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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
//import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import derigible.controller.TransactionsController;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.MockTransform;
import derigible.transformations.TransformToTransactions;
import derigible.utils.FileU;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;


/**
 * @author marphill
 *
 */
public class CSVToTransactionsTest {

    private static File fgood;
    private static File fbad;
    private CSVToTransactions cgood;
    private CSVToTransactions cbad;
    private CSVToTransactions mint;

    @BeforeClass
    public static void setCSVFiles() {
        try {
            fgood = FileU.getFileInJavaProjectFolder("testDocs/csvModified.csv");
            fbad = FileU.getFileInJavaProjectFolder("testDocs/completelyBadFormat.csv");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        cgood = new CSVToTransactions(FileU.getFileInJavaProjectFolder("testDocs/csvModified.csv"));
        cbad = new CSVToTransactions(FileU.getFileInJavaProjectFolder("testDocs/completelyBadFormat.csv"));
        mint = new CSVToTransactions(FileU.getFileInJavaProjectFolder("testDocs/transactions.csv"));
    }

    @After
    public void cleanUp() {
        String[][] nulled = null;
        cgood.setPossibleHeaders(nulled);
        cgood = null;
    }

    @Test
    public void testCSVToTransactionsCreation() {
        boolean created = false;
        try {
            CSVToTransactions csv = new CSVToTransactions(fgood);
            CSVToTransactions csv2 = new CSVToTransactions(fbad);
            created = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue("CSVToTransactions not created correctly.", created);
    }

    @Test
    public void testCSVBadThrowsIOExceptions() {
        try {
            cbad.data_to_transactions();
        } catch (IOException e) {
            assertEquals("Wrong error message received.", "Not all keys set. "
                    + "You are missing some values. Please define the headers of your CSV.",
                    e.getMessage());
        }
    }

    @Test
    public void testCSVGoodKeysNotDefinedHardCodedGuessStillWorks() {
        try {
            cgood.data_to_transactions();
        } catch (IOException e) {
            assertFalse("Error message received.", "Not all keys set. You are missing some values. Please define the headers of your CSV."
                    .equals(e.getMessage()));
        }
    }

    @Test
    public void testCSVGoodKeysDefinedOutPutWorks() {
        try {
            String[][] possibleHeadersTemp = {{"description", "1"}, {"amount", "2"},
            {"transaction type", "3"}, {"category", "4"}, {"account name", "5"},
            {"notes", "6"}, {"labels", "7"}};
            cgood.setPossibleHeaders(possibleHeadersTemp);
            cgood.data_to_transactions();
        } catch (IOException e) {
            assertFalse("Error message received.", "Not all keys set. You are missing some values. Please define the headers of your CSV."
                    .equals(e.getMessage()));
        }
    }

    @Test
    public void testCreditsAndDebitsAddedCorrectly() {
        try {
            String[][] possibleHeadersTemp = {{"description", "1"}, {"amount", "2"},
            {"transaction type", "3"}, {"category", "4"}, {"account name", "5"},
            {"notes", "6"}, {"labels", "7"}};
            cgood.setPossibleHeaders(possibleHeadersTemp);
            TransactionsController tc = new TransactionsController(cgood);
            assertEquals("Wrong balance returned.", 1836.73, tc.getCurrentBalance(), .001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMint_comCSVCorrectlyIdentifiedAndParsed() {
        try {
            TransactionsController tc = new TransactionsController(mint);
            assertEquals("Wrong balance returned.", -28354.78, tc.getCurrentBalance(), .001);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGUIDAddedMint_CSV() {
        try {
            TransactionsController tc = new TransactionsController(mint);
            System.out.println(tc.getTransactions().getLastTransaction().getGUID());
            assertNotNull("Guid not set.", tc.getTransactions().getLastTransaction().getGUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Test of data_to_transactions method, of class CSVToTransactions.
//     */
//    @Test
//    public void testData_to_transactions() throws Exception {
//        System.out.println("data_to_transactions");
//        CSVToTransactions instance = null;
//        Transactions expResult = null;
//        Transactions result = instance.data_to_transactions();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setHeaders method, of class CSVToTransactions.
//     */
//    @Test
//    public void testSetHeaders() throws Exception {
//        System.out.println("setHeaders");
//        int[] headerMap = null;
//        CSVToTransactions instance = null;
//        instance.setHeaders(headerMap);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setPossibleHeaders method, of class CSVToTransactions.
//     */
//    @Test
//    public void testSetPossibleHeaders_StringArrArr() {
//        System.out.println("setPossibleHeaders");
//        String[][] headers = null;
//        CSVToTransactions instance = null;
//        instance.setPossibleHeaders(headers);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setPossibleHeaders method, of class CSVToTransactions.
//     */
//    @Test
//    //TODO
//    public void testSetPossibleHeaders_File() throws Exception {
//        System.out.println("setPossibleHeaders");
//        File file = null;
//        CSVToTransactions instance = null;
//        instance.setPossibleHeaders(file);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
