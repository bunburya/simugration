// NOTE:  The below is all set up to work with TornadoFX v1.7, which requires Java 8.  To work with TornadoFX 2.0,
// it would be necessary to target Java 11, and it would then be necessary to load the javafxplugin.
// See the commented code below.

val kotlinVersion by extra("1.3.0")
val tornadofxVersion by extra("1.7.17")
//val tornadofxVersion by extra("2.0.0-SNAPSHOT")
val junitVersion by extra("5.1.0")

plugins {
    java
    kotlin("jvm") version "1.3.61"
    application
    //id("org.openjfx.javafxplugin") version "0.0.8"
}

application {
    mainClassName = "eu.bunburya.simugration.app.SimugrationApp"
}

group = "eu.bunburya"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    //maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(kotlin("stdlib"))
    //testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

//javafx {
//    modules("javafx.controls", "javafx.fxml")
//}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

}
