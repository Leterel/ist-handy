# Build

## Voraussetzungen

- Windows mit PowerShell 7 oder Windows PowerShell 5.1.
- JDK 17 oder neuer (getestet: Temurin 21.0.12).
- Offizielles Android SDK: `platforms;android-36`, `build-tools;36.0.0`.
- Optional `platform-tools` für Installation auf einem angeschlossenen Gerät.

SDK-Komponenten über Android Studio oder `sdkmanager "platforms;android-36" "build-tools;36.0.0" "platform-tools"` installieren. Die Android-SDK-Lizenzbedingungen dabei lesen und akzeptieren.

## Bauen und prüfen

```powershell
./build.ps1 -SdkPath 'C:\Android\Sdk' -JavaHome 'C:\Program Files\Java\jdk-21'
```

Das Skript führt zuerst zwölf unabhängige Geräte-Klassifikationstests aus, kompiliert Ressourcen/Java/Dex und erzeugt eine APK. Es prüft APK-Signatur und ZIP-Alignment und gibt den SHA-256-Hash aus.

Ergebnis: `build/outputs/ist-handy-0.1.0.apk`. Die geprüfte Kopie dieses
Arbeitsstands liegt unter `outputs/ist-handy/ist-handy-0.1.0.apk`.

Optional: `-OutputDirectory 'C:\meine-ausgaben'` oder `-KeyStorePath 'C:\private-schluessel\ist-handy-debug.keystore'`. Der automatisch angelegte Entwicklungsschlüssel ist vom Repository ausgeschlossen. Bewahre ihn lokal auf, wenn spätere Builds bestehende Installationen aktualisieren sollen. Ein anderer Schlüssel erfordert eine vorherige Deinstallation.

Es werden nur offizielle SDK-Werkzeuge verwendet, kein Gradle und keine Online-Abhängigkeiten. Nach der Installation des SDK funktioniert der Build offline.

## Gerätetest

```powershell
adb install -r build/outputs/ist-handy-0.1.0.apk
adb shell am start -n de.leterel.isthandy/.MainActivity
```

Prüfen: automatischer Start, Ergebnis nach knapp sechs Sekunden, Scrollen bei kleinen Displays, erneuter Scan nach Home → App, Hoch-/Querformat, größere Schrift, Telefon und Tablet. Kein Berechtigungsdialog sollte erscheinen.
