package derigible.visual.main;

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

import derigible.transactions.Transaction;

public class TransactionsTable extends Composite {

	private final Table table;
	private final int MONTH = 0;
	private final int DAY = 1;
	private final int YEAR = 2;
	private final int DESC = 3;
	private final int AMOUNT = 4;
	private final int CAT = 5;
	private final int ACCOUNT = 6;
	private final int TTYPE = 7;
	private final int SUBT = 8;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TransactionsTable(Composite parent, int style) {
		super(parent, style);
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
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
	
	private void addTableEditor(){
		final TableEditor editor = new TableEditor (table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener (SWT.MouseDoubleClick, new Listener () {
			@Override
			public void handleEvent (Event event) {
				Rectangle clientArea = table.getClientArea ();
				Point pt = new Point (event.x, event.y);
				int index = table.getTopIndex ();
				while (index < table.getItemCount ()) {
					boolean visible = false;
					final TableItem item = table.getItem (index);
					for (int i=0; i<table.getColumnCount (); i++) {
						Rectangle rect = item.getBounds (i);
						if (rect.contains (pt)) {
							final int column = i;
							final Text text = new Text (table, SWT.NONE);
							Listener textListener = new Listener () {
								@Override
								public void handleEvent (final Event e) {
									switch (e.type) {
										case SWT.FocusOut:
											item.setText (column, text.getText ());
											text.dispose ();
											break;
										case SWT.Traverse:
											switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													item.setText (column, text.getText ());
													//FALL THROUGH
												case SWT.TRAVERSE_ESCAPE:
													text.dispose ();
													e.doit = false;
											}
											break;
									}
								}
							};
							text.addListener (SWT.FocusOut, textListener);
							text.addListener (SWT.Traverse, textListener);
							editor.setEditor (text, item, i);
							text.setText (item.getText (i));
							text.selectAll ();
							text.setFocus ();
							return;
						}
						if (!visible && rect.intersects (clientArea)) {
							visible = true;
						}
					}
					if (!visible) return;
					index++;
				}
			}
		});
	}
	
	private void updateTableData(int column, TableItem ti){
		Transaction t = (Transaction) ti.getData();
		switch(column){
			case MONTH:
				break;
		}
	}

}
