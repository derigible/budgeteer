package derigible.visual.main;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
	private Combo year1;
	private Combo month1;
	private Combo day1;
	private Combo year2;
	private Combo month2;
	private Combo day2;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainLeftSideBar(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new GridLayout(2, false));

		Label lblOverview = new Label(this, SWT.NONE);
		lblOverview.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblOverview.setFont(SWTResourceManager
				.getFont("Segoe UI", 12, SWT.BOLD));
		lblOverview.setText("Overview");
		new Label(this, SWT.NONE);

		Label lblBalance = new Label(this, SWT.NONE);
		lblBalance.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblBalance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblBalance.setText("Balance");

		setBalanceLbl(new Label(this, SWT.CENTER));
		getBalanceLbl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		getBalanceLbl().setText("0");
		getBalanceLbl().setVisible(false);

		Label lblCount = new Label(this, SWT.NONE);
		lblCount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblCount.setText("Count");

		setCountLbl(new Label(this, SWT.CENTER));
		getCountLbl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		getCountLbl().setText("0");
		getCountLbl().setVisible(false);

		Label lblFilters = new Label(this, SWT.NONE);
		lblFilters.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFilters.setText("Filters");
		lblFilters
				.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		new Label(this, SWT.NONE);

		Label lblCategories = new Label(this, SWT.NONE);
		lblCategories.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblCategories.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		lblCategories.setText("Categories");

		setCategoriesList(new List(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI));

		GridData gd_listAccounts = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		gd_listAccounts.heightHint = 134;
		gd_listAccounts.widthHint = 184;
		getCategoriesList().setLayoutData(gd_listAccounts);

		Label lblAccount = new Label(this, SWT.NONE);
		lblAccount.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblAccount.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));
		lblAccount.setText("Accounts");

		setListAccounts(new List(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI));
		GridData gd_listAccounts2 = new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1);
		gd_listAccounts2.heightHint = 126;
		gd_listAccounts2.widthHint = 120;
		getListAccounts().setLayoutData(gd_listAccounts2);

		Label lblStartDate = new Label(this, SWT.NONE);
		lblStartDate
				.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblStartDate.setText("Start Date");
		new Label(this, SWT.NONE);

		Label lblYear = new Label(this, SWT.NONE);
		lblYear.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblYear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblYear.setText("Year");

		year1 = new Combo(this, SWT.NONE);
		year1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label lblMonth = new Label(this, SWT.NONE);
		lblMonth.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMonth.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblMonth.setText("Month");

		month1 = new Combo(this, SWT.NONE);
		month1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label lblDay = new Label(this, SWT.NONE);
		lblDay.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblDay.setText("Day");

		day1 = new Combo(this, SWT.NONE);
		day1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblEndDate = new Label(this, SWT.NONE);
		lblEndDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblEndDate.setText("End Date");
		new Label(this, SWT.NONE);

		Label lblYear_1 = new Label(this, SWT.NONE);
		lblYear_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblYear_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblYear_1.setText("Year");

		year2 = new Combo(this, SWT.NONE);
		year2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label label = new Label(this, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("Month");

		month2 = new Combo(this, SWT.NONE);
		month2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("Day");

		day2 = new Combo(this, SWT.NONE);
		day2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnApplyFilters = new Button(this, SWT.NONE);
		btnApplyFilters.setText("Apply Filters");
		new Label(this, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true, false, 1,
				1);
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
	 * @param balanceLbl
	 *            the balanceLbl to set
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
	 * @param countLbl
	 *            the countLbl to set
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
	 * @param categoriesList
	 *            the categoriesList to set
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
	 * @param listAccounts
	 *            the listAccounts to set
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
	 * @param categories
	 *            the categories to set
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
	 * @param accounts
	 *            the accounts to set
	 */
	protected void setAccounts(String[] accounts) {
		Arrays.sort(accounts);
		this.accounts = accounts;
	}

	/**
	 * @return the year1
	 */
	protected int getYear1() {
		return Integer.parseInt(getYear1Comp().getText());
	}

	/**
	 * @param year1
	 *            the year1 to set
	 */
	protected void setYear1(int year1) {
		this.getYear1Comp().setText(Integer.toString(year1));
	}

	protected void setYear1Years(String[] years) {
		this.getYear1Comp().setItems(years);
	}

	/**
	 * @return the month1
	 */
	protected int getMonth1() {
		return Integer.parseInt(month1.getText());
	}

	/**
	 * @param month1
	 *            the month1 to set
	 */
	protected void setMonth1(int month1) {
		this.month1.setText(Integer.toString(month1));
	}

	protected void setMonth1Months(int[] months) {
		for (int i = 0; i < months.length; i++) {
			months[i] = months[i] + 1;
		}
		this.month1.setItems(this.intArrayToStringArray(months));
	}

	/**
	 * @return the day1
	 */
	protected int getDay1() {
		return Integer.parseInt(day1.getText());
	}

	/**
	 * @param day1
	 *            the day1 to set
	 */
	protected void setDay1(int day1) {
		this.day1.setText(Integer.toString(day1));
	}

	protected void setDay1Days(String[] days) {
		this.day1.setItems(days);
	}

	/**
	 * @return the year2
	 */
	protected int getYear2() {
		return Integer.parseInt(year2.getText());
	}

	/**
	 * @param year2
	 *            the year2 to set
	 */
	protected void setYear2(int year2) {
		this.year2.setText(Integer.toString(year2));
	}

	protected void setYear2Years(int[] years, int year) {
		int[] temp = new int[years.length - year];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = years[i + year];
		}
		this.year2.setItems(this.intArrayToStringArray(temp));
	}

	/**
	 * @return the month2
	 */
	protected int getMonth2() {
		return Integer.parseInt(month2.getText());
	}

	/**
	 * @param month2
	 *            the month2 to set
	 */
	protected void setMonth2(int month2) {
		this.month2.setText(Integer.toString(month2));
	}

	protected void setMonth2Months(int[] months, int month, int year1, int year2) {
		if (year1 == year2) {
			int[] temp = new int[months.length - month];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = months[i + month] + 1;
			}
			months = temp;
		} else {
			for (int i = 0; i < months.length; i++) {
				months[i] = months[i] + 1;
			}
		}
		this.month2.setItems(this.intArrayToStringArray(months));
	}

	/**
	 * @return the day2
	 */
	protected int getDay2() {
		return Integer.parseInt(day2.getText());
	}

	/**
	 * @param day2
	 *            the day2 to set
	 */
	protected void setDay2(int day2) {
		this.day2.setText(Integer.toString(day2));
	}

	protected void setDay2Days(int[] days, int day, boolean yearsEqual,
			boolean monthsEqual) {
		if (yearsEqual && monthsEqual) {
			int[] temp = new int[days.length - day];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = days[i + day];
			}
			days = temp;
		}
		this.day2.setItems(this.intArrayToStringArray(days));
	}

	/**
	 * @return the year1
	 */
	protected Combo getYear1Comp() {
		return year1;
	}

	/**
	 * @param year1
	 *            the year1 to set
	 */
	protected void setYear1Comp(Combo year1) {
		this.year1 = year1;
	}

	public String[] intArrayToStringArray(int[] ints) {
		Arrays.sort(ints);
		String[] temp = new String[ints.length];
		for (int i = 0; i < ints.length; i++) {
			temp[i] = Integer.toString(ints[i]);
		}
		return temp;
	}

	/**
	 * @return the month1
	 */
	protected Combo getMonth1Comp() {
		return month1;
	}

	/**
	 * @param month1
	 *            the month1 to set
	 */
	protected void setMonth1Comp(Combo month1) {
		this.month1 = month1;
	}

	/**
	 * @return the day1
	 */
	protected Combo getDay1Comp() {
		return day1;
	}

	/**
	 * @param day1
	 *            the day1 to set
	 */
	protected void setDay1Comp(Combo day1) {
		this.day1 = day1;
	}

	/**
	 * @return the year2
	 */
	protected Combo getYear2Comp() {
		return year2;
	}

	/**
	 * @param year2
	 *            the year2 to set
	 */
	protected void setYear2Comp(Combo year2) {
		this.year2 = year2;
	}

	/**
	 * @return the month2
	 */
	protected Combo getMonth2Comp() {
		return month2;
	}

	/**
	 * @param month2
	 *            the month2 to set
	 */
	protected void setMonth2Comp(Combo month2) {
		this.month2 = month2;
	}

	/**
	 * @return the day2
	 */
	protected Combo getDay2Comp() {
		return day2;
	}

	/**
	 * @param day2
	 *            the day2 to set
	 */
	protected void setDay2Comp(Combo day2) {
		this.day2 = day2;
	}
}
