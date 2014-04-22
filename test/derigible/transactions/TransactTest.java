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
		
		SubTransaction st1 = new SubTransaction(t);
		st1.setAmount(50);
		String description = "This is a description.";
		st1.setDescription(description);
		
		assertEquals("Wrong amount divided.", 50.00, t.getDividedAmount(), .001);
	}
}
