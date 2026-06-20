plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.fresnohernandez99.stpt"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.fresnohernandez99.stpt"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.000.000"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":sharedUI"))
    implementation(libs.androidx.activityCompose)
    debugImplementation(libs.compose.ui.tooling)
}
