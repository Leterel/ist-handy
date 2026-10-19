# Ist Handy?

Android-App für eine extrem unnötige Frage. Öffnen → 5,8 Sekunden Scan-Theater → ein ehrliches, möglichst dumm formuliertes Urteil.

- **„Ja, is Handy, Mois.“** bei kompaktem Touchdisplay mit gemeldeter Telefonie-Hardware.
- **„Nee, is Tablet.“** ab 600 dp kurzer Displaykante.
- Bei widersprüchlichen Merkmalen eine ausdrücklich unsichere Antwort.
- Bei jedem Zurückkehren in die App startet der Scan erneut, ohne Knopf.
- Android 8.0 / API 26 oder neuer. Kein Internet, keine Kamera, keine Berechtigungen.

Die Animation ist erfunden. Die Gerätemerkmale kommen aus Androids lokalen Display- und Hardwareinformationen. Die App liest keine Kontakte, Dateien, Geräte-IDs oder Standortdaten.

## Installieren

[APK herunterladen](https://github.com/Leterel/ist-handy/releases/tag/v0.1.1)
(öffentliches Repository).

`ist-handy-0.1.1.apk` auf dein Android-Gerät kopieren, antippen und für die verwendete Datei-App bei Bedarf „Unbekannte Apps installieren“ erlauben. Danach **Ist Handy?** öffnen. Die APK ist mit einem Entwicklungsschlüssel signiert und zum direkten Ausprobieren gedacht, nicht zur Veröffentlichung im Play Store.

Für Entwicklungstests wird ausschließlich ein ausdrücklich ausgewählter Emulator
verwendet; siehe [EMULATOR-VERIFICATION.md](EMULATOR-VERIFICATION.md).

## Was „erkennen“ hier bedeutet

Android bietet keinen allgemein zuverlässigen Schalter „Handy oder Tablet“. Die App kombiniert die maximale Displaygröße in dp (nicht nur das aktuelle geteilte Fenster), Touchscreen, Telefonie-Hardware und spezielle Gerätekategorien. Ein Tablet mit Mobilfunk bleibt aufgrund seiner Größe ein Tablet. Aufgeklappte Foldables können als Tablet eingeordnet werden; Zoom-/Dichteeinstellungen können die Einordnung beeinflussen. Computer, TV, Uhr und Auto werden nicht als Handy ausgegeben. Das ist eine nachvollziehbare Heuristik, keine garantierte Geräteidentifikation.

Ein normales Handy bleibt im Auto-Dock-Modus ein Handy: Dieser Android-Anzeigemodus
ist keine Auto-Hardware. Der aktuelle Quellcode verwendet dafür ausschließlich
`FEATURE_AUTOMOTIVE`. Android erklärt den Unterschied in der
[UiModeManager-Dokumentation](https://developer.android.com/reference/android/app/UiModeManager).
Diese Korrektur ist ab APK 0.1.1 enthalten.

## Entwicklung

Native Java-Android-App mit eigener Canvas-Animation, ohne zusätzliche Bibliotheken. Siehe [BUILD.md](BUILD.md) für den SDK-Build und [PROGRESS.md](PROGRESS.md) für den Teststand.

APK-Version, Prüfsumme, Signatur und Grenzen der aktuellen Version stehen in
[VERIFICATION-0.1.1.md](VERIFICATION-0.1.1.md).

Stand 23.09.2026: 12 Klassifikationsfälle, APK-Signatur, Ausrichtung und leere
Berechtigungsliste geprüft. Die neue API-34-Emulatormatrix besteht vier Layouts:
Handy, kleines Display mit 200 % Schrift, Querformat und Tablet-Konfiguration.
Handy-/Tablet-Urteil und Rotation stimmen; Wiederöffnen startet einen neuen Scan.
Das sind Displayvarianten eines Emulators, keine vier Geräte oder Android-Versionen.
Der Scan scrollt am Ende automatisch zum Ergebnis.

Am 25.09.2026 wurden die reinen Java-Tests auf 48 Fälle erweitert und bestanden:
zusätzlich Auto-/Schreibtisch-Dock-Modus, Tag-/Nachtmodus und Hardwarekategorien.
Diese Tests prüfen die Klassifikationslogik, nicht die Umschaltung auf einem Gerät.

Am 27.09.2026 bestand APK 0.1.1 zusätzlich **15 Emulatorprüfungen**: dieselben
vier Layouts, Wiederöffnen und der tatsächlich eingeschaltete Android-Auto-Dock-
Modus für Handy und Tablet. Die sieben Ergebnis-/Scanbilder sowie Modus- und
Hardwarebelege liegen dem neuen Prüfpaket bei; die fünf Matrixbilder und das
Handy-im-Auto-Modus-Bild wurden zusätzlich angesehen. Alle Tests liefen auf
einer API-34-Emulatorinstanz, ohne Zugriff auf ein physisches Gerät.
Siehe [EMULATOR-VERIFICATION.md](EMULATOR-VERIFICATION.md).
