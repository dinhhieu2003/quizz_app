plugins {
    id("com.android.application")
}

android {
    namespace = "com.study.quizzapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.study.quizzapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation(files("libs/discrete-scrollview-1.5.1.aar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //noinspection GradleCompatible
    implementation ("com.android.support:customtabs:28.0.0")
//    implementation ("com.android.support:support-v4:28.0.0")
//    implementation ("com.android.support:design:28.0.0")
    // circle image dependency
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // toasty
    implementation ("com.github.GrenderG:Toasty:1.5.2")

    // Awesome Splash
    implementation ("com.github.ViksaaSkool:AwesomeSplash:1.0.0")

}