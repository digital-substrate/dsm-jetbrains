plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.21"
    id("org.jetbrains.intellij.platform") version "2.20.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.4"
}

group = "com.digitalsubstrate"
version = "1.2.9"

kotlin {
    jvmToolchain(21)
    compilerOptions {
        // The Kotlin stdlib is provided by the IDE at runtime: target the one
        // bundled with the oldest supported platform (2025.1 ships Kotlin 2.1).
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        // Use JVM default methods of platform interfaces as they are, instead
        // of generating bridges that the Plugin Verifier reports as overrides
        // of internal/deprecated API (e.g. ToolWindowFactory.getIcon()).
        jvmDefault.set(org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode.NO_COMPATIBILITY)
    }
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // Compile against the oldest supported platform (since-build 251).
        intellijIdeaCommunity("2025.1")
    }
}

tasks {
    patchPluginXml {
        sinceBuild.set("251")
    }

    publishPlugin {
        token.set(System.getenv("JETBRAINS_MARKETPLACE_TOKEN"))
    }
}

intellijPlatform {
    pluginVerification {
        ides {
            // Oldest supported platform, then every unified IntelliJ IDEA
            // release since IC was discontinued (2025.3+).
            create("IC", "2025.1")
            create("IC", "2025.2")
            create("IU", "2025.3")
            create("IU", "2026.1")
            create("IU", "2026.2")
            create("IU", "2026.3")
        }
    }
}

// Sandbox on the latest platform release: ./gradlew runIdeLatest
intellijPlatformTesting {
    runIde {
        register("runIdeLatest") {
            type = org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaUltimate
            version = "2026.3"
        }
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
    }
}

// Configure Grammar-Kit's generateLexer task
tasks.named<org.jetbrains.grammarkit.tasks.GenerateLexerTask>("generateLexer") {
    sourceFile = file("src/main/kotlin/com/digitalsubstrate/dsm/lexer/DSM.flex")
    targetOutputDir = file("src/main/gen/com/digitalsubstrate/dsm/lexer")
    purgeOldFiles = true
}

// Configure Grammar-Kit's generateParser task
tasks.named<org.jetbrains.grammarkit.tasks.GenerateParserTask>("generateParser") {
    sourceFile = file("src/main/kotlin/com/digitalsubstrate/dsm/parser/DSM.bnf")
    targetRootOutputDir = file("src/main/gen")
    pathToParser = "com/digitalsubstrate/dsm/parser/DSMParser.java"
    pathToPsiRoot = "com/digitalsubstrate/dsm/psi"
    purgeOldFiles = true
}

// Make sure lexer and parser are generated before compilation
tasks.named("compileKotlin") {
    dependsOn("generateLexer", "generateParser")
}

tasks.named("compileJava") {
    dependsOn("generateLexer", "generateParser")
}

// Plugin icon (pluginIcon.svg) is automatically included in META-INF/ via resources
