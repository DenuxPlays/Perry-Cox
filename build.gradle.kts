import net.ltgt.gradle.errorprone.errorprone
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.tools.ant.filters.ReplaceTokens
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.ltgt.errorprone") version "3.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}

group = "dev.denux.perrycox"
version = "GIT_DEPLOY"

//Getting the git commit
val gitCommit: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    String(stdout.toByteArray()).trim()
}

val lombokVersion = "1.18.28"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://m2.dv8tion.net/releases")
    maven(url = "https://jitpack.io")
}


dependencies {
    //Testing stuff
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    //Discord stuff
    implementation("net.dv8tion:JDA:5.0.0-beta.13")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("xyz.dynxsty:dih4jda:1.6.2")

    //Logging
    implementation("ch.qos.logback:logback-classic:1.4.8")

    //File thing's
    implementation("dev.denux:dtp:1.0.0-alpha.2")

    //Code safety
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    errorprone("com.google.errorprone:error_prone_core:2.21.0")

    //Lombok annotations (Utility annotations)
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

application {
    mainClass.set("dev.denux.perrycox.bot.Bot")
}

val jar: Jar by tasks
val shadowJar: ShadowJar by tasks
val build: Task by tasks

tasks.withType<Test> {
    useJUnitPlatform()
}

val sourcesForRelease = task<Copy>("sourcesForRelease") {
    from("src/main/java") {
        include("**/Constants.java")
        val tokens = mapOf(
            "commit" to gitCommit
        )
        // Allow for setting null on some strings without breaking the source
        // for this, we have special tokens marked with "!@...@!" which are replaced to @...@
        filter { it.replace(Regex("\"!@|@!\""), "@") }
        // Then we can replace the @...@ with the respective values here
        filter<ReplaceTokens>(mapOf("tokens" to tokens))
    }
    into("build/filteredSrc")
    includeEmptyDirs = false
    outputs.upToDateWhen { false }
}

val generateJavaSources = task<SourceTask>("generateJavaSources") {
    val javaSources = sourceSets["main"].allJava.filter {
        it.name != "Constants.java"
    }.asFileTree

    source = javaSources + fileTree(sourcesForRelease.destinationDir)
    dependsOn(sourcesForRelease)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.errorprone.disableWarningsInGeneratedCode.set(true)
    options.errorprone.errorproneArgs.addAll(
        "-Xep:DefaultCharset:OFF", "-Xep:JavaTimeDefaultTimeZone:OFF", "-Xep:FutureReturnValueIgnored:OFF",
        "-Xep:AnnotateFormatMethod:OFF", "-Xep:StringSplitter:OFF")
    source = generateJavaSources.source
    dependsOn(generateJavaSources)
}

build.apply {
    dependsOn(jar)
    dependsOn(shadowJar)
}
