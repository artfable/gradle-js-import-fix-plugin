import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.10"
}

apply(plugin = "maven-publish")
apply(plugin = "com.jfrog.bintray")
apply(plugin = "artfable.artifact")

group = "com.github.artfable.gradle"
version = "0.0.1"

buildscript {
    val bintray_version = "1.8.4"
    val artifact_plugin_version = "0.0.1"

    repositories {
        jcenter()
        mavenCentral()
        maven(url = "http://dl.bintray.com/artfable/gradle-plugins")
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:$bintray_version")
        classpath("com.github.artfable.gradle:gradle-artifact-plugin:$artifact_plugin_version")
    }
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourceJar"])
            artifact(tasks["javadocJar"])

            pom {
                description.set("Make relative paths for import in js files")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/artfable/gradle-js-import-fix-plugin/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("artfable")
                        name.set("Artem Veselov")
                        email.set("art-fable@mail.ru")
                    }
                }
            }
        }
    }
}

configure<BintrayExtension> {
    user = if (project.hasProperty("bintrayUser")) {
        project.ext["bintrayUser"] as String
    } else System.getenv("BINTRAY_USER")
    key = if (project.hasProperty("bintrayKey")) {
        project.ext["bintrayKey"] as String
    } else System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "gradle-plugins"
        name = "gradle-js-import-fix-plugin"
        setLicenses("MIT")
        vcsUrl = "https://github.com/artfable/gradle-js-import-fix-plugin.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            attributes =
                    mapOf(Pair("gradle-plugin", "artfable.npm:com.github.artfable.gradle:gradle-js-import-fix-plugin"))
        })
    })
}