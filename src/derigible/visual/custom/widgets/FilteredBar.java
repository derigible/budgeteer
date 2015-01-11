/**
 *
 */
package derigible.visual.custom.widgets;

import org.eclipse.swt.widgets.Composite;

import derigible.controller.abstracts.AbstractController;
import derigible.visual.custom.widgets.abstracts.SideBar;

/**
 * @author derigible
 *
 */
public class FilteredBar extends SideBar {

	public FilteredBar(Composite parent, int style, AbstractController ac) {
		super(parent, style, ac);
		setSectionLabel("Filtered View");
		setOverviewSection();
	}

	public FilteredBar(Composite parent, int style, AbstractController ac, String addition) {
		super(parent, style, ac);
		setSectionLabel("Filtered View: " + addition);
		setOverviewSection();
	}

	/* (non-Javadoc)
	 * @see derigible.visual.custom.widgets.abstracts.SideBar#setBaseValues()
	 */
	@Override
	public void setBaseValues() {
		// TODO Auto-generated method stub

	}

}
