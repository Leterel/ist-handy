# Build

## Voraussetzungen

- Windows mit PowerShell 7 oder Windows PowerShell 5.1.
- JDK 17 oder neuer (getestet: Temurin 21.0.12).
- Offizielles Android SDK: `platforms;android-36`, `build-tools;36.0.0`.
- Optional `platform-tools` und ein Android-Emulator für Entwicklungstests.

SDK-Komponenten über Android Studio oder `sdkmanager "platforms;android-36" "build-tools;36.0.0" "platform-tools"` installieren. Die Android-SDK-Lizenzbedingungen dabei lesen und akzeptieren.

## Bauen und prüfen

```powershell
./build.ps1 -SdkPath 'C:\Android\Sdk' -JavaHome 'C:\Program Files\Java\jdk-21'
```

Das Skript führt zuerst 48 Geräte-Klassifikationsfälle aus, kompiliert Ressourcen/Java/Dex und erzeugt eine APK. Es prüft APK-Signatur und ZIP-Alignment und gibt den SHA-256-Hash aus.

Ergebnis: `build/outputs/ist-handy-0.1.1.apk`. Die geprüfte Kopie dieses
Arbeitsstands liegt unter `outputs/ist-handy/ist-handy-0.1.1.apk`.

Optional: `-OutputDirectory 'C:\meine-ausgaben'` oder `-KeyStorePath 'C:\private-schluessel\ist-handy-debug.keystore'`. Der automatisch angelegte Entwicklungsschlüssel ist vom Repository ausgeschlossen. Bewahre ihn lokal auf, wenn spätere Builds bestehende Installationen aktualisieren sollen. Ein anderer Schlüssel erfordert eine vorherige Deinstallation.

Es werden nur offizielle SDK-Werkzeuge verwendet, kein Gradle und keine Online-Abhängigkeiten. Nach der Installation des SDK funktioniert der Build offline.

## Emulatortest

```powershell
./tests/emulator-matrix.ps1 -Serial emulator-5570 -App handy `
  -AdbPath C:\Android\Sdk\platform-tools\adb.exe `
  -ApkPath ./build/outputs/ist-handy-0.1.1.apk `
  -OutputDirectory ./build/emulator-matrix
```

Den angegebenen Emulator vorher starten. Das Skript lehnt physische Geräte ab
und prüft vor jedem ADB-Aufruf `ro.kernel.qemu=1`. Es installiert nur auf diesem
Emulator, prüft vier Layouts und stellt Display-/Schrifteinstellungen anschließend
wieder her. Ergebnisse und Grenzen: [EMULATOR-VERIFICATION.md](EMULATOR-VERIFICATION.md).
