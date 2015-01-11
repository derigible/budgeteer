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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
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
import derigible.visual.custom.widgets.Menu;
import derigible.visual.custom.widgets.TransactionsTable;
import derigible.visual.custom.widgets.abstracts.SideBar;
import derigible.visual.utils.Actions;
import derigible.visual.utils.Listeners;

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
	private CTabItem tableOverviewTab;
	private MainLeftBar overviewBar;
	private Display display;
	private File settingsFile;
	private XMLBuilder settingsXml = null;
	private TransactionsTable allTransTable;
	private final ArrayDeque<Control> focusQueue = new ArrayDeque<Control>();
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

		/// Create TabFolders ///
		leftTabs = new CTabFolder(shell, SWT.NONE);
		leftTabs.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		tableTabs = new CTabFolder(shell, SWT.NONE);
		tableTabs.setSimple(false);
		tableTabs.setUnselectedCloseVisible(false);
		tableTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		/// Create default TabFolderItem Containers ///
		allTransTableTab = new CTabItem(tableTabs, SWT.NONE);
		allTransTableTab.setShowClose(false);
		allTransTableTab.setText("Overview");

		tableOverviewTab = new CTabItem(leftTabs, SWT.NONE);

		/// Create default TabFolderItem ///
		overviewBar = new MainLeftBar(leftTabs, SWT.NONE, new TransactionsController(new TList(new Transact[] {})));
		overviewBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

		allTransTable = new TransactionsTable(tableTabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, tc);
		allTransTable.getTable().setHeaderVisible(true);
		allTransTable.getTable().setLinesVisible(true);

		/// Set TabFolderItem Containers to control tab folder item ///
		tableOverviewTab.setControl(overviewBar);
		tableOverviewTab.setText("Overview");

		allTransTableTab.setControl(allTransTable.getTable());

		//Set the overview tab to this table
		allTransTableTab.setData("control", tableOverviewTab);
		tableOverviewTab.setData("table", allTransTableTab);

		/// Get the Menu ///
		getMenu();

		/// Get any settings ///
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
				setOverviewTable(System.getProperty("user.home") + "/Budgeteer/" + transactions + "/transactions.csv",
						transactions);
			}
		} else {
			settingsFile.mkdir();
			settingsFile = new File(settingsFile.getAbsolutePath() + "/settings.xml");
			try {
				settingsFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		shell.addListener(SWT.Close, Listeners.createCloseListener(shell, saves, settingsXml, settingsFile));

		/// Set the initial selection

		leftTabs.setSelection(0);
		tableTabs.setSelection(0);
	}

	private void setOverviewTable(String filename, String name) {
		File f = new File(filename);
		if (!f.exists())
			return;
		try {
			if (name == null) {
				tc = new TransactionsController(new CSVToTransactions(f).data_to_transactions());
			} else {
				tc = new TransactionsController(new CSVToTransactions(f).data_to_transactions(), name);
			}
			if(tc != null){
				tableTabs.setData(tc.getName());
				allTransTable.setTC(tc);
				overviewBar.setController(tc);
				setBaseValues(allTransTable, overviewBar);
				overviewBar.setButtonListener(new MouseListener(){
					@Override
					public void mouseDoubleClick(MouseEvent arg0) {
						//do nothing
					}

					@Override
					public void mouseDown(MouseEvent arg0) {
						//do nothing
					}

					@Override
					public void mouseUp(MouseEvent arg0) {
						overviewBar.applyFilters(tableTabs);
					}
				});
				allTransTableTab.setControl(allTransTable.getTable());
				saves.put(tc.getName(), tc);
				settingsXml.setTextOfNode("transactions", tc.getName());
			}
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

	private void setBaseValues(TransactionsTable table, SideBar bar) {
		table.fillTable();
		bar.setBaseValues();
	}

	private void getMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		/**** File Menu *****/
		MenuItem fileMenuHead = menu.addMenuItem("File", SWT.CASCADE);
		Menu fileMenu = menu.addMenuToMenuItem(fileMenuHead);

		fileMenu.addMenuItem("Open").addSelectionListener(new SelectionAdapter() {
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
				if(path != null){
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
			}
		});

		fileMenu.addMenuItem("Share Budget");
		fileMenu.addMenuItem("Save").addSelectionListener(new SelectionAdapter() {
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
					Actions.save(shell, saves.get(tc.getName()), "transactions", "Save failed. Are you sad?");
					settingsXml.setTextOfNode("transactions", tc.getName());
				}
			}
		});

		fileMenu.addMenuItem("Save as..");

		fileMenu.addMenuItem("Exit").addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

		/**** End File Menu *****/

		/**** Edit Menu *****/

		MenuItem editMenuHead = menu.addMenuItem("Edit", SWT.CASCADE);

		Menu editMenu = menu.addMenuToMenuItem(editMenuHead);
		editMenu.addMenuItem("Copy");
		editMenu.addMenuItem("Cut");
		editMenu.addMenuItem("Paste");
		editMenu.addMenuItem("Delete");

		/**** End Edit Menu *****/

		/**** Budget Menu *****/

		MenuItem budgetMenuHead = menu.addMenuItem("Budget", SWT.CASCADE);
		Menu budgetMenu = menu.addMenuToMenuItem(budgetMenuHead);
		budgetMenu.addMenuItem("New Budget");
		budgetMenu.addMenuItem("Open Budget");
		budgetMenu.addMenuItem("Export Budget");

		/**** End Budget Menu *****/
	}

}
