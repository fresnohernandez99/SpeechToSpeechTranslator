import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.android.kmp.library)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_17)
		}
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "audio"
		}
	}

	sourceSets {
		commonMain.dependencies {

			// coroutines
			api(libs.kotlinx.coroutines.core)

			// logging
			api(libs.napier)
//			implementation(libs.logger.kmp)

			// koin
			api(libs.koin.core)
		}

		commonTest.dependencies {
			api(kotlin("test"))
		}

		androidMain.dependencies {
			api(libs.androidx.appcompat)
			api(libs.androidx.core)

			// Wav Recorder
			api(libs.android.wave.recorder)

			// ffmpeg-kit
//			implementation(libs.ffmpegkit.kmp.android)
		}
	}
	@Suppress("OPT_IN_USAGE")
	compilerOptions {
		freeCompilerArgs = listOf("-Xexpect-actual-classes")
	}
}

android {
	namespace = "core.audio"
	compileSdk = 36
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdk = 30
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}
