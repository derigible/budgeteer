package derigible.visual.custom.widgets;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import derigible.controller.TransactionsController;
import derigible.transactions.Transact;
import derigible.transactions.abstracts.Transaction;
import derigible.transactions.utils.TransactionUpdater;
import derigible.utils.TListener;
import derigible.visual.filters.Filter;

public class TransactionsTable extends Composite {

	private TransactionsController tc;
	private final Table table;
	public static final int MONTH = 0;
	public static final int DAY = 1;
	public static final int YEAR = 2;
	public static final int DESC = 3;
	public static final int AMOUNT = 4;
	public static final int CAT = 5;
	public static final int ACCOUNT = 6;
	public static final int TTYPE = 7;
	public static final int SUBT = 8;

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public TransactionsTable(Composite parent, int style, TransactionsController tc) {
		super(parent, style);

		this.tc = tc;

		if (tc != null)
			this.setData(tc.getName());

		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnMonth = new TableColumn(table, SWT.NONE);
		tblclmnMonth.setWidth(52);
		tblclmnMonth.setText("Month");

		TableColumn tblclmnDay = new TableColumn(table, SWT.NONE);
		tblclmnDay.setWidth(37);
		tblclmnDay.setText("Day");

		TableColumn tblclmnYear = new TableColumn(table, SWT.NONE);
		tblclmnYear.setWidth(46);
		tblclmnYear.setText("Year");

		TableColumn tblclmnDescription = new TableColumn(table, SWT.NONE);
		tblclmnDescription.setWidth(190);
		tblclmnDescription.setText("Description");

		TableColumn tblclmnAmount = new TableColumn(table, SWT.NONE);
		tblclmnAmount.setWidth(100);
		tblclmnAmount.setText("Amount");

		TableColumn tblclmnCategory = new TableColumn(table, SWT.NONE);
		tblclmnCategory.setWidth(100);
		tblclmnCategory.setText("Category");

		TableColumn tblclmnAccount = new TableColumn(table, SWT.NONE);
		tblclmnAccount.setWidth(91);
		tblclmnAccount.setText("Account");

		TableColumn tblclmnTransactionType = new TableColumn(table, SWT.NONE);
		tblclmnTransactionType.setWidth(106);
		tblclmnTransactionType.setText("Transaction Type");

		TableColumn tblclmnSubtransactions = new TableColumn(table, SWT.NONE);
		tblclmnSubtransactions.setWidth(100);
		tblclmnSubtransactions.setText("SubTransactions");

		addTableEditor();
	}

	public Table getTable() {
		return table;
	}

	public GridData getPrefferedGridData() {
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumWidth = 240;
		gd_table.minimumHeight = 120;
		gd_table.widthHint = 793;
		gd_table.heightHint = 738;
		return gd_table;
	}

	public TransactionsTable setPreferredLayout() {
		return this.setGridData(this.getPrefferedGridData());
	}

	public TransactionsTable setGridData(GridData d) {
		table.setLayoutData(d);
		return this;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void addTableEditor() {
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = table.getTopIndex();
				while (index < table.getItemCount()) {
					boolean visible = false;
					final TableItem item = table.getItem(index);
					for (int i = 0; i < table.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							final int column = i;
							final Text text = new Text(table, SWT.NONE);
							Listener textListener = new Listener() {
								@Override
								public void handleEvent(final Event e) {
									switch (e.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										text.dispose();
										updateTableData(column, item);
										break;
									case SWT.Traverse:
										switch (e.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											updateTableData(column, item);
											// FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											text.dispose();
											e.doit = false;
										}
										break;
									}
								}
							};
							text.addListener(SWT.FocusOut, textListener);
							text.addListener(SWT.Traverse, textListener);
							editor.setEditor(text, item, i);
							text.setText(item.getText(i));
							text.selectAll();
							text.setFocus();
							return;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
	}

	private void updateTableData(int column, TableItem ti) {
		Transaction t = (Transaction) ti.getData();
		GregorianCalendar gc0;
		GregorianCalendar gc;
		switch (column) {
		case TransactionsTable.MONTH:
			gc0 = t.getDate();
			gc = new GregorianCalendar(gc0.get(GregorianCalendar.YEAR), Integer.parseInt(ti.getText(column)) - 1,
					gc0.get(GregorianCalendar.DAY_OF_MONTH));
			TransactionUpdater.updateDate(t, gc, tc);
			break;
		case TransactionsTable.DAY:
			gc0 = t.getDate();
			gc = new GregorianCalendar(gc0.get(GregorianCalendar.YEAR), gc0.get(GregorianCalendar.MONTH),
					Integer.parseInt(ti.getText(column)));
			TransactionUpdater.updateDate(t, gc, tc);
			break;
		case TransactionsTable.YEAR:
			gc0 = t.getDate();
			gc = new GregorianCalendar(Integer.parseInt(ti.getText(column)), gc0.get(GregorianCalendar.MONTH),
					gc0.get(GregorianCalendar.DAY_OF_MONTH));
			TransactionUpdater.updateDate(t, gc, tc);
			break;
		case TransactionsTable.AMOUNT:
			t.setAmount(Double.parseDouble(ti.getText(column)));
			// TODO this will cause situations where SubTransactions can total
			// more than Original Amount
			break;
		case TransactionsTable.CAT:
			TransactionUpdater.updateCategory(t, ti.getText(column), tc);
			break;
		case TransactionsTable.TTYPE:
			boolean credit = true;
			if (ti.getText(column).equalsIgnoreCase("debit")) {
				credit = false;
			}
			TransactionUpdater.updateCreditOrDebit(t, credit, tc);
			break;
		case TransactionsTable.SUBT:
			break;
		case TransactionsTable.DESC:
			t.setDescription(ti.getText(column));
			break;
		case TransactionsTable.ACCOUNT:
			TransactionUpdater.updateAccount(t, ti.getText(column), tc);
			break;
		}
	}

	TransactionsController getTC() {
		return tc;
	}

	public void setTC(TransactionsController tc) {
		this.tc = tc;
	}

	public List<Transaction> fillTable() {
		table.removeAll();
		return fillTable(tc.getTransactions().getTransactions());
	}

	public List<Transaction> fillTable(String[] strings, Filter filter) throws IOException{
		List<Transaction> trans;
		if(filter == Filter.ACCOUNTS){
			trans = fillTable(tc.getTransactions().getByAccounts(strings));
		} else if(filter == Filter.CATEGORIES){
			trans =  fillTable(tc.getTransactions().getByCategories(strings));
		} else {
			throw new IOException("This is not a valid filter in this context.");
		}
		return trans;
	}

	public List<Transaction> fillTable(GregorianCalendar gc1){
		return fillTable(tc.getTransactions().getByDate(gc1.getTime()));
	}

	public List<Transaction> fillTable(GregorianCalendar gc1, GregorianCalendar gc2){
		return fillTable(tc.getTransactions().getBetweenDates(gc1.getTime(), gc2.getTime()));
	}

	public List<Transaction> fillTable(String[] strings, Filter filter, GregorianCalendar gc1) throws IOException{
		List<Transaction> trans;
		if(filter == Filter.ACCOUNTS){
			trans = fillTable(tc.getTransactions().filterByDate(gc1.getTime(), tc.getTransactions().getByAccounts(strings)));
		} else if(filter == Filter.CATEGORIES){
			trans = fillTable(tc.getTransactions().getByCategoriesAndDate(strings, gc1.getTime()));
		} else {
			throw new IOException("This is not a valid filter in this context.");
		}
		return trans;
	}

	public List<Transaction> fillTable(String[] strings, Filter filter, GregorianCalendar gc1, GregorianCalendar gc2) throws IOException{
		List<Transaction> trans;
		if(filter == Filter.ACCOUNTS){
			trans = fillTable(tc.getTransactions().filterByDates(gc1.getTime(), gc2.getTime(), tc.getTransactions().getByAccounts(strings)));
		} else if(filter == Filter.CATEGORIES){
			trans = fillTable(tc.getTransactions().getByCategoriesAndDates(strings, gc1.getTime(), gc2.getTime()));
		} else {
			throw new IOException("This is not a valid filter in this context.");
		}
		return trans;
	}

	public List<Transaction> fillTable(String[] cats, String[] acts){
		return fillTable(tc.getTransactions().filterByAccounts(acts, tc.getTransactions().getByCategories(cats)));
	}

	public List<Transaction> fillTable(String[] cats, String[] acts, GregorianCalendar gc1){
		return fillTable(tc.getTransactions().filterByAccounts(acts, tc.getTransactions().getByCategoriesAndDate(cats, gc1.getTime())));
	}

	public List<Transaction> fillTable(String[] cats, String[] acts, GregorianCalendar gc1, GregorianCalendar gc2){
		return fillTable(tc.getTransactions().filterByAccounts(acts, tc.getTransactions().getByCategoriesAndDates(cats, gc1.getTime(), gc2.getTime())));
	}

	private List<Transaction> fillTable(List<Transaction> trans){
		for (Transaction t : trans) {
			if (!t.isSubTransaction()) {
				TableItem row = new TableItem(table, SWT.NONE);

				row.setText(0, Integer.toString(t.getDate().get(Calendar.MONTH) + 1));
				row.setText(1, Integer.toString(t.getDate().get(Calendar.DAY_OF_MONTH)));
				row.setText(2, Integer.toString(t.getDate().get(Calendar.YEAR)));
				row.setText(3, t.getDescription());
				row.setText(4, String.format("%1$,.2f", t.getAmount()));
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
			}
		}
		return trans;
	}
}
