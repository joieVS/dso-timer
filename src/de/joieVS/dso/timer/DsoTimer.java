/**
 *
 */
package de.joieVS.dso.timer;

import static java.lang.System.err;
import static java.lang.System.in;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author volker.schroeder
 *
 */
public class DsoTimer {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		AlertManager.listAlerts();
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while (null != (line = br.readLine())) {
				CommandProcessor.parse(line);
			}
		} catch (final IOException e) {
			err.println("Tschüss...");
			e.printStackTrace();
		}
		out.println("Bye bye...");
	}
}
