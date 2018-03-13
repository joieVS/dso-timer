package de.joieVS.dso.timer;

import static java.lang.System.err;
import static java.lang.System.in;

import java.io.IOException;

public class CommandProcessor {

	/**
	 * @param cmdLine
	 * @return
	 */
	protected static Token splitOfFirstToken(final String cmdLine) {
		int eoCmd = cmdLine.indexOf(" ");
		if (eoCmd < 0) {
			eoCmd = cmdLine.length();
		}
		final String cmd = cmdLine.substring(0, eoCmd);
		final String params = cmdLine.length() - cmd.length() > 1 ? cmdLine.substring(eoCmd + 1) : "";
		return new Token(cmd, params);
	}

	public static void parse(String cmdLine) throws IOException {
		if (cmdLine.isEmpty()) {
			AlertManager.listAlerts();
			return;
		}
		cmdLine = Aliases.processAliases(cmdLine);

		final Token token = splitOfFirstToken(cmdLine);
		final String cmd = token.token;
		final String params = token.remainder;
		if (cmd.equalsIgnoreCase(Aliases.getCommandString())) {
			Aliases.executeCmd(params);
		} else if (cmd.equalsIgnoreCase(Help.getCommandString())) {
			Help.executeCmd(params);
		} else if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("exit")) {
			in.close();
		} else {
			final AlertTypes at = AlertTypes.find(cmd);
			if (at == null) {
				err.println("Unbekannter Ereignistyp " + cmd);
			} else {
				AlertManager.parseAlertCmd(at, params);
			}
		}
	}

}