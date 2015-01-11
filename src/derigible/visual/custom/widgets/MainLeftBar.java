

package derigible.visual.custom.widgets;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import derigible.controller.TransactionsController;
import derigible.visual.custom.widgets.abstracts.SideBar;
import derigible.visual.custom.widgets.data.Dates;
import derigible.visual.custom.widgets.data.StringFilter;
import derigible.visual.filters.Filter;

/**
 * @author derigible
 *
 * Note that this sidebar is meant for transactions only, not for budgets.
 */
public class MainLeftBar extends SideBar {
	private StringFilter categoriesList;
	private StringFilter accountsList;
	private Dates date1;
	private Dates date2;

	/**
	 * @param parent
	 * @param style
	 */
	public MainLeftBar(Composite parent, int style, TransactionsController ac) {
		super(parent, style, ac);
		setSectionLabel("Overview");
		setOverviewSection();
		setSectionLabel("Filters");
		this.categoriesList = setStringFilterSelection("Categories", Filter.CATEGORIES );
		this.accountsList = setStringFilterSelection("Accounts", Filter.ACCOUNTS);
		date1 = this.setDateSelection(true, "Start Date");
		date2 = this.setDateSelection(true, "End Date ");
		date1.setDate2(date2);
		date1.addYearSelectionListener(date2);
	}

	/**
	 * @return the categoriesList
	 */
	protected StringFilter getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @return the accountsList
	 */
	protected StringFilter getAccountsList() {
		return accountsList;
	}

	/**
	 * @return the date2
	 */
	protected Dates getDate2() {
		return date2;
	}

	/**
	 * @param date1 the date1 to set
	 */
	protected Dates getDate1() {
		return date1;
	}

	@Override
	public TransactionsController getController(){
		return (TransactionsController) super.getController();
	}

	@Override
	public void setBaseValues() {
		if(this.getController() == null){
			return; //Do nothing, ac not set
		}
		date1.update(this.getController());
		String[] cats = this.getController().getTransactions().getCategories();
		Arrays.sort(cats);
		String[] acts = this.getController().getTransactions().getAccounts();
		Arrays.sort(cats);
		Arrays.sort(acts);
		categoriesList.setFilters(cats);
		accountsList.setFilters(acts);
		this.setBalanceLbl(String.format("%1$,.2f",this.getController().getCurrentBalance()));
		this.setCountLbl(Integer.toString(this.getController().getTransactions().getTransactions().size()));
		this.setButton("Filter");
	}

	public void applyFilters(final CTabFolder tableTabs){
		final CTabItem overviewControl = new CTabItem((CTabFolder) this.getParent(), SWT.NONE);
		final FilteredBar fb = new FilteredBar(this.getParent(), SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, this.getController());
		fb.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

		overviewControl.setControl(fb);
		overviewControl.setText("Filtered View");

		CTabItem tableControl = new CTabItem(tableTabs, SWT.NONE);
		tableControl.setShowClose(true);
		tableControl.setText("Filtered View");
		TransactionsTable ftt = new TransactionsTable(tableTabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, this.getController());
		ftt.getTable().setHeaderVisible(true);
		ftt.getTable().setLinesVisible(true);
		try {
			ftt.fillTable(new String[]{"Wedding Ring"}, Filter.CATEGORIES);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tableControl.setControl(ftt.getTable());

		//Set each entity in relation to the other
		tableControl.setData("control", overviewControl);
		overviewControl.setData("table", tableControl);

		//Set the table tab on focus to select the corresponding overview tab
		final CTabFolder overviewTabs = (CTabFolder) this.getParent();
		tableTabs.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				overviewTabs.setSelection((CTabItem) arg0.item.getData("control"));
			}
		});

		//Set the overview tab on focus to select the corresponding table tab
		overviewTabs.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tableTabs.setSelection((CTabItem) arg0.item.getData("table"));
			}
		});

	}

	public TransactionsTable applyFilters(CTabFolder tabletabs, CTabFolder sidebar){
		return new TransactionsTable(tabletabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, this.getController());
	}

}
