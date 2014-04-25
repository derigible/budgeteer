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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import derigible.controller.GUID;

/**
 * @author marcphillips
 *
 */
public class TransactTest {

	@Test//This is a weak test
	public void testTransactCreation(){
		Transact t = new Transact(new GregorianCalendar(), "test desc",
				150.00, "test", "testaccount", true);
		t.setGUID(GUID.generate());
		t.addNote("This is a test note");
		
		assertNotNull("The transaction was not created", t);
	}
	
	@Test
	public void testSubTransactionCreation(){
		Transact t = new Transact(new GregorianCalendar(), "test desc",
				150.00, "test", "testaccount", true);
		t.setGUID(GUID.generate());
		t.addNote("This is a test note");
		
		SubTransaction st1 = new SubTransaction(t, 50);
		String description = "This is a description.";
		st1.setDescription(description);
		
		assertEquals("Wrong amount divided.", 50.00, t.getDividedAmount(), .001);
		assertEquals("Wrong description.", "This is a description.", st1.getDescription());
		assertEquals("Wrong amount undivided.", 100, t.getUndividedAmount(), .001);
		
		SubTransaction st2 = new SubTransaction(t, 25);
		assertEquals("Wrong amount divided.", 75.00, t.getDividedAmount(), .001);
		assertEquals("Wrong description.", "", st2.getDescription());
		assertEquals("Wrong amount undivided.", 75, t.getUndividedAmount(), .001);
		
		SubTransaction st3 = new SubTransaction(t, 80);
		assertEquals("Wrong amount divided.", 150.00, t.getDividedAmount(), .001);
		System.out.println(st3.getNotes());
		assertEquals("Wrong description.", "SubTransaction amount truncated to fit allowed amount. Amount changed to: " + 75.00, st3.getNotes());
		assertEquals("Wrong amount undivided.", 0.0, t.getUndividedAmount(), .001);
	}
	
	@Test
	public void testSubTransactionRemoval(){
		Transact t = new Transact(new GregorianCalendar(), "test desc",
				150.00, "test", "testaccount", true);
		t.setGUID(GUID.generate());
		t.addNote("This is a test note");
		
		SubTransaction st1 = new SubTransaction(t, 50);
		String description = "This is a description.";
		st1.setDescription(description);
		SubTransaction st2 = new SubTransaction(t, 25);
		SubTransaction st3 = new SubTransaction(t, 80);			
		
		assertEquals("Wrong amount divided.", 150.00, t.getDividedAmount(), .001);
		
		t.removeSubTransaction(st1);
		assertEquals("Wrong amount divided.", 100.00, t.getDividedAmount(), .001);
		assertEquals("Wrong description.", "This is a description.", st1.getDescription());
		assertEquals("Wrong amount undivided.", 50, t.getUndividedAmount(), .001);
		
		t.removeSubTransaction(st2);
		assertEquals("Wrong amount divided.", 75.00, t.getDividedAmount(), .001);
		assertEquals("Wrong description.", "", st2.getDescription());
		assertEquals("Wrong amount undivided.", 75, t.getUndividedAmount(), .001);
		
		t.removeSubTransaction(st3);
		assertEquals("Wrong amount divided.", 0.0, t.getDividedAmount(), .001);
		assertEquals("Wrong description.", "SubTransaction amount truncated to fit allowed amount. Amount changed to: " + 75.00, st3.getNotes());
		assertEquals("Wrong amount undivided.", 150.0, t.getUndividedAmount(), .001);
	}
	
	@Test
	public void testSubTransactionArrayResize(){
		Transact t = new Transact(new GregorianCalendar(), "test desc",
				150.00, "test", "testaccount", true);
		t.setGUID(GUID.generate());
		t.addNote("This is a test note");
		
		SubTransaction st1 = new SubTransaction(t, 5);
		String description = "This is a description.";
		st1.setDescription(description);
		SubTransaction st2 = new SubTransaction(t, 5);
		SubTransaction st3 = new SubTransaction(t, 5);		
		SubTransaction st4 = new SubTransaction(t, 5);
		SubTransaction st5 = new SubTransaction(t, 5);
		SubTransaction st6 = new SubTransaction(t, 5);
		SubTransaction st7 = new SubTransaction(t, 5);
		
		assertEquals("Wrong amount divided.", 35.00, t.getDividedAmount(), .001);
		assertEquals("Wrong amount undivided.", 115, t.getUndividedAmount(), .001);
		assertEquals("Wrong size for array.", 9, t.getSubTransactions().length);
	}
}
