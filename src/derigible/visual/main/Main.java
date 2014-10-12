package derigible.visual.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.xml.sax.SAXException;

import derigible.controller.TransactionsController;
import derigible.saves.Saved;
import derigible.saves.XMLBuilder;
import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transformations.CSVToTransactions;
import derigible.visual.custom.widgets.MainLeftBar;
import derigible.visual.custom.widgets.SideBar;
import derigible.visual.custom.widgets.TransactionsTable;
import derigible.visual.listeners.Listeners;

/**
 * Main driver for the program.
 *
 * @author marcphillips
 *
 */
public class Main {

	protected TransactionsController tc = null;
	protected Shell shell;
	private CTabFolder tableTabs;
	private CTabFolder leftTabs;
	private CTabItem allTransTableTab;
	private CTabItem tableOverview;
	private MainLeftBar overviewBarTab;
	private Display display;
	private File settingsFile;
	private XMLBuilder settingsXml = null;
	private TransactionsTable table;
	private ArrayDeque<Control> focusQueue = new ArrayDeque<Control>();
	private HashMap<String, Saved> saves = new HashMap<String, Saved>();


	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
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

	private void createContents() {
		shell = new Shell();
		shell.setSize(1178, 795);
		shell.setText("Budgeteer");
		shell.setLayout(new GridLayout(2, false));

		// Create these first to allow setting of data
		leftTabs = new CTabFolder(shell, SWT.NONE);

		tableTabs = new CTabFolder(shell, SWT.NONE);

		allTransTableTab = new CTabItem(tableTabs, SWT.NONE);

		tableOverview = new CTabItem(leftTabs, SWT.NONE);
		overviewBarTab = new MainLeftBar(leftTabs, SWT.NONE, new TransactionsController(new TList(new Transact[] {})));
		tableOverview.setControl(overviewBarTab);
		tableOverview.setText("Overview");
		overviewBarTab.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

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
					| SWT.FULL_SELECTION | SWT.MULTI, tc);
		}
		shell.addListener(SWT.Close, Listeners.createCloseListener(shell,
				saves, settingsXml, settingsFile));

		getMenu();
		leftTabs.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false,1, 1));
		tableTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));

		allTransTableTab.setShowClose(true);
		allTransTableTab.setText("Overview");
		allTransTableTab.setControl(table.getTable());
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
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
			table.setTC(tc);
			//			table = new TransactionsTable(tableTabs, SWT.BORDER
			//					| SWT.FULL_SELECTION | SWT.MULTI, tc);

			table.getTable().setHeaderVisible(true);
			table.getTable().setLinesVisible(true);
			overviewBarTab.setController(tc);
			setBaseValues(table, overviewBarTab);
			allTransTableTab.setControl(table.getTable());
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

	private void setBaseValues(TransactionsTable table, SideBar bar){
		table.fillTable();
		bar.setBaseValues();
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
				if ((s = saves.get(tc != null ? tc.getName() : null)) != null && !s.saved()) {
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
					if (name.charAt(0) != '0' && name.charAt(1) != '0' && name.charAt(2) != '0'
							&& name.charAt(3) != '0') {
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
				if (c != null && c.getData().toString().equalsIgnoreCase(tc.getName())) {
					VisualUpdater.save(shell, saves.get(tc.getName()), "transactions", "Save failed. Are you sad?");
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
