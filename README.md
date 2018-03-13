# DSO Timer
Dies ist ein kleines Java-Proggy, um viele Ereignisse im Blick behalten zu können, ohne sich damit zu stressen.

Es können beliebig viele Ereignisse erzeugt werden.
Das Programm gibt Vorwarnungen, 30min vor dem Ereignis, 5 min vor dem Ereignis, 1 min vor dem Ereignis und schließlich 
zum Zeitpunkt des Ereignisses. Wenn das Vorwarnfenster schon offen ist, während die nächste Erinnerung käme, wird kein
weiteres Erinnerungsfenster geöffnet.

Die Bedienung ist Chat-like. Es können auf der Kommandozeile Befehle eingegeben werden um die Ereignisse zu verwalten.
Der Fokus bei der Implementierung lag nicht darin, die Bedienung 'schön' zu gestalten, sondern effizient nach den
Bedürfnissen, die ich selbst beim Spielen von DSO habe.

## Installation / Starten des Programms
Voraussetzung zur Benutzung dieses Programms ist eine Java-Installation auf dem Rechner. Soweit diese noch nicht
vorhanden ist, kann java von [hier](https://java.com/de/download/) herunter geladen werden.

Lege die jar-Datei in ein beliebiges Verzeichnis, gehe mit der Kommandozeile in dieses Verzeichnis und starte das
Programm mit
<pre>
java -jar dso-timer.jar
</pre>
Alternativ kann es auch durch Doppelclick auf das jar-file gestartet werden.

Wenn kein Interesse an dem Quellcode besteht, sondern dieses Tool nur benutzt werden soll, kann das jar-file direkt
[hier](./target/dso-timer.jar) herunter geladen werden.

## Benutzung
Sobald das Programm gestartet wurde, werden erst die aktiven Ereignisse geladen und angezeigt. Anschließend kann man
sich durch Eingabe von
<pre>
?
</pre>
die Hilfe ausgeben lassen, die (hoffentlich) alle Bedienmöglichkeiten beschreibt. 
