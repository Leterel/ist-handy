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
