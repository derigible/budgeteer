package derigible.visual.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import derigible.controller.TransactionsController;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.CSVToTransactions;
import org.eclipse.wb.swt.SWTResourceManager;

public class TabbedOverview {

	protected TransactionsController tc = null;
	protected Shell shell;
	private Table table;
	private MainLeftSideBar leftBar;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TabbedOverview window = new TabbedOverview();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1178, 795);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(2, false));
		
		getMenu();
		
		CTabFolder leftTabs = new CTabFolder(shell, SWT.NONE);
		leftTabs.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		
		CTabItem DataOverview = new CTabItem(leftTabs, SWT.NONE);
		DataOverview.setText("Overview");
		
		
		leftBar = new MainLeftSideBar(leftTabs, SWT.NONE);
		leftBar.getBalanceLbl().setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		leftBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		leftBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		DataOverview.setControl(leftBar);
		
		CTabFolder tableTabs = new CTabFolder(shell, SWT.NONE);
		tableTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableTabs.setSimple(false);
		tableTabs.setUnselectedCloseVisible(false);
		
		CTabItem Overview = new CTabItem(tableTabs, SWT.NONE);
		Overview.setText("Overview");
		
		table = new TransactionsTable(tableTabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI).getTable();
		
		Overview.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		leftTabs.setSelection(0);

	}
	
	private void applyCategoryFilter(String[] cats){
		java.util.List<Transaction> temp =  tc.getTransactions().getByCategories(cats);
		showFilteredValues(temp);
		leftBar.getBalanceLbl().setText(String.format("%1$,.2f", tc.getBalanceForCategories(cats)));
		leftBar.getCountLbl().setText(Integer.toString(temp.size()));
	}
	
	private void showFilteredValues(java.util.List<Transaction> trans){
//		hiddenTable = table;
		table.removeAll();
		fillTable(trans, table);
	}
	
	private void setTableValues(Transactions trans){
		table.removeAll();
		fillTable(trans.getTransactions(), table);
		leftBar.setCategories(tc.getTransactions().getCategories());
		leftBar.setAccounts(tc.getTransactions().getAccounts());
		leftBar.getCategoriesList().setItems(leftBar.getCategories());
		leftBar.getCategoriesList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				applyCategoryFilter(leftBar.getCategoriesList().getSelection());
			}
		});
		leftBar.getListAccounts().setItems(leftBar.getAccounts());
		leftBar.getCountLbl().setText(Integer.toString(trans.getTransactions().size()));
		leftBar.getCountLbl().setVisible(true);
		leftBar.getBalanceLbl().setText(String.format("%1$,.2f", tc.getCurrentBalance()));
		leftBar.getBalanceLbl().setVisible(true);
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
	
	private Menu getMenu(){
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
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
		
		return menu;
	}
}
