package derigible.visual.custom.widgets;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

public class LeftTabFolder extends CTabFolder {

    private OverviewBar lsb;

    public LeftTabFolder(Composite parent, int style, OverviewBar lsb) {
	super(parent, style);
	this.lsb = lsb;
    }

    /**
     * @return the lsb
     */
    public OverviewBar getLsb() {
	return lsb;
    }

    /**
     * @param lsb
     *            the lsb to set
     */
    public void setLsb(OverviewBar lsb) {
	this.lsb = lsb;
    }

}
