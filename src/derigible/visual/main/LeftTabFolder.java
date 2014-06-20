package derigible.visual.main;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

public class LeftTabFolder extends CTabFolder {

    private LeftSideBar lsb;

    public LeftTabFolder(Composite parent, int style, LeftSideBar lsb) {
	super(parent, style);
	this.lsb = lsb;
    }

    /**
     * @return the lsb
     */
    public LeftSideBar getLsb() {
	return lsb;
    }

    /**
     * @param lsb
     *            the lsb to set
     */
    public void setLsb(LeftSideBar lsb) {
	this.lsb = lsb;
    }

}
