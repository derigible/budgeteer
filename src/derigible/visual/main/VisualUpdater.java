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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import derigible.controller.TransactionsController;
import derigible.transactions.TransUpdater;
import derigible.transactions.Transaction;
import derigible.utils.Saved;

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

    public static Closer addCloseListener(Shell shell,
	    HashMap<String, Saved> saved) {
	return new VisualUpdater.Closer(shell, saved);
    }

    private static class Closer implements Listener {
	private HashMap<String, Saved> saved;
	private Shell shell;

	Closer(Shell shell, HashMap<String, Saved> saved) {
	    this.shell = shell;
	    this.saved = saved;
	}

	@Override
	public void handleEvent(Event arg0) {
	    if (!isSaved()) {
		for (Map.Entry<String, Saved> saved : this.saved.entrySet()) {
		    MessageBox save = new MessageBox(shell, SWT.ICON_QUESTION
			    | SWT.YES | SWT.NO | SWT.CANCEL);
		    save.setText("Save Information?");
		    String name;
		    if (saved.getValue().getClass() == TransactionsController.class) {
			save.setMessage("You have unsaved Transaction information. Save now?");
			name = "transactions";
		    } else {
			save.setMessage("You have unsaved budget information for "
				+ saved.getKey() + ". Save now?");
			name = saved.getKey();
		    }
		    if (handleResponse(save.open(), saved.getValue(), name) == SWT.CANCEL) {
			arg0.doit = false;
			break;
		    }
		}
	    }
	}

	private int handleResponse(int open, Saved saved, String name) {
	    if (open == SWT.YES) {
		try {
		    // File f;
		    // if ((f = saved.save(name)) != null) {
		    // MessageBox save = new MessageBox(shell,
		    // SWT.ICON_INFORMATION | SWT.OK);
		    // save.setText("File Saved");
		    // save.setMessage("File Saved at " + f.getAbsolutePath());
		    // save.open();
		    // return SWT.YES;
		    // } else {
		    // MessageBox save = new MessageBox(shell, SWT.ICON_ERROR
		    // | SWT.YES | SWT.NO);
		    // save.setText("Save Failed!");
		    // save.setMessage("Save failed! Exit Anyways?");
		    // return save.open() == SWT.NO ? SWT.CANCEL : SWT.YES;
		    // }
		    return save(shell, saved, name,
			    "Save failed! Exit Anyways?");
		} catch (NullPointerException e) {
		    // canceled save, do nothing
		    return SWT.CANCEL;
		}
	    } else if (open == SWT.CANCEL) {
		return SWT.CANCEL;
	    } else {
		return SWT.NO;
	    }
	}

	public boolean isSaved() {
	    for (Saved saved : this.saved.values()) {
		if (!saved.saved()) {
		    return false;
		}
	    }
	    return true;
	}
    }

}
