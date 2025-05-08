import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.palantir.git-version") version "3.0.0"
}

group = "com.minecraft.minepay.bukkit"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34")

    implementation(project(":core"))
    compileOnly(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()

fun getBuildDate() : String {

    val stdout = ByteArrayOutputStream()

    exec {
        commandLine("git", "show", "--no-patch", "--format=%ci")

        standardOutput = stdout
    }

    return stdout.toString().trim().replace(" -0300", "").replace(" -0400", "").replace("-", "/")
}

bukkit {
    main = "com.minecraft.minepay.bukkit.BukkitCore"
    name = "Minepay-Bukkit"
    version = "0.0.1-${details.gitHash.substring(0, 6)} from ${details.branchName} (${getBuildDate()})"
    description = "Minepay Bukkit Plugin (based on [core-*])"
    website = "https://minepay.com.br/"
    authors = listOf("ViniciuszXL")

    commands {
        register("minepay") {
            description = "Comando do Minepay-Bukkit"
            permission = "minepay.command"
            permissionMessage = "Você não possui permissão para digitar este comando"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}