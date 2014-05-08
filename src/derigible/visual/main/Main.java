package derigible.visual.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import derigible.controller.TransactionsController;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.CSVToTransactions;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Main {

	protected TransactionsController tc = null;
	protected Shell shlBudgeteer;
	private Table table;
	private Table hiddenTable;
	private TableColumn tblclmnNewColumn;
	private List categoriesList;
	private List listAccounts;
	private String[] categories;
	private String[] accounts;
	private Label balanceLbl;
	private Label countLbl;
  
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlBudgeteer.open();
		shlBudgeteer.layout();
		while (!shlBudgeteer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlBudgeteer = new Shell();
		shlBudgeteer.setMinimumSize(new Point(750, 130));
//		shlBudgeteer.setSize(1104, 804);
		shlBudgeteer.setMaximized(true);
		shlBudgeteer.setText("Budgeteer");
		shlBudgeteer.setLayout(new GridLayout(3, false));
		
		Menu menu = new Menu(shlBudgeteer, SWT.BAR);
		shlBudgeteer.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlBudgeteer, SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.csv", "*.txt"});
				dialog.setFilterPath(System.getProperty("user.home"));
				try {
					tc = new TransactionsController(new CSVToTransactions(dialog.open()).data_to_transactions());
					setTableValues(tc.getTransactions());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmOpen.setText("Open");
		
		MenuItem mntmShareBudget = new MenuItem(menu_1, SWT.NONE);
		mntmShareBudget.setText("Share Budget");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.setText("Save");
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.setText("Exit");
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		
		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);
		
		MenuItem mntmCopy = new MenuItem(menu_2, SWT.NONE);
		mntmCopy.setText("Copy");
		
		MenuItem mntmCut = new MenuItem(menu_2, SWT.NONE);
		mntmCut.setText("Cut");
		
		MenuItem mntmPaste = new MenuItem(menu_2, SWT.NONE);
		mntmPaste.setText("Paste");
		
		MenuItem mntmDelete = new MenuItem(menu_2, SWT.NONE);
		mntmDelete.setText("Delete");
		
		MenuItem mntmBudget = new MenuItem(menu, SWT.CASCADE);
		mntmBudget.setText("Budget");
		
		Menu menu_3 = new Menu(mntmBudget);
		mntmBudget.setMenu(menu_3);
		
		MenuItem mntmNewBudget = new MenuItem(menu_3, SWT.NONE);
		mntmNewBudget.setText("New Budget");
		
		MenuItem mntmOpenBudget = new MenuItem(menu_3, SWT.NONE);
		mntmOpenBudget.setText("Open Budget");
		
		MenuItem mntmExportBudget = new MenuItem(menu_3, SWT.NONE);
		mntmExportBudget.setText("Export Budget");
		
		Composite composite = new Composite(shlBudgeteer, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_composite.minimumHeight = 120;
		gd_composite.minimumWidth = 228;
		gd_composite.widthHint = 226;
		composite.setLayoutData(gd_composite);
		
		Label lblOverview = new Label(composite, SWT.NONE);
		lblOverview.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblOverview.setText("Overview");
		new Label(composite, SWT.NONE);
		
		Label lblBalance = new Label(composite, SWT.NONE);
		lblBalance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBalance.setText("Balance");
		
		balanceLbl = new Label(composite, SWT.CENTER);
		balanceLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		balanceLbl.setText("0");
		balanceLbl.setVisible(false);
		
		Label lblCount = new Label(composite, SWT.NONE);
		lblCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCount.setText("Count");
		
		countLbl = new Label(composite, SWT.CENTER);
		countLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		countLbl.setText("0");
		countLbl.setVisible(false);
		
		Label lblFilters = new Label(composite, SWT.NONE);
		lblFilters.setText("Filters");
		lblFilters.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		new Label(composite, SWT.NONE);
		
		Label lblCategories = new Label(composite, SWT.NONE);
		lblCategories.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblCategories.setText("Categories");
		
		categoriesList = new List(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		categoriesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				applyCategoryFilter(categoriesList.getSelection());
			}
		});
		GridData gd_listAccounts = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_listAccounts.heightHint = 134;
		gd_listAccounts.widthHint = 184;
		categoriesList.setLayoutData(gd_listAccounts);
		
		Label lblAccount = new Label(composite, SWT.NONE);
		lblAccount.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblAccount.setText("Accounts");
		
		listAccounts = new List(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_listAccounts2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_listAccounts2.heightHint = 126;
		gd_listAccounts2.widthHint = 120;
		listAccounts.setLayoutData(gd_listAccounts2);
		
		Label lblStartDate = new Label(composite, SWT.NONE);
		lblStartDate.setText("Start Date");
		new Label(composite, SWT.NONE);
		
		Label lblYear = new Label(composite, SWT.NONE);
		lblYear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblYear.setText("Year");
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMonth = new Label(composite, SWT.NONE);
		lblMonth.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMonth.setText("Month");
		
		Combo combo_1 = new Combo(composite, SWT.NONE);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDay = new Label(composite, SWT.NONE);
		lblDay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDay.setText("Day");
		
		Combo combo_2 = new Combo(composite, SWT.NONE);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEndDate = new Label(composite, SWT.NONE);
		lblEndDate.setText("End Date");
		new Label(composite, SWT.NONE);
		
		Label lblYear_1 = new Label(composite, SWT.NONE);
		lblYear_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblYear_1.setText("Year");
		
		Combo combo_3 = new Combo(composite, SWT.NONE);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Month");
		
		Combo combo_4 = new Combo(composite, SWT.NONE);
		combo_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Day");
		
		Combo combo_5 = new Combo(composite, SWT.NONE);
		combo_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnApplyFilters = new Button(composite, SWT.NONE);
		btnApplyFilters.setText("Apply Filters");
		new Label(composite, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_button.widthHint = 189;
		gd_button.heightHint = 39;
		gd_button.horizontalSpan = 2;
		btnApplyFilters.setLayoutData(gd_button);
		new Label(composite, SWT.NONE);
		
		table = new Table(shlBudgeteer, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.minimumWidth = 240;
		gd_table.minimumHeight = 120;
		gd_table.widthHint = 793;
		gd_table.heightHint = 738;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(37);
		tblclmnNewColumn.setText("Day");
		
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
		
		Composite composite_1 = new Composite(shlBudgeteer, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_composite_1.heightHint = 554;
		composite_1.setLayoutData(gd_composite_1);

	}
	
	private void applyCategoryFilter(String[] cats){
		java.util.List<Transaction> temp =  tc.getTransactions().getByCategories(cats);
		showFilteredValues(temp);
		this.balanceLbl.setText(String.format("%1$,.2f", tc.getBalanceForCategories(cats)));
		this.countLbl.setText(Integer.toString(temp.size()));
	}
	
	private void showFilteredValues(java.util.List<Transaction> trans){
		hiddenTable = table;
		table.removeAll();
		fillTable(trans, table);
	}
	
	private void setTableValues(Transactions trans){
		table.removeAll();
		fillTable(trans.getTransactions(), table);
		categories = tc.getTransactions().getCategories();
		accounts = tc.getTransactions().getAccounts();
		Arrays.sort(categories);
		Arrays.sort(accounts);
		categoriesList.setItems(categories);
		this.listAccounts.setItems(accounts);
		this.countLbl.setText(Integer.toString(trans.getTransactions().size()));
		this.countLbl.setVisible(true);
		this.balanceLbl.setText(String.format("%1$,.2f", tc.getCurrentBalance()));
		this.balanceLbl.setVisible(true);
	}
	
	private void fillTable(java.util.List<Transaction> trans, Table table){
		for(Transaction t : trans){
			if(!t.isSubTransaction()){
				TableItem row = new TableItem(table, SWT.NONE);
				row.setText(0, Integer.toString(t.getDate().get(Calendar.DAY_OF_MONTH)));
				row.setText(1, Integer.toString(t.getDate().get(Calendar.MONTH) + 1));
				row.setText(2, Integer.toString(t.getDate().get(Calendar.YEAR)));
				row.setText(3, t.getDescription());
				row.setText(4, Double.toString(t.getAmount()));
				row.setText(5, t.getCategory());
				row.setText(6,  t.getAccount());
				if(t.isCredit()){
					row.setText(7, "Credit");
				} else {
					row.setText(7, "Debit");
				}
				row.setData(t.getGUID());
			}
		}
	}
	
}
