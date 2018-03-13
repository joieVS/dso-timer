package de.joieVS.dso.timer;

import static java.lang.System.out;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class UIAlert {

	public static void alert(final Alert alert) {
		if (!AlertUpdater.updateActive(alert)) {
			SwingUtilities.invokeLater(() -> {
				final JFrame dialog = new JFrame(alert.type.toString());
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				final Container cp = dialog.getContentPane();
				final JTextArea textArea = new JTextArea(3, 40);
				textArea.setEditable(false);
				AlertUpdater.addTextArea(textArea, alert);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(final WindowEvent we) {
						AlertUpdater.removeTextArea(alert);
					}
				});
				cp.add(textArea);
				dialog.pack();
				dialog.setVisible(true);
				dialog.toFront();
				dialog.setLocation(400, 300);
				out.println("Dialog erzeugt für Ereignis " + alert);
			});
		}
	}
}
