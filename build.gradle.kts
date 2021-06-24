import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
}

group = "me.mateusz"

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    compile("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compile("org.apache.logging.log4j:log4j-api:2.14.0")
    compile("org.apache.logging.log4j:log4j-core:2.14.0")

}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}