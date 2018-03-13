package de.joieVS.dso.timer;

import java.util.Map;

public enum AlertTypes {
	BUCHBINDER("Buchbinder"), ENTDECKER("Entdecker"), GEOLOGE("Geologe"), MINE("Mine"), QUEST("Quest"), GENERAL(
			"General"), SONSTIGE("Sonstige");
	private final String friendlyName;

	private AlertTypes(final String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public static void addDefaultAliases(final Map<String, String> aliases) {
		aliases.put("b", BUCHBINDER.toString());
		aliases.put("e", ENTDECKER.toString());
		aliases.put("g", GEOLOGE.toString());
		aliases.put("m", MINE.toString());
		aliases.put("q", QUEST.toString());
		aliases.put("at", GENERAL.toString());
		aliases.put("s", SONSTIGE.toString());
	}

	@Override
	public String toString() {
		return friendlyName;
	}

	public static String list() {
		final StringBuilder list = new StringBuilder();
		final AlertTypes[] types = AlertTypes.values();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				list.append(", ");
			}
			list.append(types[i].friendlyName);
		}
		return list.toString();
	}

	public static AlertTypes find(final String cmd) {
		for (final AlertTypes alert : AlertTypes.values()) {
			if (alert.toString().equalsIgnoreCase(cmd)) {
				return alert;
			}
		}
		return null;
	}

}
