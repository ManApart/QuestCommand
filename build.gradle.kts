import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val reactWrappersVersion = "17.0.2-pre.214-kotlin-1.5.20"

plugins {
    kotlin("multiplatform") version "1.5.20"
    application
    id("com.github.ben-manes.versions") version("0.38.0")
}

group = "org.rak.manapart"
version = "dev"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.reflections:reflections:0.9.12")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlin:kotlin-test:1.5.0")
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }

        val tools by creating {
            kotlin.srcDir("src/tools/kotlin")
            dependsOn(commonMain)
        }


        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactWrappersVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactWrappersVersion")
            }
        }
    }
}

//sourceSets.getByName("main") {
//    java.srcDir("src/main/kotlin")
//    resources.srcDir("src/main/resource")
//}
//
//sourceSets.getByName("test") {
//    java.srcDir("src/test/kotlin")
//    resources.srcDir("src/test/resource")
//}

//sourceSets.create("tools") {
//    val main = sourceSets["main"]
//    java.srcDir("src/tools/kotlin")
//
//    compileClasspath += main.output + main.compileClasspath
//    runtimeClasspath += output + compileClasspath
//}
//
//sourceSets.create("integrationTest") {
//    val main = sourceSets["main"]
//    val tools = sourceSets["tools"]
//
//    java.srcDir("src/test-integration/kotlin")
//    resources.srcDir("src/test-integration/resource")
//
//    compileClasspath += main.output + tools.output + main.compileClasspath + tools.compileClasspath + configurations["testRuntimeClasspath"]
//    runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
//}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            languageVersion = "1.5"
            jvmTarget = "11"
        }
    }
}

//task("buildData", type = JavaExec::class) {
//    main = "building.AppBuilder"
//    classpath = sourceSets["tools"].runtimeClasspath
//}
//
//task("test-integration", type = Test::class) {
//    val integration = sourceSets["integrationTest"]
//    description = "Runs the integration tests."
//    group = "verification"
//    testClassesDirs = integration.output.classesDirs
//    classpath = integration.runtimeClasspath
//
//    outputs.upToDateWhen { false }
//    mustRunAfter(tasks["test"])
//}
//
//task("test-all") {
//    description = "Run unit AND integration tests"
//    dependsOn("test")
//    dependsOn("test-integration")
//}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}
