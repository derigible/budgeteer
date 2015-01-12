package derigible.visual.custom.widgets.data;

import java.util.Arrays;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import derigible.controller.abstracts.AbstractController;

public class Dates {

	final Combo year;
	final Combo month;
	final Combo day;
	private int[] years;
	private int[] months;
	private int[] days;
	private boolean isSelectable;
	private AbstractController ac;
	private Composite parent;
	private Dates date2 = null;
	private Button check = null;

	public Dates(Composite parent, AbstractController ac, boolean isSelectable, String text) {
		this.ac = ac;
		this.parent = parent;
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lbl.setText(text);
		this.year = new Combo(parent, SWT.NONE);
		this.month = new Combo(parent, SWT.NONE);
		this.day = new Combo(parent, SWT.NONE);
		init();

		this.isSelectable = isSelectable;
		if (!isSelectable) {
			this.year.setEnabled(false);
			this.month.setEnabled(false);
			this.day.setEnabled(false);
		} else {
			this.addMonthSelectionListener();
		}
		try {
			this.years = ac.getTransactions().getYearsWithTransactions();
		} catch (NullPointerException e) {
			this.years = new int[] {};
		}
	}

	private void init() {
		GridData ygrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		ygrid.widthHint = 35;
		this.year.setLayoutData(ygrid);
		GridData mgrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		mgrid.widthHint = 45;
		this.month.setLayoutData(mgrid);
		GridData dgrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		dgrid.widthHint = 25;
		this.day.setLayoutData(dgrid);
	}

	public void setDate2(Dates date2) {
		this.date2 = date2;
	}

	protected Composite getParent() {
		return this.parent;
	}

	private void selectYear(final Dates date2){
		setMonths(ac.getTransactions().getMonthsInYearWithTransactions(Integer.parseInt(year.getText())));
		this.setDays(ac.getTransactions().getDaysInMonthInYearWithTransactions(Integer.parseInt(year.getText()),
				months[month.getSelectionIndex()] - 1));
		int y = year.getSelectionIndex();
		int[] temp = new int[years.length - y];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = years[i + y];
		}
		if(date2 != null){
			date2.setYears(temp);
			date2.addYearSelectionListener();
		}
	}

	public void addYearSelectionListener(final Dates date2) {
		if (!this.isSelectable) {
			return; // Fail silently if year selection is disabled.
		}
		this.year.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectYear(date2);
			}
		});
	}

	public void addYearSelectionListener() {
		if (!this.isSelectable) {
			return; // Fail silently if year selection is disabled.
		}
		this.year.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMonths(ac.getTransactions().getMonthsInYearWithTransactions(Integer.parseInt(year.getText())));
			}
		});
	}

	public GregorianCalendar getDate() {
		int y = Integer.parseInt(this.year.getText());
		int m = this.months[month.getSelectionIndex()] - 1;
		int d = 0;
		try{
			d = Integer.parseInt(this.day.getText());
		} catch(NumberFormatException e){
			//TODO replace this with making day always selected
			d = 1;
		}
		return new GregorianCalendar(y, m, d);
	}

	protected AbstractController getController() {
		return this.ac;
	}

	protected void setController(AbstractController ac) {
		this.ac = ac;
		if (date2 != null) {
			date2.setController(ac);
		}
	}

	public void update() {
		if (date2 != null) {
			date2.clearAll();
		}
		this.setYears(ac.getTransactions().getYearsWithTransactions());
	}

	protected void clearAll() {
		this.year.removeAll();
		this.month.removeAll();
		this.day.removeAll();
	}

	public void update(AbstractController ac) {
		this.setController(ac);
		this.update();
	}

	/**
	 * @param years
	 *            the years to set
	 */
	public void setYears(int[] years) {
		this.month.removeAll();
		this.day.removeAll();
		this.years = years;
		this.year.setItems(intArrayToStringArray(this.years));
		this.year.select(0);
		this.selectYear(date2);
	}

	public boolean getChecked(){
		return check.getSelection();
	}

	public void setCheckBox(Button check){
		this.check = check;
	}

	private void addMonthSelectionListener() {
		this.month.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDays(ac.getTransactions().getDaysInMonthInYearWithTransactions(Integer.parseInt(year.getText()),
						months[month.getSelectionIndex()] - 1));
				if(date2 != null && Integer.parseInt(date2.year.getText()) == Integer.parseInt(year.getText())){
					date2.month.select(month.getSelectionIndex());
					date2.day.select(day.getSelectionIndex());
				}
			}
		});
	}

	/**
	 * @param months
	 *            the months to set
	 */
	//TODO figure out a way to make the dates synch with another
	void setMonths(int[] months) {
		for (int i = 0; i < months.length; i++) {
			months[i] = months[i] + 1;
		}
		this.months = months;
		this.month.setItems(monthsToStringArray(this.months));
		this.month.select(0);
		this.day.removeAll();
	}

	/**
	 * @param days
	 *            the days to set
	 */
	private void setDays(int[] days) {
		this.days = days;
		this.day.setItems(intArrayToStringArray(this.days));
		this.day.select(0);
	}

	private String[] intArrayToStringArray(int[] ints) {
		Arrays.sort(ints);
		String[] temp = new String[ints.length];
		for (int i = 0; i < ints.length; i++) {
			temp[i] = Integer.toString(ints[i]);
		}
		return temp;
	}

	private String[] monthsToStringArray(int[] months) {
		Arrays.sort(months);
		String[] temp = new String[months.length];
		for (int i = 0; i < months.length; i++) {
			switch (months[i]) {
			case (1):
				temp[i] = "1 - Jan";
			break;
			case (2):
				temp[i] = "2 - Feb";
			break;
			case (3):
				temp[i] = "3 - Mar";
			break;
			case (4):
				temp[i] = "4 - Apr";
			break;
			case (5):
				temp[i] = "5 - May";
			break;
			case (6):
				temp[i] = "6 - Jun";
			break;
			case (7):
				temp[i] = "7 - Jul";
			break;
			case (8):
				temp[i] = "8 - Aug";
			break;
			case (9):
				temp[i] = "9 - Sep";
			break;
			case (10):
				temp[i] = "10 - Oct";
			break;
			case (11):
				temp[i] = "11 - Nov";
			break;
			case (12):
				temp[i] = "12 - Dec";
			break;
			default:
				temp[i] = "-1 - ??";
				break;
			}
		}
		return temp;
	}
}
