package de.joieVS.dso.timer;

import static java.lang.Math.random;
import static java.lang.System.out;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
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
				setLocation(dialog);
				out.println("Dialog erzeugt für Ereignis " + alert);
			});
		}
	}

	/**
	 * @param dialog
	 */
	private static void setLocation(final JFrame dialog) {
		final Rectangle dialogBounds = dialog.getBounds();
		final Rectangle wa = getScreenWorkingArea(dialog);
		final double minX = wa.getX() + wa.getWidth() * 0.25d;
		final double maxDeltaX = wa.getWidth() * 0.5d - dialogBounds.getWidth();
		final double minY = wa.getY() + wa.getHeight() * 0.25d;
		final double maxDeltaY = wa.getHeight() * 0.5d - dialogBounds.getHeight();
		dialog.setLocation((int) (minX + random() * maxDeltaX), (int) (minY + random() * maxDeltaY));
	}

	private static Rectangle getScreenWorkingArea(final Window dialog) {
		final GraphicsConfiguration gc = dialog.getGraphicsConfiguration();
		final Insets insets = dialog.getToolkit().getScreenInsets(gc);
		final Rectangle bounds = gc.getBounds();
		bounds.x += insets.left;
		bounds.y += insets.top;
		bounds.width -= insets.left + insets.right;
		bounds.height -= insets.top + insets.bottom;
		return bounds;
	}
}
