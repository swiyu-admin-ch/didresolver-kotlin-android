import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
    // As suggested by https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html
    // see https://vanniktech.github.io/gradle-maven-publish-plugin/
    id("com.vanniktech.maven.publish") version "0.37.0"
}

android {
    // CAUTION Until 2.0.0 (GitHub packages), the "namespace" was set to "ch.admin.eid.didresolver".
    //         For the sake of Maven Central publishing, it must now resemble the relevant Maven Central namespace
    namespace = "ch.admin.swiyu.didresolver"
    compileSdk = 36

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
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.gson)
    //implementation(libs.jna)
    implementation("net.java.dev.jna:jna:5.19.1@aar")
}

// As suggested by https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html
mavenPublishing {
    // as suggested by https://vanniktech.github.io/gradle-maven-publish-plugin/what/#android-library-single-variant
    configure(AndroidSingleVariantLibrary(
        // don't publish a javadoc jar
        javadocJar = JavadocJar.None(),
        // don't publish a sources jar
        sourcesJar = SourcesJar.None(),
        // the publish variant
        variant = "release",
    ))

    // when publishing to https://central.sonatype.com/
    publishToMavenCentral( automaticRelease = true)

    signAllPublications()

    coordinates(
        // CAUTION Until 2.0.0 (GitHub packages), the "groupId" was set to "ch.admin.eid.didresolver".
        //         For the sake of Maven Central publishing, it must now match the relevant Maven Central namespace
        "ch.admin.swiyu.didresolver",
        "didresolver-android",
        "2.11.0-SNAPSHOT")

    pom {
        name = "DID Resolver (Kotlin/Android)"
        description = "Language bindings for the swiyu DID resolver library in Kotlin/Android"
        url = "https://github.com/swiyu-admin-ch/didresolver-kotlin-android"
        licenses {
            license {
                name = "MIT License"
                url = "http://www.opensource.org/licenses/mit"
            }
        }
        developers {
            developer {
                id = "vst-bit"
                name = "vst-bit (Swiyu Omni Developer)"
                organization = "Swiyu"
                organizationUrl = "https://github.com/swiyu-admin-ch"
            }
        }
        scm {
            url = "https://github.com/swiyu-admin-ch/didresolver-kotlin-android/tree/main"
            connection = "scm:git:git://github.com/swiyu-admin-ch/didresolver-kotlin-android.git"
            developerConnection = "scm:git:ssh://github.com:swiyu-admin-ch/didresolver-kotlin-android.git"
        }
    }
}
