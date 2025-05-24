plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    annotationProcessor ("com.jakewharton:butterknife:7.0.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



}
dependencies {
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation (libs.ktx)
    implementation (libs.glide)
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.exifinterface:exifinterface:1.3.7")
}
dependencies {
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.squareup.picasso:picasso:2.8")

}
dependencies {
    implementation ("com.google.android.gms:play-services-location:17.0.0")


    testImplementation ("junit:junit:4.12")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")


    androidTestImplementation ("androidx.test.ext:junit:1.1.1")


    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0")


}
dependencies {
    implementation ("androidx.fragment:fragment:1.5.5")
}


