package derigible.visual.main;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import derigible.controller.TransactionsController;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.utils.TListener;

public class TableTab extends CTabFolder {

    private LinkedList<Node> tables = new LinkedList<Node>();
    private CTabItem defaultTab = new CTabItem(this, SWT.NONE);

    public TableTab(Composite parent, int style, String tabName) {
	super(parent, style);
	init(tabName);
    }

    /**
     * Builds a tab with a default tabitem and associates a table and a
     * leftSideBar with the tabitem.
     * 
     * @param parent
     * @param style
     * @param tabName
     * @param lsb
     */
    public TableTab(Composite parent, int style, String tabName, Table t,
	    LeftSideBar lsb) {
	super(parent, style);
	init(tabName);
	tables.add(new Node(new TableTabItem(this, style, t), lsb));
    }

    private void init(String tabName) {
	defaultTab.setShowClose(true);
	defaultTab.setText(tabName);
	this.setUnselectedCloseVisible(false);
	this.setSimple(false);
	defaultTab.setControl(this);
	this.setSelection(0);
    }

    public void addTable(String name, java.util.List<Transaction> trans,
	    TransactionsController tc, ArrayDeque<Control> focusHistory,
	    LeftSideBar lsb) {
	Listeners.addFocusListener(this, focusHistory);

	Table newTable = new TransactionsTable(this, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();
	TableTabItem next = new TableTabItem(this, SWT.NONE, newTable);
	next.setControl(newTable);
	next.setShowClose(true);
	newTable.setHeaderVisible(true);
	newTable.setLinesVisible(true);

	for (Transaction t : trans) {
	    if (!t.isSubTransaction()) {
		TableItem row = new TableItem(newTable, SWT.NONE);

		row.setText(0,
			Integer.toString(t.getDate().get(Calendar.MONTH) + 1));
		row.setText(1, Integer.toString(t.getDate().get(
			Calendar.DAY_OF_MONTH)));
		row.setText(2, Integer.toString(t.getDate().get(Calendar.YEAR)));
		row.setText(3, t.getDescription());
		row.setText(4, Double.toString(t.getAmount()));
		row.setText(5, t.getCategory());
		row.setText(6, t.getAccount());
		if (t.isCredit()) {
		    row.setText(7, "Credit");
		} else {
		    row.setText(7, "Debit");
		}
		row.setData(t);
		if (t.hasSubTransactions()) {
		    Transact t0 = (Transact) t;
		    row.setText(8, "ST Bal: " + t0.getDividedAmount());
		}
		TListener tl = new TListener(t, tc);
		tl.addTableItem(row);
		new TListener(t, tc);
	    }
	}
	this.setSelection(next);
	tables.add(new Node(next, lsb));
    }

    /**
     * @return the tables
     */
    public LinkedList<TableTab.Node> getTables() {
	return tables;
    }

    /**
     * @param tables
     *            the tables to set
     */
    public void setTables(LinkedList<TableTab.Node> tables) {
	this.tables = tables;
    }

    /**
     * @return the defaultTab
     */
    public CTabItem getDefaultTab() {
	return defaultTab;
    }

    /**
     * @param defaultTab
     *            the defaultTab to set
     */
    public void setDefaultTab(CTabItem defaultTab) {
	this.defaultTab = defaultTab;
    }

    public class Node {
	public TableTabItem table;
	public LeftSideBar lsb;

	private Node(TableTabItem t, LeftSideBar bar) {
	    table = t;
	    lsb = bar;
	    // Couple the objects together
	    table.table.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
		    lsb.getParent().setSelection(lsb);
		}

		@Override
		public void focusLost(FocusEvent arg0) {
		    // Don't do anything
		}

	    });
	    table.addDisposeListener(new DisposeListener() {

		@Override
		public void widgetDisposed(DisposeEvent arg0) {
		    lsb.dispose();
		}

	    });
	    lsb.getLsb().addFocusListener(new FocusListener() {
		@Override
		public void focusGained(FocusEvent arg0) {
		    table.getParent().setSelection(table);
		}

		@Override
		public void focusLost(FocusEvent arg0) {
		    // Don't do anything
		}
	    });
	    lsb.addDisposeListener(new DisposeListener() {

		@Override
		public void widgetDisposed(DisposeEvent arg0) {
		    table.dispose();
		}

	    });
	}
    }

    public class TableTabItem extends CTabItem {

	private Table table;

	public TableTabItem(CTabFolder parent, int style, Table t) {
	    super(parent, style);
	    table = t;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
	    return table;
	}

    }

}
