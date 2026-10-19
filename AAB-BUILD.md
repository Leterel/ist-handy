# Android-App-Bundle — 07.10.2026

Ein unsigniertes Play-Store-AAB wurde aus dem unveränderten App-Kern gebaut.

Werkzeuge: JDK 21, Gradle 8.9, AGP 8.7.3, Android SDK API 36 und Build-Tools
36.0.0. Der Offline-Build benötigt den vorhandenen Gradle-Cache und den lokalen
AAPT2-Override:

```powershell
$env:JAVA_HOME='C:/Pfad/zum/jdk-21'
$env:ANDROID_HOME='C:/Pfad/zum/android-sdk'
$env:GRADLE_USER_HOME='C:/Pfad/zum/gradle-cache'
$env:ANDROID_USER_HOME='C:/Pfad/zum/android-benutzerordner'
$aapt=Join-Path $env:ANDROID_HOME 'build-tools/36.0.0/aapt2.exe'
./gradlew.bat --offline --no-daemon "-Pandroid.aapt2FromMavenOverride=$aapt" bundleRelease
```

Ergebnis: `app/build/outputs/bundle/release/app-release.aab` (14.332 Bytes).
Das Archiv enthält BundleConfig, Manifest, DEX und Ressourcen. Der AAB ist nicht
für den Play-Upload signiert. AGP 8.7.3 meldet für compileSdk 36 eine Kompatibilitäts-
warnung, der Build selbst besteht. Noch offen: Produktionsschlüssel, Play-
Signierung, Store-Checks, Consent/Werbung und Emulatorinstallation aus dem AAB.

## Bundletool-Prüfung

Offizielles bundletool-all 1.18.3: `validate` bestanden. Daraus erzeugte, lokal
testsignierte APKs am 07.10. im API-34-Emulator emulator-5570 installiert und
gestartet; Handy-/Tablet-Ergebnis erkannt. Beleg: result.json im lokalen Ordner
outputs/ist-handy/aab-verification-20261007-pass2. Produktionssignierung ist
nicht getestet. Wiederholbarer Runner: tests/test-app-bundle.ps1; nur explizite
QEMU-Emulatoren zulässig, neue Ausgabeordner bewahren frühere Belege.

## Automatisierter Offline-Build — 19.10.2026

`build-bundle.ps1` nimmt `SdkPath`, `JavaHome`, `GradleCache`, `AndroidUserHome`,
`BundleToolJar` und einen neuen `OutputDirectory` entgegen. Alle Pfade sind lokal
konfigurierbar. Standardmäßig offline; `-Online` erlaubt fehlende Abhängigkeiten
nachzuladen. Das Skript stellt vorherige Prozess-Umgebungsvariablen wieder her,
prüft Paketname, Archivinhalt und fehlende Signatur und validiert mit bundletool.
Ergebnis: `app-release-unsigned.aab`, Prüfsumme und Prüfprotokolle im Ausgabeordner.

Erneuter Offline-Build und fünf Prüfungen in `tests/test-bundle-guards.ps1`
bestanden: ungültiges Paket, fremder Schlüssel, physische Geräteseriennummer und
bestehender Belegordner werden abgelehnt; der fest hinterlegte lokale
Entwicklungsschlüssel erzeugt ein APK-Set. Dabei kein Gerät verwendet.
Der Testschlüssel wird ausschließlich lokal bereitgestellt und nicht eingecheckt.
Ein neuer Entwicklungsschlüssel erfordert eine bewusst geprüfte Anpassung des
öffentlichen Zertifikat-Fingerabdrucks im Runner. Keine Produktionssignierung.
