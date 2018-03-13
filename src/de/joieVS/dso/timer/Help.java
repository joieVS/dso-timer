package de.joieVS.dso.timer;

import static java.lang.System.out;

public class Help extends CommandProcessor {

	public static String getCommandString() {
		return "Hilfe";
	}

	public static void executeCmd(@SuppressWarnings("unused") final String params) {
		out.println("Befehle: \n" //
				+ "\t" + Help.getCommandString() + "\n" //
				+ "\t" + Aliases.getCommandString() + "\t\t\t\t\t\t\t\tlistet alle Aliase auf\n" //
				+ "\t" + Aliases.getCommandString() + "\t<altesAlias|command> <neuesAlias>"
				+ "\t\t\tdefiniert ein neues Alias - das alte wird gelöscht. Wird (noch) nicht persistiert.\n"
				+ "\t\t\t\t\t\t\t\t\tlistet alle laufenden Alarme auf\n"
				+ "\t<ereignisTyp>\t\t\t\t\t\t\tlistet alle Alarme eines Typs auf\n"
				+ "\t<ereignisTyp> <warteZeit> {Notiz}\t\t\t\tneuen Alarm in <warteZeit> setzen\n"
				+ "\t<ereignisTyp> <n>x<warteZeit> {Notiz}\t\t\t\tneuen Alarm in <n>-mal-<warteZeit> setzen\n"
				+ "\t<ereignisTyp> <zu produzieren>/<12h Produktionswert> {Notiz}\tneuen Alarm setzen, auf den Zeitpunkt, zu dem bei dem eingegebenen <12h-Produktionsert> eine entsprechende Menge produziert wurde\n"
				+ "\t<ereignisTyp> <alarmNr>\t\t\t\t\t\tlöscht den angegebenen Alarm" //
				+ "\n\n" //
				+ "Verfügbare EreignisTypen: " + AlertTypes.list());
	}
}
