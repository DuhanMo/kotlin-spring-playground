plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"

    // jooq
    id("org.jooq.jooq-codegen-gradle") version "3.19.2"
}

group = "io.duhanmo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // kotlin + spring web starter
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("com.h2database:h2")

    // jooq
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:3.19.3")
    implementation("org.jooq:jooq-kotlin:3.19.2")
    implementation("org.jooq:jooq-kotlin-coroutines:3.19.2")
    jooqCodegen("org.jooq:jooq-codegen:3.19.2")
    jooqCodegen("org.jooq:jooq-meta-extensions:3.19.2")
    jooqCodegen("com.h2database:h2:2.2.224")
}

sourceSets {
    main {
        java {
            setSrcDirs(
                listOf(
                    "$projectDir/src/main/java",
                    "$projectDir/build/generated/source/jooq/main/java"
                )
            )
        }
    }
}

// jooq
jooq {
    configuration {
        generator {
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                inputSchema = "PUBLIC"
                isOutputSchemaToDefault = true

                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources/db/"
                    }
                    property {
                        key = "sort"
                        value = "flyway"
                    }
                    property {
                        key = "unqualifiedSchema"
                        value = "none"
                    }
                    property {
                        key = "defaultNameCase"
                        value = "lower"
                    }
                }

                target {
                    directory = "$projectDir/build/generated/source/jooq/main/java"
                    packageName = "$group.jooq"
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
