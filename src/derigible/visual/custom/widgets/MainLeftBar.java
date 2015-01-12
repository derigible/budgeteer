

package derigible.visual.custom.widgets;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
	public StringFilter getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @return the accountsList
	 */
	public StringFilter getAccountsList() {
		return accountsList;
	}

	/**
	 * @return the date2
	 */
	public Dates getDate2() {
		return date2;
	}

	/**
	 * @param date1 the date1 to set
	 */
	public Dates getDate1() {
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
		applyFilters(tableTabs, (CTabFolder) this.getParent());
	}

	public TransactionsTable applyFilters(final CTabFolder tableTabs, final CTabFolder sidebarTabs){
		final CTabItem overviewControl = new CTabItem(sidebarTabs, SWT.NONE);
		final FilteredBar sidebar = new FilteredBar(sidebarTabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, this.getController());

		overviewControl.setControl(sidebar);
		overviewControl.setText("Filtered View");
		overviewControl.setShowClose(true);

		final CTabItem tableControl = new CTabItem(tableTabs, SWT.NONE);
		tableControl.setShowClose(true);
		tableControl.setText("Filtered View");
		TransactionsTable ftt = new TransactionsTable(tableTabs, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, this.getController());
		ftt.getTable().setHeaderVisible(true);
		ftt.getTable().setLinesVisible(true);
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

		tableControl.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				CTabItem control = (CTabItem) tableControl.getData("control");
				if(!control.isDisposed()){
					control.dispose();
				}
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

		overviewControl.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				CTabItem control = (CTabItem) overviewControl.getData("table");
				if(!control.isDisposed()){
					try{
						control.dispose();
					} catch(IllegalArgumentException e){
						System.out.println("Typical error for closing not sure how to prevent.");
					}
				}
			}
		});

		//Set the values
		String[] cats = this.categoriesList.getSelectedFilters();
		String[] acts = this.accountsList.getSelectedFilters();
		boolean d1 = this.date1.getChecked();
		boolean d2 = this.date2.getChecked();
		double balance = 0;
		int count = 0;

		if(cats.length > 0 && acts.length > 0 && d1 && d2){ //By Cats, Acts, and Dates
			count = ftt.fillTable(cats, acts, this.date1.getDate(), this.date2.getDate()).size();
			balance = this.getController().getBalanceForCategoriesForAccountsBetweenDates(cats, acts, this.date1.getDate().getTime(), this.date2.getDate().getTime());
		} else if(cats.length > 0 && acts.length == 0 && d1 && d2){ // By Cats and Dates
			try {
				count = ftt.fillTable(cats, Filter.CATEGORIES, this.date1.getDate(), this.date2.getDate()).size();
				balance = this.getController().getBalanceForCategoriesBetweenDates(cats, this.date1.getDate().getTime(), this.date2.getDate().getTime());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(cats.length == 0 && acts.length > 0 && d1 && d2){ //By acts and dates
			try {
				count = ftt.fillTable(acts, Filter.ACCOUNTS, this.date1.getDate(), this.date2.getDate()).size();
				balance = this.getController().getBalanceBetweenDatesForAccounts(acts, this.date1.getDate().getTime(), this.date2.getDate().getTime());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(cats.length == 0 && acts.length == 0 && d1 && d2){ // By Dates
			count = ftt.fillTable(this.date1.getDate(), this.date2.getDate()).size();
			balance = this.getController().getBalanceBetweenDates(this.date1.getDate().getTime(), this.date2.getDate().getTime());
		}


		else if(cats.length > 0 && acts.length > 0 && d1 && !d2){ //By Cats, Acts, and Date
			count = ftt.fillTable(cats, acts, this.date1.getDate()).size();
			balance = this.getController().getBalanceForCategoriesForAccountsBetweenDates(cats, acts, this.date1.getDate().getTime(), this.date1.getDate().getTime());
		} else if(cats.length > 0 && acts.length == 0 && d1 && !d2){ //By Cats and Date
			try {
				count = ftt.fillTable(cats, Filter.CATEGORIES, this.date1.getDate()).size();
				balance = this.getController().getBalanceForCategoriesBetweenDates(cats, this.date1.getDate().getTime(), this.date1.getDate().getTime());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(cats.length == 0 && acts.length > 0 && d1 && !d2){ //By Acts and Date
			try {
				count = ftt.fillTable(acts, Filter.ACCOUNTS, this.date1.getDate()).size();
				balance = this.getController().getBalanceBetweenDatesForAccounts(acts, this.date1.getDate().getTime(), this.date1.getDate().getTime());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(cats.length == 0 && acts.length == 0 && d1 && !d2){ //By Date
			count = ftt.fillTable(this.date1.getDate()).size();
			balance = this.getController().getBalanceBetweenDates(this.date1.getDate().getTime(), this.date1.getDate().getTime());
		}


		else if(cats.length > 0 && acts.length > 0 && !d1 && !d2){ //By Cats, Acts
			count = ftt.fillTable(cats, acts).size();
			balance = this.getController().getBalanceForCategoriesForAccounts(cats, acts);
		} else if(cats.length > 0 && acts.length == 0 && !d1 && !d2){ //By Cats
			try {
				count = ftt.fillTable(cats, Filter.CATEGORIES).size();
				balance = this.getController().getBalanceForCategories(cats);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(cats.length == 0 && acts.length > 0 && !d1 && !d2){ //By Acts
			try {
				count = ftt.fillTable(acts, Filter.ACCOUNTS).size();
				balance = this.getController().getBalanceForAccounts(acts);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Incorrect filter used.");
				e.printStackTrace();
			}
		}

		sidebar.setBalanceLbl(String.format("%1$,.2f", balance));
		sidebar.setCountLbl(Integer.toString(count));

		return ftt;
	}

}
