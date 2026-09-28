# Emulatortests — 27.09.2026

## APK 0.1.1: neue Matrix und echter Auto-Dock-Modus

Am 27.09.2026 wurde APK **0.1.1** mit SHA-256
`1F84C1B67BD14A470B38B25DF961D92623675D6AEEF7AD2A0C8978E65755C8D8`
auf einer eigens gestarteten API-34-Emulatorinstanz geprüft.

**15 automatisierte Prüfungen bestanden:** zehn in der unten beschriebenen
Display-/Wiederöffnen-Matrix und fünf im neuen Auto-Dock-Test. In allen vier
Layouts waren Urteil und erwartete Rotation korrekt. Die fünf Matrixbilder
wurden zusätzlich angesehen; nach Home und Wiederöffnen zeigt der unmittelbare
Screenshot den neuen Scan bei 15 %, anschließend wird das Ergebnis korrekt.

Der Auto-Dock-Test schaltet über Androids `cmd uimode car yes` tatsächlich in den
Auto-Anzeigemodus. `dumpsys uimode` bestätigt den Moduswert 3; die Hardwareliste
weist kein `android.hardware.type.automotive` auf. Sowohl die Handy-Konfiguration
als auch die Tablet-Konfiguration behalten jetzt das richtige Urteil. Damit ist
die 0.1.1-Korrektur auch außerhalb der reinen JVM-Tests geprüft. Der Test injiziert
keine internen App-Zustände und ändert keinen Produktionscode. Die veränderten
Display-, Schrift- und Auto-Modus-Einstellungen werden
im `finally`-Block zurückgesetzt. Die eigene Emulatorinstanz wurde danach beendet.

Zusätzliche Reproduktion aus dem Repo:

```powershell
./tests/emulator-car-mode.ps1 -Serial emulator-5570 `
  -AdbPath C:\Android\Sdk\platform-tools\adb.exe `
  -ApkPath ./build/outputs/ist-handy-0.1.1.apk `
  -OutputDirectory ./build/car-mode-verification
```

Für `emulator-matrix.ps1` ebenfalls APK 0.1.1 angeben. Die Belege liegen im
gemeinsamen Auslieferungsverzeichnis unter
`outputs/ist-handy/emulator-verification-20260927/`, einschließlich `car-mode/`.
Dies bleibt ein API-34-Emulator mit Displayvarianten, kein Test auf echter
Auto-/Tablet-Hardware oder mehreren Android-Versionen. Es wurde kein physisches
Gerät verwendet.

## Frühere Prüfung vom 23.09.2026 (APK 0.1.0)

Die veröffentlichte APK **0.1.0** wurde unverändert auf `emulator-5570`, Android
API 34, geprüft. SHA-256:
`F4026354EE70508B8A400E704CE4B9310E8B5AEF45BD0CB25317930307D2742E`.

| Konfiguration | Ergebnis |
| --- | --- |
| 1080 × 1920, 420 dpi, normale Schrift | „Ja, is Handy, Mois.“ sichtbar |
| 720 × 1280, 360 dpi, 200 % Schrift | Handy-Urteil vollständig sichtbar |
| Handy quer, tatsächlich gemessene Rotation 1 | Handy-Urteil sichtbar |
| 1600 × 2560, 320 dpi, 800 dp kurze Kante | „Nee, is Tablet.“ sichtbar |
| Home → erneut öffnen | Unmittelbare Aufnahme zeigt neuen Scan bei 15 %; Abschluss korrekt |

**10 automatisierte Prüfungen bestanden.** Screenshots zusätzlich angesehen.
Alle vier Layouts teilen dieselbe API-34-Systemabbildung. Das ist keine Abdeckung
mehrerer Android-Versionen oder echter Tablet-/Foldable-Hardware. Die Erkennung
ist weiterhin eine Heuristik und kann von Android-Dichtewerten beeinflusst werden.

## Wiederholen

Ein API-34-AVD mit Telefonie-Funktion starten, dann aus dem Repo:

```powershell
./tests/emulator-matrix.ps1 -Serial emulator-5570 -App handy `
  -AdbPath C:\Android\Sdk\platform-tools\adb.exe `
  -ApkPath ./build/outputs/ist-handy-0.1.0.apk `
  -OutputDirectory ./build/emulator-matrix
```

Die Seriennummer muss mit `emulator-` beginnen; zusätzlich prüft das Skript vor
jedem Befehl `ro.kernel.qemu=1`. Es greift nie auf ein Standardgerät zurück.
Display, Schriftgröße und Rotation werden im `finally`-Block wiederhergestellt.
Der Wiederöffnen-Check enthält eine Aufnahme, die visuell auf einen neuen Scan
geprüft werden muss; der spätere Abschluss wird automatisch geprüft.

Das zusätzliche Release-ZIP enthält JSON, UI-Hierarchien und Emulatorbilder.
Keine Aktion dieses Testlaufs betraf ein physisches Gerät.
