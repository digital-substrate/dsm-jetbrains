plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.3.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.digitalsubstrate"
version = "1.2.8"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.1")
        bundledPlugins("com.intellij.java")
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
            ide("IC", "2025.1")
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
