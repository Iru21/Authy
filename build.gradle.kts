import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
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
    id("com.modrinth.minotaur") version "2.+"
}

apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "com.github.johnrengelman.shadow")
apply(plugin = "kotlin")

group = "me.iru"
val pluginName: String by project
val pluginVersion: String by project
val minecraftVersion: String by project

val withDrivers: Configuration by configurations.creating

tasks {

    withType<KotlinCompile> {
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
        relocate("org.bstats", group)
        archiveFileName.set("${pluginName}-${pluginVersion}.jar")
    }

    register<ShadowJar>("shadowJarDrivers") {
        dependsOn(processResources)
        dependsOn(makeDefaults)
        relocate("org.bstats", group)
        archiveFileName.set("${pluginName}-${pluginVersion}-drv.jar")
        configurations = listOf(project.configurations["runtimeClasspath"], withDrivers)

    }

    modrinth {
        val version = pluginVersion

        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set("authy")
        versionNumber.set(version)
        versionType.set("release")
        versionName.set("$pluginName $version")
        uploadFile.set(shadowJar as Any)
        additionalFiles.set(listOf("build/libs/${pluginName}-${pluginVersion}-drv.jar"))
        gameVersions.addAll("1.17", "1.18", "1.19", "1.20")
        loaders.addAll("spigot", "paper", "purpur")
        changelog.set(rootProject.file("changelog.md").readText())
        syncBodyFrom.set(rootProject.file("README.md").readText())
    }

    register<Exec>("createGitHubRelease") {
        dependsOn(shadowJar)
        dependsOn("shadowJarDrivers")

        commandLine("git", "tag", "-a", "v${pluginVersion}", "-m", "Release v${pluginVersion}")
        commandLine("git", "push", "--tags")

        commandLine("gh", "release", "create", "v${pluginVersion}", "-F", "changelog.md",
            "-t", "v${pluginVersion}",
            "build/libs/${pluginName}-${pluginVersion}.jar", "build/libs/${pluginName}-${pluginVersion}-drv.jar")
    }

    register("createReleases") {
        dependsOn("createGitHubRelease")
        dependsOn(modrinth)
        dependsOn(modrinthSyncBody)
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

    withDrivers("mysql:mysql-connector-java:8.0.33")
    withDrivers("org.xerial:sqlite-jdbc:3.42.0.0")

    implementation("org.spigotmc:spigot-api:${minecraftVersion}-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}