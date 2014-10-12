package derigible.visual.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.xml.sax.SAXException;

import derigible.controller.AbstractController;
import derigible.controller.TransactionsController;
import derigible.saves.Saved;
import derigible.saves.XMLBuilder;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import derigible.transformations.CSVToTransactions;
import derigible.utils.TListener;
import derigible.visual.custom.widgets.OverviewBar;
import derigible.visual.custom.widgets.TransactionsTable;
import derigible.visual.filters.Filter;
import derigible.visual.listeners.Listeners;

public class TabbedOverview {

	protected TransactionsController tc = null;
	protected Shell shell;
	private Table table;
	private OverviewBar leftBar;
	private CTabFolder tableTabs;
	private LinkedList<Table> tables = new LinkedList<Table>();
	private CTabItem overview;
	private TreeMap<String, TListener> listeners = new TreeMap<String, TListener>();
	private HashMap<String, Saved> saves = new HashMap<String, Saved>();
	private Display display;
	private ArrayDeque<Control> focusQueue = new ArrayDeque<Control>();
	private File settingsFile;
	private XMLBuilder settingsXml = null;
	CTabFolder leftTabs;

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
		display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			@Override
			public void run() {
				display = Display.getDefault();
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
		shell.setText("Budgeteer");
		shell.setLayout(new GridLayout(2, false));

		// Create these first to allow setting of data
		leftTabs = new CTabFolder(shell, SWT.NONE);
		leftBar = new OverviewBar(leftTabs, SWT.NONE, "Overview");
		tableTabs = new CTabFolder(shell, SWT.NONE);
		overview = new CTabItem(tableTabs, SWT.NONE);
		//
		settingsFile = new File(System.getProperty("user.home") + "/Budgeteer");
		File check = new File(settingsFile.getAbsolutePath() + "/settings.xml");
		if (settingsFile.exists() && check.exists()) {
			settingsFile = check;
			try {
				settingsXml = new XMLBuilder(settingsFile);
			} catch (org.xml.sax.SAXException sax) {
				try {
					settingsXml = new XMLBuilder("settings");
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String transactions;
			if ((transactions = settingsXml.getTextFromNode("transactions")) != null) {
				setOverviewTable(System.getProperty("user.home")
						+ "/Budgeteer/" + transactions + "/transactions.csv",
						transactions);
			}

		} else {
			settingsFile.mkdir();
			settingsFile = new File(settingsFile.getAbsolutePath()
					+ "/settings.xml");
			try {
				settingsFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (table == null) {
			table = new TransactionsTable(tableTabs, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();
		}
		shell.addListener(SWT.Close, Listeners.createCloseListener(shell,
				saves, settingsXml, settingsFile));

		getMenu();

		leftTabs.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,
				1, 1));

		leftBar.getLsb().getBalanceLbl()
		.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		leftBar.getLsb().setLayoutData(
				new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		leftBar.getLsb().setBackground(
				SWTResourceManager.getColor(SWT.COLOR_WHITE));

		tableTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tableTabs.setSimple(false);
		tableTabs.setUnselectedCloseVisible(false);

		overview.setShowClose(true);
		overview.setText("Overview");

		for (Control c : shell.getChildren()) {
			Listeners.addFocusListener(c, focusQueue);
		}

		overview.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		leftTabs.setSelection(0);
		tableTabs.setSelection(0);
	}

	protected void addFocusListener(final Control c) {
		c.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				if (focusQueue.size() > 10) {
					focusQueue.removeFirst();
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				focusQueue.push(c);
			}

		});
	}

	protected void applyFilter(Filter filter) {
		String[] cats;
		String[] acts;
		GregorianCalendar gc1;
		GregorianCalendar gc2;
		int day1;
		int month1;
		int year1;
		int day2;
		int month2;
		int year2;
		TreeMap<String, Object> filters = new TreeMap<String, Object>();
		double bal = 0;
		switch (filter) {
		case CATEGORIES:
			cats = leftBar.getLsb().getCategoriesList().getSelection();
			filters.put("categories", cats);
			applyFilter(tc.getTransactions().getByCategories(cats),
					tc.getBalanceForCategories(cats), filters);
			break;
		case ACCOUNTS:
			acts = leftBar.getLsb().getListAccounts().getSelection();
			bal = 0;
			for (String act : acts) {
				bal += tc.getCurrentBalanceForAccount(act);
			}
			filters.put("accounts", acts);
			applyFilter(tc.getTransactions().getByAccounts(acts), bal, filters);
			break;
		case DATE:
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			TreeMap<String, Integer> date = new TreeMap<String, Integer>();
			date.put("day1", day1);
			date.put("month1", month1);
			date.put("year1", year1);
			filters.put("dates", date);
			applyFilter(tc.getTransactions().getByDate(gc1.getTime()),
					tc.getBalanceBetweenDates(gc1.getTime(), gc1.getTime()), filters);
			break;
		case DATES:
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			day2 = leftBar.getLsb().getDay2();
			month2 = leftBar.getLsb().getMonth2() - 1;
			year2 = leftBar.getLsb().getYear2();
			gc2 = new GregorianCalendar(year2, month2, day2);
			TreeMap<String, Integer> dates = new TreeMap<String, Integer>();
			dates.put("day1", day1);
			dates.put("month1", month1);
			dates.put("year1", year1);
			dates.put("day2", day2);
			dates.put("month2", month2);
			dates.put("year2", year2);
			filters.put("dates", dates);
			applyFilter(tc.getTransactions().getByDate(gc1.getTime()),
					tc.getBalanceBetweenDates(gc1.getTime(), gc2.getTime()), filters);
			break;
		case CATEGORIES_AND_DATE:
			cats = leftBar.getLsb().getCategoriesList().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			TreeMap<String, Integer> dateAndCats = new TreeMap<String, Integer>();
			dateAndCats.put("day1", day1);
			dateAndCats.put("month1", month1);
			dateAndCats.put("year1", year1);
			filters.put("categories", cats);
			filters.put("dates", dateAndCats);
			applyFilter(
					tc.getTransactions().getByCategoriesAndDate(cats,
							gc1.getTime()),
							tc.getBalanceForCategoriesBetweenDates(cats, gc1.getTime(),
									gc1.getTime()), filters);
			break;
		case CATEGORIES_AND_DATES:
			cats = leftBar.getLsb().getCategoriesList().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			day2 = leftBar.getLsb().getDay2();
			month2 = leftBar.getLsb().getMonth2() - 1;
			year2 = leftBar.getLsb().getYear2();
			gc2 = new GregorianCalendar(year2, month2, day2);
			TreeMap<String, Integer> datesAndCats = new TreeMap<String, Integer>();
			datesAndCats.put("day1", day1);
			datesAndCats.put("month1", month1);
			datesAndCats.put("year1", year1);
			datesAndCats.put("day2", day2);
			datesAndCats.put("month2", month2);
			datesAndCats.put("year2", year2);
			filters.put("categories", cats);
			filters.put("dates", datesAndCats);
			applyFilter(
					tc.getTransactions().getByCategoriesAndDate(cats,
							gc1.getTime()),
							tc.getBalanceForCategoriesBetweenDates(cats, gc1.getTime(),
									gc2.getTime()), filters);
			break;
		case ACCOUNTS_AND_DATE:
			acts = leftBar.getLsb().getListAccounts().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			TreeMap<String, Integer> dateAndActs = new TreeMap<String, Integer>();
			dateAndActs.put("day1", day1);
			dateAndActs.put("month1", month1);
			dateAndActs.put("year1", year1);
			filters.put("accounts", acts);
			filters.put("dates", dateAndActs);
			for (String act : acts) {
				bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
						gc1.getTime(), act);
			}
			applyFilter(
					tc.getTransactions().filterByDate(gc1.getTime(),
							tc.getTransactions().getByAccounts(acts)), bal, filters);
			break;
		case ACCOUNTS_AND_DATES:
			acts = leftBar.getLsb().getListAccounts().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			day2 = leftBar.getLsb().getDay2();
			month2 = leftBar.getLsb().getMonth2() - 1;
			year2 = leftBar.getLsb().getYear2();
			gc2 = new GregorianCalendar(year2, month2, day2);
			TreeMap<String, Integer> datesAndActs = new TreeMap<String, Integer>();
			datesAndActs.put("day1", day1);
			datesAndActs.put("month1", month1);
			datesAndActs.put("year1", year1);
			datesAndActs.put("day2", day2);
			datesAndActs.put("month2", month2);
			datesAndActs.put("year2", year2);
			filters.put("accounts", acts);
			filters.put("dates", datesAndActs);
			for (String act : acts) {
				bal += tc.getBalanceBetweenDatesForAccount(gc1.getTime(),
						gc2.getTime(), act);
			}
			applyFilter(
					tc.getTransactions().filterByDates(gc1.getTime(),
							gc2.getTime(),
							tc.getTransactions().getByAccounts(acts)), bal, filters);
			break;
		case CATEGORIES_AND_ACCOUNTS_AND_DATE:
			cats = leftBar.getLsb().getCategoriesList().getSelection();
			acts = leftBar.getLsb().getListAccounts().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			TreeMap<String, Integer> dateAndActsAndCats = new TreeMap<String, Integer>();
			dateAndActsAndCats.put("day1", day1);
			dateAndActsAndCats.put("month1", month1);
			dateAndActsAndCats.put("year1", year1);
			filters.put("accounts", acts);
			filters.put("categories", cats);
			filters.put("dates", dateAndActsAndCats);
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
											gc1.getTime(), gc1.getTime()), filters);
			break;
		case CATEGORIES_AND_ACCOUNTS_AND_DATES:
			cats = leftBar.getLsb().getCategoriesList().getSelection();
			acts = leftBar.getLsb().getListAccounts().getSelection();
			day1 = leftBar.getLsb().getDay1();
			month1 = leftBar.getLsb().getMonth1() - 1;
			year1 = leftBar.getLsb().getYear1();
			gc1 = new GregorianCalendar(year1, month1, day1);
			day2 = leftBar.getLsb().getDay2();
			month2 = leftBar.getLsb().getMonth2() - 1;
			year2 = leftBar.getLsb().getYear2();
			gc2 = new GregorianCalendar(year2, month2, day2);
			TreeMap<String, Integer> datesAndActsAndCats = new TreeMap<String, Integer>();
			datesAndActsAndCats.put("day1", day1);
			datesAndActsAndCats.put("month1", month1);
			datesAndActsAndCats.put("year1", year1);
			datesAndActsAndCats.put("day2", day2);
			datesAndActsAndCats.put("month2", month2);
			datesAndActsAndCats.put("year2", year2);
			filters.put("accounts", acts);
			filters.put("categories", cats);
			filters.put("dates", datesAndActsAndCats);
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
											gc1.getTime(), gc2.getTime()),
											filters);
			break;
		}
		leftBar.getLsb().getListAccounts().deselectAll();
		leftBar.getLsb().getCategoriesList().deselectAll();
	}

	private void applyFilter(java.util.List<Transaction> temp, double balance,
			TreeMap<String, Object> filters) {
		CTabItem next = showFilteredValues(temp);
		next.setData("amount", String.format("%1$,.2f", balance));
		next.setData("count", Integer.toString(temp.size()));
		next.setData("filters", filters);
		leftBar.getLsb().getBalanceLbl()
		.setText(next.getData("amount").toString());
		leftBar.getLsb().getCountLbl()
		.setText(next.getData("amount").toString());
	}

	private CTabItem showFilteredValues(java.util.List<Transaction> trans) {
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
		return next;
	}

	//	private void addFilteredTableFocusListener(final Widget table, final LeftSideBar leftBar){
	//		System.out.println("Adding the listener.");
	//		table.addListener(SWT.MouseDown, new Listener(){
	//
	//			@Override
	//			public void handleEvent(Event arg0) {
	//				System.out.println("Changing the leftBar");
	//				leftBar.getLsb().setCategories((String[]) table.getData("categories"));
	//
	//			}
	//
	//		});
	//	}

	private void setBaseValues(Transactions trans, Table table0) {
		table0.removeAll();
		fillTable(trans.getTransactions(), table0, tc);
		leftBar.getLsb().setCategories(tc.getTransactions().getCategories());
		leftBar.getLsb().setAccounts(tc.getTransactions().getAccounts());
		leftBar.getLsb().getCategoriesList()
		.setItems(leftBar.getLsb().getCategories());
		leftBar.getLsb().getCategoriesList()
		.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				applyFilter(Filter.CATEGORIES);
			}
		});
		leftBar.getLsb().getListAccounts()
		.setItems(leftBar.getLsb().getAccounts());
		leftBar.getLsb().getListAccounts().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				applyFilter(Filter.ACCOUNTS);
			}
		});
		leftBar.getLsb().getCountLbl()
		.setText(Integer.toString(trans.getTransactions().size()));
		leftBar.getLsb().getCountLbl().setVisible(true);
		leftBar.getLsb().getBalanceLbl()
		.setText(String.format("%1$,.2f", tc.getCurrentBalance()));
		leftBar.getLsb().getBalanceLbl().setVisible(true);
		leftBar.getLsb().setYear1Years(
				leftBar.getLsb().intArrayToStringArray(
						tc.getTransactions().getYearsWithTransactions()));
		leftBar.getLsb().getYear1Comp().select(0);
		leftBar.getLsb().getYear1Comp()
		.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				leftBar.getLsb().setMonth1Months(
						tc.getTransactions()
						.getMonthsInYearWithTransactions(
								leftBar.getLsb().getYear1()));
				leftBar.getLsb()
				.setYear2Years(
						tc.getTransactions()
						.getYearsWithTransactions(),
						leftBar.getLsb().getYear1Comp()
						.getSelectionIndex());
				leftBar.getLsb().getMonth1Comp().select(0);
			}
		});
		leftBar.getLsb().getYear2Comp()
		.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!leftBar.getLsb().getMonth1Comp().getText()
						.isEmpty()) {
					leftBar.getLsb()
					.setMonth2Months(
							tc.getTransactions()
							.getMonthsInYearWithTransactions(
									leftBar.getLsb()
									.getYear2()),
									leftBar.getLsb().getMonth1Comp()
									.getSelectionIndex(),
									leftBar.getLsb().getYear1(),
									leftBar.getLsb().getYear2());
				}
			}
		});
		leftBar.getLsb().getMonth1Comp()
		.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				leftBar.getLsb()
				.setDay1Days(
						leftBar.getLsb()
						.intArrayToStringArray(
								tc.getTransactions()
								.getDaysInMonthInYearWithTransactions(
										leftBar.getLsb()
										.getYear1(),
										leftBar.getLsb()
										.getMonth1())));
				leftBar.getLsb().getDay1Comp().select(0);
			}
		});
		leftBar.getLsb().getMonth2Comp()
		.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				leftBar.getLsb().setDay2Days(
						tc.getTransactions()
						.getDaysInMonthInYearWithTransactions(
								leftBar.getLsb().getYear2(),
								leftBar.getLsb().getMonth2()),
								leftBar.getLsb().getDay1Comp()
								.getSelectionIndex(),
								(leftBar.getLsb().getYear1() == leftBar
								.getLsb().getYear2()),
								(leftBar.getLsb().getMonth1() == leftBar
								.getLsb().getMonth2()));
			}
		});
	}

	private void fillTable(java.util.List<Transaction> trans,
			final Table table, AbstractController ac) {
		this.addFocusListener(table);
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
				// TODO determine if the below line is even necessary
				listeners.put(t.getGUID(), tl);
			}
		}
	}

	private void setOverviewTable(String filename, String name) {
		File f = new File(filename);
		if (!f.exists())
			return;
		try {
			if (name == null) {
				tc = new TransactionsController(
						new CSVToTransactions(f).data_to_transactions());
			} else {
				tc = new TransactionsController(
						new CSVToTransactions(f).data_to_transactions(), name);
			}
			tableTabs.setData(tc.getName());
			table = new TransactionsTable(tableTabs, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.MULTI, tc).getTable();

			overview.setControl(table);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			setBaseValues(tc.getTransactions(), table);
			saves.put(tc.getName(), tc);
			settingsXml.setTextOfNode("transactions", tc.getName());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			e1.printStackTrace();
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
				Saved s = null;
				if ((s = saves.get(tc != null ? tc.getName() : null)) != null
						&& !s.saved()) {
					shell.getListeners(SWT.CLOSE)[0].handleEvent(null);
				}
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.csv", "*.txt" });
				dialog.setFilterPath(System.getProperty("user.home"));
				String path = dialog.open();
				String[] split = path.split("\\" + File.separatorChar);
				String name = null;
				if (split.length > 1) {
					name = split[split.length - 2];
					// GUIDs start with four zeros at least
					if (name.charAt(0) != '0' && name.charAt(1) != '0'
							&& name.charAt(2) != '0' && name.charAt(3) != '0') {
						name = null; // Set name to null so we can keep the guid
						// already used by the transactions will
						// keep as the name
					}
				}
				setOverviewTable(path, name);
				settingsXml.setTextOfNode("transactions", tc.getName());
				tc.toggleSaved();
			}
		});
		mntmOpen.setText("Open");

		MenuItem mntmShareBudget = new MenuItem(menu_1, SWT.NONE);
		mntmShareBudget.setText("Share Budget");

		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (focusQueue.isEmpty()) {
					return;
				}
				Control c = null;
				while (!focusQueue.isEmpty()) {
					if ((c = focusQueue.removeLast()).getClass() == CTabFolder.class) {
						if (c.getData() != null) {
							break;
						}
					} else if (c.getClass() == Table.class) {
						c = c.getParent();
						if (c.getData() != null) {
							break;
						}
					}
				}
				if (c.getData() == null) {
					return;
				}
				if (c != null
						&& c.getData().toString()
						.equalsIgnoreCase(tc.getName())) {
					VisualUpdater.save(shell, saves.get(tc.getName()),
							"transactions", "Save failed. Are you sad?");
					settingsXml.setTextOfNode("transactions", tc.getName());
				}
			}
		});
		mntmSave.setText("Save");

		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.setText("Save as..");

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
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
