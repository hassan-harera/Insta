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
        classpath(Libs.hiltPlugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
allprojects {
    repositories {
        google()
        jcenter()

    }
}