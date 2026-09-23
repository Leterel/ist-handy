# Ist Handy?

Android-App für eine extrem unnötige Frage. Öffnen → 5,8 Sekunden Scan-Theater → ein ehrliches, möglichst dumm formuliertes Urteil.

- **„Ja, is Handy, Mois.“** bei kompaktem Touchdisplay mit gemeldeter Telefonie-Hardware.
- **„Nee, is Tablet.“** ab 600 dp kurzer Displaykante.
- Bei widersprüchlichen Merkmalen eine ausdrücklich unsichere Antwort.
- Bei jedem Zurückkehren in die App startet der Scan erneut, ohne Knopf.
- Android 8.0 / API 26 oder neuer. Kein Internet, keine Kamera, keine Berechtigungen.

Die Animation ist erfunden. Die Gerätemerkmale kommen aus Androids lokalen Display- und Hardwareinformationen. Die App liest keine Kontakte, Dateien, Geräte-IDs oder Standortdaten.

## Installieren

[APK herunterladen](https://github.com/Leterel/ist-handy/releases/tag/v0.1.0)
(privates Repository; GitHub-Anmeldung erforderlich).

`ist-handy-0.1.0.apk` auf dein Android-Gerät kopieren, antippen und für die verwendete Datei-App bei Bedarf „Unbekannte Apps installieren“ erlauben. Danach **Ist Handy?** öffnen. Die APK ist mit einem Entwicklungsschlüssel signiert und zum direkten Ausprobieren gedacht, nicht zur Veröffentlichung im Play Store.

Für Entwicklungstests wird ausschließlich ein ausdrücklich ausgewählter Emulator
verwendet; siehe [EMULATOR-VERIFICATION.md](EMULATOR-VERIFICATION.md).

## Was „erkennen“ hier bedeutet

Android bietet keinen allgemein zuverlässigen Schalter „Handy oder Tablet“. Die App kombiniert die maximale Displaygröße in dp (nicht nur das aktuelle geteilte Fenster), Touchscreen, Telefonie-Hardware und spezielle Gerätekategorien. Ein Tablet mit Mobilfunk bleibt aufgrund seiner Größe ein Tablet. Aufgeklappte Foldables können als Tablet eingeordnet werden; Zoom-/Dichteeinstellungen können die Einordnung beeinflussen. Computer, TV, Uhr und Auto werden nicht als Handy ausgegeben. Das ist eine nachvollziehbare Heuristik, keine garantierte Geräteidentifikation.

## Entwicklung

Native Java-Android-App mit eigener Canvas-Animation, ohne zusätzliche Bibliotheken. Siehe [BUILD.md](BUILD.md) für den SDK-Build und [PROGRESS.md](PROGRESS.md) für den Teststand.

Stand 23.09.2026: 12 Klassifikationsfälle, APK-Signatur, Ausrichtung und leere
Berechtigungsliste geprüft. Die neue API-34-Emulatormatrix besteht vier Layouts:
Handy, kleines Display mit 200 % Schrift, Querformat und Tablet-Konfiguration.
Handy-/Tablet-Urteil und Rotation stimmen; Wiederöffnen startet einen neuen Scan.
Das sind Displayvarianten eines Emulators, keine vier Geräte oder Android-Versionen.
Der Scan scrollt am Ende automatisch zum Ergebnis.
