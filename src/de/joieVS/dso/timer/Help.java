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
				+ "Format der Wartezeit: \n"
				+ "\tEs können Stunden, Minuten und Sekunden eingegeben werden. Stunde und Minute sind durch s, S, h oder H zu trennen, Minute und Sekunde durch m oder M. \n"
				+ "\n\n" //
				+ "Verfügbare EreignisTypen: " + AlertTypes.list() + "\n\n" //
				+ "Beispiele:\n" //
				+ "\tEntdecker ist noch 5 Stunden und 13 Minuten unterwegs:\n" //
				+ "\t\te 5s13\n\n"
				+ "\tMine hat noch 382 Rest und einen Produktionszyklus von 6:17. Außerdem will ich eine Notiz bekommen, um welche Mine es sich handelt. Ich will benachrichtigt werden, wenn die Mine auf Rest 2 ist.\n" //
				+ "\t\tm 380x6m17 eisen s8\n\n" //
				+ "\tFür ein Quest sollen 40 Armbrüste produziert werden, es 38 fehlen noch, der 12h-Produktionswert ist 66. Als Notiz will ich erinnert werden, dass es um das Gildenquest geht.\n" //
				+ "\t\tq 38/66 gq\n\n"
				+ "\tIch will erinnert werden, einen Freund in 8 Stunden und 53 Minuten nachzubuffen:\n"
				+ "\t\ts 8h53 Egon buffen");
	}
}
