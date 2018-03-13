package de.joieVS.dso.timer;

import static java.lang.System.err;
import static java.lang.System.out;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Aliases extends CommandProcessor {

	private final static Map<String, String> aliases = new HashMap<>();
	private static final String COMMAND_STRING = "Alias";
	static {
		AlertTypes.addDefaultAliases(aliases);
		aliases.put("?", Help.getCommandString());
		aliases.put("h", Help.getCommandString());
		aliases.put("a", COMMAND_STRING);
	}

	public static String getCommandString() {
		return COMMAND_STRING;
	}

	public static void executeCmd(final String params) {
		if (params.length() == 0) {
			listAliases();
		} else {
			final String[] aliasCmd = params.split(" ", 2);
			if (aliasCmd.length == 1) {
				listAliases(aliasCmd[0]);
			} else {
				setAlias(aliasCmd[0], aliasCmd[1]);
			}
		}
	}

	private static void listAliases() {
		out.println("aktive Aliases:");
		for (final Entry<String, String> alias : aliases.entrySet()) {
			out.println("\t" + alias.getKey() + "->" + alias.getValue());
		}
	}

	private static void listAliases(final String alias) {
		for (final Entry<String, String> aliasEntry : aliases.entrySet()) {
			if (aliasEntry.getKey().equalsIgnoreCase(alias) || aliasEntry.getValue().equalsIgnoreCase(alias)) {
				out.println("\t" + aliasEntry.getKey() + "->" + aliasEntry.getValue());
			}
		}
	}

	private static void setAlias(String value2Set, final String key2Set) {
		// lookup if given value is a known command or alias
		boolean found = false;
		for (final Iterator<Entry<String, String>> aliasIt = aliases.entrySet().iterator(); aliasIt.hasNext();) {
			final Entry<String, String> aliasEntry = aliasIt.next();
			if (aliasEntry.getValue().equalsIgnoreCase(value2Set)) {
				err.println("Alias '" + key2Set + "' kann nicht für '" + value2Set
						+ "' gesetzt werden, da es selbst ein Kommando ist.");
				return;
			}
			if (aliasEntry.getKey().equalsIgnoreCase(value2Set)) {
				value2Set = aliasEntry.getValue();
			}
			if (aliasEntry.getKey().equalsIgnoreCase(key2Set)) {
				err.println("Alias '" + key2Set + "' kann nicht für '" + value2Set
						+ "' gesetzt werden, da es bereits für '" + aliasEntry.getValue() + "' gesetzt ist.");
				return;
			}
			if (aliasEntry.getValue().equalsIgnoreCase(value2Set)) {
				aliasIt.remove();
				found = true;
				break;
			}
		}
		if (!found) {
			err.println("Das alias kann nicht gesetzt werden, da '" + value2Set + "' kein definiertes Kommando ist.");
			return;
		}
		aliases.put(key2Set, value2Set);

	}

	static String processAliases(final String cmdLine) {
		final Token token = splitOfFirstToken(cmdLine);

		for (final Entry<String, String> alias : aliases.entrySet()) {
			if (token.token.equalsIgnoreCase(alias.getKey())) {
				return alias.getValue() + " " + token.remainder;
			}
		}
		return cmdLine;
	}
}
