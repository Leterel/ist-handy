# Ist Handy? 0.1.1 — geprüfter Auslieferungsstand

APK: `ist-handy-0.1.1.apk`, **20.957 Bytes**, Version 0.1.1 / Code 2.
Android 8.0 (API 26) oder neuer; Ziel-SDK 36. Gebaut am 25.09.2026,
auf dem Emulator geprüft am 27.09.2026, Belegprüfung am 28.09.2026.

SHA-256:
`1F84C1B67BD14A470B38B25DF961D92623675D6AEEF7AD2A0C8978E65755C8D8`

## Änderung

Ein normales Handy im Android-Auto-Dock-Anzeigemodus bleibt jetzt ein Handy.
Nur das Hardwaremerkmal `FEATURE_AUTOMOTIVE` kennzeichnet ein eingebautes
Auto-System. Der Anzeigemodus wird nicht mehr mit Auto-Hardware verwechselt.
Die Activity verwendet dieselbe Feature-Zuordnung, die die JVM-Tests prüfen.

## Tatsächlich geprüft

| Prüfung | Ergebnis |
| --- | --- |
| Reine Java-Tests gegen Produktionscode | 48 Fälle bestanden: Größen, unklare Geräte, Hardwarekategorien, Auto-/Schreibtischmodus und Tag-/Nachtbits |
| APK-Prüfung mit Android Build-Tools 36.0.0 | Signatur V2/V3 gültig; ZIP-Alignment gültig; keine angeforderten Berechtigungen |
| API-34-Displaymatrix | 10 Prüfungen bestanden: Handy, 320-dp-Display mit 200 % Schrift, Querformat, 800-dp-Tablet und Wiederöffnen |
| Echte Auto-Dock-Umschaltung im Emulator | 5 Prüfungen bestanden: keine Auto-Hardware, Moduswert 3 und richtiges Handy-/Tablet-Urteil |
| Signierer 0.1.0 gegenüber 0.1.1 | Gleiches Zertifikat; vorhandene 0.1.0-Installation kann mit dieser APK aktualisiert werden |

Zertifikat-SHA-256:
`DFF1D6047787312CA57E78A4159DA9E53CCEC49900247F4F1071FE5E8CA4DA57`

Die zehn Matrixprüfungen enthalten eine Boot-Prüfung, je zwei Prüfungen für
vier Layouts und das Ergebnis nach Wiederöffnen. Der neue Scan nach Wiederöffnen
wurde zusätzlich anhand des unmittelbar aufgenommenen Screenshots bei 15 %
belegt; dies ist keine separate automatische Fortschrittsprüfung.

Alle Laufzeitprüfungen dieser Version liefen auf **einer API-34-Emulatorinstanz**.
Die vier Layouts sind Displayvarianten, keine vier Geräte oder Android-Versionen.
In diesen Prüfungen wurde kein physisches Gerät verwendet. Am 28.09. wurden
ausschließlich Dateien und vorhandene Belege geprüft, kein Emulator gestartet.

## Belege und Wiederholung

- [BUILD.md](BUILD.md): Build-Befehl und Voraussetzungen.
- [EMULATOR-VERIFICATION.md](EMULATOR-VERIFICATION.md): Matrix- und Auto-Modus-Befehle.
- `tests/DeviceClassifierTest.java`: 48 Klassifikationsfälle.
- `tests/emulator-matrix.ps1` und `tests/emulator-car-mode.ps1`: explizite
  Emulator-Seriennummer sowie QEMU-Prüfung vor jedem ADB-Befehl.
- Auslieferungsarchiv `ist-handy-0.1.1-verification-20260928.zip`: Build-/Signatur-
  und Manifestbelege, ursprüngliche Matrix-/Auto-Modus-JSONs, UI-Hierarchien und
  sieben Screenshots. Interne Prüfsummen stehen in `EVIDENCE-SHA256SUMS.txt`.

## Grenzen

Geräteerkennung bleibt eine dokumentierte Heuristik. Andere Android-Versionen,
echte Tablet-/Auto-Hardware, Foldables und Herstellerbesonderheiten sind nicht
geprüft. Änderungen der Displaydichte können das Ergebnis beeinflussen. Die
APK nutzt einen lokalen Entwicklungsschlüssel und ist für direktes Installieren
gedacht; eine Play-Store-Veröffentlichung ist nicht vorbereitet.
