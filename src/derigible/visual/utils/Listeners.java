package derigible.visual.utils;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import derigible.controller.TransactionsController;
import derigible.saves.Saved;
import derigible.saves.XMLBuilder;
import derigible.utils.FileU;

public final class Listeners {

	private Listeners() {
		// do nothing, not instantiable
	}

	public static void addFocusListener(final Control c, final ArrayDeque<Control> focusHistory) {
		c.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if (focusHistory.size() > 10) {
					focusHistory.removeFirst();
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				focusHistory.push(c);
			}
		});
	}

	public static Closer createCloseListener(Shell shell, HashMap<String, Saved> saved, XMLBuilder e, File f) {
		return new Listeners.Closer(shell, saved, e, f);
	}

	private static class Closer implements Listener {
		private HashMap<String, Saved> saved;
		private Shell shell;
		private XMLBuilder e;
		private File f;

		Closer(Shell shell, HashMap<String, Saved> saved, XMLBuilder e2, File f) {
			this.shell = shell;
			this.saved = saved;
			this.e = e2;
			this.f = f;
		}

		@Override
		public void handleEvent(Event arg0) {
			if (!isSaved()) {
				for (Map.Entry<String, Saved> saved : this.saved.entrySet()) {
					MessageBox save = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
					save.setText("Save Information?");
					String name;
					if (saved.getValue().getClass() == TransactionsController.class) {
						save.setMessage("You have unsaved Transaction information. Save now?");
						name = "transactions";
					} else {
						save.setMessage("You have unsaved budget information for " + saved.getKey() + ". Save now?");
						name = saved.getKey();
					}
					if (handleResponse(save.open(), saved.getValue(), name) == SWT.CANCEL && arg0 != null) {
						arg0.doit = false;
						break;
					}
				}
				FileU.xmlToFile(f, e.getDom());
			}
		}

		private int handleResponse(int open, Saved saved, String name) {
			if (open == SWT.YES) {
				try {
					return Actions.save(shell, saved, name, "Save failed! Exit Anyways?");
				} catch (NullPointerException e) {
					// canceled save, do nothing
					return SWT.CANCEL;
				}
			} else if (open == SWT.CANCEL) {
				return SWT.CANCEL;
			} else {
				return SWT.NO;
			}
		}

		public boolean isSaved() {
			for (Saved saved : this.saved.values()) {
				if (!saved.saved()) {
					return false;
				}
			}
			return true;
		}
	}

}
