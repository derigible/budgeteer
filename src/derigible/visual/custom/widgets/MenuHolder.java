package derigible.visual.custom.widgets;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuHolder {

	private Shell shell;
	private HashMap<String, MenuItem> items = new HashMap<String, MenuItem>();

	public MenuHolder(Shell shell) {
		this.shell = shell;
	}

	public void addSelectionAdapter(String name, SelectionAdapter sa) {
		items.get(name).addSelectionListener(sa);
	}

	private Menu getMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		items.put(mntmFile.getText(), mntmFile);

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		// mntmOpen.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// Saved s = null;
		// if ((s = saves.get(tc != null ? tc.getName() : null)) != null
		// && !s.saved()) {
		// shell.getListeners(SWT.CLOSE)[0].handleEvent(null);
		// }
		// FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		// dialog.setFilterExtensions(new String[] { "*.csv", "*.txt" });
		// dialog.setFilterPath(System.getProperty("user.home"));
		// String path = dialog.open();
		// String[] split = path.split("\\" + File.separatorChar);
		// String name = null;
		// if (split.length > 1) {
		// name = split[split.length - 2];
		// // GUIDs start with four zeros at least
		// if (name.charAt(0) != '0' && name.charAt(1) != '0'
		// && name.charAt(2) != '0' && name.charAt(3) != '0') {
		// name = null;
		// }
		// }
		// setOverviewTable(path, name);
		// StringU.setTextOfNode(settings, "transactions", tc.getName());
		// tc.toggleSaved();
		// }
		// });
		mntmOpen.setText("Open");
		items.put(mntmOpen.getText(), mntmOpen);

		MenuItem mntmShareBudget = new MenuItem(menu_1, SWT.NONE);
		mntmShareBudget.setText("Share Budget");
		items.put(mntmShareBudget.getText(), mntmShareBudget);

		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		// mntmSave.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// if (focusQueue.isEmpty()) {
		// return;
		// }
		// Control c = null;
		// while (!focusQueue.isEmpty()) {
		// if ((c = focusQueue.removeLast()).getClass() == CTabFolder.class) {
		// if (c.getData() == null) {
		// continue;
		// } else {
		// break;
		// }
		// } else if (c.getClass() == Table.class) {
		// c = c.getParent();
		// if (c.getData() == null) {
		// continue;
		// } else {
		// break;
		// }
		// }
		// }
		// if (c.getData() == null) {
		// return;
		// }
		// if (c != null
		// && c.getData().toString()
		// .equalsIgnoreCase(tc.getName())) {
		// VisualUpdater.save(shell, saves.get(tc.getName()),
		// "transactions", "Save failed. Are you sad?");
		// StringU.setTextOfNode(settings, "transactions",
		// tc.getName());
		// }
		// }
		// });
		mntmSave.setText("Save");
		items.put(mntmSave.getText(), mntmSave);

		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.setText("Save as..");
		items.put(mntmSaveAs.getText(), mntmSaveAs);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		mntmExit.setText("Exit");
		items.put(mntmExit.getText(), mntmExit);

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		items.put(mntmEdit.getText(), mntmEdit);

		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);

		MenuItem mntmCopy = new MenuItem(menu_2, SWT.NONE);
		mntmCopy.setText("Copy");
		items.put(mntmCopy.getText(), mntmCopy);

		MenuItem mntmCut = new MenuItem(menu_2, SWT.NONE);
		mntmCut.setText("Cut");
		items.put(mntmCut.getText(), mntmCut);

		MenuItem mntmPaste = new MenuItem(menu_2, SWT.NONE);
		mntmPaste.setText("Paste");
		items.put(mntmPaste.getText(), mntmPaste);

		MenuItem mntmDelete = new MenuItem(menu_2, SWT.NONE);
		mntmDelete.setText("Delete");
		items.put(mntmDelete.getText(), mntmDelete);

		MenuItem mntmBudget = new MenuItem(menu, SWT.CASCADE);
		mntmBudget.setText("Budget");
		items.put(mntmBudget.getText(), mntmBudget);

		Menu menu_3 = new Menu(mntmBudget);
		mntmBudget.setMenu(menu_3);

		MenuItem mntmNewBudget = new MenuItem(menu_3, SWT.NONE);
		mntmNewBudget.setText("New Budget");
		items.put(mntmNewBudget.getText(), mntmNewBudget);

		MenuItem mntmOpenBudget = new MenuItem(menu_3, SWT.NONE);
		mntmOpenBudget.setText("Open Budget");
		items.put(mntmOpenBudget.getText(), mntmOpenBudget);

		MenuItem mntmExportBudget = new MenuItem(menu_3, SWT.NONE);
		mntmExportBudget.setText("Export Budget");
		items.put(mntmExportBudget.getText(), mntmExportBudget);

		return menu;
	}
}
