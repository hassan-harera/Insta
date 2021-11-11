buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libs.gradleTools)
        classpath(Libs.navigationSafeArgsPlugin)
        classpath(Libs.kotlinPlugin)
        classpath(Libs.googleServices)
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.31")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}