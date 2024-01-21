val ktor: String by project
val kotlin: String by project
val logback: String by project
val postgres : String by project
val exposed : String by project
val hikariCpVersion : String by project
val koin : String by project
val jbCrypt : String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

group = "com.norrisboat"
version = "0.0.1"
application {
    mainClass.set("com.norrisboat.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor")
    implementation("io.ktor:ktor-client-core-jvm:$ktor")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktor")
    implementation("io.ktor:ktor-client-cio-jvm:$ktor")
    implementation("io.ktor:ktor-server-sessions:$ktor")
    implementation("io.ktor:ktor-server-websockets:$ktor")

    implementation("org.postgresql:postgresql:$postgres")
    implementation("org.jetbrains.exposed:exposed-core:$exposed")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed")

    implementation("ch.qos.logback:logback-classic:$logback")

    implementation("com.zaxxer:HikariCP:$hikariCpVersion")
    implementation("io.insert-koin:koin-ktor:$koin")
    implementation("org.mindrot:jbcrypt:$jbCrypt")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin")
}