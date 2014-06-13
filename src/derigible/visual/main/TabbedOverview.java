package derigible.visual.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.w3c.dom.Element;

import derigible.controller.AbstractController;
import derigible.controller.TransactionsController;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.CSVToTransactions;
import derigible.utils.Saved;
import derigible.utils.StringU;
import derigible.utils.TListener;

public class TabbedOverview {

    protected TransactionsController tc = null;
    protected Shell shell;
    private Table table;
    private MainLeftSideBar leftBar;
    private CTabFolder tableTabs;
    private LinkedList<Table> tables = new LinkedList<Table>();
    private CTabItem overview;
    private TreeMap<String, TListener> listeners = new TreeMap<String, TListener>();
    private HashMap<String, Saved> saves = new HashMap<String, Saved>();

    /**
     * Launch the application.
     * 
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
	Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
	    @Override
	    public void run() {
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
	});

    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
	shell = new Shell();
	shell.setSize(1178, 795);
	shell.setText("SWT Application");
	shell.setLayout(new GridLayout(2, false));
	shell.addListener(SWT.Close,
		VisualUpdater.addCloseListener(shell, saves));

	getMenu();

	CTabFolder leftTabs = new CTabFolder(shell, SWT.NONE);
	leftTabs.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
		1, 1));

	CTabItem DataOverview = new CTabItem(leftTabs, SWT.NONE);
	DataOverview.setText("Overview");

	leftBar = new MainLeftSideBar(leftTabs, SWT.NONE);
	leftBar.getBalanceLbl().setBackground(
		SWTResourceManager.getColor(SWT.COLOR_WHITE));
	leftBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
		1));
	leftBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

	DataOverview.setControl(leftBar);

	tableTabs = new CTabFolder(shell, SWT.NONE);
	tableTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		1));
	tableTabs.setSimple(false);
	tableTabs.setUnselectedCloseVisible(false);

	overview = new CTabItem(tableTabs, SWT.NONE);
	overview.setShowClose(true);
	overview.setText("Overview");

	table = new TransactionsTable(tableTabs, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();

	File file = new File(System.getProperty("user.home")
		+ "/Budgeteer/settings.xml");
	if (file.exists()) {
	    Element dom = StringU.stringToXML(file);
	    if (dom != null) {
		String transactions;
		if ((transactions = StringU
			.getTextFromNode(dom, "transactions")) != null) {
		    setOverviewTable(System.getProperty("user.home")
			    + "/Budgeteer/" + transactions
			    + "/transactions.csv");
		}
	    }
	}

	overview.setControl(table);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	leftTabs.setSelection(0);
	tableTabs.setSelection(0);
    }

    protected void applyFilter(Filter filter) {
	String[] cats;
	String[] acts;
	GregorianCalendar gc1;
	GregorianCalendar gc2;
	double bal = 0;
	switch (filter) {
	case CATEGORIES:
	    cats = leftBar.getCategoriesList().getSelection();
	    applyFilter(tc.getTransactions().getByCategories(cats),
		    tc.getBalanceForCategories(cats));
	    break;
	case ACCOUNTS:
	    acts = leftBar.getListAccounts().getSelection();
	    bal = 0;
	    for (String act : acts) {
		bal += tc.getCurrentBalanceForAccount(act);
	    }
	    applyFilter(tc.getTransactions().getByAccounts(acts), bal);
	    break;
	case DATE:
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    applyFilter(tc.getTransactions().getByDate(gc1.getTime()),
		    tc.getBalanceBetweenDates(gc1.getTime(), gc1.getTime()));
	    break;
	case DATES:
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    gc2 = new GregorianCalendar(leftBar.getYear2(),
		    leftBar.getMonth2() - 1, leftBar.getDay2());
	    applyFilter(tc.getTransactions().getByDate(gc1.getTime()),
		    tc.getBalanceBetweenDates(gc1.getTime(), gc2.getTime()));
	    break;
	case CATEGORIES_AND_DATE:
	    cats = leftBar.getCategoriesList().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    applyFilter(
		    tc.getTransactions().getByCategoriesAndDate(cats,
			    gc1.getTime()),
		    tc.getBalanceForCategoriesBetweenDates(cats, gc1.getTime(),
			    gc1.getTime()));
	    break;
	case CATEGORIES_AND_DATES:
	    cats = leftBar.getCategoriesList().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    gc2 = new GregorianCalendar(leftBar.getYear2(),
		    leftBar.getMonth2() - 1, leftBar.getDay2());
	    applyFilter(
		    tc.getTransactions().getByCategoriesAndDate(cats,
			    gc1.getTime()),
		    tc.getBalanceForCategoriesBetweenDates(cats, gc1.getTime(),
			    gc2.getTime()));
	    break;
	case ACCOUNTS_AND_DATE:
	    acts = leftBar.getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc1.getTime(), act);
	    }
	    applyFilter(
		    tc.getTransactions().filterByDate(gc1.getTime(),
			    tc.getTransactions().getByAccounts(acts)), bal);
	    break;
	case ACCOUNTS_AND_DATES:
	    acts = leftBar.getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    gc2 = new GregorianCalendar(leftBar.getYear2(),
		    leftBar.getMonth2() - 1, leftBar.getDay2());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc2.getTime(), act);
	    }
	    applyFilter(
		    tc.getTransactions().filterByDates(gc1.getTime(),
			    gc2.getTime(),
			    tc.getTransactions().getByAccounts(acts)), bal);
	    break;
	case CATEGORIES_AND_ACCOUNTS_AND_DATE:
	    cats = leftBar.getCategoriesList().getSelection();
	    acts = leftBar.getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc1.getTime(), act);
	    }
	    applyFilter(
		    tc.getTransactions().filterByAccounts(
			    acts,
			    tc.getTransactions().getByCategoriesAndDate(cats,
				    gc1.getTime())),
		    bal
			    + tc.getBalanceForCategoriesBetweenDates(cats,
				    gc1.getTime(), gc1.getTime()));
	    break;
	case CATEGORIES_AND_ACCOUNTS_AND_DATES:
	    cats = leftBar.getCategoriesList().getSelection();
	    acts = leftBar.getListAccounts().getSelection();
	    gc1 = new GregorianCalendar(leftBar.getYear1(),
		    leftBar.getMonth1() - 1, leftBar.getDay1());
	    gc2 = new GregorianCalendar(leftBar.getYear2(),
		    leftBar.getMonth2() - 1, leftBar.getDay2());
	    for (String act : acts) {
		bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
			gc2.getTime(), act);
	    }
	    applyFilter(
		    tc.getTransactions().filterByAccounts(
			    acts,
			    tc.getTransactions().getByCategoriesAndDates(cats,
				    gc1.getTime(), gc2.getTime())),
		    bal
			    + tc.getBalanceForCategoriesBetweenDates(cats,
				    gc1.getTime(), gc2.getTime()));
	    break;
	}
	leftBar.getListAccounts().deselectAll();
	leftBar.getCategoriesList().deselectAll();
    }

    private void applyFilter(java.util.List<Transaction> temp, double balance) {
	showFilteredValues(temp);
	leftBar.getBalanceLbl().setText(String.format("%1$,.2f", balance));
	leftBar.getCountLbl().setText(Integer.toString(temp.size()));
    }

    private void showFilteredValues(java.util.List<Transaction> trans) {
	CTabItem next = new CTabItem(tableTabs, SWT.NONE);
	Table newtable = new TransactionsTable(tableTabs, SWT.BORDER
		| SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();
	tables.add(newtable);
	next.setText("Filtered View " + tables.size());
	next.setControl(newtable);
	next.setShowClose(true);
	newtable.setHeaderVisible(true);
	newtable.setLinesVisible(true);
	fillTable(trans, newtable, tc);
	tableTabs.setSelection(next);
    }

    private void setBaseValues(Transactions trans, Table table0) {
	table0.removeAll();
	fillTable(trans.getTransactions(), table0, tc);
	leftBar.setCategories(tc.getTransactions().getCategories());
	leftBar.setAccounts(tc.getTransactions().getAccounts());
	leftBar.getCategoriesList().setItems(leftBar.getCategories());
	leftBar.getCategoriesList().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseDoubleClick(MouseEvent e) {
		applyFilter(Filter.CATEGORIES);
	    }
	});
	leftBar.getListAccounts().setItems(leftBar.getAccounts());
	leftBar.getListAccounts().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseDoubleClick(MouseEvent e) {
		applyFilter(Filter.ACCOUNTS);
	    }
	});
	leftBar.getCountLbl().setText(
		Integer.toString(trans.getTransactions().size()));
	leftBar.getCountLbl().setVisible(true);
	leftBar.getBalanceLbl().setText(
		String.format("%1$,.2f", tc.getCurrentBalance()));
	leftBar.getBalanceLbl().setVisible(true);
	leftBar.setYear1Years(leftBar.intArrayToStringArray(tc
		.getTransactions().getYearsWithTransactions()));
	leftBar.getYear1Comp().select(0);
	leftBar.getYear1Comp().addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		leftBar.setMonth1Months(tc.getTransactions()
			.getMonthsInYearWithTransactions(leftBar.getYear1()));
		leftBar.setYear2Years(tc.getTransactions()
			.getYearsWithTransactions(), leftBar.getYear1Comp()
			.getSelectionIndex());
		leftBar.getMonth1Comp().select(0);
	    }
	});
	leftBar.getYear2Comp().addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (!leftBar.getMonth1Comp().getText().isEmpty()) {
		    leftBar.setMonth2Months(
			    tc.getTransactions()
				    .getMonthsInYearWithTransactions(
					    leftBar.getYear2()), leftBar
				    .getMonth1Comp().getSelectionIndex(),
			    leftBar.getYear1(), leftBar.getYear2());
		}
	    }
	});
	leftBar.getMonth1Comp().addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		leftBar.setDay1Days(leftBar.intArrayToStringArray(tc
			.getTransactions()
			.getDaysInMonthInYearWithTransactions(
				leftBar.getYear1(), leftBar.getMonth1())));
		leftBar.getDay1Comp().select(0);
	    }
	});
	leftBar.getMonth2Comp().addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		leftBar.setDay2Days(
			tc.getTransactions()
				.getDaysInMonthInYearWithTransactions(
					leftBar.getYear2(), leftBar.getMonth2()),
			leftBar.getDay1Comp().getSelectionIndex(), (leftBar
				.getYear1() == leftBar.getYear2()), (leftBar
				.getMonth1() == leftBar.getMonth2()));
	    }
	});
    }

    private void fillTable(java.util.List<Transaction> trans,
	    final Table table, AbstractController ac) {
	for (Transaction t : trans) {
	    if (!t.isSubTransaction()) {
		TableItem row = new TableItem(table, SWT.NONE);

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
		TListener tl = new TListener(t, ac);
		tl.addTableItem(row);
		listeners.put(t.getGUID(), new TListener(t, ac));
	    }
	}
    }

    private void setOverviewTable(String filename) {
	try {
	    tc = new TransactionsController(
		    new CSVToTransactions(filename).data_to_transactions());
	    table = new TransactionsTable(tableTabs, SWT.BORDER
		    | SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();

	    overview.setControl(table);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    setBaseValues(tc.getTransactions(), table);
	    saves.put(tc.getName(), tc);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (NullPointerException e1) {
	    // Do nothing, they canceled.
	}
    }

    private Menu getMenu() {
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
		dialog.setFilterExtensions(new String[] { "*.csv", "*.txt" });
		dialog.setFilterPath(System.getProperty("user.home"));
		setOverviewTable(dialog.open());
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
