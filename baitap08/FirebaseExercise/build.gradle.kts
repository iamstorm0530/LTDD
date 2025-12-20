// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1") // Cập nhật phiên bản nếu cần
        classpath("com.google.gms:google-services:4.3.15") // Plugin Google Services
    }
}

