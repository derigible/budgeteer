/**
 *
 */
package derigible.visual.custom.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import derigible.controller.AbstractController;
import derigible.visual.custom.widgets.data.Dates;
import derigible.visual.custom.widgets.data.StringFilter;
import derigible.visual.filters.Filter;

/**
 * @author derigible
 *
 */
public abstract class SideBar extends Composite {

	private AbstractController ac;
	private Button btn;
	private Label balanceLbl;
	private Label countLbl;

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public SideBar(Composite parent, int style, AbstractController ac) {
		super(parent, style);

		this.setLayout(new GridLayout(2, false));
	}

	public Composite setOverviewSection(){
		Composite container = new Composite(this, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setVisible(true);

		Label lblBalance = new Label(container, SWT.NONE);
		lblBalance.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblBalance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,false, 1, 1));
		lblBalance.setText("Balance");

		balanceLbl = new Label(container, SWT.CENTER);
		balanceLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		balanceLbl.setText("0");
		balanceLbl.setVisible(true);

		Label lblCount = new Label(container, SWT.NONE);
		lblCount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,false, 1, 1));
		lblCount.setText("Count");

		countLbl = new Label(container, SWT.CENTER);
		countLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		countLbl.setText("0");
		countLbl.setVisible(true);

		return this;
	}

	public Label setSectionLabel(String text){
		Label lblFilters = new Label(this, SWT.NONE);
		lblFilters.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblFilters.setText(text);
		lblFilters.setFont(SWTResourceManager.getFont("Segoe UI", 12,
				SWT.BOLD));
		return lblFilters;
	}

	public StringFilter setStringFilterSelection(String text, Filter filter){
		Composite container = new Composite(this, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setVisible(true);

		String[] filters = null;
		if(filter == Filter.ACCOUNTS){
			try{
				filters = ac.getTransactions().getAccounts();
			} catch(NullPointerException e){
				//				e.printStackTrace();
				//TODO replace with logger
				filters = new String[] {};
			}
		} else if(filter == Filter.CATEGORIES) {
			try{
				filters = ac.getTransactions().getCategories();
			} catch(NullPointerException e){
				//				e.printStackTrace();
				//TODO replace with logger
				filters = new String[] {};
			}
		} else {
			filters = new String[] {};
			//Fails silently if filter is not correct
			return new StringFilter(container, filters, Filter.CATEGORIES, text);
		}

		return new StringFilter(container, filters, filter, text);
	}

	public Dates setDateSelection(boolean isSelectable, String text){
		Composite container = new Composite(this, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setVisible(true);

		Label spacer = new Label(container, SWT.NONE);
		spacer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		spacer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		spacer.setText("");

		Label lblYear = new Label(container, SWT.NONE);
		lblYear.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblYear.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblYear.setText("Year");

		Label lblMonth = new Label(container, SWT.NONE);
		lblMonth.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMonth.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblMonth.setText("Month");

		Label lblDay = new Label(container, SWT.NONE);
		lblDay.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDay.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblDay.setText("Day");

		return new Dates(container, ac, isSelectable, text);
	}

	public SideBar setButton(String text){
		btn = new Button(this, SWT.NONE);
		btn.setText(text);
		new Label(this, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true,
				false, 1, 1);
		gd_button.widthHint = 189;
		gd_button.heightHint = 39;
		gd_button.horizontalSpan = 2;
		btn.setLayoutData(gd_button);

		return this;
	}

	protected AbstractController getController(){

		return this.ac;
	}

	public void setController(AbstractController ac){
		this.ac = ac;
	}

	/**
	 * @param balanceLbl
	 *            the balanceLbl to set
	 */
	public void setBalanceLbl(String balanceLbl) {
		this.balanceLbl.setText(balanceLbl);
	}

	/**
	 * @param countLbl
	 *            the countLbl to set
	 */
	public void setCountLbl(String countLbl) {
		this.countLbl.setText(countLbl);
	}

	@Override
	public void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public abstract void setBaseValues();


	//	public void applyFilters(){
	//
	//	}
}
