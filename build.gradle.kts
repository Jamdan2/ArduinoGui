import org.gradle.internal.impldep.org.apache.maven.model.Build
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

project.apply { from("./versions.gradle.kts") }

val kotlinVrs: String by extra
val ardulinkVrs: String by extra
val tornadoFxVrs: String by extra

plugins {
    java
    kotlin("jvm") version "1.2.60-eap-7"
    application
}

group = "com.jamdan2.ArduinoGui"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap") }
}

dependencies {
    kotlin("").toString()
    compile(kotlin("stdlib-jdk8", kotlinVrs))
    compile("org.ardulink", "ardulink-core-base", ardulinkVrs)
    compile("org.ardulink", "ardulink-core-serial-jssc", ardulinkVrs)
    compile("no.tornado", "tornadofx", tornadoFxVrs)
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "com.jamdan2.arduinogui.MainKt"
    }
    from(configurations.runtime.map {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        if (it.isDirectory) it else zipTree(it)
    })
    with(tasks["jar"] as CopySpec)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    "build" {
        dependsOn(fatJar)
    }
}

application {
    mainClassName = "com.jamdan2.arduinogui.MainKt"
}
