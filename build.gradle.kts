import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    kotlin("jvm") version "1.6.10"
}

apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "com.github.johnrengelman.shadow")
apply(plugin = "kotlin")

group = "me.iru"
val pluginName: String by project
val pluginVersion: String by project
val minecraftVersion: String by project

tasks {

    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
    }

    val makeDefaults by registering(Copy::class) {
        dependsOn(processResources)

        from("src/main/resources/lang/")
        include("**/*.yml")
        into("build/resources/main/lang/defaults/")
    }

    processResources {
        outputs.upToDateWhen { false }

        filesMatching("**plugin.yml") {
            expand(
                mutableMapOf(
                    Pair("pluginVersion", pluginVersion),
                    Pair("minecraftVersion", minecraftVersion)
                )
            )
        }

    }

    shadowJar {
        dependsOn(processResources)
        dependsOn(makeDefaults)
        archiveFileName.set("${pluginName}-${pluginVersion}.jar")
        relocate("org.bstats", "me.iru")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {

    compileOnly("org.apache.logging.log4j:log4j-api:2.20.0")
    compileOnly("org.apache.logging.log4j:log4j-core:2.20.0")

    runtimeOnly("mysql:mysql-connector-java:8.0.32")

    implementation("org.spigotmc:spigot-api:${minecraftVersion}-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}