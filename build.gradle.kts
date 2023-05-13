import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    application
}

group = "chaos.unity"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.ow2.asm:asm-tree:9.5")

                implementation("io.ktor:ktor-server-netty:2.0.2")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                implementation(npm("postcss", "8.4.23"))
                implementation(npm("postcss-loader", "7.3.0")) // 5.0.0 seems not to work
                implementation(npm("autoprefixer", "10.4.14"))
                implementation(npm("tailwindcss", "3.3.2"))
            }
        }
        val jsTest by getting

        all {
            languageSettings {
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
    }
}

application {
    mainClass.set("chaos.unity.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

val copyTailwindConfig = tasks.register<Copy>("copyTailwindConfig") {
    from("./tailwind.config.js")
    into("${rootProject.buildDir}/js/packages/${rootProject.name}")

    dependsOn(":kotlinNpmInstall")
}

val copyPostcssConfig = tasks.register<Copy>("copyPostcssConfig") {
    from("./postcss.config.js")
    into("${rootProject.buildDir}/js/packages/${rootProject.name}")

    dependsOn(":kotlinNpmInstall")
}

tasks.named("processResources") {
    dependsOn(copyTailwindConfig)
    dependsOn(copyPostcssConfig)
}
