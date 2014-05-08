package derigible.visual.main;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainLeftSideBar extends Composite {

	private Label balanceLbl;
	private Label countLbl;
	private List categoriesList;
	private List listAccounts;
	private String[] categories;
	private String[] accounts;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainLeftSideBar(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new GridLayout(2, false));
		
		
		Label lblOverview = new Label(this, SWT.NONE);
		lblOverview.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblOverview.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblOverview.setText("Overview");
		new Label(this, SWT.NONE);
		
		Label lblBalance = new Label(this, SWT.NONE);
		lblBalance.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblBalance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBalance.setText("Balance");
		
		setBalanceLbl(new Label(this, SWT.CENTER));
		getBalanceLbl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		getBalanceLbl().setText("0");
		getBalanceLbl().setVisible(false);
		
		Label lblCount = new Label(this, SWT.NONE);
		lblCount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCount.setText("Count");
		
		setCountLbl(new Label(this, SWT.CENTER));
		getCountLbl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		getCountLbl().setText("0");
		getCountLbl().setVisible(false);
		
		Label lblFilters = new Label(this, SWT.NONE);
		lblFilters.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFilters.setText("Filters");
		lblFilters.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		new Label(this, SWT.NONE);
		
		Label lblCategories = new Label(this, SWT.NONE);
		lblCategories.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCategories.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblCategories.setText("Categories");
		
		setCategoriesList(new List(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI));
		
		GridData gd_listAccounts = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_listAccounts.heightHint = 134;
		gd_listAccounts.widthHint = 184;
		getCategoriesList().setLayoutData(gd_listAccounts);
		
		Label lblAccount = new Label(this, SWT.NONE);
		lblAccount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblAccount.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblAccount.setText("Accounts");
		
		setListAccounts(new List(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI));
		GridData gd_listAccounts2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_listAccounts2.heightHint = 126;
		gd_listAccounts2.widthHint = 120;
		getListAccounts().setLayoutData(gd_listAccounts2);
		
		Label lblStartDate = new Label(this, SWT.NONE);
		lblStartDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblStartDate.setText("Start Date");
		new Label(this, SWT.NONE);
		
		Label lblYear = new Label(this, SWT.NONE);
		lblYear.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblYear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblYear.setText("Year");
		
		Combo combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMonth = new Label(this, SWT.NONE);
		lblMonth.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMonth.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMonth.setText("Month");
		
		Combo combo_1 = new Combo(this, SWT.NONE);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDay = new Label(this, SWT.NONE);
		lblDay.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDay.setText("Day");
		
		Combo combo_2 = new Combo(this, SWT.NONE);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEndDate = new Label(this, SWT.NONE);
		lblEndDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblEndDate.setText("End Date");
		new Label(this, SWT.NONE);
		
		Label lblYear_1 = new Label(this, SWT.NONE);
		lblYear_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblYear_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblYear_1.setText("Year");
		
		Combo combo_3 = new Combo(this, SWT.NONE);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(this, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Month");
		
		Combo combo_4 = new Combo(this, SWT.NONE);
		combo_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Day");
		
		Combo combo_5 = new Combo(this, SWT.NONE);
		combo_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnApplyFilters = new Button(this, SWT.NONE);
		btnApplyFilters.setText("Apply Filters");
		new Label(this, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_button.widthHint = 189;
		gd_button.heightHint = 39;
		gd_button.horizontalSpan = 2;
		btnApplyFilters.setLayoutData(gd_button);
		new Label(this, SWT.NONE);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the balanceLbl
	 */
	protected Label getBalanceLbl() {
		return balanceLbl;
	}

	/**
	 * @param balanceLbl the balanceLbl to set
	 */
	protected void setBalanceLbl(Label balanceLbl) {
		this.balanceLbl = balanceLbl;
	}

	/**
	 * @return the countLbl
	 */
	protected Label getCountLbl() {
		return countLbl;
	}

	/**
	 * @param countLbl the countLbl to set
	 */
	protected void setCountLbl(Label countLbl) {
		this.countLbl = countLbl;
	}

	/**
	 * @return the categoriesList
	 */
	protected List getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @param categoriesList the categoriesList to set
	 */
	protected void setCategoriesList(List categoriesList) {
		this.categoriesList = categoriesList;
	}

	/**
	 * @return the listAccounts
	 */
	protected List getListAccounts() {
		return listAccounts;
	}

	/**
	 * @param listAccounts the listAccounts to set
	 */
	protected void setListAccounts(List listAccounts) {
		this.listAccounts = listAccounts;
	}

	/**
	 * @return the categories
	 */
	protected String[] getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	protected void setCategories(String[] categories) {
		Arrays.sort(categories);
		this.categories = categories;
	}

	/**
	 * @return the accounts
	 */
	protected String[] getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	protected void setAccounts(String[] accounts) {
		Arrays.sort(accounts);
		this.accounts = accounts;
	}

}
