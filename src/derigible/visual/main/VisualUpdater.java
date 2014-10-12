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

package derigible.visual.main;

import java.io.File;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import derigible.controller.TransactionsController;
import derigible.saves.Saved;
import derigible.transactions.TransUpdater;
import derigible.transactions.Transaction;

/**
 * @author marcphillips
 * 
 *         Utility class for the visual aspects of this program.
 */
public final class VisualUpdater {
    private VisualUpdater() {
	// Do nothing, not instantiable
    }

    public static boolean updateDate(Transaction t, GregorianCalendar gc,
	    TransactionsController tc) {
	TransUpdater tu = new TransUpdater();
	tu.setCategory(t.getCategory());
	tu.setAccount(t.getAccount());
	tu.setDebitOrCredit(t.isCredit());
	tu.setDate(gc);
	return tc.getTransactions().updateTransaction(t.getGUID(), tu);
    }

    public static boolean updateCategory(Transaction t, String category,
	    TransactionsController tc) {
	TransUpdater tu = new TransUpdater();
	tu.setCategory(category);
	tu.setAccount(t.getAccount());
	tu.setDebitOrCredit(t.isCredit());
	tu.setDate(t.getDate());
	return tc.getTransactions().updateTransaction(t.getGUID(), tu);
    }

    public static boolean updateAccount(Transaction t, String account,
	    TransactionsController tc) {
	TransUpdater tu = new TransUpdater();
	tu.setCategory(t.getCategory());
	tu.setAccount(account);
	tu.setDebitOrCredit(t.isCredit());
	tu.setDate(t.getDate());
	return tc.getTransactions().updateTransaction(t.getGUID(), tu);
    }

    public static boolean updateCreditOrDebit(Transaction t, boolean credit,
	    TransactionsController tc) {
	TransUpdater tu = new TransUpdater();
	tu.setCategory(t.getAccount());
	tu.setAccount(t.getAccount());
	tu.setDebitOrCredit(credit);
	tu.setDate(t.getDate());
	return tc.getTransactions().updateTransaction(t.getGUID(), tu);
    }

    public static int save(Shell shell, Saved saved, String name,
	    String failmessage) {
	File f;
	if ((f = saved.save(name)) != null) {
	    MessageBox save = new MessageBox(shell, SWT.ICON_INFORMATION
		    | SWT.OK);
	    save.setText("File Saved");
	    save.setMessage("File Saved at " + f.getAbsolutePath());
	    save.open();
	    return SWT.YES;
	} else {
	    MessageBox save = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES
		    | SWT.NO);
	    save.setText("Save Failed!");
	    save.setMessage(failmessage);
	    return save.open() == SWT.NO ? SWT.CANCEL : SWT.YES;
	}
    }

}
