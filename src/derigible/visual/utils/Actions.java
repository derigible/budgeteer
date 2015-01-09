package derigible.visual.utils;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import derigible.saves.Saved;

public final class Actions {

	public static int save(Shell shell, Saved saved, String name, String failmessage) {
		File f;
		if ((f = saved.save(name)) != null) {
			MessageBox save = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
			save.setText("File Saved");
			save.setMessage("File Saved at " + f.getAbsolutePath());
			save.open();
			return SWT.YES;
		} else {
			MessageBox save = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES | SWT.NO);
			save.setText("Save Failed!");
			save.setMessage(failmessage);
			return save.open() == SWT.NO ? SWT.CANCEL : SWT.YES;
		}
	}

}
