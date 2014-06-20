package derigible.visual.main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import derigible.controller.TransactionsController;
import derigible.transactions.Transaction;

public final class Filters {

    private Filters() {
	// Do nothing, static class
    }

    public static void filterValues(Filter f, LeftTabFolder lsb,
	    TransactionsController tc, TableTab tb,
	    ArrayDeque<Control> focusHistory) {

	LeftSideBar newLsb = new LeftSideBar(lsb, SWT.NONE, "Filtered View "
		+ tb.getTables().size() + 1);
	List<Transaction> trans = applyFilter(f, lsb.getLsb(), newLsb, tc);

	newLsb.getLsb().getCountLbl().setText(Integer.toString(trans.size()));

	tb.addTable("Filtered View " + tb.getTables().size(), trans, tc,
		focusHistory, newLsb);
	lsb.getLsb().getLsb().getListAccounts().deselectAll();
	lsb.getLsb().getLsb().getCategoriesList().deselectAll();
    }

    private static java.util.List<Transaction> applyFilter(Filter filter,
	    LeftSideBar oldBar, LeftSideBar newBar, TransactionsController tc) {
	java.util.List<Transaction> rslt;
	String[] cats;
	String[] acts;
	GregorianCalendar gc1;
	GregorianCalendar gc2;
	double bal = 0;
	switch (filter) {
	case CATEGORIES:
	    cats = newBar.getLsb().getCategoriesList().getSelection();
	    rslt = tc.getTransactions().getByCategories(cats);
	    setBalance(tc.getBalanceForCategories(cats), newBar);
	    break;
	case ACCOUNTS:
	    acts = newBar.getLsb().getListAccounts().getSelection();
	    bal = 0;
	    for (String act : acts) {
		bal += tc.getCurrentBalanceForAccount(act);
	    }
	    rslt = tc.getTransactions().getByAccounts(acts);
	    setBalance(bal, newBar);
	    break;
	case DATE:
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    rslt = tc.getTransactions().getByDate(gc1.getTime());
	    setBalance(tc.getBalanceBetweenDates(gc1.getTime(), gc1.getTime()),
		    newBar);
	    break;
	case DATES:
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    gc2 = new GregorianCalendar(newBar.getLsb().getYear2(), newBar
		    .getLsb().getMonth2() - 1, newBar.getLsb().getDay2());
	    rslt = tc.getTransactions().getByDate(gc1.getTime());
	    setBalance(tc.getBalanceBetweenDates(gc1.getTime(), gc2.getTime()),
		    newBar);
	    break;
	case CATEGORIES_AND_DATE:
	    cats = newBar.getLsb().getCategoriesList().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    rslt = tc.getTransactions().getByCategoriesAndDate(cats,
		    gc1.getTime());
	    setBalance(

	    tc.getBalanceForCategoriesBetweenDates(cats, gc1.getTime(),
		    gc1.getTime()), newBar);
	    break;
	case CATEGORIES_AND_DATES:
	    cats = newBar.getLsb().getCategoriesList().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    gc2 = new GregorianCalendar(newBar.getLsb().getYear2(), newBar
		    .getLsb().getMonth2() - 1, newBar.getLsb().getDay2());
	    rslt = tc.getTransactions().getByCategoriesAndDate(cats,
		    gc1.getTime());
	    setBalance(tc.getBalanceForCategoriesBetweenDates(cats,
		    gc1.getTime(), gc2.getTime()), newBar);
	    break;
	case ACCOUNTS_AND_DATE:
	    acts = newBar.getLsb().getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc1.getTime(), act);
	    }
	    rslt = tc.getTransactions().filterByDate(gc1.getTime(),
		    tc.getTransactions().getByAccounts(acts));
	    setBalance(bal, newBar);
	    break;
	case ACCOUNTS_AND_DATES:
	    acts = newBar.getLsb().getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    gc2 = new GregorianCalendar(newBar.getLsb().getYear2(), newBar
		    .getLsb().getMonth2() - 1, newBar.getLsb().getDay2());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc2.getTime(), act);
	    }
	    rslt = tc.getTransactions().filterByDates(gc1.getTime(),
		    gc2.getTime(), tc.getTransactions().getByAccounts(acts));
	    setBalance(bal, newBar);
	    break;
	case CATEGORIES_AND_ACCOUNTS_AND_DATE:
	    cats = newBar.getLsb().getCategoriesList().getSelection();
	    acts = newBar.getLsb().getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc1.getTime(), act);
	    }
	    rslt = tc.getTransactions().filterByAccounts(
		    acts,
		    tc.getTransactions().getByCategoriesAndDate(cats,
			    gc1.getTime()));
	    setBalance(
		    bal
			    + tc.getBalanceForCategoriesBetweenDates(cats,
				    gc1.getTime(), gc1.getTime()), newBar);
	    break;
	case CATEGORIES_AND_ACCOUNTS_AND_DATES:
	    cats = newBar.getLsb().getCategoriesList().getSelection();
	    acts = newBar.getLsb().getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(newBar.getLsb().getYear1(), newBar
		    .getLsb().getMonth1() - 1, newBar.getLsb().getDay1());
	    gc2 = new GregorianCalendar(newBar.getLsb().getYear2(), newBar
		    .getLsb().getMonth2() - 1, newBar.getLsb().getDay2());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc2.getTime(), act);
	    }
	    rslt = tc.getTransactions().filterByAccounts(
		    acts,
		    tc.getTransactions().getByCategoriesAndDates(cats,
			    gc1.getTime(), gc2.getTime()));
	    setBalance(
		    bal
			    + tc.getBalanceForCategoriesBetweenDates(cats,
				    gc1.getTime(), gc2.getTime()), newBar);
	    break;
	default:
	    rslt = new ArrayList<Transaction>();
	    break;
	}
	return rslt;
    }

    private static void setBalance(double balance, LeftSideBar leftBar) {
	leftBar.getLsb().getBalanceLbl()
		.setText(String.format("%1$,.2f", balance));
    }
}
