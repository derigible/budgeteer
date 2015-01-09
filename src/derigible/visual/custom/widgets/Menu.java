package derigible.visual.custom.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Menu extends org.eclipse.swt.widgets.Menu {

	public Menu(Shell parent, int swtNumber) {
		super(parent, swtNumber);
		parent.setMenuBar(this);
	}

	public Menu(org.eclipse.swt.widgets.MenuItem mi){
		super(mi);
		mi.setMenu(this);
	}

	public MenuItem addMenuItem(String text){
		MenuItem item = new MenuItem(this, SWT.NONE);
		item.setText(text);
		return item;
	}

	public MenuItem addMenuItem(String text, int swtNumber){
		MenuItem item = new MenuItem(this, swtNumber);
		item.setText(text);
		return item;
	}

	public Menu addMenuToMenuItem(MenuItem mi){
		return new Menu(mi);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
