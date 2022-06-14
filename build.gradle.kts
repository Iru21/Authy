import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "me.iru"
val mcversion = "1.19"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:${mcversion}-R0.1-SNAPSHOT")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}