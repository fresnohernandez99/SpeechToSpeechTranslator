import com.google.devtools.ksp.gradle.KspAATask
import io.kmpbits.splash.ExitAnimation
import io.kmpbits.splash.SplashColor
import io.kmpbits.splash.SplashLogo
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kmpSplash) // ERRORS ONLY ON IOS CHECK WITH ARM MACOS
}

splashScreen {
    backgroundColor = SplashColor.hex("#0975F4")       // Light mode background
    logo = SplashLogo.resource("translate_logo.png")             // File in composeResources/drawable/
    logoDark = SplashLogo.resource("translate_logo_dark.png")    // Optional: dark mode logo
    exitAnimation = ExitAnimation.SlideUp(300)         // Optional: exit animation (Android + iOS)
}

kotlin {
    androidTarget { //We need the deprecated target to have working previews
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-Xskip-prerelease-check",
                "-XXLanguage:+ExplicitBackingFields",
                "-Xbinary=bundleId=com.fresnohernandez99.stpt.sharedUI"
            )
        )
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // Compose Multiplatform
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.foundation)
            api(libs.compose.resources)
            api(libs.compose.ui.tooling.preview)
            api(libs.compose.material3)
            api(libs.compose.icons.extended)

            // Coroutines
            api(libs.kotlinx.coroutines.core)

            // Ktor Networking
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.client.serialization)
            api(libs.ktor.serialization.json)
            api(libs.ktor.client.logging)

            // Lifecycle
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime)

            // Serialization
            api(libs.kotlinx.serialization.json)

            // Navigation
            api(libs.androidx.navigation.compose)

            // Dependency Injection (Koin)
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            //api(libs.koin.compose.navigation)
            //api(libs.koin.ktor)

            // Image Loading (Coil)
            api(libs.coil)
            api(libs.coil.network.ktor)

            // Utils
            api(libs.kotlinx.datetime)

            // Database (Room)
            api(libs.room.runtime)

            // Preferences
            api(libs.datastore.preferences.core)

            api(libs.kmp.audio.recorder.player)
            implementation(libs.record.core)
            implementation(libs.alert.kmp)

            api(libs.kmpSplash.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            api(libs.kotlinx.coroutines.android)
            api(libs.kotlinx.coroutines.play.services)
            api(libs.ktor.client.okhttp)
            implementation(project(":lib"))
            implementation(libs.mlkit.translate)
            api("androidx.core:core-splashscreen:1.2.0")
        }

        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }
    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries {
                framework {
                    baseName = "SharedUI"
                    isStatic = true
                }
            }
        }

    val whisperFrameworkPath = file("${projectDir}/../iosApp/whisper.xcframework")
    iosSimulatorArm64 {
        compilations.getByName("main") {
            cinterops.create("whisperSimArm64") {
                defFile(project.file("src/nativeInterop/cinterop/whisper.def"))
                compilerOpts(
                    "-I${whisperFrameworkPath}/ios-arm64_x86_64-simulator/whisper.framework/Headers",
                    "-F${whisperFrameworkPath}"
                )
            }
        }
    }
    iosArm64 {
        compilations.getByName("main") {
            cinterops.create("whisperArm64") {
                defFile(project.file("src/nativeInterop/cinterop/whisper.def"))
                compilerOpts(
                    "-I${whisperFrameworkPath}/ios-arm64/whisper.framework/Headers",
                    "-F$whisperFrameworkPath"
                )
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

android {
    namespace = "com.fresnohernandez99.stpt.shared"
    compileSdk = 37

    defaultConfig {
        minSdk = 30
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "lib/arm64-v8a/libimage_processing_util_jni.so"
            excludes += "lib/x86_64/libimage_processing_util_jni.so"
        }
    }

    packaging {
        // Ensure reproducible packaging
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE*"
            excludes += "META-INF/NOTICE*"
            excludes += "META-INF/*.version"
            excludes += "assets/composeResources/com.module.notelycompose.resources/strings.xml"
        }

        // Force deterministic file ordering
        jniLibs {
            useLegacyPackaging = true
            // 16KB Page Size Support: Use uncompressed native libraries
            pickFirsts += listOf("**/libc++_shared.so", "**/libwhisper.so")
        }

        // Ensure reproducible DEX files
        dex {
            useLegacyPackaging = false
        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        disable.add("NullSafeMutableLiveData")
    }
    buildFeatures {
        buildConfig = true
    }
    ndkVersion = "29.0.14206865"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }
}

tasks{
    withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(listOf("-Xskip-prerelease-check"))
        }
    }
}