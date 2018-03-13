package de.joieVS.dso.timer;

public class Token {

	public final String token;
	public final String remainder;

	public Token(final String cmd, final String params) {
		token = cmd;
		remainder = params;
	}
}
