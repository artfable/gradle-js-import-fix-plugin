plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.5.21"
    `maven-publish`
    id("com.jfrog.artifactory") version "4.24.14"
    id("artfable.artifact") version "0.0.3"
}

group = "com.artfable.gradle"
version = "0.1.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    test {
        useJUnitPlatform()
    }
}

publishing {
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
                        url.set("https://raw.githubusercontent.com/artfable/gradle-js-import-fix-plugin/master/LICENSE")
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

artifactory {
    setContextUrl("https://artfable.jfrog.io/artifactory/")
    publish {
        repository {
            setRepoKey("default-maven-local")
            setUsername(artifactoryCredentials.user)
            setPassword(artifactoryCredentials.key)
        }
        defaults {
            publications ("mavenJava")

            setPublishArtifacts(true)
            setPublishPom(true)
            setPublishIvy(false)
        }
    }
}