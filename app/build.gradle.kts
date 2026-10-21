plugins { id("com.android.application") }
val adsTest = providers.gradleProperty("adsTest").map { it.toBooleanStrict() }.getOrElse(false)
android {
    namespace = "de.leterel.isthandy"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    defaultConfig {
        applicationId = "de.leterel.isthandy"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "0.1.1"
        if (adsTest) { applicationIdSuffix = ".adstest"; versionNameSuffix = "-adstest" }
    }
    sourceSets.getByName("main").java.srcDir(if (adsTest) "src/adsTest/java" else "src/offline/java")
    if (adsTest) {
        sourceSets.getByName("debug").manifest.srcFile("src/adsTest/AndroidManifest.xml")
        sourceSets.getByName("release").manifest.srcFile("src/adsTest/AndroidManifest.xml")
    }
    buildTypes { release { isMinifyEnabled = false } }
    signingConfigs.getByName("debug").storeFile = rootProject.file("build/debug.keystore")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    if (adsTest) {
        implementation("com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk:1.5.0")
        implementation("com.google.android.ump:user-messaging-platform:4.0.0")
    }
}
