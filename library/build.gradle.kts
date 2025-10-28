@file:OptIn(
    org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class,
    org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class,
)

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)
    `maven-publish`
    signing
}

dependencies {
    commonTestImplementation(kotlin("test"))
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    wasmJs {
        browser()
        nodejs()
    }

    jvmToolchain(8)

    explicitApi()

    abiValidation {
        enabled = true
    }

    compilerOptions {
    }
}

java {
    withSourcesJar()
}

tasks.withType<GenerateModuleMetadata> {
    enabled = !isSnapshot()
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier = "javadoc"
    from(tasks.dokkaGeneratePublicationHtml)
}

publishing {
    publications {
        withType<MavenPublication> {
            artifactId = artifactId.replace(project.name, "ktor-client-throttle")

            if (artifactId.endsWith("-jvm")) {
                artifact(dokkaJavadocJar)
            }

            pom {
                name = "ktor-client-throttle"
                description = "The Throttle plugin allows you to limit the number of requests within a certain time period."
                url = "https://github.com/brudaswen/ktor-client-throttle/"

                licenses {
                    license {
                        name = "The MIT License (MIT)"
                        url = "https://mit-license.org/"
                    }
                }
                developers {
                    developer {
                        id = "brudaswen"
                        name = "Sven Obser"
                        email = "dev@brudaswen.de"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/brudaswen/ktor-client-throttle.git"
                    developerConnection =
                        "scm:git:ssh://git@github.com:brudaswen/ktor-client-throttle.git"
                    url = "https://github.com/brudaswen/ktor-client-throttle/"
                }
                issueManagement {
                    system = "GitHub Issues"
                    url = "https://github.com/brudaswen/ktor-client-throttle/issues/"
                }
            }
        }
    }
}

signing {
    setRequired { !isSnapshot() }

    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications)
}

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

fun isSnapshot() = version.toString().endsWith("-SNAPSHOT")
