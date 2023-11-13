package de.joieVS.dso.timer;

import static de.joieVS.dso.timer.AlertManager.timeOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

import java.time.Instant;

public class Alert implements Comparable<Alert> {

	private static final long ONE_MINUTE = 60 * 1000l;
	private static final long SECOND_REMINDER = ONE_MINUTE * 5;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long FIRST_REMINDER = ONE_HOUR / 2;
	public final AlertTypes type;
	public final long time;
	public final String message;

	private long timeOfNextReminder;
	private int alarmCount;

	public Alert(final AlertTypes type, final long time, final String message) {
		this.type = type;
		this.time = time;
		this.message = message;
		timeOfNextReminder = time - FIRST_REMINDER;
		if (timeOfNextReminder <= currentTimeMillis()) {
			timeOfNextReminder = time - SECOND_REMINDER;
			alarmCount++;
		}
		if (timeOfNextReminder <= currentTimeMillis()) {
			timeOfNextReminder = time - ONE_MINUTE;
			alarmCount++;
		}
		if (timeOfNextReminder <= currentTimeMillis()) {
			timeOfNextReminder = time;
			alarmCount++;
		}
		out.println("Ereignis " + type + " erzeugt. Zeitpunkt: " + Instant.ofEpochMilli(time) + ", nächste Erinnerung um "
				+ Instant.ofEpochMilli(timeOfNextReminder));
	}

	@Override
	public int compareTo(final Alert o) {
		return (int) (timeOfNextReminder - o.timeOfNextReminder);
	}

	public long time2NextReminder() {
		return timeOfNextReminder - currentTimeMillis();
	}

	public void alarm() {
		alarmCount++;
		UIAlert.alert(this);
		if (alarmCount == 1) {
			timeOfNextReminder = time - SECOND_REMINDER;
		} else if (alarmCount == 2) {
			timeOfNextReminder = time - ONE_MINUTE;
		} else {
			timeOfNextReminder = time;
		}
	}

	public boolean isDone() {
		return alarmCount > 3;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(type.toString());
		sb.append(": ").append(duration2Event());
		if (!message.isEmpty()) {
			sb.append(" ").append(message);
		}
		return sb.toString();
	}

	/**
	 * @param sb
	 * @return
	 */
	public String duration2Event() {
		final long time2Event = time - currentTimeMillis();
		return asDuration(time2Event, time);
	}

	private static String asDuration(long time2Event, final long eventTime) {
		int hours = 0, minutes = 0, seconds = 0;
		if (time2Event > ONE_HOUR) {
			hours = (int) (time2Event / ONE_HOUR);
			time2Event -= hours * ONE_HOUR;
		}
		if (time2Event > ONE_MINUTE) {
			minutes = (int) (time2Event / ONE_MINUTE);
			time2Event -= minutes * ONE_MINUTE;
		}
		seconds = (int) (time2Event / 1000);

		final StringBuilder ret = new StringBuilder();
		if (hours > 0) {
			ret.append(hours).append("H");
		}
		if (hours + minutes > 0) {
			ret.append(minutes).append("M");
		}
		if (hours + minutes + seconds > 0) {
			ret.append(seconds).append("S");
		} else {
			ret.append("JETZT");
		}
		ret.append(" bis ").append(timeOf(eventTime));
		return ret.toString();
	}

}
