package derigible.visual.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TransactionsTable extends Composite {

	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TransactionsTable(Composite parent, int style) {
		super(parent, style);
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnDay = new TableColumn(table, SWT.NONE);
		tblclmnDay.setWidth(37);
		tblclmnDay.setText("Day");
		
		TableColumn tblclmnMonth = new TableColumn(table, SWT.NONE);
		tblclmnMonth.setWidth(52);
		tblclmnMonth.setText("Month");
		
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
	}
	
	public Table getTable(){
		return table;
	}
	
	public GridData getPrefferedGridData(){
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumWidth = 240;
		gd_table.minimumHeight = 120;
		gd_table.widthHint = 793;
		gd_table.heightHint = 738;
		return gd_table;
	}
	
	public TransactionsTable setPreferredLayout(){
		return this.setGridData(this.getPrefferedGridData());
	}
	
	public TransactionsTable setGridData(GridData d){
		table.setLayoutData(d);
		return this;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
