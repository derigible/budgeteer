package derigible.visual.main;

import java.util.ArrayDeque;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import derigible.controller.TransactionsController;
import derigible.saves.Saved;
import derigible.visual.custom.widgets.TableTab;

public class VisualDriver {

    private final String LEFT_BAR = "left_bar";

    private TransactionsController tc = null;
    private TreeMap<String, TableTab> tranTabs = new TreeMap<String, TableTab>();
    private TreeMap<String, Saved> saves = new TreeMap<String, Saved>();
    private ArrayDeque<Control> focusQueue = new ArrayDeque<Control>();
    private Shell shell;

    public VisualDriver(Shell shell) {
	this.shell = shell;
	shell.setSize(1178, 795);
	shell.setText("Budgeteer");
	shell.setLayout(new GridLayout(2, false));
	CTabFolder leftBar = new CTabFolder(shell, SWT.NONE);

    }

    // +++++++++++++++++++++++++
    // Getter/Setter methods

    /**
     * @return the tc
     */
    public TransactionsController getTransC() {
	return tc;
    }

    /**
     * @param tc
     *            the tc to set
     */
    public void setTransC(TransactionsController tc) {
	this.tc = tc;
    }

    /**
     * @return the tabs
     */
    public TreeMap<String, TableTab> getTabs() {
	return tranTabs;
    }

    /**
     * @param tabs
     *            the tabs to set
     */
    public void setTabs(TreeMap<String, TableTab> tabs) {
	this.tranTabs = tabs;
    }

    /**
     * @return the saves
     */
    public TreeMap<String, Saved> getSaves() {
	return saves;
    }

    /**
     * @param saves
     *            the saves to set
     */
    public void setSaves(TreeMap<String, Saved> saves) {
	this.saves = saves;
    }

    /**
     * @return the focusQueue
     */
    public ArrayDeque<Control> getFocusQueue() {
	return focusQueue;
    }

    /**
     * @param focusQueue
     *            the focusQueue to set
     */
    public void setFocusQueue(ArrayDeque<Control> focusQueue) {
	this.focusQueue = focusQueue;
    }
}
