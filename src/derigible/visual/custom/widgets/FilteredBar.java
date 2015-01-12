/**
 *
 */
package derigible.visual.custom.widgets;

import org.eclipse.swt.widgets.Composite;

import derigible.controller.TransactionsController;
import derigible.visual.custom.widgets.abstracts.SideBar;

/**
 * @author derigible
 *
 */
public class FilteredBar extends SideBar {

	public FilteredBar(Composite parent, int style, TransactionsController ac) {
		super(parent, style, ac);
		setSectionLabel("Filtered View");
		setOverviewSection();
	}

	public FilteredBar(Composite parent, int style, TransactionsController ac, String addition) {
		super(parent, style, ac);
		setSectionLabel("Filtered View: " + addition);
		setOverviewSection();
	}

	@Override
	public TransactionsController getController(){
		return (TransactionsController) super.getController();
	}

	/* (non-Javadoc)
	 * @see derigible.visual.custom.widgets.abstracts.SideBar#setBaseValues()
	 */
	@Override
	public void setBaseValues() {
		this.setBalanceLbl(String.format("%1$,.2f",this.getController().getCurrentBalance()));
		this.setCountLbl(Integer.toString(this.getController().getTransactions().getTransactions().size()));
		this.setButton("Filter");
	}

}
