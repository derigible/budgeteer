package derigible.visual.main;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import swing2swt.layout.BoxLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

public class TransactionTable extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TransactionTable(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginBottom = 1;
		gridLayout.marginRight = 1;
		gridLayout.marginTop = 1;
		gridLayout.marginLeft = 1;
		setLayout(gridLayout);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
