package de.joieVS.dso.timer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.err;

import java.util.Iterator;
import java.util.Set;

public class AlertController extends Thread {

	private final Set<Alert> alerts;

	private AlertController(final Set<Alert> alerts) {
		super("EreignisMonitor");
		this.alerts = alerts;
		setDaemon(true);
		start();
	}

	public static void startUp(final Set<Alert> alerts) {
		new AlertController(alerts);
	}

	@Override
	public void run() {
		try {
			synchronized (alerts) {
				while (true) {
					if (alerts.isEmpty()) {
						alerts.wait();
					}
					final Iterator<Alert> nextAlertIt = alerts.iterator();
					final Alert nextAlert = nextAlertIt.next();
					final long msForNextAlert = nextAlert.time2NextReminder();
					if (msForNextAlert <= 0) {
						nextAlertIt.remove();
						nextAlert.alarm();
						if (!nextAlert.isDone()) {
							alerts.add(nextAlert);
						} else {
							AlertManager.persistAlerts();
						}
					}
					// to avoid multiple windows for the same event and extreme delays after wakeup from hibernation,
					// I decided to always wait at least 0,1s and never more then 5s
					alerts.wait(max(100, min(5000, msForNextAlert)));
				}
			}
		} catch (final InterruptedException e) {
			err.println("Ereigniscontroller wurde unterbrochen - es werden keine Ereignisse mehr beobachtet!");
			e.printStackTrace();
		}

	}
}
