# Emulatortests — 23.09.2026

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
