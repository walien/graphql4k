plugins {
    kotlin("jvm") version "1.3.71"
    maven
}

group = "com.github.walien"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.expediagroup:graphql-kotlin-federation:3.3.1")
    api("com.expediagroup:graphql-kotlin-schema-generator:3.3.1")
    implementation("org.http4k:http4k-core:3.252.0")
    testImplementation("org.http4k:http4k-server-jetty:3.252.0")
    testImplementation("org.kodein.di:kodein-di:7.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}
