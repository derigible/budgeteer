/**
 *
 */
package derigible.visual.custom.widgets.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.SWTResourceManager;

import derigible.visual.filters.Filter;

/**
 * @author derigible
 *
 */
public class StringFilter{

	private List list;
	private String text;
	private String[] filters;
	private Filter filter;

	public StringFilter(Composite parent, String[] filters, Filter filter, String text) {
		this.text = text;
		this.filter = filter;

		Label filterLbl = new Label(parent, SWT.NONE);
		filterLbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		filterLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
		filterLbl.setText(text);

		this.list = new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);

		GridData list_gridData = new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1);
		list_gridData.heightHint = 120;
		list_gridData.widthHint = 240;
		list.setLayoutData(list_gridData);
		list.setVisible(true);
	}

	public StringFilter(Composite parent, String[] filters, Filter filter) {
		new StringFilter (parent, filters, filter, "");
	}

	public void addMouseListener(MouseAdapter mse){
		this.list.addMouseListener(mse);
	}

	/**
	 * @return the list
	 */
	public List getList() {
		return list;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the filters
	 */
	public String[] getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 *            the filters to set
	 */
	public void setFilters(String[] filters) {
		this.filters = filters;
		this.list.removeAll();
		this.list.setItems(filters);
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

}
