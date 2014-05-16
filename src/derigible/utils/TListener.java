package derigible.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.TableItem;

import com.ibm.icu.util.Calendar;

import derigible.transactions.Transaction;
import derigible.visual.main.TransactionsTable;

public class TListener implements PropertyChangeListener {

	private ArrayList<TableItem> tItems = new ArrayList<TableItem>();

	public TListener(Transaction t) {
		t.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		int row = row(event.getPropertyName());
		if (row != -2) {
			updateWidgets(stringify(event.getNewValue()), row);
		} else if (row == -2) {
			updateDateWidgets(event.getNewValue());
		}
	}

	public void addTableItem(TableItem item) {
		tItems.add(item);
	}

	private int row(String eName) {
		if (eName.equalsIgnoreCase("account")) {
			return TransactionsTable.ACCOUNT;
		} else if (eName.equalsIgnoreCase("category")) {
			return TransactionsTable.CAT;
		} else if (eName.equalsIgnoreCase("amount")) {
			return TransactionsTable.AMOUNT;
		} else if (eName.equalsIgnoreCase("debitorcredit")) {
			return TransactionsTable.TTYPE;
		} else if (eName.equalsIgnoreCase("originalDescription")) {
			return TransactionsTable.DESC;
		} else if (eName.equalsIgnoreCase("description")) {
			return TransactionsTable.DESC;
		} else if (eName.equalsIgnoreCase("date")) {
			return -2;
		} else {
			return -1;
		}
	}

	private void updateWidgets(String newValue, int row) {
		for (TableItem w : tItems) {
			w.setText(row, newValue);
		}
	}

	private void updateDateWidgets(Object newValue) {
		GregorianCalendar gc = (GregorianCalendar) newValue;
		updateWidgets(Integer.toString(gc.get(Calendar.YEAR)),
				TransactionsTable.YEAR);
		updateWidgets(Integer.toString(gc.get(Calendar.MONTH)),
				TransactionsTable.MONTH);
		updateWidgets(Integer.toString(gc.get(Calendar.DAY_OF_MONTH)),
				TransactionsTable.DAY);

	}

	private String stringify(Object val) {
		if (val instanceof Number) {
			if (val.getClass() == Integer.class) {
				return Integer.toString((Integer) val);
			} else if (val.getClass() == Double.class) {
				return Double.toString((Double) val);
			} else {
				return Long.toString((Long) val);
			}
		} else {
			return val.toString();
		}
	}
}
