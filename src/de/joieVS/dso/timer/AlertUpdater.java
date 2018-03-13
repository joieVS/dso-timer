package de.joieVS.dso.timer;

import static java.lang.System.err;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class AlertUpdater extends Thread {

	final static Map<Alert, JTextArea> updateTargets = Collections.synchronizedMap(new HashMap<>());

	private static AlertUpdater instance = null;

	private AlertUpdater() {
		super("Ereignisanzeigenaktualisierer");
		setDaemon(true);
		start();
	}

	public static void addTextArea(final JTextArea textArea, final Alert alert) {
		if (instance == null) {
			instance = new AlertUpdater();
		}
		synchronized (updateTargets) {
			updateTargets.put(alert, textArea);
			updateTargets.notifyAll();
			textArea.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentHidden(final ComponentEvent e) {
					synchronized (updateTargets) {
						updateTargets.remove(alert);
						updateTargets.notifyAll();
					}
				}
			});
		}
	}

	@Override
	public void run() {
		try {
			synchronized (updateTargets) {
				while (true) {
					if (updateTargets.isEmpty()) {
						updateTargets.wait();
					}
					for (final Entry<Alert, JTextArea> target : updateTargets.entrySet()) {
						final Alert alert = target.getKey();
						final JTextArea textArea = target.getValue();
						SwingUtilities.invokeLater(() -> {
							textArea.setText(
									alert.duration2Event() + (alert.message.isEmpty() ? "" : "\n" + alert.message));
						});
					}
					updateTargets.wait(1000);
				}
			}
		} catch (final InterruptedException e) {
			err.println("Ereignisaktualisierer wurde unterbochen und beendet seine Arbeit!");
			e.printStackTrace();
		}
	}

	public static boolean updateActive(final Alert alert) {
		return updateTargets.containsKey(alert);
	}

	public static void removeTextArea(final Alert alert) {
		updateTargets.remove(alert);
	}
}
