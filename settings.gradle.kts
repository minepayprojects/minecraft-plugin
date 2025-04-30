import org.gradle.api.initialization.resolve.RepositoriesMode

rootProject.name = "Minepay"

pluginManagement {

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        mavenCentral()

        maven {
            url = uri("https://libraries.minecraft.net/")
        }

        maven {
            url = uri("https://repo.codemc.io/repository/maven-snapshots/")
        }

        maven {
            url = uri("https://m2.dv8tion.net/releases")
        }

        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}

include("core")
include("bukkit")