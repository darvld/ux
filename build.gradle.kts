plugins {
    kotlin("multiplatform") version "1.4.20"
    id("maven-publish")
}

group = "com.github.darvld"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    explicitApi()

    mingwX64("native") {
        compilations.getByName("main").cinterops.create("libgtk")
        mavenPublication {
            artifactId = "ux"
        }

        /*binaries {
            executable {
                entryPoint = "main"
            }
        }*/
    }

    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
