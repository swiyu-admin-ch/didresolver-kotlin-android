plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
}

android {
    namespace = "ch.admin.eid.didresolver"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    lint {
        checkReleaseBuilds = false
        //If you want to continue even if errors found use following line
        abortOnError = false
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.gson)
    implementation("net.java.dev.jna:jna:5.14.0@aar")
    api("net.java.dev.jna:jna:5.14.0@aar")
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("gpr") {
            groupId = "ch.admin.eid.didresolver"
            artifactId = "didresolver-android"
            version = "0.0.3"
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
    repositories {
        maven {
            // To publish run in powershell -> $env:TOKEN='<github token>'; $env:USERNAME='<you user>'; ./gradlew publish
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/e-id-admin/didresolver-kotlin-android")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}