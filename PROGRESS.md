# Fortschritt

## 2026-09-23 abends — neue Emulatormatrix bestanden

- Vier API-34-Displaykonfigurationen: 411-dp-Handy, 320 dp mit 200 % Schrift,
  tatsächliches Querformat und 800-dp-Tablet.
- Zehn Prüfungen bestanden: Emulatorstart, korrektes Urteil und Rotation in
  jedem Layout sowie Abschluss nach Wiederöffnen. Die unmittelbare Aufnahme
  nach Wiederöffnen zeigt den neu gestarteten Scan bei 15 %.
- APK 0.1.0 unverändert; Screenshots, XML und JSON als zusätzliche Release-Belege.
- Sicheres Emulator-Testskript eingecheckt; kein physisches Gerät verwendet.
- Details und Reproduktion: EMULATOR-VERIFICATION.md. Frühere Aufzeichnungen unten
  bleiben als Historie erhalten; der dort damals nicht verfügbare Emulator läuft wieder.

## 2026-09-23 — Prototyp 0.1.0 geprüft und gesichert

- Automatischer Fake-Scan ohne Startknopf oder Kamera.
- Geräteklasse wird aus Displaygröße, Touchscreen, Telefonie-Hardware und
  Android-Gerätekategorie heuristisch bestimmt.
- Zwölf Klassifikationsfälle am 23.09. erneut gegen den Produktionscode bestanden.
- APK mit Android SDK API 36 / Build-Tools 36.0.0 gebaut, ausgerichtet und mit
  `apksigner` (V2/V3) geprüft; Signatur, 4-Byte-Ausrichtung und das Fehlen von
  Berechtigungsdeklarationen am 23.09. erneut an der ausgelieferten APK geprüft.
- Der Build vom 21.09. enthält den Fix, der nach dem Scan automatisch zum
  Ergebnis scrollt, sodass es auch auf kleineren Displays vollständig sichtbar ist.
- Vorhandene API-34-Emulator-Aufnahmen vom 21.09. geprüft: Handy-Ergebnis bei
  411 dp, Tablet-Ergebnis bei 720 dp und erneut laufender Scan nach Wiederöffnen.
  Das Tablet-Szenario verwendet die veränderte Displaykonfiguration desselben
  Emulators und ist kein Test auf echter Tablet-Hardware.
- Am 23.09. auf einem angeschlossenen Pixel 10a erstmals installiert
  (`Success`) und kalt gestartet (`Status: ok`, Startzeit 289 ms). Der vollständige
  Ablauf wurde dort nicht geprüft: Das Gerät wurde parallel benutzt, daher wurden
  weitere UI-Tests beendet. Es werden keine physischen Ergebnis-/Resume-Tests behauptet.

Auslieferung: `ist-handy-0.1.0.apk` (20.957 Bytes).
SHA-256: `F4026354EE70508B8A400E704CE4B9310E8B5AEF45BD0CB25317930307D2742E`.

Die Screenshots liegen mit `VERIFICATION.md` neben der ausgelieferten APK.
Der Emulator war bei der damaligen ADB-Abfrage am 23.09. offline. Eine erste
Abfrage in der Sandbox scheiterte zusätzlich am Benutzerverzeichnis; außerhalb
der Sandbox funktionierte die Abfrage. Die Emulatorbelege sind ausdrücklich vom
21.09.; es wurde kein neuer Lauf am 23.09. behauptet.

Noch auf echten Geräten zu prüfen: Verhalten auf Foldables, OEM-Sonderfällen,
Hoch-/Querformat und Lebenszyklusverhalten auf verschiedenen Herstellern. Der
Build-Schlüssel ist nur ein lokaler Entwicklungsschlüssel und wird nicht eingecheckt.
