package de.joieVS.dso.timer;

import static de.joieVS.dso.timer.CommandProcessor.splitOfFirstToken;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.err;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AlertManager {

	private final static Set<Alert> alerts = Collections.synchronizedSet(new TreeSet<Alert>());
	private static final long HALF_DAY = 12l * 3600l * 1000l;
	private static boolean initialized = false;

	/**
	 *
	 */
	private static void init() {
		AlertController.startUp(alerts);
		initialized = true;
		synchronized (alerts) {
			try (BufferedReader iniWriter = new BufferedReader(
					new InputStreamReader(new FileInputStream("alerts.ini"), StandardCharsets.UTF_8))) {
				String line;
				while (null != (line = iniWriter.readLine())) {
					final String[] parts = line.split("\\|", 3);
					if (parts.length > 1) {
						final AlertTypes at = AlertTypes.find(parts[0]);
						final long alertMs = Instant.parse(parts[1]).toEpochMilli();
						final String msg = parts.length > 2 && parts[2] != null ? parts[2] : "";
						alerts.add(new Alert(at, alertMs, msg));
					}
				}
			} catch (final FileNotFoundException fnfe) {
				err.println(
						"Es wurden keine Ereignisse geladen, da die entsprechende Datei noch nicht zu existieren scheint!");
			} catch (final IOException e) {
				err.println("Konnte Datei zum Laden der Ereignisse nicht finden!");
				e.printStackTrace();
			}
			alerts.notifyAll();
		}
	}

	public static void parseAlertCmd(final AlertTypes at, final String params) {
		if (params.isEmpty()) {
			listAlerts(at);
		} else {
			final Token token = splitOfFirstToken(params);
			try {
				parseAlertParams(at, token);
			} catch (final Exception e) {
				err.println("Ereignisparameter '" + params + "' konnten nicht geparst werden.");
				e.printStackTrace();
			}
		}

	}

	private static void parseAlertParams(final AlertTypes at, final Token token) throws ParseException {
		List<Object> parts = splitToParts(token.token);
		validatePartsTypes(parts);
		final Object part0 = parts.get(0);
		final Integer firstInt = (Integer) part0;
		if (parts.size() == 1) {
			deleteAlert(at, firstInt);
			return;
		}
		final String firstChar = (String) parts.get(1);
		final int faktor;
		final long duration;
		if (firstChar.equals("/")) {
			faktor = 1;
			duration = HALF_DAY * firstInt / (Integer) parts.get(2);
		} else {
			if (firstChar.equalsIgnoreCase("x") || firstChar.equals("*")) {
				faktor = firstInt;
				parts = parts.subList(2, parts.size());
			} else {
				faktor = 1;
			}
			duration = parseDuration(parts);
		}
		createAlert(at, faktor, duration, token.remainder);
	}

	private static void createAlert(final AlertTypes at, final int faktor, final long duration,
			final String remainder) {
		final long alertMs = currentTimeMillis() + faktor * duration;
		synchronized (alerts) {
			alerts.add(new Alert(at, alertMs, remainder));
			persistAlerts();
			alerts.notifyAll();
		}
	}

	/**
	 *
	 */
	static void persistAlerts() {
		synchronized (alerts) {
			try (PrintWriter iniWriter = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream("alerts.ini"), StandardCharsets.UTF_8))) {
				for (final Alert alert : alerts) {
					iniWriter.println(alert.type + "|" + Instant.ofEpochMilli(alert.time) + "|" + alert.message);
				}
			} catch (final FileNotFoundException fnfe) {
				err.println("Konnte Datei zum Speichern der Ereignisse nicht erzeugen!");
				fnfe.printStackTrace();
			}
		}
	}

	private static void validatePartsTypes(final List<Object> parts) throws ParseException {
		boolean expectInt = true;
		for (final Object part : parts) {
			if (expectInt && !(part instanceof Integer)) {
				throw new ParseException("der Ereignisparameter ist ungültig");
			} else if (!expectInt && (!(part instanceof String) || ((String) part).length() != 1)) {
				throw new ParseException("der Ereignisparameter ist ungültig");
			}
			expectInt = !expectInt;
		}
	}

	private static long parseDuration(List<Object> parts) throws ParseException {
		int hours = 0, minutes = 0, seconds = 0;
		final String firstTimeClasifier = (String) parts.get(1);
		final boolean hasHours = firstTimeClasifier.equalsIgnoreCase("h") || firstTimeClasifier.equalsIgnoreCase("s");
		if (parts.size() == 2) {
			if (hasHours) {
				hours = (Integer) parts.get(0);
			} else if (firstTimeClasifier.equalsIgnoreCase("m")) {
				minutes = (Integer) parts.get(0);
			} else if (firstTimeClasifier.equalsIgnoreCase("s")) {
				seconds = (Integer) parts.get(0);
			} else {
				throw new ParseException("Unbekannte Zeiteinheit '" + firstTimeClasifier + "'!");
			}
		} else {
			if (hasHours) {
				hours = (Integer) parts.get(0);
				parts = parts.subList(2, parts.size());
			}
			minutes = (Integer) parts.get(0);
			if (parts.size() > 1) {
				if (!((String) parts.get(1)).equalsIgnoreCase("m")) {
					throw new ParseException("optional erwartete Zeiteinheit 'm', gefunden '" + parts.get(1) + "'");
				}
				seconds = (Integer) parts.get(2);
			}
		}

		return (seconds + minutes * 60l + hours * 3600l) * 1000l;
	}

	private static void deleteAlert(final AlertTypes at, final Integer alertNumber) {
		int count = 0;
		synchronized (alerts) {
			for (final Iterator<Alert> alertIt = alerts.iterator(); alertIt.hasNext();) {
				final Alert alert = alertIt.next();
				if (alert.type == at) {
					if (++count == alertNumber) {
						out.println("Ereignis " + alert + " wurde gelöscht");
						alertIt.remove();
						alerts.notifyAll();
						return;
					}
				}
			}
		}
		err.println("Das Ereignis Nummer " + alertNumber + " vom Typ " + at
				+ " existiert nicht und wurde daher nicht gelöscht.");
	}

	private static List<Object> splitToParts(String token) {
		final List<Object> ret = new ArrayList<>();
		while (!token.isEmpty()) {
			if (!Character.isDigit(token.charAt(0))) {
				ret.add(token.substring(0, 1));
				token = token.substring(1);
			}
			for (int numEndPos = 0; numEndPos < token.length(); numEndPos++) {
				if (!Character.isDigit(token.charAt(numEndPos))) {
					if (numEndPos > 0) {
						ret.add(Integer.valueOf(token.substring(0, numEndPos)));
						token = token.substring(numEndPos);
						break;
					}
					return null;
				}
				if (numEndPos == token.length() - 1) {
					ret.add(Integer.valueOf(token));
					token = "";
					break;
				}
			}
		}
		return ret;
	}

	private static void listAlerts(final AlertTypes at) {
		int count = 0;
		synchronized (alerts) {
			for (final Alert alert : alerts) {
				if (alert.type == at) {
					out.println(++count + ": " + alert);
				}
			}
			if (count == 0) {
				out.println("Es gibt keine aktiven Ereignisse vom Typ " + at);
			}
		}
	}

	public static void listAlerts() {
		if (!initialized) {
			init();
		}
		synchronized (alerts) {
			if (alerts.isEmpty()) {
				out.println("Es sind keine Ereignisse gesetzt.");
			}
			for (final Alert alert : alerts) {
				out.println(alert);
			}
		}
	}

}
