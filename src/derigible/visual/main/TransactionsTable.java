package derigible.visual.main;

import java.util.GregorianCalendar;

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

import com.ibm.icu.util.Calendar;

import derigible.controller.TransactionsController;
import derigible.transactions.Transaction;

public class TransactionsTable extends Composite {

    private final TransactionsController tc;
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
    public TransactionsTable(Composite parent, int style,
	    TransactionsController tc) {
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
	    gc = new GregorianCalendar(gc0.get(Calendar.YEAR),
		    Integer.parseInt(ti.getText(column)) - 1,
		    gc0.get(Calendar.DAY_OF_MONTH));
	    VisualUpdater.updateDate(t, gc, tc);
	    break;
	case TransactionsTable.DAY:
	    gc0 = t.getDate();
	    gc = new GregorianCalendar(gc0.get(Calendar.YEAR),
		    gc0.get(Calendar.MONTH), Integer.parseInt(ti
			    .getText(column)));
	    VisualUpdater.updateDate(t, gc, tc);
	    break;
	case TransactionsTable.YEAR:
	    gc0 = t.getDate();
	    gc = new GregorianCalendar(Integer.parseInt(ti.getText(column)),
		    gc0.get(Calendar.MONTH), gc0.get(Calendar.DAY_OF_MONTH));
	    VisualUpdater.updateDate(t, gc, tc);
	    break;
	case TransactionsTable.AMOUNT:
	    t.setAmount(Double.parseDouble(ti.getText(column)));
	    // TODO this will cause situations where SubTransactions can total
	    // more than Original Amount
	    break;
	case TransactionsTable.CAT:
	    VisualUpdater.updateCategory(t, ti.getText(column), tc);
	    break;
	case TransactionsTable.TTYPE:
	    boolean credit = true;
	    if (ti.getText(column).equalsIgnoreCase("debit")) {
		credit = false;
	    }
	    VisualUpdater.updateCreditOrDebit(t, credit, tc);
	    break;
	case TransactionsTable.SUBT:
	    break;
	case TransactionsTable.DESC:
	    t.setDescription(ti.getText(column));
	    break;
	case TransactionsTable.ACCOUNT:
	    VisualUpdater.updateAccount(t, ti.getText(column), tc);
	    break;
	}
    }

    TransactionsController getTC() {
	return tc;
    }
}
