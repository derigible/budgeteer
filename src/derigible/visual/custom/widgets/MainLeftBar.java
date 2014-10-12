

package derigible.visual.custom.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import derigible.controller.TransactionsController;
import derigible.visual.custom.widgets.data.Dates;
import derigible.visual.custom.widgets.data.StringFilter;
import derigible.visual.filters.Filter;

/**
 * @author derigible
 *
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

		new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		setSectionLabel("Overview");
		setOverviewSection();
		setSectionLabel("Filters");
		this.categoriesList =  setStringFilterSelection("Categories", Filter.CATEGORIES );
		this.accountsList =  setStringFilterSelection("Accounts", Filter.ACCOUNTS);
		date1 = this.setDateSelection(true, "Start Date");
		date2 = this.setDateSelection(true, "End Date");
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
		categoriesList.setFilters(this.getController().getTransactions().getCategories());
		accountsList.setFilters(this.getController().getTransactions().getAccounts());
		this.setBalanceLbl(String.format("%1$,.2f",this.getController().getCurrentBalance()));
		this.setCountLbl(Integer.toString(this.getController().getTransactions().getTransactions().size()));
	}

}
